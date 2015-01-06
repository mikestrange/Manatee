package mvc.console.room;

import interfaces.ISocketRequest;

import java.util.ArrayList;
import java.util.List;

import net.core.BindManager;
import net.core.CarryData;
import net.core.RequestManager;
import net.utils.ByteStream;

import org.apache.mina.core.session.IoSession;

import consts.CmdDefinedName;

import client.request.room.*;

import log.Log;
import mvc.core.IConsole;

public class RoomConsole implements IConsole 
{
	private List<Integer> uidList;
	private int order;
	
	public int order() {return order;}

	public String getName() {return "map";}

	public void launch(int order) {
		this.order = order;
		uidList = new ArrayList<Integer>();
	}

	public void free() {
		
	}

	//执行责任
	public void dutyProcess(int action, Object data) 
	{
		switch(action)
		{
			case CmdDefinedName.ENTER_MAP:
				enter((CarryData) data);
				break;
			case CmdDefinedName.QUIT_MAP:
				quit((CarryData) data);
				break;
			case CmdDefinedName.MOVE_TO:
				move((CarryData) data);
				break;
			case CmdDefinedName.STAND_HERE:
				stop((CarryData) data);
				break;
		}
	}
	
	//进入房间
	private void enter(CarryData data)
	{
		int have = aboutRequest(data,CmdDefinedName.ENTER_MAP,new EnterRequest(data));
		Log.log(this).debug("#进入地图房间:uid=",data.uid);
		if(have==ByteStream.NO_HAVE) uidList.add(data.uid);
	}
	
	//退出房间的时候，看是否为空
	private boolean quit(CarryData data)
	{
		data.mapId = ByteStream.NONE;
		int index = aboutRequest(data,CmdDefinedName.QUIT_MAP,new QuitRequest(data.uid));
		Log.log(this).debug("#退出房间:uid=",data.uid);
		if(index!=ByteStream.NO_HAVE) uidList.remove(index);
		return uidList.isEmpty();
	}
	
	//移动
	private void move(CarryData data)
	{
		aboutRequest(data,CmdDefinedName.MOVE_TO,new MoveRequest(data));
	}
	
	//停止和移动一样命令
	private void stop(CarryData data)
	{
		aboutRequest(data,CmdDefinedName.STAND_HERE,new MoveRequest(data));
	}
	
	//通知周围
	protected int aboutRequest(CarryData data,int cmd,ISocketRequest request)
	{
		CarryData other;
		int other_uid;
		int have = ByteStream.NO_HAVE;
		for(int i = uidList.size()-1 ; i>=0 ; i--){
			other_uid = uidList.get(i);
			if(other_uid==data.uid){
				have = i;
				continue;
			}
			other = BindManager.gets().getData(other_uid);
			//通知这些玩家
			Log.log(this).debug(data.uid,"->通知玩家:",other_uid);
			if(data.rect.contains(other.x, other.y)) sendClient(cmd,request,other.session);
		}
		return have;
	}
	
	public void sendClient(int cmd,ISocketRequest request,IoSession socket)
	{
		RequestManager.getRequest(cmd, request).sendRequest(socket);
	}
	//ends
}
