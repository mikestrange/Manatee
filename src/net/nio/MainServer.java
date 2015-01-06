package net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import log.Log;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;


public class MainServer {     
	
	//不需要知道是做什么的
	public static CharsetEncoder encoder = Charset.forName("GB2312").newEncoder();
	protected CharsetDecoder decoder;
	
    private static MainServer mainServer = null;     		
    public static final int BIND_PORT = 9555;				//连接端口
    public static final int MAX_LINE = 5;					//最大连接
    public static final int MAX_READ = 2048;				//读字节
    public static final int MAX_WRITE = 1024;				//写字节
    public static final int SPARE_TIME = 5;				//空闲时间,秒
    
    public static MainServer getInstances() {     
        if (null == mainServer) {     
            mainServer = new MainServer();     
        }     
        return mainServer;     
    }     
    
    //服务器
    private SocketAcceptor acceptor; 
    
    private MainServer() {
    	//843端口
    	try { 
    		start();    
        } catch (IOException e) {     
            e.printStackTrace();     
        }
    	
    }     
    
    public void start() throws IOException
    {
    	Log.log(this).debug("<-启动游戏服务器->"+BIND_PORT);
    	//构造最大连接
		acceptor = new NioSocketAcceptor(MAX_LINE);  
		//解码器
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new FairyCodecFactory()));
		//设置读取缓冲区的大小
		acceptor.getSessionConfig().setReadBufferSize(MAX_READ);
		//设置写入缓冲区的大小
		acceptor.getSessionConfig().setSendBufferSize(MAX_WRITE);
		//读写通道10秒内无操作进入空闲状态
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, SPARE_TIME);
		//逻辑处理
		acceptor.setHandler(ServerHandler.getInstances());     
		//绑定端口    
		acceptor.bind(new InetSocketAddress(BIND_PORT));
    }
    
    //注册模块
    public void register()
    {
    	CommandConfig.gets().load();
    }
    
    //ends
}    