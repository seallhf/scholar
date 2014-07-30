$(document).ready(function() {
	addAuthorPaper();
	addCoAuthorDetails();
	addFarmousPaperDetails();
	addAttributeHeader();
});

function addAuthorPaper() {
	$(".author_paper")
			.each(
					function() {
						var aid = $(this).attr("id"); // 这里的value就是每一个input的value值~
						var html = "";
						if (aid != null && aid != "") {
							$
									.ajax({
										url : "paperType.action", // 后台处理程序
										data : {
											aid : aid
										},
										type : "post", // 数据发送方式
										async : false,
										dataType : "json", // 接受数据格式
										error : function() {
											alert("服务器没有返回数据，可能服务器忙，请重试");
										},
										success : function(json) {
											html = "<br/><table class=\"table_conf\" ><tr class=\"conf\">";
											var conf = "<td width=15%>会议：</td>	<td width=20%>A - "
													+ json["aconf"]
													+ "</td><td width=20%>B - "
													+ json["bconf"]
													+ "</td><td width=20%>C - "
													+ json["cconf"] + "</td>";
											html = html + conf;
											html += "</tr><tr class=\"jounal\">";
											var jounal = "<td width=15%>期刊：</td>	<td width=20%>A - "
													+ json["ajounal"]
													+ "</td><td width=20%>B - "
													+ json["bjounal"]
													+ "</td><td width=20%>C - "
													+ json["cjounal"] + "</td>";
											html = html + jounal;
											html = html + "</tr></table>";
										}
									});
							$(this).html(html);
						}
					});
}

function addCoAuthorDetails() {
	$(".author_coAuthor")
			.each(
					function() {
						var aid = $(this).attr("id"); // 这里的value就是每一个input的value值~
						var html = "";
						if (aid != null && aid != "") {
							$
									.ajax({
										url : "coAuthor.action", // 后台处理程序
										data : {
											aid : aid
										},
										type : "post", // 数据发送方式
										async : false,
										dataType : "json", // 接受数据格式
										error : function() {
											alert("服务器没有返回数据，可能服务器忙，请重试");
										},
										success : function(json) {
											html = "&nbsp;&nbsp;<i class=\"icon-user\"></i>&nbsp;&nbsp;"
													+ "合作者<a href = \"http://scholar.google.com.cn/citations?hl=zh-CN&user="
													+ json["aid"]
													+ "&view_op=list_works&pagesize=100\">"
													+ json["name"]
													+ "</a>&nbsp;(<strong>"
													+ json["citeindex"]
													+ "</strong>)";
										}
									});
							$(this).html(html);
						} else {
							$(this).css("height", "0px");
						}
					});
}

function addFarmousPaperDetails() {
	$(".author_farmousPaper")
			.each(
					function() {
						var pid = $(this).attr("id"); // 这里的value就是每一个input的value值~
						var html = "";
						if (pid != null && pid != "") {
							$
									.ajax({
										url : "farmousPaper.action", // 后台处理程序
										data : {
											pid : pid
										},
										type : "post", // 数据发送方式
										async : false,
										dataType : "json", // 接受数据格式
										error : function() {
											alert("服务器没有返回数据，可能服务器忙，请重试");
										},
										success : function(json) {
											html = "<i style=\"float:left;margin-left: 10px\" class=\"icon-star\" /><ui style=\"float:left;margin-left: 10px;width:90%\"><li>"
													+ "代表文章&nbsp;\""
													+ json["title"]
													+ "\"&nbsp;(<strong>"
													+ json["citeIndex"]
													+ "</strong>)</li>"
													+ "<li style=\"font-size:12px;color:#9D9D9D\">"
													+ json["jounalName"]
													+ "</li></ui>";
										}
									});
							$(this).html(html);
						} else {
							$(this).css("height", "0px");
						}
					});
}

function contains(list,element) {

	for (var i = 0; i < list.length; i++) {
		if (list[i] == element) {
			return true;
		}
	}
	return false;
};

function addAttributeHeader() {
	var query = $("#searchQuery").val();
	var terms = $("#searchTerms").val().split(';');
	var checked = new Array();
	for (var i = 0; i < terms.length; i++) {
		var term = terms[i];
		if (term != null)
			checked.push(term.split(',')[1]);
	}
	//alert(checked);
	var html = "";
	{
		$
				.ajax({
					url : "searchClassification.action", // 后台处理程序
					data : {
						query : query
					},
					type : "post", // 数据发送方式
					async : false,
					dataType : "json", // 接受数据格式
					error : function() {
						alert("服务器没有返回数据，可能服务器忙，请重试");
					},
					success : function(json) {
						html = "&nbsp;&nbsp;";
						var a = json["company"];
						for ( var term in a) {
							if (contains(checked,term))
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input type=\"checkbox\" onchange=\"changefunction()\" checked=\"checked\" name=\"company\" id = \"company\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
							else
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input type=\"checkbox\" onchange=\"changefunction()\" name=\"company\" id = \"company\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
						}
						$(".checkbox_company").html(html);
						html = "&nbsp;&nbsp;";
						var a = json["position"];
						for ( var term in a) {
							if (contains(checked,term))
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input style =\"width:10%\" type=\"checkbox\" checked=\"checked\" onchange=\"changefunction()\" name=\"position\" id = \"position\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
							else
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input style =\"width:10%\" type=\"checkbox\" onchange=\"changefunction()\" name=\"position\" id = \"position\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
						}
						$(".checkbox_position").html(html);
						html = "&nbsp;&nbsp;";
						var a = json["system"];
						for ( var term in a) {
							if (contains(checked, term))
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input style =\"width:10%\" type=\"checkbox\" checked=\"checked\" onchange=\"changefunction()\" name=\"system\" id = \"system\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
							else
								html = html
										+ "<div style =\"width:17%;float:left;text-align: left;\"><input style =\"width:10%\" type=\"checkbox\" onchange=\"changefunction()\" name=\"system\" id = \"system\" value=\""
										+ term + "\" />" + term + "(" + a[term]
										+ ")&nbsp;&nbsp;</div>";
						}
						
						$(".checkbox_system").html(html);
					}
				});
	}

}

function changefunction() {
	var terms = [];
	var company = document.getElementsByName("company");
	for (var i = 0; i < company.length; i++) {
		if (company[i].checked) {
			terms += ("location," + company[i].value + ";");
		}
	}
	var position = document.getElementsByName("position");
	for (var i = 0; i < position.length; i++) {
		if (position[i].checked) {
			terms += ("location," + position[i].value + ";");
		}
	}
	var system = document.getElementsByName("system");
	for (var i = 0; i < system.length; i++) {
		if (system[i].checked) {
			terms += ("location," + system[i].value + ";");
		}
	}
	document.getElementById('searchTerms').value = terms;
	//alert($("#searchTerms").val());
};
