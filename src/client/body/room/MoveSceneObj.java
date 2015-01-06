package client.body.room;

import net.utils.FtpRead;
import interfaces.ISocketResult;

public class MoveSceneObj implements ISocketResult 
{
	public int point;
	public int user_x;
	public int user_y;		
	public int end_x;
	public int end_y;
	public int leftx;
	public int lefty;
	public int width;
	public int height;
	
	public void resolve(FtpRead bytes) 
	{
		point = bytes.readByte();
		user_x = bytes.readUShort();
		user_y = bytes.readUShort();
		leftx = bytes.readUShort();
		lefty = bytes.readUShort();
		width = bytes.readShort();
		height = bytes.readShort();
	}
	
	//ends
}
