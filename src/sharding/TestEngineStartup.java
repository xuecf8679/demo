package sharding;

import java.util.Date;
import java.util.List;

import myD7Util.myDB.MyJdbcDao;

import com.bstek.dorado.core.EngineStartupListener;
import com.bstek.dorado.data.variant.Record;

public class TestEngineStartup extends EngineStartupListener{
	public void onStartup(){
		MyJdbcDao mjd=MyJdbcDao.getInstance("jdbcDao_sharding");
		//String sql="insert into carPosition_1(carNo,gpsTime,speed,lon,lat,address) values(?,?,?,?,?,?)";
		//mjd.executeIUD(sql,"鲁AXC949",new Date(),68,129.345678,34.876543,"胶州湾高速");
		//System.out.println("##########insert语句执行完毕!");
		String qsql="select b.company as company1 from carPosition_1 a left outer join carInfo b on a.carNo=b.carNo where a.carNo=?";
		List<Record> list=mjd.queryForMyRecordList(qsql,"吉AXC949");
		for(int i=0;i<list.size();i++){
			Record r=list.get(i);
			String company=r.getString("company1");
			System.out.println("##########company="+company);
		}
	}
}