package net.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import log.Log;

public class FtpRead 
{
	private ByteBuffer buff;
	
	public FtpRead(byte[] bytes)
	{
		buff = ByteBuffer.wrap(bytes);
	}
	
	public FtpRead(byte[] bytes,int offset,int length)
	{
		buff = ByteBuffer.wrap(bytes,offset,length);
	}
	
	public boolean readBoolean()
	{
		return readByte()==ByteStream.TRUE;
	}
	
	//read
	public byte readByte()
	{
		return buff.get();
	}
	
	public int readUByte()
	{
		return readByte() & 0xff;
	}
	
	public short readShort()
	{
		return buff.getShort();
	}
	
	public int readUShort()
	{
		return readShort() & 0xffff;
	}
	
	public long readLong()
	{
		return buff.getLong();
	}
	
	public int readInt()
	{
		return buff.getInt();
	}
	
	public long readUint()
	{
		return readInt() & 0xffffffff;
	}
	
	public String readString()
	{
		short len = buff.getShort();
		if(len == ByteStream.SHORT_NONE) return ByteStream.EMPTY;
		byte[] _bytes = new byte[len];
		buff.get(_bytes, ByteStream.NONE, len);
		return new String(_bytes);
	}
	
	public String readString(String charset)
	{
		short len = buff.getShort();
		if(len == ByteStream.SHORT_NONE) return ByteStream.EMPTY;
		byte[] _bytes = new byte[len];
		buff.get(_bytes, ByteStream.NONE, len);
		try {
			return new String(_bytes,charset);
		} catch (UnsupportedEncodingException e) {
			Log.log(this).warn("###解析字段出错");
		}
		return new String(_bytes);
	}
	
	public void mark()
	{
		buff.mark();
	}
	
	public void reset()
	{
		buff.reset();
	}
	
	public void clear()
	{
		buff.clear();
	}
	//ends
}
