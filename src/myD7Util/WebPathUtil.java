package myD7Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.jar.JarFile;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

import org.jsoup.nodes.Element;
/**
*
* @author xuecf
*/
public class WebPathUtil{
   //以request对象生成的本类的实例所有方法都能用,以application对象生成的本类的实例只能用getRealPath()方法
   public HttpServletRequest request;
   public ServletContext application;
   //
   public static String getUri_SmallImage(String bigImageUriOrUrlOrSaveName,boolean validateSmallImageExist,HttpServletRequest request){
       WebPathUtil wpu=WebPathUtil.getInstance(request);
       return wpu.getVirtualPathUri(wpu.getSmallImageVirtualPath(bigImageUriOrUrlOrSaveName,validateSmallImageExist));
   }
   public static String getUrl_SmallImage(String bigImageUriOrUrlOrSaveName,boolean validateSmallImageExist,HttpServletRequest request){
       WebPathUtil wpu=WebPathUtil.getInstance(request);
       return wpu.getVirtualPathUrl(wpu.getSmallImageVirtualPath(bigImageUriOrUrlOrSaveName,validateSmallImageExist));
   }
   public static String getVirtualPath_SmallImage(String bigImageUriOrUrlOrSaveName,boolean validateSmallImageExist,HttpServletRequest request){
       return WebPathUtil.getInstance(request).getSmallImageVirtualPath(bigImageUriOrUrlOrSaveName,validateSmallImageExist);
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   //根据上传的大图片的名称或VirtualPath,来获取它的缩略图的VirtualPath
   //如果该大图片不存在缩略图,validateSmallImageExist参数为true,则返回大图片的VirtualPath,以保证图片能够显示
   public String getSmallImageVirtualPath(String bigImageUriOrUrlOrSaveName,boolean validateSmallImageExist){
       String SmallImageVirtualPath="";
       
       String bigImageVirtualPath=getVirtualPath$UriOrUrlOrUploadFile(bigImageUriOrUrlOrSaveName);
       //根据bigImageVirtualPath获取smallImageVirtualPath
       String SmallImageNeedAddName=SysParam.getString("Upload_2_CreateSmallImage_SmallImageNeedAddName");
       AssertUtil.Assert(SmallImageNeedAddName!=null,"无法从SysParam中获取Upload_2_CreateSmallImage_SmallImageNeedAddName参数的值!");
       SmallImageVirtualPath=bigImageVirtualPath.substring(0,bigImageVirtualPath.lastIndexOf("."))+SmallImageNeedAddName;
       //判断缩略图存不存在,如果不存在则返回大图片的url
       //如果不想判断缩略图存不存在,而是想不管缩略图存不存在都返回缩略图的url则可以注释掉以下的这一段
       if(validateSmallImageExist){
           if(!new File(getRealPath(SmallImageVirtualPath)).exists()){
               SmallImageVirtualPath=bigImageVirtualPath;
           }
       }  
       //      
       return SmallImageVirtualPath;
   }  
   //getVirtualPath$UriOrUrlOrUploadFileName(String uriOrUrlOrUploadFileName)的静态版
   //主要用在DownloadUtil中来获取下载文件的VirtualPath
   public static String getVirtualPath_UriOrUrlOrUploadFile(String uriOrUrlOrUploadFile,HttpServletRequest request){        
       return WebPathUtil.getInstance(request).getVirtualPath$UriOrUrlOrUploadFile(uriOrUrlOrUploadFile);
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   //该方法可以获取一个Uri或Url或上传文件的VirtualPath
   //上传文件可以是在数据库中保存的文件名如:admin/a.jpg,也可以是上传文件的VirtualPath如:/UserUpload/admin/a.jpg
   //主要用在DownloadUtil中来获取下载文件的VirtualPath
   public String getVirtualPath$UriOrUrlOrUploadFile(String uriOrUrlOrUploadFile){
       String returnVirtualPath="";
       
       String vp=getVirtualPath$Url(uriOrUrlOrUploadFile);
       if(!vp.equals("")){//参数是url
           returnVirtualPath=vp;
           return returnVirtualPath;
       }
       vp=getVirtualPath$Uri(uriOrUrlOrUploadFile);
       if(!vp.equals("")){//参数是uri
           returnVirtualPath=vp;
           return returnVirtualPath;
       }
       //参数是上传文件名,如:admin/a.jpg,并且不包括/UserUpload,所以要给文件添加/UserUpload/
       String FileSaveRootPath="";
       
       FileSaveRootPath=SysParam.getString("Upload_1_FileSaveRootPath");
       AssertUtil.Assert(FileSaveRootPath!=null,"无法从SysParam中获取Upload_1_FileSaveRootPath参数的值!");
       if(FileSaveRootPath.lastIndexOf("/")==FileSaveRootPath.length()-1){
           FileSaveRootPath=FileSaveRootPath.substring(0,FileSaveRootPath.length()-1);
       }
       returnVirtualPath=FileSaveRootPath+"/"+uriOrUrlOrUploadFile;        

       return returnVirtualPath;
   }
   
   //getVirtualPath$Uri(String Uri)的静态版
   public static String getVirtualPath_Uri(String Uri,HttpServletRequest request){
       return WebPathUtil.getInstance(request).getVirtualPath$Uri(Uri);
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   public String getVirtualPath$Uri(String Uri){
       String returnVirtualPath="";
       String contextUri=getContextUri();
       if(contextUri.equals("/")&&Uri.startsWith("/")){
           returnVirtualPath=Uri;
       }else if(Uri.startsWith(contextUri)){
           returnVirtualPath=Uri.replace(contextUri,"");
       }else if(Uri.startsWith("/")){
           returnVirtualPath=Uri;
       }

       return returnVirtualPath;
   }
   //getVirtualPath$Url(String Url)的静态版
   public static String getVirtualPath_Url(String Url,HttpServletRequest request){
       return WebPathUtil.getInstance(request).getVirtualPath$Url(Url);
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   public String getVirtualPath$Url(String Url){
       String returnVirtualPath="";
       String contextUrl=getContextUrl();
       //RegexUtil ru=RegexUtil.getInstance_CASE_INSENSITIVE();
       //contextUrl=RegexUtil.encodeStrEscapePattern(contextUrl);
       //if(ru.findMatchedStr(Url,contextUrl)!=null){//参数是url
           //returnVirtualPath=ru.replaceFirstMatchedStr(Url,contextUrl,"");
       //}
       if(Url.startsWith(contextUrl)){
           returnVirtualPath=Url.replace(contextUrl,"");
       }

       return returnVirtualPath;
   }
   //getVirtualPathUri(String virtualPath)的静态版
   public static String getUri_VirtualPath(String virtualPath,HttpServletRequest request){
       return WebPathUtil.getInstance(request).getVirtualPathUri(virtualPath);
   }
   //getVirtualPathUrl(String virtualPath)的静态版
   public static String getUrl_VirtualPath(String virtualPath,HttpServletRequest request){
       return WebPathUtil.getInstance(request).getVirtualPathUrl(virtualPath);
   }
   //该静态方法可以获取web项目中任意文件或文件夹的绝对路径,virtualPath相对于应用程序根目录,如"/WEB-INF/WebParameters.xml".
   public static String getAbsolutePath_VirtualPath(String virtualPath){
	   String vp=virtualPath.replace('/',File.separatorChar);
       String absolutePath="";
       WebPathUtil wpu=new WebPathUtil();
       URL pathUrl=wpu.getClass().getResource("/");//取classpath的根目录作为参照,这样返回的路径中包含F:\myUtil\build\web\WEB-INF\classes
       File f=null;
       try{
           f=new File(pathUrl.toURI());
       }catch(Exception e){
    	   throw new RuntimeException("@@@@@WebPathUtil.getAbsolutePath_VirtualPath(\""+virtualPath+"\")抛出异常@@@@@@"+e.getMessage());
           //System.out.println("@@@@@WebPathUtil.getAbsolutePath_VirtualPath(\""+virtualPath+"\")抛出异常@@@@@@"+e.getMessage());
       }
       absolutePath=f.getAbsolutePath();//此时absolutePath可能为F:\myUtil\build\web\WEB-INF\classes  
       int webinf=absolutePath.toUpperCase().indexOf("WEB-INF");
       //
       if(webinf<0){//处在eclipse开发环境中，此时absolutePath可能为F:\workspace\21bride\build\classes	   
    	   absolutePath=absolutePath.split("\\"+File.separatorChar+"build"+"\\"+File.separatorChar)[0]+File.separatorChar+"WebContent"+vp;
       }else{
    	   absolutePath=absolutePath.substring(0,webinf-1);//此时absolutePath为F:\myUtil\build\web
           absolutePath=absolutePath+vp;//此时absolutePath为F:\myUtil\build\web\WEB-INF\WebParameters.xml达到要求
       }
       //
       return absolutePath;
   }
   //该静态方法可以获取任意一个处在classpath(classes目录)中的文件的绝对路径,classVirtualPath相对于classpath根目录(classes目录),如"/myUtil/myWebParameter/WebParameters.xml".
   //classpath指的是classes目录非lib目录,lib目录中的jar包里面的文件由于包含在.jar文件中所以不能用该方法直接获取它的绝对路径)
   public static String getAbsolutePath_ClassVirtualPath(String classVirtualPath){
       String absolutePath="";
       WebPathUtil wpu=new WebPathUtil();
       URL pathUrl=wpu.getClass().getResource("/");//取classpath的根目录作为参照,这样返回的路径中包含F:\myUtil\build\web\WEB-INF\classes
       //URL pathUrl=wpu.getClass().getResource(classVirtualPath);
       File f=null;
       try{
           f=new File(pathUrl.toURI());
       }catch(Exception e){
    	   throw new RuntimeException("@@@@@WebPathUtil.getAbsolutePath_ClassVirtualPath(\""+classVirtualPath+"\")抛出异常@@@@@@"+e.getMessage());
           //System.out.println("@@@@@WebPathUtil.getAbsolutePath_ClassVirtualPath(\""+classVirtualPath+"\")抛出异常@@@@@@"+e.getMessage());
       }
       absolutePath=f.getAbsolutePath();//此时absolutePath为F:\myUtil\build\web\WEB-INF\classes或F:\myUtil\build\web\WEB-INF\lib
       String vp=classVirtualPath.replace('/',File.separatorChar);
       absolutePath=absolutePath+vp;//此时absolutePath为F:\myUtil\build\web\WEB-INF\classes\myUtil\myWebParameter\WebParameters.xml
                                    //或F:\myUtil\build\web\WEB-INF\lib\myUtil\myWebParameter\WebParameters.xml
       //
       return absolutePath;
   }
   //该静态方法获取jar包中某个文件的输入流
   //jarFileName可以是某jar包的绝对路径,也可以只是jar包的名字,如果只是jar包的名字则认为该jar包处在web的lib目录中
   //fileVirtualPathInJar要读取的文件在jar包中的虚拟路径如:"/myUtil/myWebParameter/WebParameters.xml".
   public static InputStream getInputStream_FileInJar(String jarFileName,String fileVirtualPathInJar) throws IOException{
       InputStream is=null;
       if(jarFileName.indexOf(":")>0){//windows操作系统下的绝对路径
       }else if(jarFileName.startsWith("/")){//linux操作系统下的绝对路径
       }else{//jarFileName只是jar包名称,则认为该jar包处在web的lib目录中
           jarFileName=getAbsolutePath_VirtualPath("/WEB-INF/lib/"+jarFileName);
       }
       JarFile jf=new JarFile(jarFileName,false);
       if(fileVirtualPathInJar.startsWith("/")){
           fileVirtualPathInJar=fileVirtualPathInJar.substring(1);//将虚拟路径变为:"myUtil/myWebParameter/WebParameters.xml"
       }
       is=jf.getInputStream(jf.getJarEntry(fileVirtualPathInJar));
       //
       return is;
   }
   
   
   
   //
   public static WebPathUtil getInstance(HttpServletRequest request){
       WebPathUtil wpu=new WebPathUtil();
       wpu.request=request;
       wpu.application=request.getSession().getServletContext();
       //
       return wpu;
   }
   //该方法主要用在MyServletContextListener中,因为在ServletContextListener中获取不到request对象
   public static WebPathUtil getInstance(ServletContext application){
       WebPathUtil wpu=new WebPathUtil();
       wpu.application=application;
       //以ServletContext生成WebPathUtil对象时,不能存储WebPath对象,因为没有request
       return wpu;
   }
   
   //例如假设web上下文为"myUtil"当virtualPath为"/test.jsp"时将返回"F:\myUtil\build\web\test.jsp"也就是项目部署的硬盘的绝对路径
   public String getRealPath(String virtualPath){
       String realPath="";
       if(this.application!=null){
           realPath=application.getRealPath(virtualPath);
       }else if(this.request!=null){
           realPath=request.getSession().getServletContext().getRealPath(virtualPath);
       }
       if(realPath.lastIndexOf("\\")==realPath.length()-1){
    	   realPath=realPath.substring(0,realPath.length()-1);
       }
       
       return realPath;
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当virtualPath为"/a.jsp"时将返回"/myUtil/a.jsp"也就是"a.jsp"相对项目部署上下文的访问路径
   //当没有配置上下文时,virtualPath便是URI
   public String getVirtualPathUri(String virtualPath){
       String Uri="";
       String cp=getContextPath();
       if(cp.equals("/")){
           cp="";
       }
       if(!cp.equals("")&&virtualPath.equals("/")){
           Uri=cp;
       }else{
           Uri=cp+virtualPath;
       }
       
       return Uri;
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当virtualPath为"/a.jsp"时将返回"http://localhost:8084/myUtil/a.jsp"也就是"a.jsp"应该的访问路径
   public String getVirtualPathUrl(String virtualPath){
       String Url="";
       String subs="";
       String sp=getServletPath();        
       String ru=getRequestURL();
       int spLength=sp.length();
       int ruLength=ru.length();
       if(ru.endsWith("/")){
           spLength=sp.lastIndexOf("/")+1;
       }
       subs=ru.substring(0,ruLength-spLength);
       Url=subs+virtualPath;
       if(Url.endsWith("/")){
           Url=Url.substring(0,Url.length()-1);
       }
       return Url;
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp时将返回"/myUtil"
   //当没有配置上下文时,ContextUri为"/".
   public String getContextUri(){
       return getVirtualPathUri("/");
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"调用该方法将返回"http://localhost:8084/myUtil"
   public String getContextUrl(){
       return getVirtualPathUrl("/");
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   public String getConfigedUserUploadRootFolderUri(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String FileSaveRootPath="";
       
       FileSaveRootPath=SysParam.getString("Upload_1_FileSaveRootPath");
       AssertUtil.Assert(FileSaveRootPath!=null,"无法从SysParam中获取Upload_1_FileSaveRootPath参数的值!");
       if(FileSaveRootPath.lastIndexOf("/")==FileSaveRootPath.length()-1){
           FileSaveRootPath=FileSaveRootPath.substring(0,FileSaveRootPath.length()-1);
       }
       return getVirtualPathUri(FileSaveRootPath);
   }
   //(该方法只有以request对象生成的本类的实例才能用)
   public String getConfigedUserUploadRootFolderUrl(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String FileSaveRootPath="";
       
       FileSaveRootPath=SysParam.getString("Upload_1_FileSaveRootPath");
       AssertUtil.Assert(FileSaveRootPath!=null,"无法从SysParam中获取Upload_1_FileSaveRootPath参数的值!");
       if(FileSaveRootPath.lastIndexOf("/")==FileSaveRootPath.length()-1){
           FileSaveRootPath=FileSaveRootPath.substring(0,FileSaveRootPath.length()-1);
       }
       return getVirtualPathUrl(FileSaveRootPath);
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp时将返回"/myUtil"
   public String getContextPath(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String contextPath="";
       contextPath=request.getContextPath();
       return contextPath;
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"a=11&b=12"
   public String getQueryString(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       return request.getQueryString();
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"/test.jsp"
   //返回当前请求路径的虚拟路径
   public String getServletPath(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       return request.getServletPath();
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"/myUtil/test.jsp"
   public String getRequestURI(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       return request.getRequestURI();
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"/myUtil/test.jsp?a=11&b=12"
   public String getRequestURIwithQueryString(){
       String returnStr="";
       AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String qs=getQueryString();
       if(qs==null){
           returnStr=request.getRequestURI();
       }else{
           returnStr=request.getRequestURI()+"?"+qs;
       }
       return returnStr;
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"http://localhost:8084/myUtil/test.jsp"
   public String getRequestURL(){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       return request.getRequestURL().toString();
   }
   //(该方法只有以request对象生成的本类的实例才能用)例如假设web上下文为"myUtil"当请求根目录中的test.jsp?a=11&b=12时将返回"http://localhost:8084/myUtil/test.jsp?a=11&b=12"
   public String getRequestURLwithQueryString(){
       String returnStr="";
       AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String qs=getQueryString();
       if(qs==null){
           returnStr=request.getRequestURL().toString();
       }else{
           returnStr=request.getRequestURL().toString()+"?"+qs;
       }
       return returnStr;
   }

   public HttpServletRequest getRequest() {
       return request;
   }

   public void setRequest(HttpServletRequest request) {
       this.request = request;
   }

   public ServletContext getApplication() {
       return application;
   }

   public void setApplication(ServletContext application) {
       this.application = application;
   }
   //将普通URL或URI或上传文件转换为伪静态URL
   //eg:将http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   //转换为http://www.test.com/UserUpload___201004___11___admin___1___2010-04-11-12-35-40-544__p1_1__p2_2.html#a
   public String transUriOrUrlOrUploadFileToNotRealStaticUrl(String uriOrUrlOrUploadFile){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String NotRealStaticUrl=getContextUrl()+"/";
       String vp=getVirtualPath$UriOrUrlOrUploadFile(uriOrUrlOrUploadFile).substring(1);
       String[] temp=vp.split("\\.");
       NotRealStaticUrl=NotRealStaticUrl+temp[0].replace("/","___");
       //添加参数
       String[] extAndParamAndMao=temp[1].split("\\?");
       if(extAndParamAndMao.length>1){//有参数也可能有锚的情况
           String[] paramAndMao=extAndParamAndMao[1].split("\\#");
           if(paramAndMao.length>1){//有参数也有锚
               //循环添加参数
               String[] params=paramAndMao[0].split("\\&");
               for(int i=0;i<params.length;i++){
                   String[] t=params[i].split("=");
                   if(t.length==1){
                       NotRealStaticUrl=NotRealStaticUrl+"__"+params[i].replace("=","_")+"kongchuan";
                   }else{
                       NotRealStaticUrl=NotRealStaticUrl+"__"+params[i].replace("=","_");
                   }
               }
               NotRealStaticUrl=NotRealStaticUrl+".html#"+paramAndMao[1];
           }else{//有参数但无锚
               //循环添加参数
               String[] params=paramAndMao[0].split("\\&");
               for(int i=0;i<params.length;i++){
                   String[] t=params[i].split("=");
                   if(t.length==1){
                       NotRealStaticUrl=NotRealStaticUrl+"__"+params[i].replace("=","_")+"kongchuan";
                   }else{
                       NotRealStaticUrl=NotRealStaticUrl+"__"+params[i].replace("=","_");
                   }
               }
               NotRealStaticUrl=NotRealStaticUrl+".html";
           }

       }else{//无参数但是可能有锚的情况
           String[] extAndMao=temp[1].split("\\#");
           if(extAndMao.length>1){//无参数但是有锚
               NotRealStaticUrl=NotRealStaticUrl+".html#"+extAndMao[1];
           }else{//无参数也无锚
               NotRealStaticUrl=NotRealStaticUrl+".html";
           }
       }
       NotRealStaticUrl=NotRealStaticUrl.replace("%","baifenhao");
       
       return NotRealStaticUrl;
   }
   //将普通URL或URI或上传文件转换为伪静态URL
   //eg:将http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   //转换为http://www.test.com/UserUpload---201004---11---admin---1---2010-04-11-12-35-40-544--p1-1--p2-2.html#a
   public String transUriOrUrlOrUploadFileToNotRealStaticUrl2(String uriOrUrlOrUploadFile){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String NotRealStaticUrl=getContextUrl()+"/";
       String vp=getVirtualPath$UriOrUrlOrUploadFile(uriOrUrlOrUploadFile).substring(1);
       String[] temp=vp.split("\\.");
       NotRealStaticUrl=NotRealStaticUrl+temp[0].replace("/","---");
       //添加参数
       String[] extAndParamAndMao=temp[1].split("\\?");
       if(extAndParamAndMao.length>1){//有参数也可能有锚的情况
           String[] paramAndMao=extAndParamAndMao[1].split("\\#");
           if(paramAndMao.length>1){//有参数也有锚
               //循环添加参数
               String[] params=paramAndMao[0].split("\\&");
               for(int i=0;i<params.length;i++){
                   String[] t=params[i].split("=");
                   if(t.length==1){
                       NotRealStaticUrl=NotRealStaticUrl+"--"+params[i].replace("=","-")+"kongchuan";
                   }else{
                       NotRealStaticUrl=NotRealStaticUrl+"--"+params[i].replace("=","-");
                   }
               }
               NotRealStaticUrl=NotRealStaticUrl+".html#"+paramAndMao[1];
           }else{//有参数但无锚
               //循环添加参数
               String[] params=paramAndMao[0].split("\\&");
               for(int i=0;i<params.length;i++){
                   String[] t=params[i].split("=");
                   if(t.length==1){
                       NotRealStaticUrl=NotRealStaticUrl+"--"+params[i].replace("=","-")+"kongchuan";
                   }else{
                       NotRealStaticUrl=NotRealStaticUrl+"--"+params[i].replace("=","-");
                   }
               }
               NotRealStaticUrl=NotRealStaticUrl+".html";
           }

       }else{//无参数但是可能有锚的情况
           String[] extAndMao=temp[1].split("\\#");
           if(extAndMao.length>1){//无参数但是有锚
               NotRealStaticUrl=NotRealStaticUrl+".html#"+extAndMao[1];
           }else{//无参数也无锚
               NotRealStaticUrl=NotRealStaticUrl+".html";
           }
       }
       NotRealStaticUrl=NotRealStaticUrl.replace("%","baifenhao");

       return NotRealStaticUrl;
   }
   //将伪静态URL转换为普通URL
   //eg:将http://www.test.com/UserUpload___201004___11___admin___1___2010-04-11-12-35-40-544__p1_1__p2_2.html#a
   //转换为http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   public String transNotRealStaticUrlToCommonUrl(String NotRealStaticUrl){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String commonUrl=null;
       String temp_NotRealStaticUrl=NotRealStaticUrl.replace("___","/");
       int dotIndex=temp_NotRealStaticUrl.lastIndexOf(".");
       if(dotIndex==-1){
           return NotRealStaticUrl;
       }
       String[] t=temp_NotRealStaticUrl.substring(dotIndex).split("#");

       int __Index=temp_NotRealStaticUrl.indexOf("__");
       if(__Index<0){
           commonUrl=temp_NotRealStaticUrl+t[0];
       }else{
           commonUrl=temp_NotRealStaticUrl.substring(0,__Index)+t[0];
       }
       //判断对应目录下是否存在真实静态页,如不存在,就返回对应的动态页
       String absolutePath_url=getRealPath(getVirtualPath$Url(commonUrl));
       if(new File(absolutePath_url).exists()){
       }else{
           commonUrl=commonUrl.substring(0,commonUrl.lastIndexOf("."))+".jsp";
       }
       //加上参数
       if(__Index>-1){
           String paramStr=temp_NotRealStaticUrl.substring(__Index+2,dotIndex);
           String[] params=paramStr.split("__");
           for(int i=0;i<params.length;i++){
               String[] param=params[i].split("_");
               int len=param.length;
               if(param[1].equals("kongchuan")){
                   param[1]="";
               }
               if(i==0){
                   if(len==2){
                       commonUrl=commonUrl+"?"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"_";
                           }
                       }
                       commonUrl=commonUrl+"?"+param[0]+"="+tempParam;
                   }

               }else{
                   if(len==2){
                       commonUrl=commonUrl+"&"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"_";
                           }
                       }
                       commonUrl=commonUrl+"&"+param[0]+"="+tempParam;
                   }

               }
           }
       }
       //加上锚点
       if(t.length>1){
           commonUrl=commonUrl+"#"+t[1];
       }
       
       return commonUrl;
   }
   //将伪静态URL转换为普通URL
   //eg:将http://www.test.com/UserUpload---201004---11---admin---1---2010-04-11-12-35-40-544--p1-1--p2-2.html#a
   //转换为http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   public String transNotRealStaticUrlToCommonUrl2(String NotRealStaticUrl){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String commonUrl=null;
       String temp_NotRealStaticUrl=NotRealStaticUrl.replace("---","/");
       int dotIndex=temp_NotRealStaticUrl.lastIndexOf(".");
       if(dotIndex==-1){
           return NotRealStaticUrl;
       }
       String[] t=temp_NotRealStaticUrl.substring(dotIndex).split("#");

       int __Index=temp_NotRealStaticUrl.indexOf("--");
       if(__Index<0){
           commonUrl=temp_NotRealStaticUrl+t[0];
       }else{
           commonUrl=temp_NotRealStaticUrl.substring(0,__Index)+t[0];
       }
       //判断对应目录下是否存在真实静态页,如不存在,就返回对应的动态页
       String absolutePath_url=getRealPath(getVirtualPath$Url(commonUrl));
       if(new File(absolutePath_url).exists()){
       }else{
           commonUrl=commonUrl.substring(0,commonUrl.lastIndexOf("."))+".jsp";
       }
       //加上参数
       if(__Index>-1){
           String paramStr=temp_NotRealStaticUrl.substring(__Index+2,dotIndex);
           String[] params=paramStr.split("--");
           for(int i=0;i<params.length;i++){
               String[] param=params[i].split("-");
               int len=param.length;
               if(param[1].equals("kongchuan")){
                   param[1]="";
               }
               if(i==0){
                   if(len==2){
                       commonUrl=commonUrl+"?"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";                    
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"-";
                           }
                       }
                       commonUrl=commonUrl+"?"+param[0]+"="+tempParam;
                   }
                   
               }else{
                   if(len==2){
                       commonUrl=commonUrl+"&"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"-";
                           }
                       }
                       commonUrl=commonUrl+"&"+param[0]+"="+tempParam;
                   }
                   
               }
           }
       }
       //加上锚点
       if(t.length>1){
           commonUrl=commonUrl+"#"+t[1];
       }

       return commonUrl;
   }

   //将普通URL或URI或上传文件转换为目录静态URL
   //eg:将http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   //转换为http://www.test.com/UserUpload---201004---11---admin---1---2010-04-11-12-35-40-544--p1-1--p2-2#a
   public String transUriOrUrlOrUploadFileToMuluStaticUrl(String uriOrUrlOrUploadFile){
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String MuluStaticUrl=getContextUrl()+"/";
       String vp=getVirtualPath$UriOrUrlOrUploadFile(uriOrUrlOrUploadFile).substring(1);
       String[] temp=vp.split("\\.");
       MuluStaticUrl=MuluStaticUrl+temp[0].replace("/","---");
       //添加参数
       String[] extAndParam=temp[1].split("\\?");
       if(extAndParam.length>1){//有参数
           //循环添加参数
           String[] params=extAndParam[1].split("\\&");
           for(int i=0;i<params.length;i++){
               String[] t=params[i].split("=");
               if(t.length==1){
                   MuluStaticUrl=MuluStaticUrl+"--"+params[i].replace("=","-")+"kongchuan";
               }else{
                   MuluStaticUrl=MuluStaticUrl+"--"+params[i].replace("=","-");
               }
           }          
       }
       
       MuluStaticUrl=MuluStaticUrl.replace("%","baifenhao");

       return MuluStaticUrl;
   }

   //将目录静态URL转换为普通URL
   //eg:将http://www.test.com/UserUpload---201004---11---admin---1---2010-04-11-12-35-40-544--p1-1--p2-2#a
   //转换为http://www.test.com/UserUpload/201004/11/admin/1/2010-04-11-12-35-40-544.jsp?p1=1&p2=2#a
   public String transMuluStaticUrlToCommonUrl(String muluStaticUrl){       
	   AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
       String commonUrl=null;       
       String contextUrl=getContextUrl();
       String vp=muluStaticUrl.replace(contextUrl,"");   
       if(vp.equals("/")||vp.contains(".")){//是常规路径,无需转换,直接返回
           return muluStaticUrl;
       }else{//是目录路径,需要转换
           vp=vp.replace("---","/");
           //判断目录是否以"/"结尾,如果是就统一去掉最后的"/".
           if(vp.endsWith("/")){
               vp=vp.substring(0,vp.length()-1);
           }
       }
       int __Index=vp.indexOf("--");
       if(__Index<0){
           commonUrl=contextUrl+vp;
       }else{
           commonUrl=contextUrl+vp.substring(0,__Index);
       }     
       //检测目录是否存在
       String absolutePath_url=getRealPath(getVirtualPath$Url(commonUrl));     
       if(new File(absolutePath_url).exists()){
           //System.out.println("@@@@@@@@@@@@"+absolutePath_url+"存在");
       }else{
           commonUrl=commonUrl+".jsp";
       }
       //加上参数
       if(__Index>-1){
           String paramStr=vp.substring(__Index+2);
           String[] params=paramStr.split("--");
           for(int i=0;i<params.length;i++){
               String[] param=params[i].split("-");
               int len=param.length;
               if(param[1].equals("kongchuan")){
                   param[1]="";
               }
               if(i==0){
                   if(len==2){
                       commonUrl=commonUrl+"?"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"-";
                           }
                       }
                       commonUrl=commonUrl+"?"+param[0]+"="+tempParam;
                   }

               }else{
                   if(len==2){
                       commonUrl=commonUrl+"&"+param[0]+"="+param[1].replace("baifenhao","%");
                   }else{
                       String tempParam="";
                       for(int j=1;j<len;j++){
                           if(j==len-1){
                               tempParam=tempParam+param[j].replace("baifenhao","%");
                           }else{
                               tempParam=tempParam+param[j].replace("baifenhao","%")+"-";
                           }
                       }
                       commonUrl=commonUrl+"&"+param[0]+"="+tempParam;
                   }

               }
           }
       }
       
       return commonUrl;
   }
   //参考一个完整的URL可以将一个相对的URL转换为它的完整URL,如果参数已经是一个完整URL则直接返回
   //eg:参考http://localhost:8084/myUtil/test1/test2/a.jsp将../b.jsp转换为:http://localhost:8084/myUtil/test1/b.jsp
   //eg:参考http://localhost:8084/myUtil/test1/test2/a.jsp将/b.jsp转换为:http://localhost:8084/b.jsp
   public static String transRelativeUrl2AbsoluteUrl(String referenceUrlStr,String relativeUrlStr) throws MalformedURLException{
       URL url=null;
       try{
           url=new URL(new URL(referenceUrlStr),relativeUrlStr);
       }catch(MalformedURLException e){
           throw new MalformedURLException("#####WebPathUtil.transRelativeUrl2AbsoluteUrl('"+referenceUrlStr+"','"+relativeUrlStr+"')抛出MalformedURLException#####"+e.getMessage());
       }
       return url.toString();
   }
   //
   public String transAnyPath2Url(String needTransPath){
	    AssertUtil.Assert(this.request!=null,"只有以request对象生成的WebPathUtil类的实例才可以调用该方法!");
		String uri=(String)this.request.getAttribute("javax.servlet.include.request_uri");
		if(uri==null){
			uri=this.request.getRequestURI();
		}
		String referenceUrlStr=this.getVirtualPathUrl(this.getVirtualPath$Uri(uri));
		String contextUri=this.getContextUri();
		if(contextUri.equals("/")||contextUri.equals("")){
			
		}else{
			if(needTransPath.startsWith(contextUri+"/")){
			   	
			}else if(needTransPath.startsWith("/")){
				needTransPath=contextUri+needTransPath;
			}
		}
	    //
		String returnStr=null;
		try{
			returnStr=transRelativeUrl2AbsoluteUrl(referenceUrlStr,needTransPath);
		}catch(MalformedURLException e){
			returnStr=needTransPath;
			//throw new RuntimeException(e.getMessage());
		}
		return returnStr;
	}
   //
   public static void main(String[] args) throws UnsupportedEncodingException, MalformedURLException{
       String a="http://localhost:8084/myUtil/test1/test2/a.jsp?a=11&b=22";
//       HashMap<String,String> hm=new HashMap<String,String>();
//       hm.put("a","中国");
//       //hm.put("b","汉字测试");
//       hm.put("c","ccccccccccc");
//       hm.put("d","44");
       //System.out.println(addParametersForUrlOrUri(a,hm));
       //System.out.println(addParameters2UrlOrUri(a,hm,""));
       System.out.println(transRelativeUrl2AbsoluteUrl(a,"/b.jsp"));
   }
   //添加参数到URL/URI中,如果url/uri中已经有某个参数,则会覆盖该参数的值,该方法不将参数编码.
   public static String addParameters2UrlOrUri(String urlOrUri,HashMap<String,String> parameters){
       String returnStr=urlOrUri;
       if(parameters!=null&&parameters.size()>0){
           //
           String[] urlParams=null;
           String[] urlArry1=urlOrUri.split("\\?");
           if(urlArry1.length>1){
               urlParams=urlArry1[1].split("\\&");
           }
           if(urlParams!=null){
               for(int i=0;i<urlParams.length;i++){
                   String[] urlParam=urlParams[i].split("\\=");
                   String paramValue=parameters.get(urlParam[0]);
                   if(paramValue!=null){
                       urlParams[i]=urlParam[0]+"="+paramValue;
                       parameters.remove(urlParam[0]);
                   }
                   if(urlArry1[0].indexOf("?")!=-1){
                       urlArry1[0]=urlArry1[0]+"&"+urlParams[i];
                   }else{
                       urlArry1[0]=urlArry1[0]+"?"+urlParams[i];
                   }
               }
           }
           //
           Set<String> parameterNames=parameters.keySet();
           Iterator<String> it=parameterNames.iterator();
           while(it.hasNext()){
               String parameterName=it.next();
               String parameterValue=parameters.get(parameterName);
               if(urlArry1[0].indexOf("?")!=-1){
                   urlArry1[0]=urlArry1[0]+"&"+parameterName+"="+parameterValue;
               }else{
                   urlArry1[0]=urlArry1[0]+"?"+parameterName+"="+parameterValue;
               }
           }
           //
           returnStr=urlArry1[0];
       }
       return returnStr;
   }
   //添加参数到URL/URI中,如果url/uri中已经有某个参数,则会覆盖该参数的值,该方法将按照charSet将参数编码,该方法不会对没有被覆盖的参数编码.
   //当charSet参数为空时返回结果和addParameters2UrlOrUri(String urlOrUri,HashMap<String,String> parameters)一样
   public static String addParameters2UrlOrUri(String urlOrUri,HashMap<String,String> parameters,String charSet) throws UnsupportedEncodingException{
       if(charSet==null||charSet.equals("")){
           return addParameters2UrlOrUri(urlOrUri,parameters);
       }
       String returnStr=urlOrUri;
       if(parameters!=null&&parameters.size()>0){
           //
           String[] urlParams=null;
           String[] urlArry1=urlOrUri.split("\\?");
           if(urlArry1.length>1){
               urlParams=urlArry1[1].split("\\&");
           }
           if(urlParams!=null){
               for(int i=0;i<urlParams.length;i++){
                   String[] urlParam=urlParams[i].split("\\=");
                   String paramValue=parameters.get(urlParam[0]);
                   if(paramValue!=null){
                       urlParams[i]=urlParam[0]+"="+URLEncoder.encode(paramValue,charSet);
                       parameters.remove(urlParam[0]);
                   }
                   if(urlArry1[0].indexOf("?")!=-1){
                       urlArry1[0]=urlArry1[0]+"&"+urlParams[i];
                   }else{
                       urlArry1[0]=urlArry1[0]+"?"+urlParams[i];
                   }
               }
           }
           //
           Set<String> parameterNames=parameters.keySet();
           Iterator<String> it=parameterNames.iterator();
           while(it.hasNext()){
               String parameterName=it.next();
               String parameterValue=URLEncoder.encode(parameters.get(parameterName),charSet);
               if(urlArry1[0].indexOf("?")!=-1){
                   urlArry1[0]=urlArry1[0]+"&"+parameterName+"="+parameterValue;
               }else{
                   urlArry1[0]=urlArry1[0]+"?"+parameterName+"="+parameterValue;
               }
           }
           //
           returnStr=urlArry1[0];
       }
       return returnStr;
   }
           
   //
   public static String encodeParametersForUrlOrUri(String urlOrUri,String charSet) throws UnsupportedEncodingException{
       String returnStr=urlOrUri;
       if(charSet!=null&&!charSet.equals("")){
           String[] urlParams=null;
           String[] urlArry1=urlOrUri.split("\\?");
           if(urlArry1.length>1){
               urlParams=urlArry1[1].split("\\&");
           }
           if(urlParams!=null){
               for(int i=0;i<urlParams.length;i++){
                   String[] urlParam=urlParams[i].split("\\=");
                   if(urlParam.length==2){
                       urlParams[i]=urlParam[0]+"="+URLEncoder.encode(urlParam[1],charSet);
                   }else{
                       urlParams[i]=urlParam[0]+"=";
                   }
                   //
                   if(urlArry1[0].indexOf("?")!=-1){
                       urlArry1[0]=urlArry1[0]+"&"+urlParams[i];
                   }else{
                       urlArry1[0]=urlArry1[0]+"?"+urlParams[i];
                   }
               }
               returnStr=urlArry1[0];
           }
       }
       return returnStr;
   }
   //
}