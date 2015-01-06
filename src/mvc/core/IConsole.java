package mvc.core;

/*
 * 控制台 
 **/
public interface IConsole 
{
	int order();										//识别码
	String getName();									//控制台名称
	void launch(int order);								//启动
	void free();										//释放
	void dutyProcess(int action,Object data);	//事件处理
}
