package myD7Util.myDB;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import com.bstek.dorado.data.variant.Record;
import myD7Util.myTrans.DateTransUtil;

@SuppressWarnings("serial")
public class MyRecord extends Record{
		public MyRecord(){
			
		}
		public MyRecord(Map<String,Object> map){
			super(map);
		}
		//
		public BigDecimal getBigDecimal(String key) throws RuntimeException{
			BigDecimal returnValue=null;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof BigDecimal){
				returnValue=(BigDecimal)value;
			}else if(value instanceof Number){
				returnValue=new BigDecimal(((Number)value).toString());
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public boolean getBoolean(String key) throws RuntimeException{
			boolean returnValue=false;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Boolean){
				returnValue=((Boolean)value).booleanValue();
			}else if(value instanceof Number){
				int t=((Number)value).intValue();
				if(t==1){
					returnValue=true;
				}else if(t==0){
					returnValue=false;
				}else{
					throw new RuntimeException("类型转换错误!");
				}
			}else if(value instanceof String){
				String t=(String)value;
				if(t.equalsIgnoreCase("true")||t.equalsIgnoreCase("1")){
					returnValue=true;
				}else if(t.equalsIgnoreCase("false")||t.equalsIgnoreCase("0")){
					returnValue=false;
				}else{
					throw new RuntimeException("类型转换错误!");
				}
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public Date getDate(String key) throws RuntimeException{
			Date returnValue=null;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Date){
				returnValue=(Date)value;
			}else if(value instanceof String){
				String t=(String)value;
				if(t.contains("GMT")){
					returnValue=DateTransUtil.parseStringToDate$EEE_d_MMM_yyyy_HH_mm_ss_z(t);
				}else if(t.contains("日")){
					returnValue=DateTransUtil.parseStringToDate$yyyy年M月d日(t);
				}else if(t.indexOf("-")>0 && t.indexOf(":")>0 && t.indexOf(".")>0){
					returnValue=DateTransUtil.parseStringToDate$yyyy_MM_dd_HH_mm_ss_SSS(t);
				}else if(t.indexOf("-")>0 && t.indexOf(":")>0){
					returnValue=DateTransUtil.parseStringToDate$yyyy_MM_dd_HH_mm_ss(t);
				}else if(t.indexOf("-")>0){
					returnValue=DateTransUtil.parseStringToDate$yyyy_MM_dd(t);
				}else{
					throw new RuntimeException("类型转换错误!");
				}					
			}else if(value instanceof Long){
				long t=((Long)value).longValue();
				returnValue=new Date(t);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public double getDouble(String key) throws RuntimeException{
			double returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).doubleValue();
			}else if(value instanceof String){
				returnValue=Double.parseDouble((String)value);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public float getFloat(String key) throws RuntimeException{
			float returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).floatValue();
			}else if(value instanceof String){
				returnValue=Float.parseFloat((String)value);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public int getInt(String key) throws RuntimeException{
			int returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).intValue();
			}else if(value instanceof String){
				returnValue=Integer.parseInt((String)value);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public byte getByte(String key) throws RuntimeException{
			byte returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).byteValue();
			}else if(value instanceof String){
				returnValue=Byte.parseByte((String)value);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		public void setByte(String key,byte value){
			super.set(key,value);
		}
		
		public short getShort(String key) throws RuntimeException{
			short returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).shortValue();
			}else if(value instanceof String){
				returnValue=Short.parseShort((String)value);
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		public void setShort(String key,short value){
			super.set(key,value);
		}
		
		public long getLong(String key) throws RuntimeException{
			long returnValue=0;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof Number){
				returnValue=((Number)value).longValue();
			}else if(value instanceof String){
				returnValue=Long.parseLong((String)value);
			}else if(value instanceof Date){
				returnValue=((Date)value).getTime();
			}else{
				throw new RuntimeException("类型转换错误!");
			}
			return returnValue;
		}
		
		public String getString(String key) throws RuntimeException{
			String returnValue=null;
			Object value=this.get(key);
			if(value==null){
				return returnValue;
			}
			if(value instanceof String){
				returnValue=(String)value;
			}else if(value instanceof Date){
				returnValue=DateTransUtil.getDateString$yyyy_MM_dd_HH_mm_ss_SSS((Date)value);
			}else if(value instanceof Number){
				returnValue=((Number)value).toString();
			}else if(value instanceof Boolean){
				returnValue=((Boolean)value).toString();
			}else{
				returnValue=value.toString();
			}
			return returnValue;
		}
		//
		public Record toRecord(){
			return (Record)this;
		}
		
}
