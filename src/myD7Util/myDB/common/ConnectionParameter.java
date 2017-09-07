package myD7Util.myDB.common;

public class ConnectionParameter {
	//所用连接池类型
	public String connectionPoolType;//可选值有dbcp,c3p0等
	//
	public String driverClassName;
    public String url;
    public String username;
    public String password="";
    //Dbcp连接池专用参数
    public int maxActive;
    public int maxIdle;
    public int minIdle;
    public int maxWait;
    public boolean removeAbandoned;
    public int removeAbandonedTimeout;
    public boolean logAbandoned;
    public boolean testWhileIdle;
    public String validationQuery;
    public long timeBetweenEvictionRunsMillis;
    public long minEvictableIdleTimeMillis;
    //
    
}
