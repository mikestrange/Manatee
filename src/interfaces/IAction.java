package interfaces;

import events.Evented;

public interface IAction {
	void execute(Evented event);
}
