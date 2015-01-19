package com.search.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.search.pojo.AuthorPage;
import com.search.pojo.AuthorPaper;
import com.spider.pojo.Author;
import com.spider.pojo.Paper;
import com.spider.service.MongoService;
import com.utils.spring.SpringBeanFactory;

@Repository
public class MysqlDao {

	@Autowired
	private JdbcTemplate pageJdbcTemplate;

	public AuthorPage findAuthorPage(String aid) {
		String sql = "select * from author_page where aid = ?";
		List<AuthorPage> list = pageJdbcTemplate.query(sql,
				new Object[] { aid }, new AuthorPageMapper());
		if (list.size() != 0)
			return list.get(0);
		return new AuthorPage();
	}

	public Author findAuthor(String aid) {
		String sql = "select * from author where aid = ?";
		List<Author> list = pageJdbcTemplate.query(sql, new Object[] { aid },
				new AuthorMapper());
		if(list.size() != 0)
			return list.get(0);
		return new Author();
	}

	public Paper findPaper(String pid) {
		String sql = "select * from paper where pid = ?";
		List<Paper> list = pageJdbcTemplate.query(sql, new Object[] { pid },
				new PaperMapper());
		if(list.size() != 0)
			return list.get(0);
		return new Paper();
	}

	public AuthorPaper findAuthorPaper(String aid) {
		String sql = "select * from author_paper where aid = ?";
		List<AuthorPaper> list = pageJdbcTemplate.query(sql, new Object[] { aid },
				new AuthorPaperMapper());
		if(list.size() != 0)
			return list.get(0);
		return new AuthorPaper();
	}

	public AuthorPage insert(AuthorPage authorPage) {
		String sql = "INSERT INTO author_page(aid,name,college,email,tags,citeindex,coBigAuthors,mostFarmousPaper,year,homePage,imgUrl) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { authorPage.getAid(),
				authorPage.getName(), authorPage.getCollege(),
				authorPage.getEmail(), authorPage.getTags(),
				authorPage.getCiteindex(), authorPage.getCoBigAuthors(),
				authorPage.getMostFarmousPaper(), authorPage.getYear(),
				authorPage.getHomePage(), authorPage.getImgUrl() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		pageJdbcTemplate.update(sql, params, types);
		return authorPage;
	}

	public AuthorPage update(AuthorPage authorPage) {
		String sql = "UPDATE author_page SET name= ?,college= ?,email= ?,tags= ?,citeindex= ?,coBigAuthors= ?,mostFarmousPaper= ?,year= ?,homePage= ?,img_url= ? WHERE aid = ?";
		Object[] params = new Object[] { authorPage.getName(),
				authorPage.getCollege(), authorPage.getEmail(),
				authorPage.getTags(), authorPage.getCiteindex(),
				authorPage.getCoBigAuthors(), authorPage.getMostFarmousPaper(),
				authorPage.getYear(), authorPage.getHomePage(),
				authorPage.getImgUrl(), authorPage.getAid() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR,
				Types.INTEGER, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
		pageJdbcTemplate.update(sql, params, types);
		return authorPage;
	}

	public AuthorPaper insert(AuthorPaper authorPaper) {
		String sql = "INSERT INTO author_paper(aid,ajounal,aconf,bjounal,bconf,cjounal,cconf) VALUES(?,?,?,?,?,?,?)";
		Object[] params = new Object[] { authorPaper.getAid(),
				authorPaper.getAjounal(), authorPaper.getAconf(),
				authorPaper.getBjounal(), authorPaper.getBconf(),
				authorPaper.getCjounal(), authorPaper.getCconf() };
		int[] types = new int[] { Types.VARCHAR, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };
		pageJdbcTemplate.update(sql, params, types);
		return authorPaper;
	}

	public AuthorPaper update(AuthorPaper authorPaper) {
		String sql = "UPDATE author_paper SET ajounal=?,aconf=?,bjounal=?,bconf=?,cjounal=?,cconf=? WHERE aid=? ";
		Object[] params = new Object[] { authorPaper.getAjounal(),
				authorPaper.getAconf(), authorPaper.getBjounal(),
				authorPaper.getBconf(), authorPaper.getCjounal(),
				authorPaper.getCconf(), authorPaper.getAid() };
		int[] types = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER,
				Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR };
		pageJdbcTemplate.update(sql, params, types);
		return authorPaper;
	}

	private class AuthorPageMapper implements RowMapper<AuthorPage> {

		public AuthorPage mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AuthorPage authorPage = new AuthorPage();
			authorPage.setAid(rs.getString("aid"));
			authorPage.setName(rs.getString("name"));
			authorPage.setCollege(rs.getString("college"));
			authorPage.setEmail(rs.getString("email"));
			authorPage.setTags(rs.getString("tags"));
			authorPage.setCiteindex(rs.getInt("citeindex"));
			authorPage.setCoBigAuthors(rs.getString("coBigAuthors"));
			authorPage.setMostFarmousPaper(rs.getString("mostFarmousPaper"));
			authorPage.setYear(rs.getString("year"));
			authorPage.setHomePage(rs.getString("homePage"));
			authorPage.setImgUrl(rs.getString("imgUrl"));
			return authorPage;
		}
	}

	private class AuthorMapper implements RowMapper<Author> {

		public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Author author = new Author();
			author.setAid(rs.getString("aid"));
			author.setName(rs.getString("name"));
			author.setCollege(rs.getString("college"));
			author.setEmail(rs.getString("email"));
			author.setTags(rs.getString("tags"));
			author.setCiteindex(rs.getInt("citeindex"));
			author.setHomePage(rs.getString("homePage"));
			author.setImgUrl(rs.getString("imgUrl"));
			return author;
		}
	}

	private class PaperMapper implements RowMapper<Paper> {

		public Paper mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			Paper paper = new Paper();
			paper.setBrief(rs.getString("brief"));
			paper.setDate(rs.getString("date"));
			paper.setPid(rs.getString("pid"));
			paper.setTag(rs.getString("tag"));
			paper.setJounalName(rs.getString("jounalName"));
			paper.setTitle(rs.getString("title"));
			paper.setCiteIndex(rs.getInt("citeIndex"));
			paper.setUrl(rs.getString("url"));
			paper.setAuthors(rs.getString("authors"));
			return paper;
		}
	}

	private class AuthorPaperMapper implements RowMapper<AuthorPaper> {

		public AuthorPaper mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AuthorPaper authorPaper = new AuthorPaper();
			authorPaper.setAid(rs.getString("aid"));
			authorPaper.setAconf(rs.getInt("aconf"));
			authorPaper.setBconf(rs.getInt("bconf"));
			authorPaper.setCconf(rs.getInt("cconf"));
			authorPaper.setAjounal(rs.getInt("ajounal"));
			authorPaper.setBjounal(rs.getInt("bjounal"));
			authorPaper.setCjounal(rs.getInt("cjounal"));
			return authorPaper;
		}
	}

	public static void main(String[] args) throws IOException,
			FileNotFoundException {

		MongoService mongo = (MongoService) SpringBeanFactory
				.getBean("mongoService");
		MysqlDao dao = (MysqlDao) SpringBeanFactory.getBean("mysqlDao");
		AuthorPage author = dao.findAuthorPage("o2ZRDe4AAAAJ");
		System.out.println(author);
		// List<String> list = mongo.getAllAuthorId();
		// int i = 0;
		// StringBuilder line = new StringBuilder();
		//
		// for (String id : list) {
		// AuthorPage page = mongo.findAuthorPage(id);
		// if (page != null) {
		// line.append("\"" + i + "\",\"" + page.getCollege() + "\",\""
		// + page.getCiteindex() + "\",\"" + page.getYear()
		// + "\",\"" + page.getMostFarmousPaper() + "\",\""
		// + page.getHomePage() + "\",\"" + page.getTags()
		// + "\",\"" + page.getImgUrl() + "\",\""
		// + page.getCoBigAuthors() + "\",\"" + "" + "\",\""
		// + page.getName() + "\",\"" + page.getAid() + "\",\""
		// + page.getEmail() + "\"" + "\r\n");
		// System.out.println(i + ":" + id);
		// }
		// i++;
		// }

		// for (String id : list) {
		// AuthorPaper paper = mongo.findAuthorPaper(id);
		// if (paper != null) {
		// line.append("\"" + i + "\",\"" + paper.getAid() + "\",\""
		// + paper.getAconf() + "\",\"" + paper.getBconf()
		// + "\",\"" + paper.getCconf() + "\",\""
		// + paper.getAjounal() + "\",\"" + paper.getBjounal()
		// + "\",\"" + paper.getCjounal() + "\"" + "\r\n");
		// System.out.println(i + ":" + id);
		// }
		// i++;
		// }

		// Map<String, DBObject> map = mongo.getAllAuthor();
		// for (String id : map.keySet()) {
		// Author author = (Author) JSONObject
		// .toJavaObject(
		// ((JSONObject) JSONObject.toJSON(map.get(id))),
		// Author.class);
		// if (author != null) {
		// line.append("\"" + i + "\",\"" + author.getCollege() + "\",\""
		// + author.getImgUrl() + "\",\"" + author.getCiteindex()
		// + "\",\"" + author.getName() + "\",\""
		// + author.getAid() + "\",\"" + author.getHomePage()
		// + "\",\"" + author.getEmail() + "\",\""
		// + author.getTags() + "\"" + "\r\n");
		// System.out.println(i + ":" + id);
		// }
		// i++;
		// }
		// IOUtil.write2File("C:\\Users\\seal\\Desktop\\author_.csv", new
		// String(
		// line));
		// Writer wr = new OutputStreamWriter(new
		// FileOutputStream("C:\\Users\\seal\\Desktop\\paper.csv"),
		// "GB2312");
		// DBCursor map = mongo.getAllPaperByCursor();
		// while(map.hasNext()){
		// Paper paper = (Paper) JSONObject.toJavaObject(
		// ((JSONObject) JSONObject.toJSON(map.next())), Paper.class);
		// if (paper != null) {
		// wr.write("\"" + i + "\",\"" + paper.getBrief() + "\",\""
		// + paper.getDate() + "\",\"" + paper.getPid() + "\",\""
		// + paper.getTag() + "\",\"" + paper.getJounalName()
		// + "\",\"" + paper.getTitle() + "\",\""
		// + paper.getCiteIndex() + "\",\"" + paper.getUrl()
		// + "\",\"" + paper.getAuthors() + "\"\r\n");
		// System.out.println(i + ":" + paper.getPid());
		// }
		// i++;
		// }
		// wr.close();

	}
}
