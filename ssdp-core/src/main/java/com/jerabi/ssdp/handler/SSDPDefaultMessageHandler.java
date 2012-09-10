package com.jerabi.ssdp.handler;

import java.util.logging.Logger;

import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.UpdateMessage;

/**
 * 
 * Provides a dummy implementation for processing a {@link com.jerabi.ssdp.message.ISSDPMessage} received.
 * 
 * You need to implements the methods that you need.  A reference implementation is UPNP API that used SSDP for
 * notification and discover devices/services.
 * 
 * @example.
 * <pre>
 * // You could implements ISSDPMessageHandler or override SSDPDefaultMessageHandler 
 * 
 * ISSDPMessageHandler listener = new SSDPDefaultMessageHandler(){
 * 	&#64;Override
 *  public void processSSDPAliveMessage(AliveMessage ssdpMessage)
 *			 throws Exception {
 *		// log message here
 *	}
 * };
 * </pre>
 * @author Sebastien Dionne
 */
public class SSDPDefaultMessageHandler implements ISSDPMessageHandler {
	private static Logger logger = Logger.getLogger(SSDPDefaultMessageHandler.class.getName());
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSSDPAliveMessage(AliveMessage ssdpMessage) throws Exception {
		logger.warning("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSSDPByeByeMessage(ByeByeMessage ssdpMessage) throws Exception {
		logger.warning("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSSDPDiscoverMessage(String remoteAddr, int remotePort, DiscoverMessage ssdpMessage) throws Exception {
		logger.warning("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSSDPUpdateMessage(UpdateMessage ssdpMessage) throws Exception {
		logger.warning("not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processSSDPDiscoverResponseMessage(DiscoverResponseMessage ssdpMessage) throws Exception {
		logger.warning("not implemented");
	}

}
