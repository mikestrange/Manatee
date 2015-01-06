package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeUtil
{
	private static long SYSTEM_TIME = 0;
	
	//所有的毫秒
	public static final int SECOND = 1000;
	public static final int MINUTE = 60*1000;
	public static final int HOUR = 60*60*1000;
	public static final int DOY = 24*60*60*1000;
	//心跳一次时间
	public static final int BEAT_TIME= 1000;//MINUTE;	
	//时间格式
	public static final String LINES="yyyy-MM-dd HH:mm:ss";
	public static final String YMD="yyyy-MM-dd";
	public static final String TIME="HH:mm:ss";
	
	//默认
	public static String getDefTime()
	{
		return getTimeFormat(LINES);
	}
	
	public static String getTimeFormat(String type)
	{
		//设置日期格式    
		SimpleDateFormat df = new SimpleDateFormat(type);
		return df.format(new Date());
	}
	
	//时间戳
	public static long getTimeStamp()
	{
		return System.currentTimeMillis();
	}
	
	//取系统时间
	public static long getTimer()
	{
		return System.currentTimeMillis() - SYSTEM_TIME;
	}
	
	//系统开始
	public static void start()
	{
		SYSTEM_TIME = System.currentTimeMillis();
	}
	//ends
}
