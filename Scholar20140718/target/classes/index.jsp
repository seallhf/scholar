<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String home = "http://scholar.google.com.cn/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>人才搜索</title>
<link
	href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.0/css/bootstrap-combined.min.css"
	rel="stylesheet">
</head>
<body>
	<div align="center" style="margin-top: 150px;">
		<img src="<%=basePath%>img/logo.jpg">
	</div>
	<div align="center" style="margin-top: 20px;">
		<s:form action="search" class="form-search" display="inline-block">
			<input
				style="width: 600px; height: 20px; display: inline-block; margin-bottom: 0; vertical-align: middle;"
				type="text" class="form-control" placeholder="请输入查询关键词..."
				name="query">
			<button
				style="width: 90px;height: auto; display: inline-block; margin-bottom: 0; vertical-align: middle;"
				type="submit" class="btn btn-primary">查询</button>
		</s:form>
	</div>
</body>
</html>