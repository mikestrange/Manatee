package mvc.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import interfaces.IAction;
import interfaces.IController;
import interfaces.ISocketRequest;
import net.core.RequestManager;
import net.utils.FtpRead;

import org.apache.mina.core.session.IoSession;


import events.Evented;


/*
 * 主要的逻辑部分
 * */
public class BaseNotice implements IController 
{
	private static ConcurrentHashMap<Integer,List<BindObserver>> invokeHash = new ConcurrentHashMap<Integer,List<BindObserver>>();
	private static final int LIT = 1;
	private static final int END = 0;
	
	//自身标记添加的事态，在摧毁后一次性删除
	private List<Integer> cmdList = new ArrayList<Integer>();
	
	//添加一个事物
	public void add(int cmd,IAction action)
	{
		List<BindObserver> vector = invokeHash.get(cmd);
		if(vector == null){
			vector = new ArrayList<BindObserver>();
			cmdList.add(cmd);	//自身标记
			invokeHash.put(cmd, vector);
		}
		vector.add(new BindObserver(this,action));
	}
	
	//移除事务  protected外界不能删除
	protected void remove(int cmd,IAction action)
	{
		if(!invokeHash.containsKey(cmd)) return;
		List<BindObserver> vector = invokeHash.get(cmd);
		for(int i=vector.size()-LIT;i>=END;i--){
			if(vector.get(i).action == action && vector.get(i).match(this)){
				vector.remove(i);
				break;
			}
		}
		if(vector.isEmpty()) invokeHash.remove(cmd);
	}
	
	//移除一个事物id
	protected void remove(int cmd)
	{
		if(!invokeHash.containsKey(cmd)) return;
		List<BindObserver> vector = invokeHash.get(cmd);
		for(int i=vector.size()-LIT;i>=END;i--){
			if(vector.get(i).match(this)) vector.remove(i);
		}
		if(vector.isEmpty()) invokeHash.remove(cmd);
	}
	
	//移除事务所有id吧
	public void destroy()
	{
		while(cmdList.isEmpty()) remove(cmdList.remove(END));
	}
	
	//处理事务
	public void execute(int cmd,IoSession session,FtpRead data)
	{
		if(!invokeHash.containsKey(cmd)) return;
		List<BindObserver> vector = getBservers(cmd,this);
		if(vector.isEmpty()) return;
		Evented event = new Evented(this,cmd,session,data);
		for(int i=vector.size()-LIT;i>=END;i--) vector.remove(i).handler(event);
	}	
	
	//直接发送到客户端
	public void sendClient(int cmd,ISocketRequest request,IoSession socket)
	{
		RequestManager.getRequest(cmd, request).sendRequest(socket);
	}
	
	//处理客服端的消息
	public void invoke(String type,IoSession socket,Object client)
	{
		
	}
	
	//*****************************所有命令入口************全局发送目标是null***********
	public static void executeEvery(int cmd,IoSession session,FtpRead data)
	{
		if(!invokeHash.containsKey(cmd)) return;
		List<BindObserver> vector = getBservers(cmd,null);
		if(vector.isEmpty()) return;
		Evented event = new Evented(null,cmd,session,data);
		for(int i=vector.size()-LIT;i>=END;i--) vector.remove(i).handler(event);
	}
	
	//取消息列表
	private static List<BindObserver> getBservers(int cmd,Object target){
		List<BindObserver> vector=invokeHash.get(cmd);
		List<BindObserver> list = new ArrayList<BindObserver>();
		if(target==null){
			for(int i=vector.size()-LIT;i>=END;i--){
				list.add(vector.get(i));
			}
		}else{
			for(int i=vector.size()-LIT;i>=END;i--){
				if(list.get(i).match(target)) list.add(vector.get(i));
			}
		}
		return list;
	}
	//ends
}
