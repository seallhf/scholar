function getmulti(oldstr) {
	var newstr = oldstr;// 字段内容
	var i = 0, j = 0, t = 1;// 判断是否有多个部分内容
	var foreindex;// 记录前一个分隔符的位置
	var index, depchar;// 记录当前分隔符及其位置
	var linkstr = "";// 链接方式
	var astr = new Array(10);
	var index1 = newstr.indexOf(",");
	var index2 = newstr.indexOf(";");
	var index3 = newstr.indexOf("%");
	var index4 = newstr.indexOf("，");
	var index5 = newstr.indexOf("；");
	var index6 = newstr.indexOf(" ");
	function LTrim(str) {
		for (var x = 0; str.charAt(x) == " "; x++)
			;
		return str.substring(x, str.length);
	}
	if (index1 != -1) {
		index = index1;
		depchar = ",";
	} else if (index2 != -1) {
		index = index2;
		depchar = ";";
	} else if (index3 != -1) {
		index = index3;
		depchar = "%";
	} else if (index4 != -1) {
		index = index4;
		depchar = "，";
	} else if (index5 != -1) {
		index = index5;
		depchar = "；";
	} else if (index6 != -1) {
		index = index6;
		depchar = " ";
	} else {
		t = 0;
		index = -1;
		linkstr = "<a href='\search?query="+newstr+"'>" + newstr + "</a>";
	}
	foreindex = 0;
	while (index != -1) {
		index = newstr.indexOf(depchar, foreindex);
		if (index == foreindex) {
			foreindex = index + 1;
			continue;
		}
		if (index != -1) {
			astr[j] = newstr.substring(foreindex, index);
			astr[j] = LTrim(astr[j]);
			foreindex = index + 1;
			j = j + 1;
		}

	}
	astr[j] = newstr.substring(foreindex);
	if (t == 1) {
		for (i = 0; i < j + 1; i++) {
			linkstr = linkstr + "<a href='\search?query="+astr[i]+"'>" + astr[i]
					+ "</a>&nbsp;";
		}
	}
	return linkstr;
}