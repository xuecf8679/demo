package myD7Util.myDB;

import java.util.Map;
import javax.sql.DataSource;
import com.bstek.ureport.env.EnvironmentProvider;

public class UreportDataSource implements EnvironmentProvider{
	
	public Map<String,DataSource> ureportDataSources;
	
	public Map<String, DataSource> getUreportDataSources() {
		return ureportDataSources;
	}

	public void setUreportDataSources(Map<String, DataSource> ureportDataSources) {
		this.ureportDataSources = ureportDataSources;
	}

	@Override
	public Map<String,DataSource> getDatasourceMap() {
		System.out.println("@@@@@@@@@@Ureport数据源已替换为："+getUreportDataSources());
		return getUreportDataSources();
	}

	@Override
	public String getTemporaryDirectory() {
		//如果返回null，那么就取jvm的临时目录
		return null;
	}

}
