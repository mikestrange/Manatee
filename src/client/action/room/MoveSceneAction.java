package client.action.room;

import java.awt.Rectangle;

import net.core.BindManager;
import net.core.CarryData;
import client.body.room.MoveSceneObj;
import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import interfaces.IAction;
import events.Evented;

public class MoveSceneAction implements IAction {
	
	public void execute(Evented event) 
	{
		CarryData data = BindManager.gets().getData(event.session);
		if(data==null) return;
		MoveSceneObj result = (MoveSceneObj) event.result;
		data.point = result.point;
		data.x = result.user_x;
		data.y = result.user_y;
		data.rect =new Rectangle(result.leftx,result.lefty,result.width,result.height);
		//
		ConsoleManager.gets().sendConsole(ConsoleType.ROOM, data.mapId, event.action, data);
	}
	//ends
}
