package net.core;

import org.apache.mina.core.session.IoSession;

public interface IRequest {
	boolean sendRequest(IoSession session);
}
