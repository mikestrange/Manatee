package client.request;

import net.utils.FtpWrite;
import interfaces.ISocketRequest;

public class LogicRequest implements ISocketRequest 
{
	public static final int LENG = 16;
	public static final String DYNAMIC_CHAT = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()~_+{}[]<>,.";
	
	//获取16位的动态密码
	public static String getDynamic()
	{
		final int len = DYNAMIC_CHAT.length();
		char[] chat = new char[LENG];
		for(int i = 0;i<LENG;i++){
			int ran = (int) Math.floor(Math.random()*len);
			chat[i] = DYNAMIC_CHAT.charAt(ran);
		}
		return String.valueOf(chat);
	}
	
	//
	public int uid;
	public String usn;
	public String dynamic_password;
	public boolean logged;
	public int error;
	/*
	 * 登陆返回成功失败
	 * */
	public LogicRequest(int error)
	{
		this.dynamic_password = getDynamic();
		this.logged = false;
		this.error = error;
	}
	
	public LogicRequest(String name)
	{
		this.usn = name;
		this.dynamic_password = getDynamic();
		this.logged = true;
	}
	
	//
	public void enwrap(FtpWrite bytes) {
		bytes.writeBoolean(logged);
		if(!logged){
			bytes.writeShort((short) error);
		}else{
			bytes.writeString(usn);
			bytes.writeString(dynamic_password);
		}
	}
	//ends
}
