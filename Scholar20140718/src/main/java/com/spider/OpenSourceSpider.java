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
import com.spider.service.MongoService;
import com.utils.HttpUtil;
import com.utils.IOUtil;
import com.utils.MD5Util;
import com.utils.spring.SpringBeanFactory;

@Service
public class OpenSourceSpider {
	
	@Resource
	private MongoService mongo;
	
	@Resource
	private IndexService index;
	
	private final long sleeptime = 100;

	private static Logger log = Logger.getLogger(ScholarSpider.class);

	private Map<String, String> header = new HashMap<String, String>();

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

	public int pageCount=0;
	
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
			
			log.info("GET user <" + authorId + "> details!");
			//index.updateIndex(author, papers);
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

	public static void main(String[] args) {
		ScholarSpider spider = new ScholarSpider();
		String id = spider.getPaperDetail("V_NdI3sAAAAJ", "u5HHmVD_uO8C")
				.getPid();
		// System.out.println(id);
		Author a = spider.getAuthorDetail("V_NdI3sAAAAJ");
		System.out.println(a.getPapers().get(id));
	}

}
