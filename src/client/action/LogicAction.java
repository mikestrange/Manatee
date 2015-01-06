package client.action;

import java.sql.ResultSet;
import java.sql.SQLException;

import consts.CmdDefinedName;
import consts.ErrorType;

import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import net.core.BindManager;
import net.core.CarryData;
import net.core.RequestManager;
import net.utils.ByteStream;

import sql.MySqlOperate;
import sql.SqlServer;
import interfaces.IAction;
import log.Log;
import client.body.LogicObj;
import client.request.LogicRequest;
import client.request.room.EnterRequest;
import events.Evented;
/*
 * 登录
 * */
public class LogicAction implements IAction {

	public void execute(Evented event) 
	{
		LogicObj obj = (LogicObj) event.result;
		//踢下之前在线的页面
		BindManager.gets().sendError(obj.uid, ErrorType.NONE);
		//
		Log.log(this).debug("pass=",obj.password,",uid=",obj.uid);
		//查看数据库，成功后设置查询数据的动态密码
		ResultSet result = SqlServer.gets().query(MySqlOperate.getUserInfo(obj.uid));
		int type = -1;	//0表示登陆成功
		if(null!=result){
			try {
				while(result.next()){
					try {
						if(obj.password.equals(result.getString("pass"))){
							Log.log(this).debug("logic true");
							type = ByteStream.NONE;
							break;
						}else{
							type = 2;
							Log.log(this).debug("password is false");
						}
					} catch (SQLException e) {
						Log.log(this).debug("unknown error");
						type = 3;
					}
				}
			} catch (SQLException e) {
				Log.log(this).debug("no next");
				type = 4;
			}
		}else{
			Log.log(this).debug("no user");
			type = 1;
		}
		//成功登陆
		if(type == ByteStream.NONE){
			Log.log(this).debug("logic ok");
			//绑定，由于之前有删除，那么不会重复绑定
			BindManager.gets().bindUser(obj.uid,event.session);
			//注入数据
			CarryData userdata = BindManager.gets().getData(event.session);
			CarryData.fetch(userdata);
			//登陆成功
			LogicRequest request = new LogicRequest(userdata.usn);
			//写入一个动态密码和登陆时间
			SqlServer.gets().update(MySqlOperate.setUserInfo(request.dynamic_password, obj.uid));
			//通知登陆
			event.sendClient(request);
			//通知进入地图
			RequestManager.getRequest(CmdDefinedName.ENTER_MAP, new EnterRequest(userdata)).sendRequest(event.session);
			ConsoleManager.gets().sendConsole(ConsoleType.ROOM, userdata.mapId, CmdDefinedName.ENTER_MAP, userdata);
			//
		}else{
			event.sendClient(new LogicRequest(type));
			//断开连接
			BindManager.gets().clearSession(event.session);
		}
		
	}
	
	//ends
}
