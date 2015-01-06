package sql;

import utils.TimeUtil;

public class MySqlOperate 
{
	//用户登陆信息
	public static String getUserInfo(int uid)
	{
		return "SELECT * FROM user_info WHERE uid='"+uid+"'";
	}
	
	//取用户角色信息
	public static String getInfo(int uid)
	{
		return "SELECT * FROM player WHERE uid='"+uid+"'";
	}
	
	//保存用户数据
	public static String shareInfo(int uid,String chat)
	{
		return "UPDATE player SET "+chat+" WHERE uid='"+uid+"'";
	}
	
	//设置动态密码和登陆
	public static String setUserInfo(String pass,int uid)
	{
		return "UPDATE user_info SET dyn_pass = '"+pass+"', l_date = '"+TimeUtil.getDefTime()+"' WHERE uid='"+uid+"'";
	}
	
	
	
	
	
	
	
	
	//ends
}
