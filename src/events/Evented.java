package events;

import interfaces.IController;
import interfaces.ISocketResult;
import interfaces.ISocketRequest;
import net.core.RequestManager;
import net.core.ResultManager;
import net.utils.FtpRead;

import org.apache.mina.core.session.IoSession;

public class Evented
{
	public int action;
	public IController target;
	public IoSession session;
	public ISocketResult result;
	//
	private FtpRead data;
	
	public Evented(IController target,int cmd,IoSession session,FtpRead data)
	{
		this.target = target;
		this.action = cmd;
		this.session = session;
		this.data = data;
		this.result = ResultManager.create(cmd,data);
	}
	
	public int action()
	{
		return this.action;
	}
	
	//true表示模块事件
	public boolean isValid()
	{
		return null!=target;
	}
	
	public boolean isResult()
	{
		return null!=result;
	}
	
	public FtpRead getBytes()
	{
		return data;
	}
	
	//牺牲一点，直接回调通知
	public void sendClient(ISocketRequest request)
	{
		if(request==null) return;
		RequestManager.getRequest(action, request).sendRequest(session);
	}
	
	//ends
}
