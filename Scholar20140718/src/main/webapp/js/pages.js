/*
 * iCurrPage 当前页
 * iPageSize 每页条数
 * iProCount 数据总数
 * */
function initializePageNav(iCurrPage,iPageSize,iProCount){
    var b = ((iProCount%iPageSize)==0);
    var iPageCount = parseInt(iProCount/iPageSize)+(b?0:1);
    if (iCurrPage > iPageCount) return;
    iCurrPage = parseInt(iCurrPage);
//    var sTemp1 = "<p class='pt_left'>找到&nbsp;"+ iProCount +"&nbsp;条数据</p>";
    var sTemp = "<div class='pt_center'><p onclick='javascript:checkWord(1);'>首页</p>";
    if(iCurrPage==1 || iCurrPage ==0){
        sTemp += "<p onclick='javascript:checkWord("+ parseInt(iCurrPage)+");'>上一页</p>";
    }else if(parseInt(iCurrPage)>1){
        sTemp +="<p onclick='javascript:checkWord("+ parseInt(iCurrPage-1)+");'>上一页</p>";
    }

    if(iPageCount>10 && iPageCount-iCurrPage>5 && iCurrPage>5){
        sTemp += "<p onclick='javascript:checkWord("+parseInt(iCurrPage-4)+");'>"+parseInt(iCurrPage-4)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage-3)+");'>"+parseInt(iCurrPage-3)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage-2)+");'>"+parseInt(iCurrPage-2)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage-1)+");'>"+parseInt(iCurrPage-1)+"</p>"
            +"<p onclick='javascript:checkWord("+iCurrPage+");' style='color:#92c3d1;'>"+iCurrPage+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage+1)+");'>"+parseInt(iCurrPage+1)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage+2)+");'>"+parseInt(iCurrPage+2)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage+3)+");'>"+parseInt(iCurrPage+3)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage+4)+");'>"+parseInt(iCurrPage+4)+"</p>"
            +"<p onclick='javascript:checkWord("+parseInt(iCurrPage+5)+");'>"+parseInt(iCurrPage+5)+"</p>";

    }else if(iPageCount>10 && iPageCount-iCurrPage>5 && iCurrPage<=5){
        var check = 1;
        for(var j =1;j<iCurrPage;j++){
            sTemp +="<p onclick='javascript:checkWord("+j+");'>"+j+"</p>";
            check++;
        }
        for(var k=0;k<=(10-check);k++){
            if(k==0)
                sTemp += "<p onclick='javascript:checkWord("+parseInt(check+k)+");' style='color:#92c3d1;'>"+parseInt(check+k)+"</p>";
            else
                sTemp += "<p onclick='javascript:checkWord("+parseInt(check+k)+");'>"+parseInt(check+k)+"</p>";
        }
    }else if(iPageCount>10 && parseInt(iPageCount-iCurrPage)<=5){

        for(var j=9-parseInt(iPageCount-iCurrPage);j>0;j--){
            sTemp +="<p onclick='javascript:checkWord("+parseInt(iCurrPage-j)+");'>"+parseInt(iCurrPage-j)+"</p>";
        }
        for(var k=0;k<=parseInt(iPageCount-iCurrPage);k++){
            if(k==0)
                sTemp += "<p onclick='javascript:checkWord("+parseInt(iCurrPage+k)+");' style='color:#92c3d1;'>"+parseInt(iCurrPage+k)+"</p>";
            else
                sTemp += "<p onclick='javascript:checkWord("+parseInt(iCurrPage+k)+");'>"+parseInt(iCurrPage+k)+"</p>";
        }
    }else if(iPageCount<=10){
        var check=1;
        for(var j=1;j<iCurrPage;j++){
            sTemp +="<p onclick='javascript:checkWord("+j+",2);'>"+j+"</p>";
            check++;
        }
        for(var k=0;k<=parseInt(iPageCount-check);k++){
            if(k==0)
                sTemp += "<p onclick='javascript:checkWord("+parseInt(check+k)+");' style='color:#92c3d1;'>"+parseInt(check+k)+"</p>";
            else
                sTemp += "<p onclick='javascript:checkWord("+parseInt(check+k)+");'>"+parseInt(check+k)+"</p>";
        }
    }

    if(iPageCount==iCurrPage){
        sTemp +="<p onclick='javascript:checkWord("+parseInt(iCurrPage)+");'>下一页</p>";
    }else if(iPageCount>iCurrPage){
        sTemp +="<p onclick='javascript:checkWord("+parseInt(iCurrPage+1)+");'>下一页</p>";
    }
    sTemp +="<p onclick='javascript:checkWord("+ parseInt(iPageCount)+");'>末页</p></div>";
//    var sTemp2 = "</div><div class='pt_right'><span>跳转</span><input type='text' name='pages' id='pages' onkeypress='if(event.keyCode==13) {checkWord(this.value,2);}'/><span>/&nbsp;&nbsp;"+iPageCount+"&nbsp;页</span></div>";
//    $(".pageTool").css("display","block").html(sTemp1+ sTemp+ sTemp2);
    $(".pageTool").css("display","block").html(sTemp);
}