package com.jerabi.ssdp.handler;

import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.UpdateMessage;

/**
 * Provides API to process {@link com.jerabi.ssdp.message.ISSDPMessage} received.
 * 
 * @author Sebastien Dionne
 * @see com.jerabi.ssdp.message.AliveMessage
 * @see com.jerabi.ssdp.message.ByeByeMessage
 * @see com.jerabi.ssdp.message.DiscoverMessage
 * @see com.jerabi.ssdp.message.DiscoverResponseMessage
 * @see com.jerabi.ssdp.message.UpdateMessage
 */
public interface ISSDPMessageHandler {
	
	/**
	 * Process AliveMessage
	 * 
	 * @param ssdpMessage message to process
	 * @throws Exception
	 */
	void processSSDPAliveMessage(AliveMessage ssdpMessage) throws Exception;
	
	/**
	 * Process UpdateMessage
	 * 
	 * @param ssdpMessage message to process
	 * @throws Exception
	 */
	void processSSDPUpdateMessage(UpdateMessage ssdpMessage) throws Exception;
	
	/**
	 * Process ByeByeMessage
	 * 
	 * @param ssdpMessage message to process
	 * @throws Exception
	 */
	void processSSDPByeByeMessage(ByeByeMessage ssdpMessage) throws Exception;
	
	/**
	 * Process DiscoverMessage (M-SEARCH)
	 * 
	 * @param remoteAddr sender IP address
	 * @param remotePort sender port
	 * @param ssdpMessage message to process
	 * @throws Exception
	 */
	void processSSDPDiscoverMessage(String remoteAddr, int remotePort, DiscoverMessage ssdpMessage) throws Exception;
	
	/**
	 * Process DiscoverResponseMessage
	 * 
	 * @param ssdpMessage message to process
	 * @throws Exception
	 */
	void processSSDPDiscoverResponseMessage(DiscoverResponseMessage ssdpMessage) throws Exception;
	
}
