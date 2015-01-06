package net.nio;

import net.core.BindManager;
import net.utils.TcpResolve;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/*
 * 解码器
 * */
public class FairyDecoder implements ProtocolDecoder {

	//@Override
	public void decode(IoSession session, IoBuffer in, ProtocolDecoderOutput output) throws Exception 
	{
		if(in.remaining()>TcpResolve.NULL) BindManager.gets().read(session, in, output);
	}
	
	//@Override
	public void dispose(IoSession session) throws Exception 
	{
		
	}

	//@Override
	public void finishDecode(IoSession session, ProtocolDecoderOutput output)throws Exception 
	{
		
	}
	//ends
}
