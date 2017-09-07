package myD7Util.myDB.myPaging;

import java.util.Map;

public class PagingParameter {
    public String oldSql;//分页查询的原始sql语句
    public int pageSize=30;//每页多少条记录
    public int pageNo=1;//当前页号,如第1页就是1
    //
    public String d7ModelName;//使用d7的dbModel作为sql语句的载体
    public String[] needColumns;//要使用dbModel中的哪些列
    public Map<String,Object> argsValue;//向d7的dbModel赋参数值
    
    //
    public PagingParameter(String oldSql,int pageSize,int pageNo){
        this.oldSql=oldSql;
        if(pageSize>0){
        	this.pageSize=pageSize;
        }
        if(pageNo>0){
        	this.pageNo=pageNo;
        }
        
    }
    public PagingParameter(String oldSql,int pageSize){
        this.oldSql=oldSql;
        if(pageSize>0){
        	this.pageSize=pageSize;
        }
    }
    //
    public PagingParameter(String d7ModelName,String[] needColumns,Map<String,Object> argsValue,int pageSize,int pageNo){
        this.d7ModelName=d7ModelName;
        this.needColumns=needColumns;
        this.argsValue=argsValue;
        if(pageSize>0){
        	this.pageSize=pageSize;
        }
        if(pageNo>0){
        	this.pageNo=pageNo;
        }
    }
    public PagingParameter(String d7ModelName,String[] needColumns,Map<String,Object> argsValue,int pageSize){
        this.d7ModelName=d7ModelName;
        this.needColumns=needColumns;
        this.argsValue=argsValue;
        if(pageSize>0){
        	this.pageSize=pageSize;
        }
    }
    public PagingParameter(String d7ModelName,Map<String,Object> argsValue,int pageSize){
        this.d7ModelName=d7ModelName;
        this.argsValue=argsValue;
        if(pageSize>0){
        	this.pageSize=pageSize;
        }
    }
  
}
