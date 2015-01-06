package net.core;

import interfaces.ISocketResult;

import java.util.concurrent.ConcurrentHashMap;

import net.utils.FtpRead;

import log.Log;

public class ResultManager {
	//所有解析包
	private static ConcurrentHashMap<Integer,Class<ISocketResult>> resultHash = new ConcurrentHashMap<Integer,Class<ISocketResult>>();
	
	@SuppressWarnings("unchecked")
	public static void register(int cmd,String name)
	{
		if(name==null||name==""||name=="null") return;
		Log.log().debug("#######注册解析包:"+name);
		Class<ISocketResult> actionClass = null;
		try {
			actionClass = (Class<ISocketResult>) Class.forName(name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		//
		resultHash.put(cmd, actionClass);
	}
	
	//直接解析
	public static ISocketResult create(int cmd,FtpRead bytes)
	{
		ISocketResult result = null;
		if(resultHash.containsKey(cmd)){
			try {
				result = resultHash.get(cmd).newInstance();
				result.resolve(bytes);
			} catch (InstantiationException e) {
				Log.log().warn("解析失败:"+e);
				return null;
			} catch (IllegalAccessException e) {
				Log.log().warn("解析失败:"+e);
				return null;
			}
		}
		return result;
	}
	//ends
}
