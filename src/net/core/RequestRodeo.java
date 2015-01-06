package net.core;

import interfaces.ISocketRequest;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;

import log.Log;
import net.utils.ByteStream;
import net.utils.FtpWrite;

/*
 * 一个封装结构体
 * */
public class RequestRodeo extends FtpWrite implements IRequest 
{
	protected int cmd;
	protected short type = 0;
	
	public RequestRodeo(int cmd,int type)
	{
		this.cmd = cmd;
		this.type = (short) type;
	}
	
	public RequestRodeo(int cmd)
	{
		this.cmd = cmd;
	}
	
	//这里作为封装包驶入
	public void organize(ISocketRequest data)
	{
		this.position(ByteStream.LEN);
		super.writeInt(cmd);
		super.writeShort(type);
		//写入具体数据
		if(data!=null) data.enwrap(this);
		//已经写入的长度
		final int leng = this.limts();
		//跳到最开始，写入长度
		this.position(ByteStream.BEGIN);
		this.writeInt(leng);
	}
	
	//发送客户端
	public boolean sendRequest(IoSession session)
	{
		if(null==session){
			Log.log(this).warn("不存在的连接 :"+this);
			return false;
		}
		Log.log(this).debug("->server推送:cmd="+cmd+",type="+type);
		if(session.isClosing()){
			Log.log(this).warn("推送失败，目标已经关闭连接"+this);
			return false;
		}
		if(this.limts()>this.capacity()){
			Log.log(this).warn("推送失败，写入字节大于缓存区域"+this);
			return false;
		}
		session.write(IoBuffer.wrap(array(),ByteStream.BEGIN,this.limts()));
		return true;
	}
	
	//ends
}
