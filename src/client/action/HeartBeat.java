package client.action;

import interfaces.IAction;
import events.Evented;
import net.core.BindManager;
/*
 * 心跳
 * */
public class HeartBeat implements IAction {

	public void execute(Evented event)
	{
		BindManager.gets().unobstructed(event.session);
	}
	//ends
}
