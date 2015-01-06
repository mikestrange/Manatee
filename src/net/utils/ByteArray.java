package net.utils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import log.Log;


public class ByteArray 
{
	private ByteBuffer buff;
	private int length;
	private int limt = 0;
	
	public ByteArray()
	{
		this(ByteStream.COOKIE);
	}
	
	public ByteArray(int size)
	{
		length = size;
		buff = ByteBuffer.allocate(length);
	}
	
	public ByteArray(ByteBuffer buffer)
	{
		buff = buffer;
		length = buffer.limit();
	}
	
	public ByteArray(byte[] bytes)
	{
		buff = ByteBuffer.wrap(bytes);
		length = bytes.length;
	}
	
	//write
	//0xf
	public void writeByte(byte value){
		limt++;
		buff.put(value);
	}

	
	public void writeLong(long value){
		limt+=8;
		buff.putLong(value);
	}

	//0xffffff
	public void writeInt(int value)
	{
		limt+=4;
		buff.putInt(value);
	}
	
	//0xffff
	public void writeShort(short i)
	{
		limt+=2;
		buff.putShort(i);
	}
	
	public void writeBytes(byte[] bytes)
	{
		limt+=bytes.length;
		buff.put(bytes);
	}
	
	public void writeBoolean(boolean value)
	{
		limt++;
		byte v = 0;
		if(value) v = 1;
		buff.put(v);
	}
	
	public void writeString(String str)
	{
		byte[] str_bytes = str.getBytes();
		short len=(short)(str_bytes.length);
		writeShort(len);
		writeBytes(str_bytes);
	}
	
	public void writeString(String str,String charset)
	{
		try {
			byte[] str_bytes=str.getBytes(charset);
			short len = (short)(str_bytes.length);
			writeShort(len);
			writeBytes(str_bytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public int limts()
	{
		return limt;
	}
	
	//read
	public byte readByte()
	{
		return buff.get();
	}
	
	public long readLong()
	{
		return buff.getLong();
	}
	
	public int readInt()
	{
		return buff.getInt();
	}
	
	public short readShort()
	{
		return buff.getShort();
	}
	
	public String readString()
	{
		short len = buff.getShort();
		byte[] _bytes = new byte[len];
		buff.get(_bytes, 0, len);
		return new String(_bytes);
	}
	
	public String readString(String charset)
	{
		short len=buff.getShort();
		byte[] _bytes=new byte[len];
		buff.get(_bytes, 0, len);
		try {
			return new String(_bytes,charset);
		} catch (UnsupportedEncodingException e) {
			Log.log(this).warn("###解析字段出错");
			System.exit(0);
		}
		return new String(_bytes);
	}
	//write ends
	
	public ByteBuffer byteBuffer()
	{
		return buff;
	}
	
	public ByteBuffer pack()
	{
		int l=length();
		ByteBuffer buffer = ByteBuffer.allocate(l);
		if(position()>0) flip();
		buffer.put(array(), 0, l);
		buffer.flip();
		return buffer;
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
	
	public void flip()
	{
		buff.flip();
	}
	
	public void clear()
	{
		buff.clear();
	}
	
	public int length()
	{
		return length;
	}
	
	public int remaining()
	{
		return buff.remaining();
	}
	
	public boolean hasRemaining()
	{
		return buff.hasRemaining();
	}
	//ends
}
