$(document).ready(function() {
	addAuthorPaper();
	addCoAuthorDetails();
	addFarmousPaperDetails();
	addAttributeHeader();
	showmorecondition();
});

function addAuthorPaper() {
	$(".author-paper")
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
											html = "<span class=\"work-num-icon\"></span>"
											      + "<span class=\"mr5\">论文数量:</span>"
											      + "["
											      + "<label><span>会议:</span>"
											      + "<span class=\"ml8\">A级-<span class=\"lighting-color\">"
											      + json["aconf"]
											      + "</span></span>"
											      + "<span class=\"ml8\">B级-<span class=\"lighting-color\">"
											      + json["bconf"]
											      + "</span></span>"
											      + "<span class=\"ml8\">C级-<span class=\"lighting-color\">"
											      + json["cconf"]
											      + "</span></span>"
											      + "</label>"
											      + "<label class=\"ml8\"><span class=\"ml8\">期刊:</span>"
											      + "<span class=\"ml8\">A级-<span class=\"lighting-color\">"
											      + json["ajounal"]
											      + "</span></span>"
											      + "<span class=\"ml8\">B级-<span class=\"lighting-color\">"
											      + json["bjounal"]
											      + "</span></span>"
											      + "<span class=\"ml8\">C级-<span class=\"lighting-color\">"
											      + json["cjounal"]
											      + "</span></span>"
											      + "</label>"
											      +"]"
										}
									});
							$(this).html(html);
						}
					});
}

function addCoAuthorDetails() {
	$(".author-coAuthor")
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
											html = "<span class=\"con-user-icon\"></span>"
													+ "<span>高级合作者:</span><a href = \"http://scholar.google.com.cn/citations?hl=zh-CN&user="
													+ json["aid"]
													+ "&view-op=list-works&pagesize=100\">"
													+ json["name"]
													+ "</a>"
													+ '[论文引用总数:&nbsp;&nbsp;<span class="lighting-color">'
													+ json["citeindex"]
													+ "</span>]";
										}
									});
							$(this).html(html);
						} else {
							$(this).css("height", "0px");
						}
					});
}

function addFarmousPaperDetails() {
	$(".author-farmousPaper")
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
											var titlevalue = json["title"];
											html = "<div class=\"inline-block-css\"><span class=\"reprent-work-icon\"></span>"
											       + "<span>代表文章:</span>"
											       + "<span class=\"ml5\">[引用次数:<span class=\"lighting-color ml5\">"
											       + json["citeIndex"]
											       + "</span>] </span>"
											       + '<a class="title-link-css" title="'+ titlevalue +'">' + titlevalue + "</a>"
											       + "</div>"
											       + "<div class=\"ml77\"><span>发表期刊:</span>"
											       + "<span class=\"ml5\">" + json["jounalName"] + "</span>"
											       + "</div>"
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
}


// 2014/8/19 edited by dym
function addAttributeHeader() {
	var query = $("#searchQuery").val();
	var terms = $("#searchTerms").val().split(';');
	var checked = new Array();
	for (var i = 0; i < terms.length; i++) {
		var term = terms[i];
		if (term != null){

			checked.push(term.split(',')[1]);
		}
	}

	$.ajax({
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
			var comarray = json["company"];
			var posarray = json["position"];
			var sysarray = json["system"];
			var showUl = $('#free-condition-ul');
			var comcounter = 0;
			var poscounter = 0;
			var syscounter = 0;
			$('#flagCompany').empty();
			$('#flagPosition').empty();
			$('#flagSystem').empty();
			showUl.empty();

			for ( var term in comarray) {
				comcounter++;
				if (contains(checked,term)){
					showUl.append(
						'<li name="'+ term +'" class="free-condition-css" onclick="removecondition(1, this);">' +
							'<a>' +
								'<span>公司:</span>' +
								'<span class="lighting-color ml2">'+ term +'</span>' +
								'<span class="close-css">x</span>' +
							'</a>' +
						'</li>'
					);
					if(comcounter <= 6){
						$('#flagCompany').append(
						'<li name="'+ term +'" class="disabed-css">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ comarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagCompany').append(
						'<li name="'+ term +'" class="disabed-css otherdisplay">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ comarray[term] +')</span>'+
						'</li>'
						);
					}
					
				}else{

					if(comcounter <= 6){
						$('#flagCompany').append(
						'<li name="'+ term +'" class="selected-hover-css" onclick="addcondition(1,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ comarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagCompany').append(
						'<li name="'+ term +'" class="otherdisplay selected-hover-css" onclick="addcondition(1,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ comarray[term] +')</span>'+
						'</li>'
						);
					}

				}
			}
			
			for ( var term in posarray) {
				poscounter++;
				if (contains(checked,term)){
					showUl.append(
						'<li name="'+ term +'" class="free-condition-css" onclick="removecondition(2, this);">' +
							'<a>' +
								'<span>职位:</span>' +
								'<span class="lighting-color ml2">'+ term +'</span>' +
								'<span class="close-css">x</span>' +
							'</a>' +
						'</li>'
					);
					if(poscounter <= 6){
						$('#flagPosition').append(
						'<li name="'+ term +'" class="disabed-css">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ posarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagPosition').append(
						'<li name="'+ term +'" class="disabed-css otherdisplay">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ posarray[term] +')</span>'+
						'</li>'
						);
					}
					
				}else{
					if(poscounter <= 6){
						$('#flagPosition').append(
						'<li name="'+ term +'" class="selected-hover-css" onclick="addcondition(2,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ posarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagPosition').append(
						'<li name="'+ term +'" class="otherdisplay selected-hover-css" onclick="addcondition(2,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ posarray[term] +')</span>'+
						'</li>'
						);
					}
				}
			}
			
			for ( var term in sysarray) {
				syscounter++;
				if (contains(checked,term)){

					showUl.append(
					'<li name="'+ term +'" class="free-condition-css" onclick="removecondition(3, this);">' +
						'<a>' +
							'<span>领域:</span>' +
							'<span class="lighting-color ml2">'+ term +'</span>' +
							'<span class="close-css">x</span>' +
						'</a>' +
					'</li>'
					);
					if(syscounter <= 6){
						$('#flagSystem').append(
						'<li name="'+ term +'" class="disabed-css">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ sysarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagSystem').append(
						'<li name="'+ term +'" class="disabed-css otherdisplay">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ sysarray[term] +')</span>'+
						'</li>'
						);
					}

				}else{
					if(syscounter <= 6){
						$('#flagSystem').append(
						'<li name="'+ term +'" class="selected-hover-css" onclick="addcondition(3,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ sysarray[term] +')</span>'+
						'</li>'
						);
					}else{
						$('#flagSystem').append(
						'<li name="'+ term +'" class="otherdisplay selected-hover-css" onclick="addcondition(3,this);">'+ 
							'<a title="'+ term +'">'+ term +'</a>'+
							'<span>('+ sysarray[term] +')</span>'+
						'</li>'
						);
					}
				}
				
			}
			if(comcounter > 6){
				$('#flagCompany').next().text('更多');
			}
			if(poscounter > 6){
				$('#flagPosition').next().text('更多');
			}
			if(syscounter > 6){
				$('#flagSystem').next().text('更多');
			}
		}
	});
}


// 2014/8/12 written by dym

function addcondition(flag, _this) {  //选择条件
	var elemvalue = $(_this).attr('name');
	var terms = [];
	
	if(flag == "1"){

		$('#free-condition-ul').append(
			'<li name="'+ elemvalue +'" class="disabed-css free-condition-css" onclick="removecondition(1, this);">' +
				'<a>' +
					'<span>公司:</span>' +
					'<span class="lighting-color ml2">'+ elemvalue +'</span>' +
					'<span class="close-css">x</span>' +
				'</a>' +
			'</li>'
		);

	}else if(flag == "2"){
 
		$('#free-condition-ul').append(
			'<li name="'+ elemvalue +'" class="disabed-css free-condition-css" onclick="removecondition(2, this);">' +
				'<a>' +
					'<span>职位:</span>' +
					'<span class="lighting-color ml2">'+ elemvalue +'</span>' +
					'<span class="close-css">x</span>' +
				'</a>' +
			'</li>'
		);

	}else if(flag == "3"){

		$('#free-condition-ul').append(
			'<li name="'+ elemvalue +'" class="disabed-css free-condition-css" onclick="removecondition(3, this);">' +
				'<a>' +
					'<span>领域:</span>' +
					'<span class="lighting-color ml2">'+ elemvalue +'</span>' +
					'<span class="close-css">x</span>' +
				'</a>' +
			'</li>'
		);
	}

	var selectedelem = $('#free-condition-ul').find('span.lighting-color');
	for(var i=0; i<selectedelem.length; i++){
        var value = $(selectedelem[i]).text();
		terms += ("location," + value + ";");
	}

	$('#searchTerms').attr('value',terms);
	$('#searchbutton').click();
}

function removecondition(flag2, _this){  // 清除条件
	var removevalue = $(_this).attr('name');
	var terms = [];
	$(_this).remove();

	/*if(flag2 == '1'){
		$('#flagCompany').find('li:contains(' + removevalue +')').removeClass('hideblock');
	}else if(flag2 == '2'){
		$('#flagPosition').find('li:contains('+removevalue +')').removeClass('hideblock');
	}else if(flag2 == '3'){
		$('#flagSystem').find('li:contains('+ removevalue +')').removeClass('hideblock');
	}*/

	var selectedelem = $('#free-condition-ul').find('span.lighting-color');
	for(var i=0; i<selectedelem.length; i++){
        var value = $(selectedelem[i]).text();
		terms += ("location," + value + ";");
	}

	$('#searchTerms').attr('value',terms);
	$('#searchbutton').click();
}

//点击“更多”
function showmorecondition(){
	$('.more-show-css').click(function(){
		var textvalue = $(this).text();
		if(textvalue == '更多'){
			$(this).prev().find('li.otherdisplay').removeClass('otherdisplay').addClass('movetoshow');
			$(this).text('收起');
		}else if(textvalue == '收起'){
			$(this).prev().find('li.movetoshow').addClass('otherdisplay').removeClass('movetoshow');
		    $(this).text('更多');
		}	
	});
}




