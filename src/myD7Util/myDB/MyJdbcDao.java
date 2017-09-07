package myD7Util.myDB;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


import myD7Util.myDB.myPaging.PageData;
import myD7Util.GUID;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.bstek.dorado.data.provider.Page;
import com.bstek.dorado.data.variant.Record;
import com.bstek.dorado.jdbc.iapi.IDialect;
import com.bstek.dorado.jdbc.internal.JdbcDao;
import com.bstek.dorado.web.DoradoContext;

public class MyJdbcDao extends JdbcDao {
	public static MyJdbcDao getInstance() throws RuntimeException{
		MyJdbcDao mjd=null;
		ApplicationContext applicationContext=null;
		try{
			applicationContext=DoradoContext.getCurrent().getApplicationContext();
		}catch(Exception e){
			throw new RuntimeException("无法获取Spring的ApplicationContext"+e.getMessage());
		}
		mjd=(MyJdbcDao)applicationContext.getBean("dorado.jdbcDao");
		if(mjd==null){
			throw new RuntimeException("系统未配置ID为dorado.jdbcDao、类型为myD7Util.myDB.MyJdbcDao的bean!");
		}
		return mjd;
	}
	//
	public static MyJdbcDao getInstance(String configedDaoBeanId) throws RuntimeException{
		MyJdbcDao mjd=null;
		ApplicationContext applicationContext=null;
		try{
			applicationContext=DoradoContext.getCurrent().getApplicationContext();
		}catch(Exception e){
			throw new RuntimeException("无法获取Spring的ApplicationContext"+e.getMessage());
		}
		mjd=(MyJdbcDao)applicationContext.getBean(configedDaoBeanId);
		if(mjd==null){
			throw new RuntimeException("系统未配置ID为"+configedDaoBeanId+"、类型为myD7Util.myDB.MyJdbcDao的bean!");
		}
		return mjd;
	}
	//
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
		return (NamedParameterJdbcTemplate)this.getOperations();
	}
	//
	public JdbcTemplate getJdbcTemplate(){
		return (JdbcTemplate)this.getJdbcOperations();
	}
	
	//使用?参数,执行insert/update/delete操作
	//例如:mjd.executeIUD(sql,"paramValue1",new Date(),21);
	//也可以写为:mjd.executeIUD(sql,new Object[]{"paramValue1",new Date(),21});
	//如果sql语句没有参数,则直接写为:mjd.executeIUD(sql);
	public int executeIUD(String sqlUseUnNamedParameter,Object... argsValue) throws RuntimeException{
		return this.getJdbcTemplate().update(sqlUseUnNamedParameter, argsValue);
	}
	//使用:参数,执行insert/update/delete操作
	//如果sql语句没有参数,则可以写为:mjd.executeIUD2(sql,null);
	public int executeIUD2(String sqlUseNamedParameter,Map<String,Object> argsValue) throws RuntimeException{
		return this.getNamedParameterJdbcTemplate().update(sqlUseNamedParameter,argsValue);
	}
	//使用?参数,批量执行insert/update/delete操作
	//	例如:String sql="insert into huiYuan(xingMing,createDate) values(?,?)";
	//    List<Object[]> params=new ArrayList<Object[]>();
	//    params.add(new Object[]{"11111",new Date()});
	//    params.add(new Object[]{"22222",new Date()});
	//    int[] c=mjd.batchExecuteIUD(sql, params);
	public int[] batchExecuteIUD(String sqlUseUnNamedParameter,List<Object[]> batchArgsValue) throws RuntimeException{
		return this.getJdbcTemplate().batchUpdate(sqlUseUnNamedParameter, batchArgsValue);
	}
	//sql语句不包含参数的批量执行
	public int[] batchExecuteIUD(String[] sql) throws RuntimeException{
		return this.getJdbcTemplate().batchUpdate(sql);
	}
	//使用:参数,批量执行insert/update/delete操作
	public int[] batchExecuteIUD2(String sqlUseNamedParameter,List<Map<String,Object>> batchArgsValue) throws RuntimeException{
		int paramSize=0;
		if(batchArgsValue!=null){
			paramSize=batchArgsValue.size();
		}
		@SuppressWarnings("unchecked")
		Map<String,Object>[] hm=new HashMap[paramSize];
		for(int i=0;i<paramSize;i++){
			hm[i]=batchArgsValue.get(i);
		}
		return this.getNamedParameterJdbcTemplate().batchUpdate(sqlUseNamedParameter, hm);
	}
	//执行数据库定义语句
	public void executeDDL(String sql) throws RuntimeException{
		this.getJdbcTemplate().execute(sql);
	}
	
	//使用?参数,执行insert操作,并返回生成的键,如果返回null,表示抛出异常.
	//例如:keyHolder.getKey().intValue();
	public KeyHolder executeInsert_returnGeneratedKey(String sqlUseUnNamedParameter,Object... argsValue) throws RuntimeException{
		if(argsValue==null){
			return this.executeInsert_returnGeneratedKey2(sqlUseUnNamedParameter, null);
		}
		Object[] argsValue2=null;
		if(argsValue.length==1){
			if(argsValue[0] instanceof  Object[]){
				argsValue2=(Object[])argsValue[0];
			}else{
				argsValue2=argsValue;
			}
		}else{
			argsValue2=argsValue;
		}
		StringBuffer sb=new StringBuffer();
		Map<String,Object> newParams=new HashMap<String,Object>();
		String[] temp=sqlUseUnNamedParameter.split("\\?");
		if(temp.length!=argsValue2.length+1){
			throw new RuntimeException("参数数量与sql语句中的不一致!");
		}
		for(int i=0;i<temp.length;i++){
			sb.append(temp[i]);
			if(i<argsValue2.length){
				String paramName="param_"+GUID.getGUID_36().replace("-","");
				sb.append(":");
				sb.append(paramName);
				newParams.put(paramName, argsValue2[i]);
			}
		}
		return this.executeInsert_returnGeneratedKey2(sb.toString(), newParams);
	}
	//使用:参数,执行insert操作,并返回生成的键,如果返回null,表示抛出异常.
	//例如:keyHolder.getKey().intValue();
	public KeyHolder executeInsert_returnGeneratedKey2(String sqlUseNamedParameter,Map<String,Object> argsValue) throws RuntimeException{
		KeyHolder keyHolder=new GeneratedKeyHolder();
		SqlParameterSource sps = new MapSqlParameterSource(argsValue);
		int updateCount=this.getNamedParameterJdbcTemplate().update(sqlUseNamedParameter,sps,keyHolder);
		if(updateCount<0){
			keyHolder=null;
		}
		return keyHolder;
	}
	//
	//查询返回一条记录
	//如果没有查询到记录则返回null
	public Record queryFor1MyRecord(String sqlUseUnNamedParameter,Object... argsValue) throws RuntimeException{
		MyRecord record=null;
		try{
			record=new MyRecord(this.getJdbcTemplate().queryForMap(sqlUseUnNamedParameter,argsValue));		
		}catch(EmptyResultDataAccessException ex){
			//没有查询到记录时会抛出异常,捕捉后啥都不做,最终返回null
		}
		return record;
	}
	public Record queryFor1MyRecord2(String sqlUseNamedParameter,Map<String,Object> argsValue) throws RuntimeException{
		MyRecord record=null;
		try{
			record=new MyRecord(this.getNamedParameterJdbcTemplate().queryForMap(sqlUseNamedParameter,argsValue));		
		}catch(EmptyResultDataAccessException ex){
			//没有查询到记录时会抛出异常,捕捉后啥都不做,最终返回null
		}
		return record;
	}
	//查询返回一条记录中的第一个字段的值
	//如果没有查询到记录则返回null
	public Object queryForFirstFieldValue(String sqlUseUnNamedParameter,Object... argsValue) throws RuntimeException{
		Object returnValue=null;
		try{
			Map<String,Object> map=this.getJdbcTemplate().queryForMap(sqlUseUnNamedParameter,argsValue);
			Set<String> keys=map.keySet();
			Object key=keys.toArray()[0];
			returnValue=map.get(key);
		}catch(EmptyResultDataAccessException ex){
			//没有查询到记录时会抛出异常,捕捉后啥都不做,最终返回null
		}
		return returnValue;
	}
	public Object queryForFirstFieldValue2(String sqlUseNamedParameter,Map<String,Object> argsValue) throws RuntimeException{
		Object returnValue=null;
		try{
			Map<String,Object> map=this.getNamedParameterJdbcTemplate().queryForMap(sqlUseNamedParameter,argsValue);
			Set<String> keys=map.keySet();
			Object key=keys.toArray()[0];
			returnValue=map.get(key);
		}catch(EmptyResultDataAccessException ex){
			//没有查询到记录时会抛出异常,捕捉后啥都不做,最终返回null
		}
		return returnValue;
	}
	//查询返回记录集
	//如果没有查询到记录则返回一个空的List
	public List<Record> queryForMyRecordList(String sqlUseUnNamedParameter,Object... argsValue) throws RuntimeException{	
		List<Map<String, Object>> resultList=this.getJdbcTemplate().queryForList(sqlUseUnNamedParameter,argsValue);
		List<Record> returnList=new ArrayList<Record>();
		Iterator<Map<String, Object>> it=resultList.iterator();
		while(it.hasNext()){
			returnList.add(new MyRecord(it.next()));
		}
		return returnList;
	}
	public List<Record> queryForMyRecordList2(String sqlUseNamedParameter,Map<String,Object> argsValue) throws RuntimeException{
		List<Map<String, Object>> resultList=this.getNamedParameterJdbcTemplate().queryForList(sqlUseNamedParameter,argsValue);
		List<Record> returnList=new ArrayList<Record>();
		Iterator<Map<String, Object>> it=resultList.iterator();
		while(it.hasNext()){
			returnList.add(new MyRecord(it.next()));
		}
		return returnList;
	}
	//查询返回记录集中的前n条记录
	//如果没有查询到记录则返回一个空的List
	public List<Record> queryForMyRecordList_topN(String sqlUseUnNamedParameter,int topN,Object... argsValue) throws RuntimeException{
		return this.queryForPageData(sqlUseUnNamedParameter,topN,1,argsValue).getRecordList();
	}
	public List<Record> queryForMyRecordList_topN2(String sqlUseNamedParameter,Map<String,Object> argsValue,int topN) throws RuntimeException{
		return this.queryForPageData2(sqlUseNamedParameter,argsValue,topN,1).getRecordList();
	}
	//查询返回一页数据,封装在PageData里面
    //如果没有查询到记录则返回一个空的PageData
	public PageData queryForPageData(String sqlUseUnNamedParameter,int pageSize,int pageNo,Object... argsValue) throws RuntimeException{	
		PageData pd=PageData.getInstance(pageSize,pageNo);
    	IDialect id=this.getDialect();
    	String countSql=id.countSql(sqlUseUnNamedParameter);
    	String pagingSql=id.pageSql(sqlUseUnNamedParameter,pageSize,(pageNo-1)*pageSize);
    	pd.setTotalRecordCount(
    			Integer.valueOf(String.valueOf(this.queryForFirstFieldValue(countSql,argsValue)))
    			);
    	pd.setRecordList(this.queryForMyRecordList(pagingSql,argsValue));
    	return pd;
	}
	public PageData queryForPageData2(String sqlUseNamedParameter,Map<String,Object> argsValue,int pageSize,int pageNo) throws RuntimeException{	
		PageData pd=PageData.getInstance(pageSize,pageNo);
    	IDialect id=this.getDialect();
    	String countSql=id.countSql(sqlUseNamedParameter);
    	String pagingSql=id.pageSql(sqlUseNamedParameter,pageSize,(pageNo-1)*pageSize);
    	pd.setTotalRecordCount(((Long)this.queryForFirstFieldValue2(countSql,argsValue)).intValue());
    	pd.setRecordList(this.queryForMyRecordList2(pagingSql,argsValue));
    	return pd;
	}
	
	
	
	
	
	
	//查询返回一条记录
	//如果没有查询到记录则返回null
	public Record queryFor1D7Record(String d7ModelName,Map<String,Object> argsValue) throws RuntimeException{
		Collection<Record> c=this.query(d7ModelName,argsValue);
		Record record=null;
		if(c.size()>1){
			throw new RuntimeException("使用queryFor1D7Record方法查询的记录数不能超过1!");
		}else if(c.size()==1){
			record=(Record)(c.toArray()[0]);
		}
		return record;
	}
	public Record queryFor1D7Record(String d7ModelName,String[] needColumns,Map<String,Object> argsValue) throws RuntimeException{
		Collection<Record> c=this.query(d7ModelName,needColumns,argsValue);
		Record record=null;
		if(c.size()>1){
			throw new RuntimeException("使用queryFor1D7Record方法查询的记录数不能超过1!");
		}else if(c.size()==1){
			record=(Record)(c.toArray()[0]);
		}
		return record;
	}
	//查询返回一条记录中的第一个字段的值
	//如果没有查询到记录则返回null
	public Object queryForFirstFieldValue_d7(String d7ModelName,Map<String,Object> argsValue) throws RuntimeException{
		Object returnValue=null;
		Collection<Record> c=this.query(d7ModelName,argsValue);
		Record record=null;
		if(c.size()>1){
			throw new RuntimeException("使用queryForFirstFieldValue_d7方法查询的记录数不能超过1!");
		}else if(c.size()==1){
			record=(Record)(c.toArray()[0]);
			Map<String,Object> map=record.toMap();
			Set<String> keys=map.keySet();
			Object key=keys.toArray()[0];
			returnValue=map.get(key);
		}
		return returnValue;
	}
	public Object queryForFirstFieldValue_d7(String d7ModelName,String needColumn,Map<String,Object> argsValue) throws RuntimeException{
		Object returnValue=null;
		Collection<Record> c=this.query(d7ModelName,new String[]{needColumn},argsValue);
		Record record=null;
		if(c.size()>1){
			throw new RuntimeException("使用queryForFirstFieldValue_d7方法查询的记录数不能超过1!");
		}else if(c.size()==1){
			record=(Record)(c.toArray()[0]);
			Map<String,Object> map=record.toMap();
			Set<String> keys=map.keySet();
			Object key=keys.toArray()[0];
			returnValue=map.get(key);
		}
		return returnValue;
	}
	//查询返回记录集
	//如果没有查询到记录则返回一个空的List
	public List<Record> queryForD7RecordList(String d7ModelName,Map<String,Object> argsValue) throws RuntimeException{
		Collection<Record> c=this.query(d7ModelName,argsValue);
		List<Record> returnList=new ArrayList<Record>();
		returnList.addAll(c);
		return returnList;
	}
	public List<Record> queryForD7RecordList(String d7ModelName,String[] needColumns,Map<String,Object> argsValue) throws RuntimeException{
		Collection<Record> c=this.query(d7ModelName,needColumns,argsValue);
		List<Record> returnList=new ArrayList<Record>();
		returnList.addAll(c);
		return returnList;
	}
	//查询返回记录集中的前n条记录
	//如果没有查询到记录则返回一个空的List
	public List<Record> queryForD7RecordList_topN(String d7ModelName,Map<String,Object> argsValue,int topN) throws RuntimeException{
		Page<Record> page=new Page<Record>(topN,1);
		this.page(d7ModelName,argsValue,page);
		Collection<Record> c=page.getEntities();
		List<Record> returnList=new ArrayList<Record>();
		returnList.addAll(c);
		return returnList;
	}
	public List<Record> queryForD7RecordList_topN(String d7ModelName,String[] needColumns,Map<String,Object> argsValue,int topN) throws RuntimeException{
		Page<Record> page=new Page<Record>(topN,1);
		this.page(d7ModelName,needColumns,argsValue,page);
		Collection<Record> c=page.getEntities();
		List<Record> returnList=new ArrayList<Record>();
		returnList.addAll(c);
		return returnList;
	}
	//查询返回一页数据,封装在PageData里面
    //如果没有查询到记录则返回一个空的PageData
	public PageData queryForPageData_D7(String d7ModelName,Map<String,Object> argsValue,int pageSize,int pageNo) throws RuntimeException{
		Page<Record> page=new Page<Record>(pageSize,pageNo);
		this.page(d7ModelName,argsValue,page);
		return PageData.getInstance(page);
	}
	public PageData queryForPageData_D7(String d7ModelName,String[] needColumns,Map<String,Object> argsValue,int pageSize,int pageNo) throws RuntimeException{
		Page<Record> page=new Page<Record>(pageSize,pageNo);
		this.page(d7ModelName,needColumns,argsValue,page);
		return PageData.getInstance(page);
	}
	
	
}