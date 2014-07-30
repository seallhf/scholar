<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" "http://java.sun.com/dtd/web-app_2_3.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String home = "http://scholar.google.com.cn/";
%>
<html>
<head>
<script src="<%=basePath%>js/jquery-1.11.1.min.js"
	type="text/javascript"></script>
<script src="<%=basePath%>js/pages.js" type="text/javascript"></script>


<link type="text/css" rel="stylesheet" href="<%=basePath%>css/table.css" />
<link
	href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.0/css/bootstrap-combined.min.css"
	rel="stylesheet">
<title><s:property value="query" /> 搜索结果</title>
</head>
<div style="margin: 0 auto; width: 1000px;">
	<div class="search_header">
		<div style="margin-top: 20px; vertical-align: right;">
			<s:form action="search" class="form-search" display="inline-block">
				<input
					style="width: 600px; height: 30px; display: inline-block; margin-bottom: 0; vertical-align: middle;"
					type="text" class="form-control"
					placeholder="<s:property value="query" />" name="query">
				<button id="searchbutton"
					style="width: 90px; height: auto; display: inline-block; margin-bottom: 0; vertical-align: middle;"
					type="submit" class="btn btn-primary">查询</button>
				<input id="searchTerms" type="hidden" name="terms" value="<s:property value="terms" />">
			</s:form>
		</div>
	</div>
	<input id="searchQuery" type="hidden"
		value="<s:property value="query" />">
	<div class="search_classification">
		<div class="search_classification_left">
			<div class="search_classification_left_block">公司</div>
			<hr style="margin-bottom: 0; margin-top: 0;" />
			<div class="search_classification_left_block">职位</div>
			<hr style="margin-bottom: 0; margin-top: 0;" />
			<div class="search_classification_left_block">领域</div>
		</div>
		<div class="search_classification_right">
			<div class="search_classification_right_block">
				<div class="checkbox_company"></div>
			</div>
			<hr style="margin-bottom: 0; margin-top: 0;" />
			<div class="search_classification_right_block">
				<div class="checkbox_position"></div>
			</div>
			<hr style="margin-bottom: 0; margin-top: 0;" />
			<div class="search_classification_right_block">
				<div class="checkbox_system"></div>
			</div>
		</div>
	</div>
	<br />

	<div class="search_result_tip">
		<div style="width: 50%; float: left; line-height: 10px;">
			<c:choose>
				<c:when test="${sortby == ''}">
					<a style="color: red">默认排序</a>
					<i class="icon-arrow-down"></i>&nbsp;&nbsp;</c:when>
				<c:otherwise>
					<a class="sortby_button"
						href="search.action?query=<s:property value="query" />&pno=1&terms=<s:property value="terms" />">默认排序
					</a>&nbsp;&nbsp;
					</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${sortby == 'apapers'}">
					<a style="color: red">A级论文</a>
					<i class="icon-arrow-down"></i>&nbsp;&nbsp;</c:when>
				<c:otherwise>
					<a class="sortby_button"
						href="search.action?query=<s:property value="query" />&pno=1&sortby=apapers&terms=<s:property value="terms" />">A级论文</a>&nbsp;&nbsp;
					</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${sortby == 'year'}">
					<a style="color: red">最早发表年限</a>
					<i class="icon-arrow-down"></i>&nbsp;</c:when>
				<c:otherwise>
					<a class="sortby_button"
						href="search.action?query=<s:property value="query" />&pno=1&sortby=year&terms=<s:property value="terms" />">最早发表年限</a>
				</c:otherwise>
			</c:choose>
		</div>
		<div style="width: 50%; float: right; line-height: 10px;"
			align="right">
			共找到<span class="rowCount"><em style="color: gray">${myPage.rowCount}</em></span>人
		</div>
	</div>

	<div class="search_list">
		<c:forEach items="${myPage.list}" var="AuthorPage">
			<hr />
			<div class="author_details">
				<div class="author_details_left">
					<div class="author_img">
						<img src="<%=home%>${AuthorPage.imgUrl}">
					</div>
					<br />
					<div
						style="height: 30px; display: inline-block; margin-top: 10px; vertical-align: middle;">
						<a href="${AuthorPage.homePage}">个人主页 </a>
					</div>
				</div>
				<div class="author_details_right">
					<div class="author_details_right_vertical">
						<div class="author_name">
							<a
								href="<%=home%>citations?hl=zh-CN&user=${AuthorPage.aid}&view_op=list_works&pagesize=100">
								${AuthorPage.name} </a>
						</div>
						<div class="author_college">
							&nbsp;&nbsp;<i class="icon-bookmark"></i>&nbsp;&nbsp;${AuthorPage.college}
						</div>
						<div class="author_email">
							&nbsp;&nbsp;<i class="icon-envelope"></i>&nbsp;&nbsp;最早发文年限： <b>${AuthorPage.year}</b>
						</div>
						<div class="author_tags">
							&nbsp;&nbsp;<i class="icon-tags"></i>&nbsp;&nbsp;
							<c:set value="${fn:split(AuthorPage.tags, ';')}" var="str1" />
							<c:forEach items="${ str1 }" var="s">
								<font color='#006699'><a href='search.action?query=${s}'>${fn:trim(s)}</a></font>&nbsp;
								</c:forEach>
						</div>
						<div class="author_citeindex">
							&nbsp;&nbsp;<i class="icon-book"></i>&nbsp;&nbsp;论文被引用次数:<strong>${AuthorPage.citeindex}</strong>
						</div>
						<div class="author_coAuthor" id="${AuthorPage.coBigAuthors}"></div>
						<div class="author_farmousPaper"
							id="${AuthorPage.mostFarmousPaper}"></div>
						<div class="author_paper" id="${AuthorPage.aid}"></div>
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
				href="search.action?query=<s:property value="query" />&pno=1&sortby=${sortby}&terms=<s:property value="terms" />">首页</a>
			<a class="prePage"
				href="search.action?query=<s:property value="query" />&pno=${myPage.prePage}&sortby=${sortby}&terms=<s:property value="terms" />">上一页</a>
			<a class="nextPage"
				href="search.action?query=<s:property value="query" />&pno=${myPage.nextPage}&sortby=${sortby}&terms=<s:property value="terms" />">下一页</a>
			<a class="lastPage"
				href="search.action?query=<s:property value="query" />&pno=${myPage.totalPage}&sortby=${sortby}&terms=<s:property value="terms" />">尾页</a>

		</div>
	</div>
</div>
<br />
<br />
<br />
</body>
</html>
