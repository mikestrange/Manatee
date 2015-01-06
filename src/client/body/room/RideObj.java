package client.body.room;

import net.utils.FtpRead;
import interfaces.ISocketResult;

/*
 * 乘坐 
 **/
public class RideObj implements ISocketResult {
	public int uid;
	public boolean state;
	
	public void resolve(FtpRead bytes) {
		uid = bytes.readInt();
		state = bytes.readBoolean();
	}
	//ends
}
