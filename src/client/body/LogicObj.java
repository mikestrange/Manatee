package client.body;

import interfaces.ISocketResult;
import net.utils.FtpRead;

/*
 * Action 封装
 * */
public class LogicObj implements ISocketResult 
{
	public int uid;			//游戏id
	public String openId;		//平台id
	public String password;		//密码
	public int appId;			//应用id
	public int serverId;		//服务器id
	public int remark; 		//第二验证
	
	public void resolve(FtpRead bytes)
	{
		uid = bytes.readInt();
		openId = bytes.readString();
		password = bytes.readString();
		appId = bytes.readInt();
		serverId = bytes.readInt();
		remark = bytes.readInt();
	}
	//ends
}
