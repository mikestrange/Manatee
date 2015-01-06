package log;

import java.util.ArrayList;
import java.util.List;

import utils.TimeUtil;

public class Log {
	public static final int DEBUG = 1;
    public static final int INFO = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;
	private static final List<String> LITE_TEXT = new ArrayList<String>();
	private static final String DEFAULT_NAME = "[ NONE ]";			//默认日志属性
	//保存最多
	public static int LOG_LENG = 5000;
	//是否保存
	public static boolean SHARE_LOG = true;
	//是否输出
	public static boolean TRACE = true;
	//日志界点
	private static List<String> Log_List = new ArrayList<String>();
	
	//[log object]
	public String className;
	
	public void debug(Object ...rest)
	{
		output(DEBUG, rest);
	}
	
	public void info(Object ...rest)
	{
		output(INFO, rest);
	}
	
	public void warn(Object ...rest)
	{
		output(WARN, rest);
	}
	
	public void error(Object ...rest)
	{
		output(ERROR, rest);
	}
	
	//输入日志
	protected void output(int type,Object[] rest)
	{
		String str = "";
		for(int i = 0 ; i < rest.length ; i++) str += rest[i]+" ";
		//
		String chat = this.className + str + "<-last->" + LITE_TEXT.get(type)+" time:"+TimeUtil.getDefTime();
		//输出
		if(TRACE) System.out.print(chat+"\n");
		//是否保存日子，直接文本保存
		if (SHARE_LOG) addLog(chat);
	}
	
	private static void addLog(String str)
	{
		if(length() > LOG_LENG) Log_List.remove(0);
		Log_List.add(str);
	}
	
	public static void clear()
	{
		while (Log_List.size()>0){
			Log_List.remove(0);
		}
	}
	
	public static int length()
	{
		return Log_List.size();
	}

	/*
	 * 日志
	 * */
	private static Log _log;
	private static boolean _value;
	
	public static Log log(Object target)
	{
		if(!_value){
			_value = true;
			LITE_TEXT.add("[----]");
			LITE_TEXT.add("[debug]");
			LITE_TEXT.add("[info]");
			LITE_TEXT.add("[warn]");
			LITE_TEXT.add("[error]");
			_log = new Log();
		}
		if(null == target){
			_log.className = DEFAULT_NAME;
		}else{
			_log.className = "[ "+target.getClass().toString()+" ]";
		}
        return _log;
	}
	
	public static Log log()
	{
		return log(null);
	}
	
	//查看当前内存
	public static void runTime()
	{
		Runtime run = Runtime.getRuntime(); 
		long max = run.maxMemory(); 
		long total = run.totalMemory(); 
		long free = run.freeMemory(); 
		long usable = max - total + free; 
		Log.log().debug("->最大内存 = " + max/1024+"kb");
		Log.log().debug("->已分配内存 = " + total/1024+"kb");
		Log.log().debug("->已分配内存中的剩余空间 = " + free/1024+"kb");
		Log.log().debug("->最大可用内存 = " + usable/1024+"kb");
	}
	//ends
}
