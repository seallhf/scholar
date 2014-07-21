function tagSplit(){   
         datastr="2,2,3,5,6,6";      
  var str= new Array();   
  
  str=datastr.split(",");      
    for (i=0;i<str.length ;i++ )   
    {   
        document.write(str[i]+"<br/>");   
    }   
}   