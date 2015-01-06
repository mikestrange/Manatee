package interfaces;

import net.utils.FtpWrite;

/*
 * 要发送的数据,只关心发送的数据，不关心包头
 * */
public interface ISocketRequest 
{
	void enwrap(FtpWrite bytes);
}
