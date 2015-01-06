package client.action.room;

import client.request.room.QuitRequest;
import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import net.core.BindManager;
import net.core.CarryData;
import interfaces.IAction;
import events.Evented;

public class QuitMapAction implements IAction {
	
	public void execute(Evented event) 
	{
		CarryData data = BindManager.gets().getData(event.session);
		if(data==null) return;
		event.sendClient(new QuitRequest(data.uid));
		ConsoleManager.gets().sendConsole(ConsoleType.ROOM, data.mapId, event.action, data);
	}
	
	//ends
}
