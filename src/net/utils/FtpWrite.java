package net.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import log.Log;

public class FtpWrite 
{
	private ByteBuffer buff;
	private int limt = 0;
	private int capacity = 0;
	
	public FtpWrite()
	{
		this(ByteStream.COOKIE);
	}
	
	public FtpWrite(int size)
	{
		buff = ByteBuffer.allocate(size);
		capacity = size;
	}
	
	public void writeByte(byte value){
		limt++;
		buff.put(value);
	}

	public void writeShort(short value)
	{
		limt+=2;
		buff.putShort(value);
	}
	
	public void writeInt(int value)
	{
		limt+=4;
		buff.putInt(value);
	}
	
	public void writeLong(long value){
		limt+=8;
		buff.putLong(value);
	}
	
	public void writeBytes(byte[] bytes)
	{
		limt+=bytes.length;
		buff.put(bytes);
	}
	
	public void writeBoolean(boolean value)
	{
		writeByte(value ? ByteStream.TRUE : ByteStream.FALSE);
	}
	
	//默认机器格式
	public void writeString(String str)
	{
		if(str==null){
			writeShort(ByteStream.SHORT_NONE);
			return;
		}
		byte[] str_bytes = str.getBytes();
		short len = (short)(str_bytes.length);
		writeShort(len);
		if(len == ByteStream.SHORT_NONE) return;
		writeBytes(str_bytes);
	}
	
	//自定义
	public void writeString(String str,String charset)
	{
		if(str==null){
			writeShort(ByteStream.SHORT_NONE);
			return;
		}
		byte[] str_bytes = null;
		try {
			str_bytes = str.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			Log.log(this).warn("写入字符出错:" + e);
			writeShort(ByteStream.SHORT_NONE);
			return;
		}
		short len = (short)(str_bytes.length);
		writeShort(len);
		writeBytes(str_bytes);
	}
	
	public int limts()
	{
		return limt;
	}
	
	public byte[] array()
	{
		return buff.array();
	}
	
	public int position()
	{
		return buff.position();
	}
	
	public void position(int value)
	{
		buff.position(value);
	}
	
	public void clear()
	{
		limt = 0;
		buff.clear();
	}
	
	public int capacity()
	{
		return this.capacity;
	}
	//ends
}
