package net.nio;


import interfaces.IAction;
import interfaces.IController;

import java.util.concurrent.ConcurrentHashMap;

import log.Log;
import mvc.core.BaseNotice;

import net.core.BindManager;
import net.utils.FtpRead;
import net.utils.TcpResolve;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

/*
 * 逻辑处理
 * 
 * */

public class ServerHandler extends IoFilterAdapter implements IoHandler 
{
	private static ServerHandler samplMinaServerHandler = null;

	public static ServerHandler getInstances() {
		if (null == samplMinaServerHandler) {
			samplMinaServerHandler = new ServerHandler();
			samplMinaServerHandler.moduleHash = new ConcurrentHashMap<Integer,IController>();
		}
		return samplMinaServerHandler;
	}

	//一个命令模块
	private ConcurrentHashMap<Integer,IController> moduleHash = null;
	
	public boolean hasController(int type)
	{
		return moduleHash.containsKey(type);
	}
	
	//添加模块
	public void addMoule(int type,IController controller)
	{
		if(moduleHash.containsKey(type)) return;
		Log.log(this).debug("成功注册 IController:type = "+type+" , className = ["+controller.getClass().toString()+"]");
		moduleHash.put(type, controller);
	}
	
	//为一个模块添加事件
	public void addAction(int type,int cmd,IAction action)
	{
		if(moduleHash.containsKey(type)){
			Log.log(this).debug("添加 IAction cmd = "+cmd+" , className = ["+action.getClass().toString()+"], 解析包:无");
			moduleHash.get(type).add(cmd, action);
		}
	}
	
	//移除模块
	public void removeController(int type)
	{
		if(moduleHash.containsKey(type)){
			Log.log(this).debug("移除模块 IController:type = "+type);
			moduleHash.remove(type).destroy();
		}
	}
	
	//连接
	public void sessionCreated(IoSession session) throws Exception 
	{
		Log.log(this).debug("#连接"+session.getServiceAddress().toString());
		BindManager.gets().add(session);	//绑定一个连接解包器
	}

	//打开连接
	public void sessionOpened(IoSession session) throws Exception
	{
		
	}
	
	//关闭通知
	public void sessionClosed(IoSession session) 
	{
		Log.log(this).debug("#关闭连接");
		BindManager.gets().clearSession(session);
	}
	
	//客户端发过来的信息
	public void messageReceived(IoSession session, Object message) throws Exception 
	{	
		FtpRead bytes = (FtpRead) message;
		int cmd = bytes.readInt();
		int type = bytes.readShort();	
		Log.log(this).debug("->client cmd call:"+cmd+",type:"+type);
		//type ==0的时候给所有模块发送
		if(type==TcpResolve.NULL){
			BaseNotice.executeEvery(cmd, session, bytes);
		}else{
			if(moduleHash.containsKey(type)){
				moduleHash.get(type).execute(cmd,session,bytes);
			}else{
				Log.log(this).warn("无模块发送:"+type);
			}
		}
	}
	
	//连接异常
	public void exceptionCaught(IoSession session, Throwable data) throws Exception 
	{
		BindManager.gets().clearSession(session);
		Log.log(this).debug("#连接异常");
	}

	//发送给客服端回调
	public void messageSent(IoSession session, Object data) throws Exception 
	{
		//Log.log(this).debug("#写入调用");
	}
	
	//空闲状态，发送心跳吧
	public void sessionIdle(IoSession session, IdleStatus data) throws Exception 
	{
		Log.log(this).debug("#空闲状态");
		BindManager.gets().beat(session);
	}

	//写入关闭？
	public void inputClosed(IoSession session) throws Exception 
	{
		Log.log(this).debug("#inputClosed");
	}
	
	//ends
}
