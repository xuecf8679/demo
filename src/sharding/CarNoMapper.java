package sharding;

import java.util.HashMap;
import java.util.Map;

public class CarNoMapper {
	private static Map<String,String> mapper;
	static{
		if(mapper==null){
			mapper=new HashMap<String,String>();
		}
		mapper.put("鲁","lu");
		mapper.put("吉","ji");
		mapper.put("黑","hei");
	}
	public static String getMapValue(String carNoStr){
		String result=mapper.get(carNoStr);
		if(result==null){
			result="other";
		}
		return result;
	}
}