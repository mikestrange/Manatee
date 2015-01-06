package net.core;

import org.apache.mina.core.session.IoSession;

import consts.CmdDefinedName;

import client.request.ErrorRequest;
import interfaces.ISocketRequest;


public class RequestManager 
{
	
	//全通发送  ,一旦打包，之后可以发送给所有目标，不需要重新建立
	public static IRequest getRequest(int cmd,ISocketRequest data,int type)
	{
		RequestRodeo request = new RequestRodeo(cmd,type);
		request.organize(data);
		return request;
	}
	
	//单纯命令发送
	public static IRequest getRequest(int cmd,ISocketRequest data)
	{
		return getRequest(cmd,data,0);
	}
	
	public static IRequest getRequest(int cmd)
	{
		return getRequest(cmd,null,0);
	}
	
	//给客户端错误
	public static void sendError(int type,IoSession session)
	{
		if(session==null) return;
		getRequest(CmdDefinedName.ERROR,new ErrorRequest(type)).sendRequest(session);
	}
	
	//给客户端错误
	public static void sendHeart(IoSession session)
	{
		if(session==null) return;
		getRequest(CmdDefinedName.HEART_BEAT).sendRequest(session);
	}
	
	//ends
}
