package myD7Util.myDB.myPaging;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

import com.bstek.dorado.jdbc.iapi.IDialect;

import myD7Util.WebPathUtil;
import myD7Util.myDB.MyJdbcDao;

/**
 *
 * @author xuecf
 */
public class PagingUtil {
    private PagingParameter pagingParameter;//存储分页的参数们
    private PageData currentPageData;
    //
    public String componentWidth="100%";
    public String componentPadding="5";
    public String componentAlign="center";
    public boolean takePostParams=true;
    public String requestEncoding_post="";
    public String requestEncoding_get="";
    //
    //private HttpServletRequest request;
    //
    public static PagingUtil getInstance(PagingParameter pagingParameter,HttpServletRequest request){
        PagingUtil pu=new PagingUtil();
        String currentPage=request.getParameter("currentPage");
        if(currentPage!=null&&!currentPage.equals("")){
            pagingParameter.pageNo=Integer.parseInt(currentPage);
        }
        pu.pagingParameter=pagingParameter;
        return pu;
    }
    
    public PageData getCurrentPageData(MyJdbcDao mjd){
    	if(pagingParameter.oldSql!=null&&!pagingParameter.oldSql.equals("")){
    		currentPageData=mjd.queryForPageData(pagingParameter.oldSql,pagingParameter.pageSize,pagingParameter.pageNo);
    	}else{
    		if(pagingParameter.needColumns!=null&&pagingParameter.needColumns.length>0){
    			currentPageData=mjd.queryForPageData_D7(pagingParameter.d7ModelName,pagingParameter.needColumns,pagingParameter.argsValue,pagingParameter.pageSize,pagingParameter.pageNo);
    		}else{
    			currentPageData=mjd.queryForPageData_D7(pagingParameter.d7ModelName,pagingParameter.argsValue,pagingParameter.pageSize,pagingParameter.pageNo);
    		}
    	}
    	
        return currentPageData;
    }
    public void showComponent(JspWriter out,HttpServletRequest request) throws IOException, Exception{
        showComponent(out,request,null,null,null,this.takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,boolean takePostParams) throws IOException, Exception{
        showComponent(out,request,null,null,null,takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,String width) throws IOException, Exception{
        showComponent(out,request,width,null,null,this.takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,String width,boolean takePostParams) throws IOException, Exception{
        showComponent(out,request,width,null,null,takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,String width,String align) throws IOException, Exception{
        showComponent(out,request,width,null,align,this.takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,String width,String align,boolean takePostParams) throws IOException, Exception{
        showComponent(out,request,width,null,align,takePostParams);
    }
    public void showComponent(JspWriter out,HttpServletRequest request,String width,String padding,String align,boolean takePostParams) throws IOException, Exception{
        out.print(getComponentScript(request,width,padding,align,takePostParams));
    }
    //
    public String getComponentScript(HttpServletRequest request) throws Exception{
    	return getComponentScript(request,null,null,null,this.takePostParams);
    }
    public String getComponentScript(HttpServletRequest request,boolean takePostParams) throws Exception{
    	return getComponentScript(request,null,null,null,takePostParams);
    }
    //
    public String getComponentScript(HttpServletRequest request,String width,String padding,String align,boolean takePostParams) throws Exception{
    	if(width!=null&&!width.equals("")){
            this.componentWidth=width;
        }
        if(padding!=null&&!padding.equals("")){
            this.componentPadding=padding;
        }
        if(align!=null&&!align.equals("")){
            this.componentAlign=align;
        }
        String outScript="<table width=\""+componentWidth+"\" cellspacing=\"0\" cellpadding=\""+componentPadding+"\">";
        outScript=outScript+"<tr><td align=\""+componentAlign+"\">";
        if(currentPageData.getRecordList().size()>0){
            outScript=outScript+"<font color=\"#cc6633\">";
            outScript=outScript+"共"+currentPageData.getTotalRecordCount()+"条记录 第"+currentPageData.getPageNo()+"/"+currentPageData.getPageCount()+"页&nbsp;";
            outScript=outScript+"</font>";
            if(pagingParameter.pageNo!=1){
                outScript=outScript+"<a href=\""+getGoPageToUrl(request,1,takePostParams)+"\">首页</a>&nbsp;";
                outScript=outScript+"<a href=\""+getGoPageToUrl(request,currentPageData.getPageNo()-1,takePostParams)+"\">上一页</a>&nbsp;";
            }else{
                outScript=outScript+"首页&nbsp;上一页&nbsp;";
            }
            //
            if(currentPageData.getPageNo()!=currentPageData.getPageCount()){
                outScript=outScript+"<a href=\""+getGoPageToUrl(request,currentPageData.getPageNo()+1,takePostParams)+"\">下一页</a>&nbsp;";
                outScript=outScript+"<a href=\""+getGoPageToUrl(request,currentPageData.getPageCount(),takePostParams)+"\">末页</a>&nbsp;";
            }else{
                outScript=outScript+"下一页&nbsp;末页&nbsp;";
            }
        }else{
            outScript=outScript+"没有相关记录";
        }
        outScript=outScript+"</td></tr></table>";
        //
        return outScript;
    }
    //
    public String getGoPageToUrl(HttpServletRequest request,int pageIndex,boolean takePostParams) throws Exception{
        String goPageToUrl="";
        WebPathUtil wpu=WebPathUtil.getInstance(request);
        String requestUrl=wpu.getRequestURL();
        //请求方式
        String requestMethod=request.getMethod();
        //将请求中的所有参数都添加到requestUrl中
        if(takePostParams){
            Enumeration<String> allParamNames=request.getParameterNames();
            String parameterName="";
            while(allParamNames.hasMoreElements()){
                parameterName=allParamNames.nextElement();
                if(!parameterName.equalsIgnoreCase("currentPage")){
                    String parameterValue=request.getParameter(parameterName);
                    if(requestMethod.equalsIgnoreCase("POST")){
                    	if(this.requestEncoding_post!=null&&!this.requestEncoding_post.equals("")){
                        	parameterValue=new String(parameterValue.getBytes("ISO-8859-1"),this.requestEncoding_post);
                        }
                    }else{
                    	if(this.requestEncoding_get!=null&&!this.requestEncoding_get.equals("")){
                        	parameterValue=new String(parameterValue.getBytes("ISO-8859-1"),this.requestEncoding_get);
                        }
                    }
                    parameterValue=URLEncoder.encode(parameterValue,"UTF-8");
                    if(requestUrl.indexOf("?")==-1){
                        requestUrl=requestUrl+"?"+parameterName+"="+parameterValue;
                    }else{
                        requestUrl=requestUrl+"&"+parameterName+"="+parameterValue;
                    }
                }
            }
            
        }else{
            String queryString=wpu.getQueryString();
            if(queryString!=null&&!queryString.equals("")){
                HashMap<String,String> parameters=new HashMap<String,String>();
                String[] urlParams=queryString.split("\\&");
                for(int i=0;i<urlParams.length;i++){
                    String[] urlParam=urlParams[i].split("\\=");
                    if(!urlParam[0].equals("")&&!urlParam[0].equalsIgnoreCase("currentPage")){
                        parameters.put(urlParam[0],urlParam[1]);
                    }
                }
                requestUrl=WebPathUtil.addParameters2UrlOrUri(requestUrl,parameters);
            }
        }
        //
        if(requestUrl.indexOf("?")==-1){
            requestUrl=requestUrl+"?currentPage="+pageIndex;
        }else{
            requestUrl=requestUrl+"&currentPage="+pageIndex;
        }
        goPageToUrl=requestUrl;
        
        //goPageToUrl=wpu.transUriOrUrlOrUploadFileToMuluStaticUrl(goPageToUrl);
        
        return goPageToUrl;
    }

}
