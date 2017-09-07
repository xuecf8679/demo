package myD7Util.myDB.common;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import myD7Util.WebPathUtil;

import org.apache.commons.dbcp.BasicDataSource;

public class DataSourceUtil {
	private static String connConfigFileVirtualPath="/WEB-INF/dorado-home/configure.properties";
	private static String defaultDataSourceName="dataSource";
	private static Map<String,ConnectionParameter> connectionParameters=new HashMap<String,ConnectionParameter>();
	private static Map<String,DataSource> dataSources=new HashMap<String,DataSource>();
	
	public static ConnectionParameter getConnectionParameter(String dataSourceName) throws RuntimeException{
		if(dataSourceName==null||dataSourceName.equals("")){
			dataSourceName=defaultDataSourceName;
		}
		ConnectionParameter connectionParameter=connectionParameters.get(dataSourceName);
		if(connectionParameter==null){
			String absPath=WebPathUtil.getAbsolutePath_VirtualPath(connConfigFileVirtualPath);
			Properties props=new Properties();
			try{
				props.load(new BufferedInputStream(new FileInputStream(absPath)));
			}catch(FileNotFoundException fnfe){
				throw new RuntimeException(fnfe.getCause());
			}catch(IOException ioe){
				throw new RuntimeException(ioe.getCause());
			}		
			ConnectionParameter cp=new ConnectionParameter();
			cp.driverClassName=props.getProperty(dataSourceName+"."+"driverClassName");
			cp.url=props.getProperty(dataSourceName+"."+"url");
			cp.username=props.getProperty(dataSourceName+"."+"username");
			cp.password=props.getProperty(dataSourceName+"."+"password");
			String cpt=props.getProperty(dataSourceName+"."+"connectionPoolType");
			if(cpt!=null&&!cpt.equals("")){
				cp.connectionPoolType=cpt;
			}
			if(cp.connectionPoolType.equalsIgnoreCase("dbcp")){
				String t=props.getProperty(dataSourceName+"."+"maxActive");
				if(t!=null&&!t.equals("")){
					cp.maxActive=Integer.parseInt(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"maxIdle");
				if(t!=null&&!t.equals("")){
					cp.maxIdle=Integer.parseInt(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"minIdle");
				if(t!=null&&!t.equals("")){
					cp.minIdle=Integer.parseInt(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"maxWait");
				if(t!=null&&!t.equals("")){
					cp.maxWait=Integer.parseInt(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"removeAbandoned");
				if(t!=null&&!t.equals("")){
					cp.removeAbandoned=Boolean.parseBoolean(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"removeAbandonedTimeout");
				if(t!=null&&!t.equals("")){
					cp.removeAbandonedTimeout=Integer.parseInt(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"logAbandoned");
				if(t!=null&&!t.equals("")){
					cp.logAbandoned=Boolean.parseBoolean(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"testWhileIdle");
				if(t!=null&&!t.equals("")){
					cp.testWhileIdle=Boolean.parseBoolean(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"validationQuery");
				if(t!=null&&!t.equals("")){
					cp.validationQuery=t;
				}
				//
				t=props.getProperty(dataSourceName+"."+"timeBetweenEvictionRunsMillis");
				if(t!=null&&!t.equals("")){
					cp.timeBetweenEvictionRunsMillis=Long.parseLong(t);
				}
				//
				t=props.getProperty(dataSourceName+"."+"minEvictableIdleTimeMillis");
				if(t!=null&&!t.equals("")){
					cp.minEvictableIdleTimeMillis=Long.parseLong(t);
				}
			}else if(cp.connectionPoolType.equalsIgnoreCase("c3p0")){
				
			}
			connectionParameter=cp;
			connectionParameters.put(dataSourceName,connectionParameter);
		}
		return connectionParameter;
	}
	public static DataSource getDataSource(String dataSourceName){
		if(dataSourceName==null||dataSourceName.equals("")){
			dataSourceName=defaultDataSourceName;
		}
		DataSource dataSource=dataSources.get(dataSourceName);
		if(dataSource==null){
			ConnectionParameter cp=getConnectionParameter(dataSourceName);
			if(cp.connectionPoolType.equalsIgnoreCase("dbcp")){
				BasicDataSource bds=new BasicDataSource();
				bds.setDriverClassName(cp.driverClassName);
				bds.setUrl(cp.url);
				bds.setUsername(cp.username);
				bds.setPassword(cp.password);
				bds.setMaxActive(cp.maxActive);
				bds.setMaxIdle(cp.maxIdle);
				bds.setMinIdle(cp.minIdle);
				bds.setMaxWait(cp.maxWait);
				bds.setRemoveAbandoned(cp.removeAbandoned);
				bds.setRemoveAbandonedTimeout(cp.removeAbandonedTimeout);
				bds.setLogAbandoned(cp.logAbandoned);
				bds.setTestWhileIdle(cp.testWhileIdle);
				bds.setValidationQuery(cp.validationQuery);
				bds.setTimeBetweenEvictionRunsMillis(cp.timeBetweenEvictionRunsMillis);
				bds.setMinEvictableIdleTimeMillis(cp.minEvictableIdleTimeMillis);
				dataSource=bds;
			}else if(cp.connectionPoolType.equalsIgnoreCase("c3p0")){
				
			}
			//
			dataSources.put(dataSourceName,dataSource);		
		}
		return dataSource;
	}
	
	public static void main(String[] args){
		System.out.println("@@@@@@@@@@@@@@"+DataSourceUtil.getConnectionParameter("dataSource").driverClassName);
		
	}
	
}
