package client.action.room;

import java.awt.Rectangle;

import net.core.BindManager;
import net.core.CarryData;
import log.Log;
import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import client.body.room.EnterMapObj;
import client.request.room.EnterRequest;
import events.Evented;
import interfaces.IAction;

public class EnterMapAction implements IAction {

	public void execute(Evented event) 
	{
		CarryData data = BindManager.gets().getData(event.session);
		if(data==null) return;
		EnterMapObj result = (EnterMapObj) event.result;
		data.mapId = result.mapId;
		data.point = result.point;
		data.x = result.user_x;
		data.y = result.user_y;
		data.rect =new Rectangle(result.leftx,result.lefty,result.width,result.height);
		//
		event.sendClient(new EnterRequest(data));
		Log.log(this).debug("进入游戏房间");
		//
		ConsoleManager.gets().sendConsole(ConsoleType.ROOM, data.mapId, event.action, data);
		/*
		RoomConsole console = (RoomConsole) ConsoleManager.gets().createConsole(ConsoleType.ROOM, data.mapId);
		if(null!=console) console.enter(data);
		*/
	}
	//ends
}
