package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import net.utils.ByteStream;

import log.Log;


/*
 * 多个数据库，继承他就可以了
 * */
public class SqlServer
{
	private Statement sql = null;
	private Connection conn = null;
	private boolean isLink = false;
	
	public void connect()
	{
		connect("jdbc:mysql://127.0.0.1:3306/mina_game" , "root" , "");
	}
	
	protected void connect(String url,String name,String pass)
	{
		if(isLink) return;
        isLink = false;
		try {  
            //加载数据库驱动，注册到去送管理器  
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url,name,pass);
            if (conn != null){
            	isLink = true;
            	Log.log(this).debug("数据库连接成功!");
            }else{
            	Log.log(this).debug("数据库连接失败!");  
            }
		}catch (ClassNotFoundException e){
			Log.log(this).debug("数据库错误!"+e);
		}catch (SQLException e){ 
			Log.log(this).debug("数据库错误!"+e); 
		} catch (InstantiationException e) {
			Log.log(this).debug("数据库错误!"+e); 
		} catch (IllegalAccessException e) {
			Log.log(this).debug("数据库错误!"+e);
		}finally{
			Log.log(this).debug("------sql end----");
		}
	}
	
	//读数据库
	public ResultSet query(String chat)
	{
		if(!isLink) return null;
		ResultSet res = null;
		try{
			if(null == sql) sql = conn.createStatement();
			res = sql.executeQuery(chat);
			
		} catch (SQLException e) {
			Log.log(this).debug("查询数据库错误! Error:",e," ->error sql:",chat);
		}
		return res;
	}
	
	//0表示失败  刷新，可以插入
	public int update(String chat)
	{
		if(!isLink) return ByteStream.NONE;
		try {
			if(null == sql) sql = conn.createStatement();
			return sql.executeUpdate(chat);
		} catch (SQLException e) {
			Log.log(this).debug("刷新数据库错误! Error:",e," ->error sql:",chat);
		}
		return ByteStream.NONE;
	}
	
	//
	public void close()
	{
		if(isLink){
			try {
				conn.close();
			} catch (SQLException e) {
				Log.log(this).warn("关闭数据库错误 Error"+conn);
			}finally{
				conn = null;
				isLink = false;
			}
		}
	}
	
	//一个全局
	private static SqlServer _ins = null;
	//公开
	public static SqlServer gets()
	{
		if(null==_ins){
			_ins = new SqlServer();
			_ins.connect();
		}
		return _ins;
	}
	
	//ends
}
