<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" "http://java.sun.com/dtd/web-app-2-3.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link type="text/css" rel="stylesheet" href="<%=basePath%>css/base.css" />
<link type="text/css" rel="stylesheet" href="<%=basePath%>css/index.css" />
<style type="text/css">
.form-search-css form{float:right;}
.search-input-type{border-top:3px solid #0072bf; border-bottom:3px solid #0072bf; border-left:3px solid #0072bf; border-right:0; width:461px; height:38px; color:#9fa1a3; padding-left:5px;}
.form-search-css form input[type="submit"]{border-top:3px solid #0072bf; border-bottom:3px solid #0072bf; border-right:3px solid #0072bf; border-left:0; margin-left:0px; width:100px; height:38px;  vertical-align:top; color:#fff; background:url(img/searchbtn.png); font-weight:bold;}
.ml3{margin-left:3px;}
.ml4{margin-left:4px;}
.ml5{margin-left:5px;}
.ml77{margin-left:77px;}
.mr5{margin-right:5px;}
.inline-block-css{width:810px; overflow:hidden; text-overflow:ellipsis; white-space:nowrap;}
.title-link-css:hover{cursor:pointer;}
.otherdisplay{display:none;}
.sortby-icon-css{}
</style>
<script src="<%=basePath%>js/jquery-1.11.1.js" type="text/javascript"></script>
<script src="<%=basePath%>js/pages.js" type="text/javascript"></script>
<title><s:property value="query" /> 搜索结果</title>
</head>
<body>
    <!-- start header -->
	<div class="header">
		<div class="header-middle-content">
			<a class="header-img-left" href="#">
				<img src="img/logo.png"></img>
			</a>
			<a class="header-img-right" href="#">
				<img src="img/headerrimg.png"></img>
			</a>
			<div class="header-middle-rnav">
				<span>注册</span>
				<span> | </span>
				<span>登陆</span>
			</div>
		</div>
	</div>
	<!-- end header -->

	<!-- start 主要内容 -->
	<div class="content">
	    <input id="searchQuery" type="hidden" value="<s:property value="query" />" />
        <!--搜索-->
		<div class="form-search-css">
		    <div class="top-img-css"><img src="img/talentsearch.png"></img></div>
		    <s:form action="search">
				<input class="search-input-type" type="text" value="<s:property value="query" />" placeholder="请输入要搜索的关键词" name="query" />
				<input id="searchbutton" type="submit" value="搜 索" />
				<input id="searchTerms" type="hidden" name="terms" value="<s:property value="terms" />" />
			</s:form>
		</div>
		<!-- end搜索 -->

		<!-- 条件选择 -->
		<div id="show-condition-area">
			<div class="first-div">您已选择 : <span><s:property value="query" /> > </span></div>
			<ul id="free-condition-ul"></ul>
			<div class="result-numshow">
				<span>共找到 <span class="lighting-color">${myPage.rowCount}</span> 人</span>
			</div>
		</div>

		<div class="conditions-search-css">
			<div class="single-condition-css">
				<div class="left">公司 :</div>
				<ul id="flagCompany" class="selected-li more-ul-css"></ul>
				<div class="more-show-css"></div>
			</div>
			<div class="single-condition-css">
				<div class="left">职位 :</div>
				<ul id="flagPosition" class="selected-li more-ul-css"></ul>
				<div class="more-show-css"></div>
			</div>
			<div class="single-condition-css">
				<div class="left">领域 :</div>
				<ul id="flagSystem" class="selected-li more-ul-css"></ul>
				<div class="more-show-css"></div>
			</div>
		</div>
		<!-- end条件选择 -->

		<!-- 条件排序 -->
		<div class="sequencing-results-css">
			<ul class="qequencing-ul-css">
			    <c:choose>
					<c:when test="${sortby == ''}">
						<li>
							<a class="remind-color">默认排序</a>
							<span class="sortby-icon-css">!</span>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<a class="sortby-button"
								href="search.action?query=<s:property value="query" />&pno=1&terms=<s:property value="terms" />">默认排序
							</a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sortby == 'apapers'}">
						<li>
							<a style="color: red">A级论文</a>
							<i class="icon-arrow-down"></i>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<a class="sortby-button" href="search.action?query=<s:property value="query" />&pno=1&sortby=apapers&terms=<s:property value="terms" />">A级论文</a>
						</li>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${sortby == 'year'}">
						<li>
							<a style="color: red">最早发表年限</a>
							<i class="icon-arrow-down"></i>
						</li>
				    </c:when>
					<c:otherwise>
						<li>
							<a class="sortby-button"
								href="search.action?query=<s:property value="query" />&pno=1&sortby=year&terms=<s:property value="terms" />">最早发表年限</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<div class="pagebar list-page">
				<div class="message"></div>
				<span class="currentPage">${myPage.currentPage} </span>/
				<span class="totalPage"> ${myPage.totalPage} </span> 
				<a class="firstPage" href="search.action?query=<s:property value="query" />&pno=1&sortby=${sortby}&terms=<s:property value="terms" />">首页</a>
				<a class="prePage" href="search.action?query=<s:property value="query" />&pno=${myPage.prePage}&sortby=${sortby}&terms=<s:property value="terms" />">上一页</a>
				<a class="nextPage" href="search.action?query=<s:property value="query" />&pno=${myPage.nextPage}&sortby=${sortby}&terms=<s:property value="terms" />">下一页</a>
				<a class="lastPage" href="search.action?query=<s:property value="query" />&pno=${myPage.totalPage}&sortby=${sortby}&terms=<s:property value="terms" />">尾页</a>
			</div>
			<p></p>
		</div>
		<!-- end条件排序 -->

		<!-- 搜索结果 -->

		<div class="search-list">
			<c:forEach items="${myPage.list}" var="AuthorPage">
				<div class="author-details">
					<div class="author-details-left">
						<div class="author-img">
							<img src="<%=home%>${AuthorPage.imgUrl}">
						</div>
						<div class="person-link-css">
							<a href="${AuthorPage.homePage}">个人主页 </a>
						</div>
					</div>
					<div class="author-details-right">
						<div class="author-details-right-vertical">
							<div class="author-name">
								<a href="<%=home%>citations?hl=zh-CN&user=${AuthorPage.aid}&view-op=list-works&pagesize=100">${AuthorPage.name} </a>
								<span class="author-year conmmon-css">&nbsp;最早发文年限:<span>${AuthorPage.year}</span></span>
								<span class="author-citeindex conmmon-css">&nbsp;论文引用总数:<span>${AuthorPage.citeindex}</span></span>
							</div>
							<!--现职单位-->
							<div class="author-college">
							    <span class="unit-icon"></span>
								<span>现职单位:</span>
								<span class="ml3">${AuthorPage.college}</span>
							</div>
							<!--代表文章-->
							<div class="author-farmousPaper" id="${AuthorPage.mostFarmousPaper}"></div>
							<!--论文数量-->
							<div class="author-paper" id="${AuthorPage.aid}"></div>
							<!--兴趣标签-->
							<div class="author-tags">
							    <span class="interest-tag-icon"></span>
							    <span class="mr5">兴趣标签:</span>
								<c:set value="${fn:split(AuthorPage.tags, ';')}" var="str1" />
								<c:forEach items="${ str1 }" var="s">
									<a href='search.action?query=${s}'>${fn:trim(s)}</a>
								</c:forEach>
							</div>
							<!--高级合作者-->
							<div class="author-coAuthor" id="${AuthorPage.coBigAuthors}"></div>
						</div>
					</div>
				</div>
			</c:forEach>
		</div>
		
		<div class="footer">
			<div class="message"></div>
			<div class="pagebar list-page mr5">
				<span class="currentPage">${myPage.currentPage} </span>/<spanclass="totalPage"> ${myPage.totalPage} </span> <a class="firstPage" href="search.action?query=<s:property value="query" />&pno=1&sortby=${sortby}&terms=<s:property value="terms" />">首页</a>
				<a class="prePage" href="search.action?query=<s:property value="query" />&pno=${myPage.prePage}&sortby=${sortby}&terms=<s:property value="terms" />">上一页</a>
				<a class="nextPage" href="search.action?query=<s:property value="query" />&pno=${myPage.nextPage}&sortby=${sortby}&terms=<s:property value="terms" />">下一页</a>
				<a class="lastPage" href="search.action?query=<s:property value="query" />&pno=${myPage.totalPage}&sortby=${sortby}&terms=<s:property value="terms" />">尾页</a>
			</div>
		</div>
		<!-- end搜索结果 -->
	</div>
	<!-- end main content -->
</body>
</html>
