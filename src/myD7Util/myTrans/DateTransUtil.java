package myD7Util.myTrans;

import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 *
 * @author xuecf
 */
public class DateTransUtil {
    public ThreadLocal threadLocalDate=new ThreadLocal();
    public static SimpleDateFormat yyyy_MM_dd_HH_mm_ss=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static SimpleDateFormat yyyy_MM_dd_HH_mm_ss_SSS=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static SimpleDateFormat yyyy_MM_dd=new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat yyyy年M月d日=new SimpleDateFormat("yyyy年M月d日");
    public static SimpleDateFormat M月d日=new SimpleDateFormat("M月d日");
    public static SimpleDateFormat yyyy年M月d日_星期E=new SimpleDateFormat("yyyy年M月d日 E");
    public static SimpleDateFormat M月d日_星期E=new SimpleDateFormat("M月d日 E");
    public static SimpleDateFormat EEE_d_MMM_yyyy_HH_mm_ss_z=new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z",Locale.US);
    //
    public static void main(String[] args){
        Date d=parseStringToDate$yyyy_MM_dd_HH_mm_ss("2009-05-01 11:11:11");
        System.out.println("@@@@@@getWeekInYearOfDate@@@@@@@@@"+getWeekInYearOfDate(new Date(),2));
    }
    public static DateTransUtil getInstance(){
        return new DateTransUtil();
    }
    public Date getThreadLocalDate(){
       Date date=(Date)threadLocalDate.get();
       if(date==null){
           date=new Date();
           threadLocalDate.set(date);
       }
       return date;
    }
    //获取如"Fri, 8 Aug 2008 12:00:59 CST"样子的日期字符串
    public static String getDateString$EEE_d_MMM_yyyy_HH_mm_ss_z(Date d){
        String dateStr="";
        dateStr=EEE_d_MMM_yyyy_HH_mm_ss_z.format(d);
        return dateStr;
    }
    //获取如"1983-11-24 12:05:16"样子的日期字符串
    public static String getDateString$yyyy_MM_dd_HH_mm_ss(Date d){
        String dateStr="";
        dateStr=yyyy_MM_dd_HH_mm_ss.format(d);
        return dateStr;
    }
    //获取如"1983-11-24 12:05:16.268"样子的日期字符串
    public static String getDateString$yyyy_MM_dd_HH_mm_ss_SSS(Date d){
        String dateStr="";
        dateStr=yyyy_MM_dd_HH_mm_ss_SSS.format(d);
        return dateStr;
    }
    //获取如"1983-11-24"样子的日期字符串
    public static String getDateString$yyyy_MM_dd(Date d){
        String dateStr="";
        dateStr=yyyy_MM_dd.format(d);
        return dateStr;
    }
    //获取如"1983年11月24日"样子的日期字符串
    public static String getDateString$yyyy年M月d日(Date d){
        String dateStr="";
        dateStr=yyyy年M月d日.format(d);
        return dateStr;
    }
    //获取如"11月24日"样子的日期字符串
    public static String getDateString$M月d日(Date d){
        String dateStr="";
        dateStr=M月d日.format(d);
        return dateStr;
    }
    //获取如"1983年11月24日 星期四"样子的日期字符串
    public static String getDateString$yyyy年M月d日_星期E(Date d){
        String dateStr="";
        dateStr=yyyy年M月d日_星期E.format(d);
        return dateStr;
    }
    //获取如"11月24日 星期四"样子的日期字符串
    public static String getDateString$M月d日_星期E(Date d){
        String dateStr="";
        dateStr=M月d日_星期E.format(d);
        return dateStr;
    }
    //将"Fri, 8 Aug 2008 02:56:34 GMT"样子的字符串解析为日期对象
    public static Date parseStringToDate$EEE_d_MMM_yyyy_HH_mm_ss_z(String dateStr){
        Date d=null;
        try{
            d=EEE_d_MMM_yyyy_HH_mm_ss_z.parse(dateStr);
        }catch(ParseException pe){
        	throw new RuntimeException(pe.getMessage());         
        }
        return d;
    }
    //将"1983-11-24 12:05:16"样子的字符串解析为日期对象
    public static Date parseStringToDate$yyyy_MM_dd_HH_mm_ss(String dateStr){
        Date d=null;
        try{
            d=yyyy_MM_dd_HH_mm_ss.parse(dateStr);
        }catch(ParseException pe){
        	throw new RuntimeException(pe.getMessage());
        }
        return d;
    }
    //将"1983-11-24 12:05:16.555"样子的字符串解析为日期对象
    public static Date parseStringToDate$yyyy_MM_dd_HH_mm_ss_SSS(String dateStr){
        Date d=null;
        try{
            d=yyyy_MM_dd_HH_mm_ss_SSS.parse(dateStr);
        }catch(ParseException pe){
        	throw new RuntimeException(pe.getMessage());
        }
        return d;
    }
    //将"1983-11-24"样子的字符串解析为日期对象
    public static Date parseStringToDate$yyyy_MM_dd(String dateStr){
        Date d=null;
        try{
            d=yyyy_MM_dd.parse(dateStr);
        }catch(ParseException pe){
        	throw new RuntimeException(pe.getMessage());
        }
        return d;
    }
    //将"1983年11月24日"样子的字符串解析为日期对象
    public static Date parseStringToDate$yyyy年M月d日(String dateStr){
        Date d=null;
        try{
            d=yyyy年M月d日.parse(dateStr);
        }catch(ParseException pe){
        	throw new RuntimeException(pe.getMessage());
        }
        return d;
    }
    //获取当前日期中的年份
    public int getCurrentYear(){
        return getYearOfDate(getThreadLocalDate());
    }
    public static int getYearOfDate(Date d){
        int currentYear=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentYear=Integer.parseInt(dateString.substring(0,4));
        return currentYear;
    }
    
  //获取当前日期中的年份；如果本月是一月，则返回去年的年份
    public int getCurrentOrLastYear(){
        return getCorLYearOfDate(getThreadLocalDate());
    }
    public static int getCorLYearOfDate(Date d){
        int currentYear=0;
        int currentMonth=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentYear=Integer.parseInt(dateString.substring(0,4));
        currentMonth=Integer.parseInt(dateString.substring(5,7));
        if(currentMonth==1){
        	return currentYear-1;
        }else{
        return currentYear;
        }
    }
    
    //获取当前日期中的月份
    public int getCurrentMonth(){
        return getMonthOfDate(getThreadLocalDate());
    }
    public static int getMonthOfDate(Date d){
        int currentMonth=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentMonth=Integer.parseInt(dateString.substring(5,7));
        return currentMonth;
    }
    //获取当前日期中的前一个月份(这个方法与前台的monthDropDown搭配使用)
    public int getBeforeMonth(){
        return getBeforeMonthOfDate(getThreadLocalDate());
    }
    public static int getBeforeMonthOfDate(Date d){
        int currentMonth=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentMonth=Integer.parseInt(dateString.substring(5,7));
        return currentMonth-1;
     
    }
    //获取当前日期中的前两个月份(这个方法与前台的monthDropDown搭配使用)；比如这个月是三月，则返回一月
    public int getTwoBeforeMonth(){
        return getTwoBeforeMonthOfDate(getThreadLocalDate());
    }
    public static int getTwoBeforeMonthOfDate(Date d){
        int currentMonth=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentMonth=Integer.parseInt(dateString.substring(5,7));
        if(currentMonth==1){
        	return 11;
        }else{
        	return currentMonth-2;
        }      
    }
    //获取当前日期中的日
    public int getCurrentDay(){
        return getDayOfDate(getThreadLocalDate());
    }
    public static int getDayOfDate(Date d){
        int currentDay=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentDay=Integer.parseInt(dateString.substring(8,10));
        return currentDay;
    }
    //获取当前日期中的小时
    public int getCurrentHour(){
        return getHourOfDate(getThreadLocalDate());
    }
    public static int getHourOfDate(Date d){
        int currentHour=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentHour=Integer.parseInt(dateString.substring(11,13));
        return currentHour;
    }
    //获取当前日期中的分钟
    public int getCurrentMinute(){
        return getMinuteOfDate(getThreadLocalDate());
    }
    public static int getMinuteOfDate(Date d){
        int currentMinute=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentMinute=Integer.parseInt(dateString.substring(14,16));
        return currentMinute;
    }
    //获取当前日期中的秒
    public int getCurrentSecond(){
        return getSecondOfDate(getThreadLocalDate());
    }
    public static int getSecondOfDate(Date d){
        int currentSecond=0;
        String dateString=getDateString$yyyy_MM_dd_HH_mm_ss(d);
        currentSecond=Integer.parseInt(dateString.substring(17,19));
        return currentSecond;
    }
    //获取当前日期在该年份中属于第几个周
    //minDaysInFirstWeek设置一年中第一个星期所需的最少天数,例如,如果定义第一个星期包含一年第一个月的第一天,
    //则使用值1调用此方法.如果最少天数必须是一整个星期,则使用值7调用此方法
    public int getCurrentWeekInYear(int minDaysInFirstWeek){
        return getWeekInYearOfDate(getThreadLocalDate(),minDaysInFirstWeek);
    }
    //获取该日期在该年份中属于第几个周
    //minDaysInFirstWeek设置一年中第一个星期所需的最少天数,例如,如果定义第一个星期包含一年第一个月的第一天,
    //则使用值1调用此方法.如果最少天数必须是一整个星期,则使用值7调用此方法
    public static int getWeekInYearOfDate(Date d,int minDaysInFirstWeek){
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        c.setMinimalDaysInFirstWeek(minDaysInFirstWeek);
        return c.get(Calendar.WEEK_OF_YEAR);
    }
    //获取当前日期在该月份中属于第几个周
    //minDaysInFirstWeek设置一年中第一个星期所需的最少天数,例如,如果定义第一个星期包含一年第一个月的第一天,
    //则使用值1调用此方法.如果最少天数必须是一整个星期,则使用值7调用此方法
    public int getCurrentWeekInMonth(int minDaysInFirstWeek){
        return getWeekInMonthOfDate(getThreadLocalDate(),minDaysInFirstWeek);
    }
    //获取该日期在该月份中属于第几个周
    //minDaysInFirstWeek设置一年中第一个星期所需的最少天数,例如,如果定义第一个星期包含一年第一个月的第一天,
    //则使用值1调用此方法.如果最少天数必须是一整个星期,则使用值7调用此方法
    public static int getWeekInMonthOfDate(Date d,int minDaysInFirstWeek){
        Calendar c=Calendar.getInstance();
        c.setTime(d);
        c.setMinimalDaysInFirstWeek(minDaysInFirstWeek);
        return c.get(Calendar.WEEK_OF_MONTH);
    }

    //获取当前日期距1970-1-1的毫秒数
    public long getTotalHaoMiaoOfCurrentDate(){
        long totalHaoMiaoOfCurrentDate=0;
        totalHaoMiaoOfCurrentDate=getThreadLocalDate().getTime();
        return totalHaoMiaoOfCurrentDate;
    }
    public long getTotalHaoMiaoOfDate(Date d){
        long totalHaoMiaoOfCurrentDate=0;
        totalHaoMiaoOfCurrentDate=d.getTime();
        return totalHaoMiaoOfCurrentDate;
    }
    //获取某日期之前或之后多少天的日期
    public static Date getDateOfDayOffset(Date oldDate,int offsetValue){
        return getOffsetDate(oldDate,offsetValue,Calendar.DAY_OF_MONTH);
    }
    //获取某日期之前或之后多少个月的日期
    public static Date getDateOfMonthOffset(Date oldDate,int offsetValue){
        return getOffsetDate(oldDate,offsetValue,Calendar.MONTH);
    }
    //获取某日期之前或之后多少个星期的日期
    public static Date getDateOfWeekOffset(Date oldDate,int offsetValue){
        return getOffsetDate(oldDate,offsetValue,Calendar.WEEK_OF_MONTH);
    }
    //获取某日期之前或之后多少年的日期
    public static Date getDateOfYearOffset(Date oldDate,int offsetValue){
        return getOffsetDate(oldDate,offsetValue,Calendar.YEAR);
    }
    //获取某日期之前或之后多少个小时的日期
    public static Date getDateOfHourOffset(Date oldDate,int offsetValue){
        return getOffsetDate(oldDate,offsetValue,Calendar.HOUR);
    }
    //获取某日期之前或之后多少小时/天/周/月/年等时间单位的日期
    public static Date getOffsetDate(Date oldDate,int offsetValue,int offsetType){
        Calendar c=Calendar.getInstance();
        c.setTime(oldDate);
        c.add(offsetType,offsetValue);
        return c.getTime();
    }
    
    //获取两个日期之间相差的天数,非精确,只要两个日期不在一天即使相差不到24小时也算是相差一天
    public static long getDaysBetween(Date date1,Date date2){
        String str_d1=getDateString$yyyy_MM_dd(date1);
        String str_d2=getDateString$yyyy_MM_dd(date2);
        Date d1=parseStringToDate$yyyy_MM_dd(str_d1);
        Date d2=parseStringToDate$yyyy_MM_dd(str_d2);
        return getDaysBetween2(d1,d2);
    }
    //获取两个日期之间相差的天数,精确,两个日期必须相差满24小时才算是差一天
    public static long getDaysBetween2(Date datetime1,Date datetime2){
        long returnValue=0;
        long HaoMiao1=datetime1.getTime();
        long HaoMiao2=datetime2.getTime();
        long HaoMiaoCha=0;
        if(HaoMiao1>HaoMiao2){
            HaoMiaoCha=HaoMiao1-HaoMiao2;
        }else{
            HaoMiaoCha=HaoMiao2-HaoMiao1;
        }
        returnValue=HaoMiaoCha/86400000;//1000*60*60*24

        return returnValue;
    }
    public static long getHoursBetween(Date datetime1,Date datetime2){
        long returnValue=0;
        long HaoMiao1=datetime1.getTime();
        long HaoMiao2=datetime2.getTime();
        long HaoMiaoCha=0;
        if(HaoMiao1>HaoMiao2){
            HaoMiaoCha=HaoMiao1-HaoMiao2;
        }else{
            HaoMiaoCha=HaoMiao2-HaoMiao1;
        }
        returnValue=HaoMiaoCha/3600000;//1000*60*60

        return returnValue;
    }
    public static long getMinutesBetween(Date datetime1,Date datetime2){
        long returnValue=0;
        long HaoMiao1=datetime1.getTime();
        long HaoMiao2=datetime2.getTime();
        long HaoMiaoCha=0;
        if(HaoMiao1>HaoMiao2){
            HaoMiaoCha=HaoMiao1-HaoMiao2;
        }else{
            HaoMiaoCha=HaoMiao2-HaoMiao1;
        }
        returnValue=HaoMiaoCha/60000;//1000*60

        return returnValue;
    }
    public static long getSecondsBetween(Date datetime1,Date datetime2){
        long returnValue=0;
        long HaoMiao1=datetime1.getTime();
        long HaoMiao2=datetime2.getTime();
        long HaoMiaoCha=0;
        if(HaoMiao1>HaoMiao2){
            HaoMiaoCha=HaoMiao1-HaoMiao2;
        }else{
            HaoMiaoCha=HaoMiao2-HaoMiao1;
        }
        returnValue=HaoMiaoCha/1000;//1000

        return returnValue;
    }
    //返回如:5天3小时零6分或10分钟
    public static String getTimeStrBetween(Date datetime1,Date datetime2){
        String returnStr="";
        long HaoMiao1=datetime1.getTime();
        long HaoMiao2=datetime2.getTime();
        long HaoMiaoCha=0;
        if(HaoMiao1>HaoMiao2){
            HaoMiaoCha=HaoMiao1-HaoMiao2;
        }else{
            HaoMiaoCha=HaoMiao2-HaoMiao1;
        }
        //
        long tian=HaoMiaoCha/86400000;//(24*60*60*1000)
        if(tian>0){
            returnStr=returnStr+tian+"天";
        }
        //
        long tianShengYu=HaoMiaoCha%86400000;
        long xiaoshi=tianShengYu/3600000;//(60*60*1000)
        if(xiaoshi>0){
            returnStr=returnStr+xiaoshi+"小时";
        }
        //
        long fenShengYu=tianShengYu%3600000;
        long fen=fenShengYu/60000;//(60*1000)
        if(fen>0){
            if(returnStr.equals("")){
                returnStr=returnStr+fen+"分钟";
            }else{
                returnStr=returnStr+"零"+fen+"分";
            }

        }
        if(returnStr.equals("")){
          returnStr="少于1分钟";
        }
        //
        return returnStr;
    }

}

