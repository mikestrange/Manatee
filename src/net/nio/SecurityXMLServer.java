package net.nio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import log.Log;

/*
 * 推送本地策略文件
 * */
public class SecurityXMLServer{

	private static SecurityXMLServer _ins;
	
	public static SecurityXMLServer gets()
	{
		if(null==_ins){
			_ins = new SecurityXMLServer();
		}
		return _ins;
	}
	
	private final String xmlChar = "<cross-domain-policy><allow-access-from domain='*' to-ports='*'/></cross-domain-policy>\0";
	private final int POLICY_PORT = 843;
	private boolean isClose = false;
	//启动服务器
	public void start() throws IOException
	{
		Log.log(this).debug("->策略文件是："+xmlChar);
		start(new ServerSocket(POLICY_PORT));
		Log.log(this).debug("服务监听端口：" + POLICY_PORT);
	}
	  
	private void start(ServerSocket server)
	{
		BufferedReader reader = null;
		BufferedWriter writer = null;
		Socket client = null;
		final int NONE = 0;
		while(true){
			if(isClose){
				close(server);
				break;
			}
			try{
				client = server.accept();
				InputStreamReader input = new InputStreamReader(client.getInputStream(), "UTF-8");
				reader = new BufferedReader(input);
				reader.toString();
				OutputStreamWriter output = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
				writer = new BufferedWriter(output);
				String info = "<policy-file-request/>";
				//接收到客户端的请求之后，将策略文件发送出去
				if(info.indexOf("<policy-file-request/>") >= NONE)
				{
					writer.write(xmlChar);
					writer.flush();
					Log.log(this).debug("->将安全策略文件发送至: " + client);
				}else{
					writer.flush();
					Log.log(this).debug("请求无法识别: "+client);
				}
			}catch(Exception e){
				Log.log(this).error("获取策略文件失败:",e);
			}finally{
				try {
					client.close();
				} catch (IOException e) {
					Log.log(this).error("关闭失败:",e); 
				}
			}
		}
	}
	
	private void close(ServerSocket server)
	{
		try {
			server.close();
		} catch (IOException e) {
			Log.log(this).error("关闭843失败:",e); 
		}
	}
	
	public void close()
	{
		isClose = true;
	}
	
	//ends
}