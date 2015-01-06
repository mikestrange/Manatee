package client.body.room;

import net.utils.FtpRead;
import interfaces.ISocketResult;

public class StandObj implements ISocketResult 
{
	public int uid;
	public void resolve(FtpRead bytes) 
	{
		uid=bytes.readInt();
	}
	//ends
}
