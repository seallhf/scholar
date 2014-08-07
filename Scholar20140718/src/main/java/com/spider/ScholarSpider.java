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

import com.nlp.CCFComparation;
import com.search.service.IndexService;
import com.spider.pojo.Author;
import com.spider.pojo.Paper;
import com.spider.service.MongoService;
import com.utils.HttpUtil;
import com.utils.IOUtil;
import com.utils.MD5Util;
import com.utils.spring.SpringBeanFactory;

@Service
public class ScholarSpider {

	@Resource
	private CCFComparation ccf;

	@Resource
	private MongoService mongo;
	
	@Resource
	private IndexService index;

	private static Logger log = Logger.getLogger(ScholarSpider.class);

	private Map<String, String> header = new HashMap<String, String>();

	private final long sleeptime = 3000;
	
	/**
	 * HttpRequest header 拼接HTTP头参数
	 */
	private Map<String, String> getHeader() {
		header.put("X-Client-Data",
				"CP21yQEIkbbJAQiktskBCKm2yQEIwbbJAQiehsoBCLiIygE=");
		header.put(
				"cookie",
				"GSP=ID=7a0b905d571ed94f:LM=1406876542:S=o7KPkQyC2qGQHYdc; PREF=ID=7a0b905d571ed94f:TM=1406876542:LM=1406876542:S=7Th_6FkodKAXj41r; GOOGLE_ABUSE_EXEMPTION=ID=1a29c8a569c4e77a:TM=1407133190:C=c:IP=116.251.217.178-:S=APGng0uW4QD0totJ3QQtFJO-OMXJ1II-dA");
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
								+ query, getHeader(),sleeptime);
			else
				html = HttpUtil.get("http://scholar.google.com.cn" + url,
						getHeader(),sleeptime);
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
							+ authorId, getHeader(),sleeptime);
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
	 * 获得目标用户的论文列表
	 * 
	 * @param page
	 * @return
	 */
	public Map<String, String> getAuthorPapers(Document page, String authorId) {
		try {
			List<Paper> papers=new ArrayList<Paper>();
			this.papers = null;
			Map<String, String> lists = new HashMap<String, String>();
			Elements e = page.getElementsByAttributeValue("class",
					"cit-table item");
			for (Element table : e) {
				String line = table.getElementsByTag("a").first().attr("href");
				Pattern p = Pattern.compile(authorId + "\\:.+");
				Matcher m = p.matcher(line);
				String urlId = "";
				if (m.find()) {
					// System.out.println(m.group().replaceAll(id + "\\:", ""));
					urlId = m.group().replaceAll(authorId + "\\:", "");
				}
				String title = table.getElementsByTag("a").first().text();
				lists.put(MD5Util.MD5(title), urlId);
				//Paper paper = (Paper)SpringBeanFactory.getBean("paper");
				Paper paper = getPaperDetail(table, authorId, urlId);
				papers.add(paper);
				if (paper != null && mongo.findPaper(paper.getPid()) == null) {
					mongo.insertPaper(paper);
					System.out.println(authorId + ":" + paper.getPid()
							+ "----->" + "inserted");
				} else
					System.out.println(authorId + ":" + paper.getPid());
			}
			int i = 2;
			boolean stop = false;
			while (!stop) {
				String html = HttpUtil.get(
						"http://scholar.google.com.cn/citations?user="
								+ authorId
								+ "&pagesize=100&view_op=list_works&cstart="
								+ (i * 100), getHeader(),sleeptime);
				i++;
				page = Jsoup.parse(html);
				e = page.getElementsByAttributeValue("class", "cit-table item");
				if (e.size() != 0) {
					for (Element table : e) {
						String line = table.getElementsByTag("a").first()
								.attr("href");
						Pattern p = Pattern.compile(authorId + "\\:.+");
						Matcher m = p.matcher(line);
						String urlId = "";
						if (m.find()) {
							// System.out.println(m.group().replaceAll(id +
							// "\\:", ""));
							urlId = m.group().replaceAll(authorId + "\\:", "");

						}
						String title = table.getElementsByTag("a").first()
								.text();
						lists.put(MD5Util.MD5(title), urlId);
						//Paper paper = (Paper)SpringBeanFactory.getBean("paper");
						Paper paper = getPaperDetail(table, authorId, urlId);
						papers.add(paper);
						if (paper != null
								&& mongo.findPaper(paper.getPid()) == null) {
							mongo.insertPaper(paper);
							System.out
									.println(authorId + ":" + paper.getPid()+ "----->" + "inserted");
						} else
							System.out
									.println(authorId + ":" + paper.getPid());
					}
				} else
					stop = true;
			}
			log.info("GET user <" + authorId + "> PAPER list!");
			this.papers=papers;
			return lists;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Paper> papers;
	
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
					getHeader(),sleeptime);
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
					getHeader(),sleeptime);
			page = Jsoup.parse(html);
			Element title = page.getElementsByClass("cit-user-info").first();
			Pattern p = Pattern.compile("user=.+?\\&");
			Matcher m = p.matcher(title.getElementsByTag("a").first()
					.attr("href"));
			if (m.find()) {
				author.setAid(m.group().replaceAll("user=|\\&", ""));
			}
			try {
				author.setName(page
						.getElementsByAttributeValue("id", "cit-name-display")
						.first().text());
			} catch (Exception e1) {
				System.out.println("name exception！");
				// IOUtil.write2File("./error/error.html", html);
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
						.getElementsByAttributeValue("style",
								"text-align: center;").first()
						.getElementsByTag("img").first().attr("src"));
			} catch (Exception e1) {
				System.out.println("image exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				author.setCollege(page
						.getElementsByAttributeValue("id",
								"cit-affiliation-display").first().text());
			} catch (Exception e1) {
				System.out.println("college exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				author.setTags(page
						.getElementsByAttributeValue("id", "cit-int-read")
						.text().replaceAll("\\&nbsp", "").replaceAll("-", ";"));
			} catch (Exception e1) {
				System.out.println("tags exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				author.setEmail(page.getElementsByAttributeValue("id",
						"cit-domain-display").text());
			} catch (Exception e1) {
				System.out.println("email exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				author.setHomePage(page
						.getElementsByAttributeValue("id", "cit-homepage-read")
						.first().getElementsByTag("a").first().attr("href"));
			} catch (Exception e1) {
				System.out.println("homepage exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				author.setCiteindex(Integer.parseInt(page
						.getElementsByAttributeValue("class",
								"cit-borderbottom")
						.first()
						.getElementsByAttributeValue("class",
								"cit-borderleft cit-data").first().text()));
			} catch (Exception e1) {
				System.out.println("index exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			author.setPapers(getAuthorPapers(page, author.getAid()));
			author.setCoAuthors(getCoAuthor(author.getAid()));
			log.info("GET user <" + authorId + "> details!");
			index.updateIndex(author, papers);
			return author;
		} catch (Exception e) {
			System.out.println("html exception！Author : " + authorId);
			if (pageCount < 3) {
				pageCount++;
				return getAuthorDetail(authorId);
			} else {
				pageCount = 0;
				return null;
			}
			// IOUtil.write2File("./error/error.html", html);
		}
	}

	public Paper getPaperDetail(Element table, String authorId, String urlId) {
		Paper paper = new Paper();
		Element e = table.getElementById("col-title");
		paper.setTitle(e.getElementsByTag("a").first().text());
		paper.setPid(MD5Util.MD5(paper.getTitle()));
		Elements es = e.getElementsByTag("span");
		if (es.size() > 1) {
			paper.setJounalName(es.get(1).text());
			paper.setAuthors(es.get(0).text());
		} else if (es.size() <= 1) {
			// return getPaperDetail(authorId,urlId);
			paper.setJounalName("");
			paper.setAuthors("");
		}
		try {
			paper.setCiteIndex(Integer.parseInt(table
					.getElementById("col-citedby").getElementsByTag("a")
					.first().text()));
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("get citeIndex exception!");
		}
		try {
			paper.setDate(table.getElementById("col-year").text());
		} catch (Exception e1) {
			// e1.printStackTrace();
			System.out.println("get Date exception!");
		}
		paper.setTag(ccf.getPaperType(paper.getJounalName()));
		return paper;
	}

	private int pageCount = 0;

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
		try {
			String html = "";
			Paper paper = (Paper) SpringBeanFactory.getBean("paper");
			String url = "http://scholar.google.com.cn/citations?view_op=view_citation&hl=zh-CN&user="
					+ authorId
					+ "&pagesize=100&citation_for_view="
					+ authorId
					+ ":" + paperId;
			html = HttpUtil.get(url, getHeader(),sleeptime);
			Document page = Jsoup.parse(html);
			try {
				paper.setTitle(page.getElementsByAttributeValue("id", "title")
						.first().text());
			} catch (Exception e1) {
				System.out.println("title exception！");
				IOUtil.write2File("./error/error.html", html);
				if (page.getElementsByTag("title").first().text().equals(url)) {
					System.out.println("Warning Detected! Take a break!");
					Thread.sleep(120000);
					return getPaperDetail(authorId, paperId);
				} else
					System.out.println("Google's error!" + paperId);
				return null;
			}
			try {
				String id = MD5Util.MD5(paper.getTitle());
				paper.setPid(id);
			} catch (Exception e1) {
				System.out.println("paperid exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setUrl(page.getElementsByAttributeValue("id", "title")
						.first().getElementsByTag("a").attr("href"));
			} catch (Exception e1) {
				System.out.println("url exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setDate(page
						.getElementsByAttributeValue("id", "pubdate_sec")
						.first().getElementsByAttributeValue("class", "cit-dd")
						.text());
			} catch (Exception e1) {
				System.out.println("date exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setAuthors(page
						.getElementsByAttributeValue("class", "cit-dd").get(1)
						.text());
			} catch (Exception e1) {
				System.out.println("authors exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setBrief(page
						.getElementsByAttributeValue("id", "description_sec")
						.first().getElementsByAttributeValue("class", "cit-dd")
						.text());
			} catch (Exception e1) {
				System.out.println("brief exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setJounalName(page
						.getElementsByAttributeValue("id", "venue_sec").first()
						.getElementsByAttributeValue("class", "cit-dd").text());
			} catch (Exception e1) {
				System.out.println("jounalName exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			try {
				paper.setCiteIndex(Integer.parseInt(page
						.getElementsByAttributeValue("id", "scholar_sec")
						.first().getElementsByAttributeValue("class", "cit-dd")
						.text().replaceAll("被引用次数：", "")));
			} catch (Exception e1) {
				System.out.println("citeIndex exception！");
				// IOUtil.write2File("./error/error.html", html);
			}
			if (paper.getJounalName() != null)
				paper.setTag(ccf.getPaperType(paper.getJounalName()));
			log.info("GET paper <" + paperId + "> PAPER details!");
			return paper;
		} catch (Exception e) {
			System.out.println("html exception！Paper");
			if (pageCount < 3) {
				pageCount++;
				return getPaperDetail(authorId, paperId);
			} else {
				pageCount = 0;
				return null;
			}
			// IOUtil.write2File("./error/error.html", html);
		}
	}

	public static void main(String[] args) {
		ScholarSpider spider = new ScholarSpider();
		String id = spider.getPaperDetail("V_NdI3sAAAAJ", "u5HHmVD_uO8C")
				.getPid();
		// System.out.println(id);
		Author a = spider.getAuthorDetail("V_NdI3sAAAAJ");
		System.out.println(a.getPapers().get(id));
	}

}
