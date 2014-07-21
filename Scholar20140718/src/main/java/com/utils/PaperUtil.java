package com.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.http.client.ClientProtocolException;

public class PaperUtil {

	@SuppressWarnings("unchecked")
	public static Map<String, String> paperType = new CaseInsensitiveMap();

	@SuppressWarnings("unchecked")
	public static Map<String, String> paperShortType = new CaseInsensitiveMap();

	static {

		paperType = IOUtil
				.read2Map("./src/main/resources/config.property/paperType.properties");

		paperShortType = IOUtil
				.read2ShortMap("./src/main/resources/config.property/paperType.properties");
	}

	private Map<String, String> getHeader() {
		Map<String, String> header = new HashMap<String, String>();
		header.put(
				"cookie",
				"Hm_lvt_5c8ff59a597e79d44acca49c3d8d0ee1=1403686186; JSESSIONID=B2C06375FDA763786AEED8F79B554F9F");
		header.put("Host", "www.ccf.org.cn");
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

	public void spide() {
		String html;
		try {
			html = IOUtil
					.get("http://www.ccf.org.cn/sites/ccf/biaodan.jsp?contentId=2567814757432",
							getHeader());

			String leibie = "前沿、交叉与综合	计算机";
			Document page = Jsoup.parse(html);
			String result = "";
			IOUtil.write2File("d:\\test", html);
			Elements e = page.getElementsByTag("table");
			Element type = e.get(2);
			Elements tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "A" + "\t" + getTag(td) + "\t" + leibie + "\t期刊"
						+ "\r\n";
			}

			type = e.get(3);
			tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "B" + "\t" + getTag(td) + "\t" + leibie + "\t期刊"
						+ "\r\n";
			}

			type = e.get(4);
			tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "C" + "\t" + getTag(td) + "\t" + leibie + "\t期刊"
						+ "\r\n";
			}

			type = e.get(5);
			tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "A" + "\t" + getTag(td) + "\t" + leibie + "\t会议"
						+ "\r\n";
			}

			type = e.get(6);
			tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "B" + "\t" + getTag(td) + "\t" + leibie + "\t会议"
						+ "\r\n";
			}

			type = e.get(7);
			tr = type.getElementsByTag("tr");
			for (int i = 1; i < tr.size(); i++) {
				Element e1 = tr.get(i);
				Elements td = e1.getElementsByTag("td");
				result += "C" + "\t" + getTag(td) + "\t" + leibie + "\t会议"
						+ "\r\n";
			}
			IOUtil.write2File("D:\\paper.txt", result);
		} catch (ClientProtocolException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	public String getTag(Elements td) {
		String tags = "";
		tags += td.get(1).text() + "\t" + td.get(2).text();
		return tags;
	}

	public static void main(String[] args) {
		// PaperUtil paper = new PaperUtil();
		//
		// System.out.println(paper.paperType);
	}

}
