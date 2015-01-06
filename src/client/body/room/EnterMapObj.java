package client.body.room;

import net.utils.FtpRead;
import interfaces.ISocketResult;

public class EnterMapObj implements ISocketResult 
{
	//32767
	public int mapId; 		//short
	//默认方向byte
	public int point = 0;
	//short
	//人物位置
	public int user_x;			
	public int user_y;		
	//这里是屏幕一半,如果玩家在最左或者最右，那么这个数值就不一样了
	public int leftx;
	public int lefty;
	public int width;		
	public int height;
	
	public void resolve(FtpRead bytes) 
	{
		mapId = bytes.readUShort();
		point = bytes.readByte();
		user_x = bytes.readUShort();
		user_y = bytes.readUShort();
		leftx = bytes.readUShort();
		lefty = bytes.readUShort();
		width = bytes.readShort();
		height = bytes.readShort();
		//
	}

	
	
	//ends
}
