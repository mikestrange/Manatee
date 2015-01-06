package net.nio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/*
 * 编码
 * */

public class FairyEncoder implements ProtocolEncoder
{

	//@Override
	public void dispose(IoSession session) throws Exception {
		
	}

	//@Override
	public void encode(IoSession session, Object data, ProtocolEncoderOutput output) throws Exception 
	{
		output.write((IoBuffer) data);
	}

	//ends
}
