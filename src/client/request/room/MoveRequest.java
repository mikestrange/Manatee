package client.request.room;

import net.core.CarryData;
import net.utils.FtpWrite;
import interfaces.ISocketRequest;
/*
 * stop也是一样
 * */
public class MoveRequest implements ISocketRequest {
	public int uid;
	public int type;
	public String usn;
	public int level;
	public int point;
	public int x;
	public int y;
	
	public MoveRequest(CarryData data)
	{
		uid = data.uid;
		type = data.type;
		level = data.level;
		usn = data.usn;
		point = data.point;
		x = data.x;
		y = data.y;
	}
	
	public MoveRequest(int uid,CarryData data)
	{
		this.uid = uid;
		type = data.type;
		level = data.level;
		usn = data.usn;
		point = data.point;
		x = (int) ((data.x-200)+Math.random()*450);
		y = (int) ((data.y-200)+Math.random()*400);
	}
	
	@Override
	public void enwrap(FtpWrite bytes) {
		bytes.writeInt(uid);
		bytes.writeShort((short) type);
		bytes.writeString(usn);
		bytes.writeShort((short) level);
		bytes.writeByte((byte) point);
		bytes.writeShort((short) x);
		bytes.writeShort((short) y);
	}
	//ends
}
