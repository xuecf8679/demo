package myD7Util.myDB.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import myD7Util.GUID;
import myD7Util.myDB.MyRecord;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.bstek.dorado.data.variant.Record;

public class DBUtil {
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//
	private DBUtil(){
		super();
	}
	//使用默认数据源生成实例
	public static DBUtil getInstance(){
		return getInstance(null);
	}
	//使用指定名称的数据源生成实例
	public static DBUtil getInstance(String dataSourceName){
		DBUtil dbu=new DBUtil();
		dbu.jdbcTemplate=new JdbcTemplate(DataSourceUtil.getDataSource(dataSourceName));
		dbu.namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(dbu.jdbcTemplate);
		//
		return dbu;
	}
	//
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
		return this.namedParameterJdbcTemplate;
	}
	//
	public JdbcTemplate getJdbcTemplate(){
		return this.jdbcTemplate;
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
	
	public static void main(String[] args){
		String sql="insert into account(userName,password,touXiangPath,sex,createTime,isActive,shengId,shiId) values(?,?,?,'女','2014-11-25 10:00:30',5,'370000','370200')";
		DBUtil dbu=DBUtil.getInstance();
		for(int i=1501;i<=1957;i++){
			dbu.executeIUD(sql,"username"+i,"username"+i,"touXiang/username"+(i-1000)+".jpg.jpg");
			System.out.println("@@@@@@@@@@username"+i+"@@@@@@@@@");
		}
		System.out.println("########完成##########");
	}
	
}
