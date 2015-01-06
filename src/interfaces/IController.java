package interfaces;

import net.utils.FtpRead;

import org.apache.mina.core.session.IoSession;

public interface IController 
{
	//添加指令
	void add(int cmd,IAction action);
	//释放模块，一般不会
	void destroy();
	//处理自身事物
	void execute(int cmd,IoSession session,FtpRead data);
	//直接发送到客户端
	void sendClient(int cmd,ISocketRequest request,IoSession socket);
	//处理客户端事务 action为第一次处理，这里是逻辑处理
	void invoke(String type,IoSession socket,Object client);
	//ends
}
