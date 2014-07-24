package com.search.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.pojo.AuthorPage;
import com.pojo.AuthorPaper;
import com.utils.spring.SpringBeanFactory;

@Repository
public class MysqlDao {

	@Autowired
	private JdbcTemplate pageJdbcTemplate;

	public List<AuthorPage> findAuthorPage(String aid) {
		String sql = "select * from author_page where aid = ?";
		return pageJdbcTemplate.query(sql, new Object[] { aid },
				new AuthorPageMapper());
	}

	public List<AuthorPaper> findAuthorPaper(String aid) {
		String sql = "select * from author_paper where aid = ?";
		return pageJdbcTemplate.query(sql, new Object[] { aid },
				new AuthorPaperMapper());
	}

	public AuthorPage insert(AuthorPage authorPage) {
		String sql = "INSERT INTO author_page(aid,name,college,email,tags,citeindex,big_coauthor_id,famous_paper_id,year,home_page,img_url) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = new Object[] { authorPage.getAid(),
				authorPage.getName(), authorPage.getCollege(),
				authorPage.getEmail(), authorPage.getTags(),
				authorPage.getCiteindex(), authorPage.getCoBigAuthors(),
				authorPage.getMostFarmousPaper(),
				authorPage.getYear(), authorPage.getHomePage(),
				authorPage.getImgUrl() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR, Types.VARCHAR, Types.INTEGER, Types.VARCHAR,
				Types.VARCHAR, Types.INTEGER, Types.VARCHAR, Types.VARCHAR };
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

	private class AuthorPageMapper implements RowMapper<AuthorPage> {

		public AuthorPage mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AuthorPage authorPage = new AuthorPage();
			authorPage.setAid(rs.getNString("aid"));
			authorPage.setName(rs.getNString("name"));
			authorPage.setCollege(rs.getNString("college"));
			authorPage.setEmail(rs.getNString("email"));
			authorPage.setTags(rs.getNString("tags"));
			authorPage.setCiteindex(rs.getInt("citeindex"));
			authorPage.setCoBigAuthors(rs.getNString("big_coauthor_id"));
			authorPage.setMostFarmousPaper(rs.getNString("famous_paper_id"));
			authorPage.setYear(rs.getInt("year"));
			authorPage.setHomePage(rs.getNString("home_page"));
			authorPage.setImgUrl(rs.getNString("img_url"));
			return authorPage;
		}
	}

	private class AuthorPaperMapper implements RowMapper<AuthorPaper> {

		public AuthorPaper mapRow(ResultSet rs, int rowNum) throws SQLException {
			// TODO Auto-generated method stub
			AuthorPaper authorPaper = new AuthorPaper();
			authorPaper.setAid(rs.getNString("aid"));
			authorPaper.setAconf(rs.getInt("aconf"));
			authorPaper.setBconf(rs.getInt("bconf"));
			authorPaper.setCconf(rs.getInt("cconf"));
			authorPaper.setAjounal(rs.getInt("ajounal"));
			authorPaper.setBjounal(rs.getInt("bjounal"));
			authorPaper.setCjounal(rs.getInt("cjounal"));
			return authorPaper;
		}
	}

	public static void main(String[] args) {
		MysqlDao dao = (MysqlDao) SpringBeanFactory.getBean("mysqlDao");
		// System.out.println(dao.pageJdbcTemplate);
	}
}
