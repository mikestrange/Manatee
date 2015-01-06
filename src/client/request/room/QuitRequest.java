package client.request.room;

import net.utils.FtpWrite;
import interfaces.ISocketRequest;

public class QuitRequest implements ISocketRequest {
	public int uid;
	public QuitRequest(int uid)
	{
		this.uid = uid;
	}
	
	@Override
	public void enwrap(FtpWrite bytes) {
		bytes.writeInt(uid);
	}

}
