package net.core;

import java.util.concurrent.ConcurrentHashMap;

import log.Log;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


import net.utils.TcpResolve;

/*
 * 一个解析包管理
 * */
public class BindManager {
	private static BindManager clients = null;
	
	public static BindManager gets() {     
        if (null == clients) {     
        	clients = new BindManager();
        	clients.hashmap = new ConcurrentHashMap<IoSession,BindData>();
        	clients.clientHash = new ConcurrentHashMap<Integer,BindData>();
        }     
        return clients;     
    }     
	
	//一个连接对应一个socket包
	private ConcurrentHashMap<IoSession,BindData> hashmap = null;
	//客户端的绑定
	private ConcurrentHashMap<Integer,BindData> clientHash = null;

	public boolean add(IoSession key)
	{
		if(hashmap.containsKey(key)) return false;
		hashmap.put(key, new BindData(key));
		return true;
	}
	
	//关闭连接,必须在这里调用
	public void clearSession(IoSession key)
	{
		if(null==key) return;
		if(key.isConnected()) key.close(false);
		remove(key);
	}
	
	//通过socket连接
	public boolean remove(IoSession key)
	{
		BindData bind = hashmap.remove(key);
		if(null!=bind){
			bind.free();
			clientHash.remove(bind.client_uid);
		}
		return true;
	}
	
	public int getUid(IoSession key)
	{
		BindData bind = hashmap.get(key);
		if(null!=bind) return bind.client_uid;
		return -1;
	}
	
	public TcpResolve getResolve(IoSession key)
	{
		if(!hashmap.containsKey(key)) return null;
		return hashmap.get(key).resolve;
	}
	
	public void beat(IoSession key)
	{
		if(hashmap.containsKey(key)) hashmap.get(key).beat();
	}
	
	public void unobstructed(IoSession key)
	{
		if(hashmap.containsKey(key)) hashmap.get(key).unobstructed();
	}
	
	//读数据
	public void read(IoSession key,IoBuffer in,ProtocolDecoderOutput output)
	{
		if(hashmap.containsKey(key)){
			hashmap.get(key).read(in, output);
		}else{
			Log.log(this).debug("#无解码#->可能会引发错误");
			in.clear();
		}
	}
	
	//绑定用户
	public boolean bindUser(int uid,IoSession key)
	{
		if(!hashmap.containsKey(key)) return false;
		if(clientHash.containsKey(uid)) return false;
		//双向绑定
		hashmap.get(key).bind(uid);
		clientHash.put(uid, hashmap.get(key));
		return true;
	}
	
	//是否绑定过用户
	public boolean isBindUser(int uid)
	{
		return clientHash.containsKey(uid);
	}
	
	//取用户之前的socket
	public IoSession getIoSession(int uid)
	{
		if(clientHash.containsKey(uid)){
			return clientHash.get(uid).session;
		}
		return null;
	}
	
	//取用户数据
	public CarryData getData(int uid)
	{
		if(clientHash.containsKey(uid)) return clientHash.get(uid).data;
		return null;
	}
	
	public CarryData getData(IoSession key)
	{
		BindData bind = hashmap.get(key);
		if(null!=bind) return bind.data;
		return null;
	}
	
	//断开之前给客户端发信息
	public void sendError(int uid,int type)
	{
		if(isBindUser(uid)){
			RequestManager.sendError(type, BindManager.gets().getIoSession(uid));
			BindManager.gets().clearSession(BindManager.gets().getIoSession(uid));
		}
	}
	//ends
}
