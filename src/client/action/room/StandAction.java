package client.action.room;

import mvc.core.ConsoleManager;
import mvc.core.ConsoleType;
import net.core.BindManager;
import net.core.CarryData;
import events.Evented;
import interfaces.IAction;

public class StandAction implements IAction {

	@Override
	public void execute(Evented event) 
	{
		CarryData data = BindManager.gets().getData(event.session);
		if(data==null) return;
		ConsoleManager.gets().sendConsole(ConsoleType.ROOM, data.mapId, event.action, data);
	}

}
