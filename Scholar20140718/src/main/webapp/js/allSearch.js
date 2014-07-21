function checkWord(page){
    var keyword = $("#ms_keyWord").val();
    if(keyword==null || keyword==""){
        $(".ms_keyWord").focus();
        alert("请输入关键词检索！");return;
    }
    var dataSource = $(".dataSource").val();
//    var page=1;
    var targetWorkArea = "all";
    var targetSalary = "all";
    var workExperience = "all";
    var degree = "all";
    var targetWorkType = "all";
    $(".result_Allcount").html("<p class='no_found'><img src='"+$("#base").val()+"intern/img/loading.gif'/>正在查询...</p>");$(".rs_content").css("display","none");
    $.ajax({
        type:'post',
        url :$("#base").val()+"search/searchUser",
        data:{keyword:keyword,data_source:dataSource,page:page},
        success:function(data){
              eval(data);
              var msg="";
              var msgCount = "";

              if (!data.flag) {
                msgCount = "<p class='no_found'>没有找到 <a style='color:red;'>" + keyword+ "</a> 相关信息</p>";
                $(".pageTool").css("display","none");
              } else {
                  var user = data.data;
                  var count = user.count;
                  var page = user.page;
                  var infos =  user.users;
                  if(null== infos || infos.length==0){
                      msgCount = "<p class='no_found'>没有找到 <a style='color:red;'>" + keyword+ "</a> 相关信息</p>";
                      $(".pageTool").css("display","none");
                  }else{
                      initializePageNav(page,10,count);
                      for (var i in infos){
                           msg += '<div class="rs_one"><div class="ro_left"><p class="ro_img"><img src="'
                               +infos[i].user_img+'"/></p><p class="ro_connect" onclick="alert(1)">收藏</p></div><div class="ro_right"><div class="ro_baseInfo"><p class="ro_name">'
                               +'<a href="javascript:window.open(\''+$("#base").val()+'search/userDispatch?uid='+infos[i].uid+'&data_source='+infos[i].data_source+'\');void(0);">'
                               //'<a href="javascript:window.location.href=\''+$("#base").val()+'intern/userDetail.jsp?uid='+infos[i].uid+'&type='+infos[i].data_source+'\';">'
                               //<a href="javascript:alert(\''+infos[i].uid+'------------'+infos[i].data_source+'\')">'+infos[i].user_name
                               +infos[i].user_name+'</a></p><p class="ro_job">'+infos[i].recent_work+'</p><p class="ro_location">'+infos[i].location
                               +'</p></div><div class="ro_description"><p class="ro_descTitle">简介:</p><p class="ro_descContent">'+infos[i].description
                               +'</p></div><div class="ro_trait">';
                           //获取用户标签
                           for(var j in infos[i].tags){
                               msg +='<p>'+infos[i].tags[j]+'</p>';
                           }
                           msg +='</div></div></div>';
                      }
                  }

                  if(count==0){
                      $(".result_Allcount").html(msgCount); msg="";
                  }else
                      $(".result_Allcount").html("为您找到了"+count+"个与"+keyword+"相关的人");
              }
              $(".rs_content").css("display","block").html(msg);
        }
    });
}