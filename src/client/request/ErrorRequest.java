package client.request;

import net.utils.FtpWrite;
import interfaces.ISocketRequest;

public class ErrorRequest implements ISocketRequest {

	/*错误包*/
	private int type;
	
	public ErrorRequest(int errorType)
	{
		type = errorType;
	}
	
	public void enwrap(FtpWrite bytes) {
		bytes.writeInt(type);
	}
	
	
	//
}
