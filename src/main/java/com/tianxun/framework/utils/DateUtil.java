/**
 * 
 */
package com.tianxun.framework.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author ken.ma
 *
 */
public class DateUtil {
    
    private static Logger logger = Logger.getLogger(DateUtil.class);
    
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final SimpleDateFormat simpleDateFormatH =new SimpleDateFormat("yyyy-MM-dd HH");
    
    private static final SimpleDateFormat simpleDateFormatHM =new SimpleDateFormat("yyyy-MM-dd HH:mm");
    
    private static final SimpleDateFormat simpleDateFormatHMS =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static final SimpleDateFormat simpleFormatHMS =new SimpleDateFormat("HH:mm:ss");
    
    /**
     * 将Date对象转换为字符串格式为yyyy-MM-dd
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        String ret = null;
        synchronized(simpleDateFormat) {
            ret = simpleDateFormat.format(date);
        }
        return ret;
    }
    
    /**
     * 将Date对象转换为字符串格式为yyyy-MM-dd HH
     * @param date
     * @return
     */
    public static String dateToFullString3(Date date){
        String ret = null;
        synchronized(simpleDateFormatH) {
            ret = simpleDateFormatH.format(date);
        }
        return ret;
    }
    
    /**
     * 将Date对象转换为字符串格式为yyyy-MM-dd HH:mm
     * @param date
     * @return
     */
    public static String dateToFullString2(Date date){
        String ret = null;
        synchronized(simpleDateFormatHM) {
            ret = simpleDateFormatHM.format(date);
        }
        return ret;
    }
    
    /**
     * 将Date对象转换为字符串格式为yyyy-MM-dd HH:mm:ss
     * @param date
     * @return
     */
    public static String dateToFullString(Date date){
        String ret = null;
        synchronized(simpleDateFormatHMS) {
            ret = simpleDateFormatHMS.format(date);
        }
        return ret;
    }
    
    /**
     * 
     * 将Date对象转换为特定格式字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String dateToString(Date date, String pattern){
        return new SimpleDateFormat(pattern).format(date);
    }
    
    /**
     * 将字符串格式为HH:mm:ss转换为Date对象
     * @param date
     * @return
     */
    public static Date stringToDateHMS(String dateStr){
        Date ret = null;
        try {
            synchronized(simpleFormatHMS) {
                ret = simpleFormatHMS.parse(dateStr);
            }
            return ret;
        } catch (ParseException e) {
            logger.error(dateStr + " convert error:" + e.getMessage());
            return null;
        }       
    }
    
    /**
     * 将字符串格式为yyyy-MM-dd转换为Date对象
     * @param date
     * @return
     */
    public static Date stringToDate(String dateStr){
        Date ret = null;
        try {
            synchronized(simpleDateFormat) {
                ret = simpleDateFormat.parse(dateStr);
            }
            return ret;
        } catch (ParseException e) {
            logger.error(dateStr + " convert error:" + e.getMessage());
            return null;
        }       
    }
    
    /**
     * 将字符串格式为yyyy-MM-dd HH:mm:ss转换为Date对象
     * @param date
     * @return
     */
    public static Date fullStringToDate(String dateStr){
        Date ret = null;
        try{
        	if(StringUtils.isBlank(dateStr)){
        		return null;
        	}
        	String[] nums = dateStr.split(":");
        	if(nums.length == 3){
        		synchronized(simpleDateFormatHMS) {
                    ret = simpleDateFormatHMS.parse(dateStr);
                }
        	}else if(nums.length == 2){
        		synchronized(simpleDateFormatHM) {
                    ret = simpleDateFormatHM.parse(dateStr);
                }
        	}
            
            return ret;
        } catch (ParseException e) {
            logger.error(dateStr + " convert error:" + e.getMessage());
            return null;
        }
    }
    
    /**
     * 
     * 将特定格式字符串转换为Date对象
     * @param date
     * @param pattern
     * @return
     */
    public static Date stringToDate(String dateStr, String pattern){
        try{
            return new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            logger.error(dateStr + " convert error:" + e.getMessage());
            return null;
        }
    }
    
    /**
     * 对某日期增加天数
     * @param date
     * @param increaseDays 支持负数
     * @return
     */
    public static String addDays(String date, int increaseDays){
        if(increaseDays == 0) {
            return date;
        }
        String[] s = date.split("-");
        Calendar calendar = new GregorianCalendar(Integer.parseInt(s[0]),
                Integer.parseInt(s[1]) - 1, Integer.parseInt(s[2]));
        calendar.add(Calendar.DATE, increaseDays);
        String ret = null;
        synchronized(simpleDateFormat) {
            ret = simpleDateFormat.format(calendar.getTime());
        }
        return ret;
    }
    
    /**
     * 对某日期时间进行计算
     * @param date
     * @param hours 支持负数
     * @param mins 支持负数
     * @param seconds 支持负数
     * @return
     */
    public static String addTime(String date, int hours, int mins, int seconds){
        Calendar calendar = new GregorianCalendar();
        try {
            synchronized(simpleDateFormatHMS) {
                calendar.setTime(simpleDateFormatHMS.parse(date));   
            }           
        } catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
            throw new RuntimeException("date is not match 'yyyy-MM-dd HH:mm:ss' format.");
        }
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, mins);
        calendar.add(Calendar.SECOND, seconds);
        String ret = null;
        synchronized(simpleDateFormatHMS) {
            ret = simpleDateFormatHMS.format(calendar.getTime());
        }
        return ret;
    }
    
    /**
     * 更改日期时间模式
     * @param date
     * @param fromFormat
     * @param toFormat
     * @return
     */
    public static String changeDateFormat(String date, String fromFormat, String toFormat){
        SimpleDateFormat fromMater = new SimpleDateFormat(fromFormat);
        SimpleDateFormat toMater = new SimpleDateFormat(toFormat);
        String ret = "";
        try {
            Date fromDate = fromMater.parse(date);
            ret = toMater.format(fromDate);
        } catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return ret;
    }
    
    
    /**
     * 计算两日期天数的差
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffDays(String startDate, String endDate){
        int rs = 0;
        try {
            synchronized(simpleDateFormat) {
                rs = (int)((simpleDateFormat.parse(endDate).getTime() - simpleDateFormat.parse(startDate).getTime()) / (1000 * 60 * 60 * 24));   
            }           
        } catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return rs;
    }
    
    /**
     * 计算两日期天数的差
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffDaysForDate(Date startDate, Date endDate){
           Long rs = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);      
        return rs.intValue();
    } 
    
    /**
     * 计算时间差
     * @param startDate
     * @param endDate
     * @param format
     * @return
     */
    public static int diffDaysWithFormat(String startDate, String endDate, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try{
            return (int)((sf.parse(endDate).getTime() - sf.parse(startDate).getTime()) / (1000 * 60 * 60 * 24));
        }catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return 0;
    }
    
    /** 
     * 取得星期几 
     * @param someDay 
     * @return 
     */ 
    public static int getDayOfWeek(Date someDay){ 
		Calendar cal = Calendar.getInstance(); 
		cal.setTime(someDay); 
		int day = cal.get(Calendar.DAY_OF_WEEK) - 1; 
		if (day == 0) day = 7; 
    	return day; 
    } 
    /**
     * 计算两日期相差分钟数
     * @param startDate
     * @param endDate
     * @return
     */
    public static int diffMinute(String startDate, String endDate){
        int rs = 0;
        try {
            synchronized(simpleDateFormat) {
                rs = (int)((simpleDateFormat.parse(endDate).getTime() - simpleDateFormat.parse(startDate).getTime()) / (1000 * 60));   
            }           
        } catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return rs;
    }
    
    /**
     * 计算两个日期间相差多少秒
     * @param date1
     * @param date2
     * @return
     */
    public static int getDiffSecs(Date date1, Date date2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        
        if (date1.before(date2)){
            c1.setTime(date1);
            c2.setTime(date2);
        }else{
            c1.setTime(date2);
            c2.setTime(date1);
        }
        
        int year = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
        for(int i=0;i<year;i++){
               c1.set(Calendar.YEAR,(c1.get(Calendar.YEAR)+1));
               day += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        int h = c2.get(Calendar.HOUR_OF_DAY)-c1.get(Calendar.HOUR_OF_DAY);
        int m =  c2.get(Calendar.MINUTE)-c1.get(Calendar.MINUTE);
        int s =  c2.get(Calendar.SECOND)-c1.get(Calendar.SECOND);
        
        return ((day*24 + h)*60 +m)*60 + s;
    }
    
    public static int getDiffMins(String startDate, String endDate){
        int rs = 0;
        try {
            synchronized(simpleDateFormatHM) {
                rs = (int)((simpleDateFormatHM.parse(endDate).getTime() - simpleDateFormatHM.parse(startDate).getTime()) / (1000 * 60));   
            }           
        } catch (ParseException e) {
            ExceptionLogUtil.logExceptionInfo(logger, e);
        }
        return rs;
    }
    /**
     * 计算两个日期间相差多少分钟
     * @param date1
     * @param date2
     * @return
     */
    public static int getDiffMins(Date date1, Date date2){
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        
        if (date1.before(date2)){
            c1.setTime(date1);
            c2.setTime(date2);
        }else{
            c1.setTime(date2);
            c2.setTime(date1);
        }
        
        int year = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
        int day = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
        for(int i=0;i<year;i++){
               c1.set(Calendar.YEAR,(c1.get(Calendar.YEAR)+1));
               day += c1.getMaximum(Calendar.DAY_OF_YEAR);
        }
        int h = c2.get(Calendar.HOUR_OF_DAY)-c1.get(Calendar.HOUR_OF_DAY);
        int m =  c2.get(Calendar.MINUTE)-c1.get(Calendar.MINUTE);
        
        return (day*24 + h)*60 +m;
    }
    
	/**
	 * 计算两个日期间相差多少天
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDiffDay(Date date1, Date date2){
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		if (date1.before(date2)){
			c1.setTime(date1);
			c2.setTime(date2);
		}else{
			c1.setTime(date2);
			c2.setTime(date1);
		}
		
		int year = c2.get(Calendar.YEAR)-c1.get(Calendar.YEAR);
		int day = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
		for(int i=0;i<year;i++){
			   c1.set(Calendar.YEAR,(c1.get(Calendar.YEAR)+1));
			   day += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}

		return day;
	}
	
	/**
	 * 计算年龄
	 * @param dateStr
	 * @return
	 */
	public static int calAge(String dateStr) {
	    Date date = stringToDate(dateStr);
	    Calendar birthday = Calendar.getInstance();
	    birthday.setTime(date);
	    Calendar now = Calendar.getInstance();
	    int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);
	    int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);
	    int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
	    //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。
	    if(day<0){
	        month -= 1;
	        now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
	        day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
	    }
	    if(month<0){
	        month = (month+12)%12;
	        year--;
	    }
	    
	    return year;
	}
	
	/**
     * 计算两个日期间相差多少分钟
     * @param date1
     * @param date2
     * @return
     */
    public static String getDuration(String startDate, String endDate){
        java.util.Calendar c1=java.util.Calendar.getInstance();
        java.util.Calendar c2=java.util.Calendar.getInstance();
        java.text.DateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        String HOUR="", MINUTE ="";
        
        Date date1,date2;
        try {
            date1 = df.parse(startDate);
            date2 = df.parse(endDate);          
            c1.setTime(date1);
            c2.setTime(date2);
            int day = c2.get(Calendar.DAY_OF_YEAR)-c1.get(Calendar.DAY_OF_YEAR);
            int h = c2.get(Calendar.HOUR_OF_DAY)-c1.get(Calendar.HOUR_OF_DAY);
            int m =  c2.get(Calendar.MINUTE)-c1.get(Calendar.MINUTE);
            if(day >= 0){
                h = h + day * 24;
            }else{
                return "FAIL";
            }
            if(h < 0 || (h == 0 && m <= 0)){
                return "FAIL";
            }                       
            if(m<0){
                m = m + 60;
                h = h - 1;
            }
            if(m<10 ){
                MINUTE = "0" + String.valueOf(m);
            }else{
                MINUTE = String.valueOf(m);
            }
            if(h < 10 ){
                HOUR = "0" + String.valueOf(h);
            }else{
                HOUR = String.valueOf(h);
            }
            return HOUR + ":" + MINUTE;
        } catch (Exception e) {
        }
        return HOUR + ":" + MINUTE;
    }
	
	public static void main(String[] args) {
	    System.out.println(DateUtil.dateToFullString3(new Date()));
    }
}
