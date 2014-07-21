<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" "http://java.sun.com/dtd/web-app_2_3.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String home = "http://scholar.google.com.cn/";
%>
<html>
<head>
<script src="/s2sh/main/webapp/res/jquery/jquery-1.3.2.min.js"
	type="text/javascript"></script>
<link
	href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.0/css/bootstrap-combined.min.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=basePath%>css/table.css" />

<script type="text/javascript"
	src="/s2sh/main/webapp/query/editTable.js"></script>

<title><s:property value="query" /> 搜索结果</title>
</head>
<body>
	<div style="margin: 0 auto; width: 1000px;">
		<div class="search_header">
			<div style="margin-top: 20px; vertical-align: right;">
				<s:form action="search" class="form-search" display="inline-block">
					<input
						style="width: 600px; height: 30px; display: inline-block; margin-bottom: 0; vertical-align: middle;"
						type="text" class="form-control"
						placeholder="<s:property value="query" />" name="query">
					<button
						style="width: 90px; height: auto; display: inline-block; margin-bottom: 0; vertical-align: middle;"
						type="submit" class="btn btn-primary">查询</button>
				</s:form>
			</div>
		</div>
		<div class="search_classification">这里是人才分类标签收缩处</div>
		<br />
		
		<div class="search_result_tip">
			<div style="width: 50%; float: left; line-height: 10px;">
				<em style="color: red"><s:property value="query" /></em> 查询结果
			</div>
			<div style="width: 50%; float: right; line-height: 10px;"
				align="right">
				共找到<span class="rowCount"><em style="color: gray">${myPage.rowCount}</em></span>人
			</div>
		</div>
		<div class="search_list">
			<c:forEach items="${myPage.list}" var="Author">
				<hr />
				<div class="author_details">
					<div class="author_details_left">
						<div class="author_img">
							<img src="<%=home%>${Author.imgUrl}">
						</div>
						<br />
						<div
							style="height: 30px; display: inline-block; margin-top: 10px; vertical-align: middle;">
							<a href="${Author.homePage}">个人主页 </a>
						</div>
					</div>
					<div class="author_details_right">
						<div class="author_details_right_vertical">
							<div class="author_name">
								<a
									href="<%=home%>citations?hl=zh-CN&user=${Author.aid}&view_op=list_works&pagesize=100">
									${Author.name} </a>
							</div>
							<div class="author_college">
								&nbsp;&nbsp;<i class="icon-bookmark"></i>&nbsp;&nbsp;${Author.college}
							</div>
							<div class="author_email">
								&nbsp;&nbsp;<i class="icon-envelope"></i>&nbsp;&nbsp;${Author.email }
							</div>
							<div class="author_tags">
								&nbsp;&nbsp;<i class="icon-tags"></i>&nbsp;&nbsp;${Author.tags }
							</div>
							<div class="author_citeindex">
								&nbsp;&nbsp;<i class="icon-book"></i>&nbsp;&nbsp;论文被引用次数:<strong>${Author.citeindex}</strong>
							</div>
							<div class="author_paper">
								<table class="table_conf">
									<tr class="jounal">
										<td width=15%>会议：</td>
										<td width=20%>A - ${Author.authorPaper.aconf}</td>
										<td width=20%>B - ${Author.authorPaper.bconf}</td>
										<td width=20%>C - ${Author.authorPaper.cconf}</td>
									</tr>
									<tr class="jounal">
										<td width=15%>期刊：</td>
										<td width=20%>A - ${Author.authorPaper.ajounal}</td>
										<td width=20%>B - ${Author.authorPaper.bjounal}</td>
										<td width=20%>C - ${Author.authorPaper.cjounal}</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>

		<hr />
		<div class="footer">
			<div class="message"></div>
			<div class="pagebar list_page">
				<span class="currentPage">${myPage.currentPage} </span>/<span
					class="totalPage"> ${myPage.totalPage} </span> <a class="firstPage"
					href="search.action?query=<s:property value="query" />&pno=1">首页</a>
				<a class="prePage"
					href="search.action?query=<s:property value="query" />&pno=${myPage.prePage}">上一页</a>
				<a class="nextPage"
					href="search.action?query=<s:property value="query" />&pno=${myPage.nextPage}">下一页</a>
				<a class="lastPage"
					href="search.action?query=<s:property value="query" />&pno=${myPage.totalPage}">尾页</a>

			</div>
		</div>
	</div>
	<br />
	<br />
	<br />
</body>
</html>
