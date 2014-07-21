package com.spider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.pojo.Author;
import com.pojo.Paper;
import com.service.CCFComparation;
import com.utils.HttpUtil;
import com.utils.IOUtil;
import com.utils.spring.SpringBeanFactory;

@Service
public class ScholarSpider {

	@Resource
	private CCFComparation ccf;

	private static Logger log = Logger.getLogger(ScholarSpider.class);

	private Map<String, String> header = new HashMap<String, String>();

	/**
	 * HttpRequest header 拼接HTTP头参数
	 */
	private Map<String, String> getHeader() {
		header.put("X-Client-Data",
				"CP21yQEIkbbJAQiktskBCKm2yQEIwbbJAQiehsoBCLiIygEIqZTKAQ==");
		header.put(
				"cookie",
				"PREF=ID=7a4885fa2516ec22:NW=1:TM=1404377596:LM=1404377596:S=5FiX8nNjLa1Y-i_v; GOOGLE_ABUSE_EXEMPTION=ID=ca4042f01131d9fe:TM=1404897105:C=c:IP=171.217.190.148-:S=APGng0tE0uhAnPe0u2gNVHEM6wUGYsS_mg");
		header.put("Host", "scholar.google.com.cn");
		header.put("Connection", "keep-alive");
		header.put("Accept-Encoding", "gzip,deflate,sdch");
		header.put("Content-type", "text/html; charset=UTF-8");
		header.put("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		header.put("Accept-Language", "zh-CN,zh;q=0.8");
		header.put(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
		return header;
	}

	/**
	 * 获取高级大牛用户的ID列表
	 * 
	 * @param query
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public Map<String, String> getSeniorUser(String query) throws IOException {

		Map<String, String> authorList = new HashMap<String, String>();
		boolean stop = false;
		String url = null;
		int i = 0;
		Writer wr = new OutputStreamWriter(
				new FileOutputStream("D://data.txt"), "GB2312");
		while (!stop) {
			String html = getSeniorHtml(query, url);
			try {
				Document page = Jsoup.parse(html);
				System.out.println(i++);
				Element a = page.getElementsByAttributeValue("style",
						"border-left: 0.4em solid #9fd9a7; padding: 0.7em;")
						.first();
				Element next = page.getElementsByAttributeValue("class",
						"g-section cit-dgb").last();
				url = next
						.getElementsByAttributeValue("class", "cit-dark-link")
						.last().attr("href");
				Elements authors = a.getElementsByTag("table");
				for (Element author : authors) {
					Element e = author.getElementsByAttributeValue("class",
							"cit-dark-large-link").first();
					String name = e.text();
					String location = e.attr("href");
					Pattern p = Pattern.compile("被引用次数：.+?<br />");
					Matcher m = p.matcher(author.html());
					Integer index = 0;
					if (m.find()) {
						index = Integer.parseInt(m.group().replaceAll("[\\D]",
								""));
					}
					p = Pattern.compile("user=.+?\\&");
					m = p.matcher(location);
					if (m.find()) {
						location = m.group().replaceAll("user=|\\&", "");
					}
					if (index < 500) {
						stop = true;
						break;
					}
					authorList.put(location, name);
					wr.write(location + "\r\n");
				}
			} catch (Exception e) {
				stop = true;
			}
		}
		wr.close();
		log.info("GET query <" + query + "> SENIOR USER list!");
		return authorList;
	}

	/**
	 * 获取高级作者页的Html 其中URL为开始页，为NULL时从第一页开始
	 * 
	 * @param query
	 * @param url
	 * @return
	 */
	public String getSeniorHtml(String query, String url) {
		String html = "";
		try {
			if (url == null)
				html = HttpUtil
						.get("http://scholar.google.com.cn/citations?view_op=search_authors&hl=zh-CN&mauthors="
								+ query, getHeader());
			else
				html = HttpUtil.get("http://scholar.google.com.cn" + url,
						getHeader());
			return html;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return getSeniorHtml(query, url);
		}
	}

	/**
	 * 
	 * 获取目标用户的全部合作者
	 * 
	 * @param query
	 * @return
	 */
	public List<String> getCoAuthor(String authorId) {
		try {
			List<String> candidatesList = new ArrayList<String>();
			String html = HttpUtil.get(
					"http://scholar.google.com.cn/citations?view_op=list_colleagues&user="
							+ authorId, getHeader());
			IOUtil.write2File("d:/1.txt", html);
			Document page = Jsoup.parse(html);
			Elements candidates = page.getElementsByAttributeValue("style",
					"text-align:center;padding:0px 5px;width:150px");
			for (Element candidate : candidates) {
				Element e = candidate.getElementsByTag("a").first();
				String id = e.attr("href");
				Pattern p = Pattern.compile("user=.+?\\&");
				Matcher m = p.matcher(id);
				if (m.find()) {
					id = m.group().replaceAll("user=|\\&", "");
				}
				// System.out.println(id + "\r\n");
				candidatesList.add(id);
			}
			log.info("GET user <" + authorId + "> COAUTHOR list!");
			return candidatesList;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 获得目标用户的全部
	 * 
	 * @param page
	 * @return
	 */
	public List<String> getAuthorPapers(Document page, String authorId) {
		try {
			List<String> lists = new ArrayList<String>();
			Elements e = page.getElementsByAttributeValue("class",
					"cit-table item");
			for (Element paper : e) {
				String line = paper.getElementsByTag("a").first().attr("href");
				Pattern p = Pattern.compile(authorId + "\\:.+");
				Matcher m = p.matcher(line);
				if (m.find()) {
					// System.out.println(m.group().replaceAll(id + "\\:", ""));
					String paperId = m.group().replaceAll(authorId + "\\:", "");
					lists.add(paperId);
				}
			}
			int i = 2;
			boolean stop = false;
			while (!stop) {
				String html = HttpUtil.get(
						"http://scholar.google.com.cn/citations?user="
								+ authorId
								+ "&pagesize=100&view_op=list_works&cstart="
								+ (i * 100), getHeader());
				i++;
				page = Jsoup.parse(html);
				e = page.getElementsByAttributeValue("class", "cit-table item");
				if (e.size() != 0) {
					for (Element paper : e) {
						String line = paper.getElementsByTag("a").first()
								.attr("href");
						Pattern p = Pattern.compile(authorId + "\\:.+");
						Matcher m = p.matcher(line);
						if (m.find()) {
							String paperId = m.group().replaceAll(
									authorId + "\\:", "");
							lists.add(paperId);
						}
					}
				} else
					stop = true;
			}
			log.info("GET user <" + authorId + "> PAPER list!");
			return lists;
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * 评估作者是否是年轻高端人群
	 * 
	 * @param candidateId
	 * @return
	 */
	public boolean isYongEnough(String candidateId) {
		try {
			String html = HttpUtil.get(
					"http://scholar.google.com.cn/citations?user="
							+ candidateId + "&pagesize=100&view_op=list_works",
					getHeader());
			Integer year = 0;
			Document page = Jsoup.parse(html);
			Element e = page.getElementsByAttributeValue("class",
					"cit-table item").first();
			year = Integer.parseInt(e
					.getElementsByAttributeValue("id", "col-year").first()
					.text());
			if (year > 2008)
				return true;
		} catch (Exception e) {

		}
		return false;
	}

	public boolean isYongEnough(Document page) {
		try {
			Integer year = 0;
			Element e = page.getElementsByAttributeValue("class",
					"cit-table item").first();
			year = Integer.parseInt(e
					.getElementsByAttributeValue("id", "col-year").first()
					.text());
			if (year > 2008)
				return true;
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 
	 * 获取作者的详细信息
	 * 
	 * @param AuthorId
	 * @return
	 */

	public Author getAuthorDetail(String authorId) {
		String html = "";
		Author author = (Author) SpringBeanFactory.getBean("author");
		Document page = null;
		try {
			html = HttpUtil.get("http://scholar.google.com.cn/citations?user="
					+ authorId + "&pagesize=100&view_op=list_works",
					getHeader());
			page = Jsoup.parse(html);
			Element title = page.getElementsByClass("cit-user-info").first();
			Pattern p = Pattern.compile("user=.+?\\&");
			Matcher m = p.matcher(title.getElementsByTag("a").first()
					.attr("href"));
			if (m.find()) {
				author.setAid(m.group().replaceAll("user=|\\&", ""));
			}
		} catch (Exception e) {
			System.out.println("html exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setName(page
					.getElementsByAttributeValue("id", "cit-name-display")
					.first().text());
		} catch (Exception e1) {
			System.out.println("name exception！");
			IOUtil.write2File("./error/error.html", html);
			try {
				System.out.println("Warning Detected! Take a break!");
				Thread.sleep(120000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (!html.equals(""))
				return getAuthorDetail(authorId);
		}
		try {
			author.setImgUrl(page
					.getElementsByAttributeValue("style", "text-align: center;")
					.first().getElementsByTag("img").first().attr("src"));
		} catch (Exception e1) {
			System.out.println("image exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setCollege(page
					.getElementsByAttributeValue("id",
							"cit-affiliation-display").first().text());
		} catch (Exception e1) {
			System.out.println("college exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setTags(page
					.getElementsByAttributeValue("id", "cit-int-read").text()
					.replaceAll("\\&nbsp", "").replaceAll("-", ";"));
		} catch (Exception e1) {
			System.out.println("tags exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setEmail(page.getElementsByAttributeValue("id",
					"cit-domain-display").text());
		} catch (Exception e1) {
			System.out.println("email exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setHomePage(page
					.getElementsByAttributeValue("id", "cit-homepage-read")
					.first().getElementsByTag("a").first().attr("href"));
		} catch (Exception e1) {
			System.out.println("homepage exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			author.setCiteindex(Integer.parseInt(page
					.getElementsByAttributeValue("class", "cit-borderbottom")
					.first()
					.getElementsByAttributeValue("class",
							"cit-borderleft cit-data").first().text()));
		} catch (Exception e1) {
			System.out.println("index exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		author.setIsYoungEnough(isYongEnough(page));
		author.setPapers(getAuthorPapers(page, author.getAid()));
		author.setCoAuthors(getCoAuthor(author.getAid()));
		log.info("GET user <" + authorId + "> details!");
		return author;
	}

	/**
	 * 
	 * 获取论文的详细信息
	 * 
	 * @param authorId
	 * @param paperId
	 * @return
	 */
	@SuppressWarnings("static-access")
	public Paper getPaperDetail(String authorId, String paperId) {
		String html = "";
		Paper paper = (Paper) SpringBeanFactory.getBean("paper");
		try {
			html = HttpUtil.get(
					"http://scholar.google.com.cn/citations?view_op=view_citation&hl=zh-CN&user="
							+ authorId + "&pagesize=100&citation_for_view="
							+ authorId + ":" + paperId, getHeader());

		} catch (Exception e) {
			System.out.println("html exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		Document page = Jsoup.parse(html);
		try {
			paper.setTitle(page.getElementsByAttributeValue("id", "title")
					.first().text());
		} catch (Exception e1) {
			System.out.println("title exception！");
			IOUtil.write2File("./error/error.html", html);
			if (!html.equals("")
					&& !page.getElementsByTag("title").first().text()
							.equals("Error 404 (Not Found)!!1")) {
				try {
					System.out.println("Warning Detected! Take a break!");
					Thread.sleep(120000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return getPaperDetail(authorId, paperId);
			} else
				System.out.println("Google's error!");
			return null;
		}
		try {
			paper.setUrl(page.getElementsByAttributeValue("id", "title")
					.first().getElementsByTag("a").attr("href"));
		} catch (Exception e1) {
			System.out.println("url exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			paper.setDate(page.getElementsByAttributeValue("id", "pubdate_sec")
					.first().getElementsByAttributeValue("class", "cit-dd")
					.text());
		} catch (Exception e1) {
			System.out.println("date exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		paper.setPid(paperId);
		try {
			paper.setAuthors(page
					.getElementsByAttributeValue("class", "cit-dd").get(1)
					.text());
		} catch (Exception e1) {
			System.out.println("authors exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			paper.setBrief(page
					.getElementsByAttributeValue("id", "description_sec")
					.first().getElementsByAttributeValue("class", "cit-dd")
					.text());
		} catch (Exception e1) {
			System.out.println("brief exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			paper.setJounalName(page
					.getElementsByAttributeValue("id", "venue_sec").first()
					.getElementsByAttributeValue("class", "cit-dd").text());
		} catch (Exception e1) {
			System.out.println("jounalName exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		try {
			paper.setCiteIndex(Integer.parseInt(page
					.getElementsByAttributeValue("id", "scholar_sec").first()
					.getElementsByAttributeValue("class", "cit-dd").text()
					.replaceAll("被引用次数：", "")));
		} catch (Exception e1) {
			System.out.println("citeIndex exception！");
			IOUtil.write2File("./error/error.html", html);
		}
		if (paper.getJounalName() != null)
			paper.setTag(ccf.getPaperType(paper.getJounalName()));
		log.info("GET paper <" + paperId + "> PAPER details!");
		return paper;
	}

	public static void main(String[] args) {
		ScholarSpider spider = new ScholarSpider();

		System.out.println(spider.getAuthorDetail("MXgWgmEAAAAJ"));
	}

}
