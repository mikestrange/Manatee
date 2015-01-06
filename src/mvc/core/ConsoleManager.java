package mvc.core;

import java.util.concurrent.ConcurrentHashMap;

import mvc.console.room.RoomConsole;

/*
 * 所有控制台的注册和销毁
 * */
public class ConsoleManager 
{
	//静态
	private static ConsoleManager _ins = null;
	private static boolean _iscreate = false;
			
	public static ConsoleManager gets()
	{
		if(!_iscreate){
			_ins = new ConsoleManager();
			_ins.consoleHash = new ConcurrentHashMap<Integer,IConsole>();
			_iscreate = true;
		}
		return _ins;
	}	
	
	//所有控制台
	private ConcurrentHashMap<Integer,IConsole> consoleHash = null;
	
	//创建一个控制台
	public IConsole createConsole(int type,int order)
	{
		if(consoleHash.containsKey(order)) return consoleHash.get(order);
		IConsole console = create(type);
		if(console!=null){
			console.launch(order);	//启动
			consoleHash.put(order, console);
			return console;
		}
		return null;
	}
	
	//取控制台
	protected IConsole getConsole(int order)
	{
		return consoleHash.get(order);
	}
	
	public boolean hasConsole(int order)
	{
		return consoleHash.containsKey(order);
	}
	
	//释放一个控制台
	public IConsole free(int order)
	{
		IConsole console = consoleHash.remove(order);
		if(null!=console) console.free();
		return console;
	}
	
	//给模块发信息
	public void sendConsole(int type,int order,int action,Object data)
	{
		IConsole console = createConsole(type,order);
		if(null == console) return;
		console.dutyProcess(action,data);
	}
	
	private IConsole create(int type)
	{
		switch(type)
		{
			case ConsoleType.ROOM:		return new RoomConsole();
		}
		return null;
	}
	
	//ends
}
