package net.core;

import java.awt.Rectangle;
import java.sql.ResultSet;
import java.sql.SQLException;

import log.Log;

import net.utils.ByteStream;

import org.apache.mina.core.session.IoSession;

import sql.MySqlOperate;
import sql.SqlServer;

public class CarryData {
	public IoSession session;
	public int uid;
	//
	public String usn;
	public int type;
	public int sex;
	public int level;
	public int exp;
	//
	public int mapId;
	public int point;
	public int x;
	public int y;
	//
	public long money;
	public int shoe;
	//
	public Rectangle rect;
	
	public CarryData(IoSession session)
	{
		this.session = session;
		this.rect = new Rectangle(ByteStream.NONE,ByteStream.NONE,ByteStream.WIN_WIDTH,ByteStream.WIN_HEIGHT);
	}
	
	//退出的时候保存数据
	public static boolean share(CarryData data)
	{
		String chat="";
		chat += "mapid = "+data.mapId;
		chat += ", x = "+data.x;
		chat += ", y = "+data.y;
		SqlServer.gets().update(MySqlOperate.shareInfo(data.uid, chat));
		return true;
	}
	
	//去用户基本数据
	public static boolean fetch(CarryData data)
	{
		ResultSet result = SqlServer.gets().query(MySqlOperate.getInfo(data.uid));
		if(null==result) return false;
		try {
			while(result.next()){
				data.usn = result.getString("usn");
				data.type = result.getInt("type");
				data.sex = result.getInt("sex");
				data.mapId = result.getInt("mapid");
				data.x = result.getInt("x");
				data.y = result.getInt("y");
				data.money = result.getLong("money");
				data.shoe = result.getInt("shoe");
				data.level = result.getInt("level");
				data.exp = result.getInt("exp");
			}
		} catch (SQLException e) {
			Log.log().debug("取数据错误:uid=",data.uid," ->Error:",e);
			return false;
		}
		return true;
	}
	//ends
}
