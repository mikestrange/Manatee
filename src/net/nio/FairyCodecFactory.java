package net.nio;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

//工程
public class FairyCodecFactory implements ProtocolCodecFactory
{	
	private FairyEncoder encoder;	//编码
	private FairyDecoder decoder;	//解码
	
	public FairyCodecFactory()
	{
		encoder = new FairyEncoder();
		decoder = new FairyDecoder();
	}
	
	//@Override
	public ProtocolDecoder getDecoder(IoSession session) throws Exception 
	{
		return decoder;
	}

	//@Override
	public ProtocolEncoder getEncoder(IoSession session) throws Exception 
	{
		return encoder;
	}

    //ends
}
