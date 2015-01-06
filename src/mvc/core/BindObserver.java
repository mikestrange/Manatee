package mvc.core;

import events.Evented;
import interfaces.IAction;

public class BindObserver 
{
	public IAction action;
	public Object target;
	
	public BindObserver(Object target,IAction action)
	{
		this.target = target;
		this.action = action;
	}
	
	public boolean match(Object data)
	{
		return data==target;
	}
	
	public void handler(Evented event)
	{
		action.execute(event);
	}
	
	//ends
}
