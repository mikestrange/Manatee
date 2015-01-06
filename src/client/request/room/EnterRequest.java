package client.request.room;

import net.core.CarryData;
import net.utils.FtpWrite;
import interfaces.ISocketRequest;

public class EnterRequest implements ISocketRequest 
{
	public int mapId;
	public int uid;
	public int type;
	public String usn;
	public int level;
	public int point;
	public int x;
	public int y;
	
	public EnterRequest(CarryData data)
	{
		mapId = data.mapId;
		uid = data.uid;
		usn = data.usn;
		type = data.type;
		level = data.level;
		point = data.point;
		x = data.x;
		y = data.y;
	}
	@Override
	public void enwrap(FtpWrite bytes) {
		bytes.writeShort((short) mapId);
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
