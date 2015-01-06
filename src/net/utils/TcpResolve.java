package net.utils;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


import log.Log;

public class TcpResolve {
	public static final int NULL = 0;
	public static final int INT = 4;
	//保存所有
	private ByteBuffer buffer = null;
	//封包列表
	protected List<FtpRead> packets;
	
	public TcpResolve()
	{
		packets = new ArrayList<FtpRead>();
	}
	
	//分解包体
	public void resolve(byte[] bytes) 
	{
		if(bytes.length==NULL) return;
		try{
			if(!this.hasRemaining()) bufferClear();
			if(null==buffer){
				buffer = ByteBuffer.wrap(bytes);
			}else{
				//粘包
				byte[] before = this.cutNew();
				buffer = ByteBuffer.wrap(getBytes(before,bytes));
			}
		}catch(BufferOverflowException e){
			Log.log(this).debug("解析出错 Error:404"+e);
		}
		next();
	}
	
	private void next()
	{
		if(this.remaining()>=INT){
			buffer.mark();
			int leng = buffer.getInt();
			if(this.remaining()>= leng){
				if(cut(leng)) next();
			}else{
				buffer.reset();
			}
		}
	}
	
	//打包
	private boolean cut(int leng)
	{
		byte[] bytes = new byte[leng];
		buffer.get(bytes,NULL,leng);
		packets.add(new FtpRead(bytes));
		return newly();
	}
		
	//重新刷
	private boolean newly()
	{
		if(hasRemaining())
		{
			buffer = ByteBuffer.wrap(cutNew());
			return true;
		}
		bufferClear();
		return false;
	}
	
	private byte[] cutNew()
	{
		int leng = buffer.remaining();
		byte[] info = new byte[leng];
		buffer.get(info, NULL, leng);
		return info;
	}
	
	public FtpRead getNext()
	{
		return packets.remove(NULL);
	}
	
	public boolean hasNext()
	{
		return packets.size() > NULL;
	}
	
	public int size()
	{
		return packets.size();
	}
	
	public int remaining()
	{
		if(null==buffer) return NULL;
		return buffer.remaining();
	}
	
	public boolean hasRemaining()
	{
		return remaining()>NULL;
	}
	
	public void bufferClear()
	{
		if(null != buffer){
			buffer.clear();
			buffer = null;
		}
	}
	
	public void clear()
	{
		bufferClear();
		while(hasNext()) getNext().clear();
	}
	
	//static
	//合并
	public static byte[] getBytes(byte[] a1,byte[] a2)
	{
		byte[] all = new byte[a1.length + a2.length];
		System.arraycopy(a1, NULL, all, NULL, a1.length);
		System.arraycopy(a2, NULL, all, a1.length, a2.length);
		return all;
	}
	
	//ends
}
