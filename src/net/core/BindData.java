package net.core;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import consts.CmdDefinedName;

import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import net.utils.TcpResolve;

public class BindData {
	//通信
	public IoSession session = null;
	//解包
	public TcpResolve resolve = null;
	//客户端唯一识别标记
	public int client_uid;
	//是否登录
	public boolean isBind = false;
	//是否有效，
	public boolean isValid = false;
	//心跳
	public boolean isBeat = true;
	//用户数据缓存
	public CarryData data = null;
	
	public BindData(IoSession session)
	{
		this.session = session;
		this.resolve = new TcpResolve();
	}
	
	//绑定
	public boolean bind(int uid)
	{
		if(isBind) return false;
		client_uid = uid;
		isBind = true;
		isValid = true;
		data = new CarryData(session);
		data.uid = uid;
		return true;
	}
	
	//更改绑定 绑定的用户才能更改
	public void change(int uid)
	{
		if(isBind){
			client_uid = uid;
			if(null!=data) data.uid = uid;
		}
	}
	
	//读取
	public void read(IoBuffer in,ProtocolDecoderOutput output)
	{
		byte[] bytes = new byte[in.remaining()];
		in.get(bytes);
		resolve.resolve(bytes);
		while(resolve.hasNext()) output.write(resolve.getNext());
	}
	
	//心跳
	public void beat()
	{
		if(isBeat){
			isBeat = false;
			RequestManager.sendHeart(session);
		}else{
			BindManager.gets().clearSession(session);
		}
	}
	
	//心跳通畅
	public void unobstructed()
	{
		isBeat = true;
	}
	
	//释放
	public void free()
	{
		if(isBind){
			CarryData.share(data);	//保存用户数据
			ConsoleManager.gets().sendConsole(ConsoleType.ROOM, data.mapId, CmdDefinedName.QUIT_MAP, data);
			data = null;
		}
		isBind = false;
		isBeat = false;
		isValid = false;
		resolve.clear();
	}
	
	//ends
}
