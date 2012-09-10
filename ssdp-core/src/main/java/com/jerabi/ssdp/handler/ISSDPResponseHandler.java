package com.jerabi.ssdp.handler;

import com.jerabi.ssdp.ISSDPControler;

/**
 * Provides a API to handle raw messages received.
 * 
 * Normally the handle methods call {@link com.jerabi.ssdp.ISSDPControler} to process
 * the message and it call be used to filter the messages received.
 * 
 * {@link com.jerabi.ssdp.handler.SSDPDiscoverResponseHandler} is a good
 * example of a ISSDPHandler that doesn't process DiscoverMessage (M-SEARCH)
 * 
 * This class look like {@link ISSDPMessageHandler} but ISSDPMessageHandler actually process a {@link com.jerabi.ssdp.message.ISSDPMessage} 
 * generated from the raw message received by this ISSDPHandler.
 * 
 * @author Sebastien Dionne
 * @see ISSDPMessageHandler 
 */
public interface ISSDPResponseHandler {
	
	/**
	 * Sets the ISSDPControler
	 * @param controler
	 */
	void setSSDPControler(ISSDPControler controler);
	
	/**
	 * Returns the ISSDPControler
	 * @return ISSDPControler
	 */
	ISSDPControler getSSDPControler();
	
	/**
	 * Handle the raw message received.  If the message has to be process it should be sent to
	 * the ISSDPControler.  
	 * 
	 * @param message raw message received from the network
	 * @throws Exception
	 */
	void handle(String message) throws Exception;
	
	/**
	 * Handle the raw message received.  If the message has to be process it should be sent to
	 * the ISSDPControler.  This method have informations about the sender (address and port) to send a
	 * response if required.
	 * 
	 * @param remoteAddr sender IP address
	 * @param remotePort sender port
	 * @param message raw message received from the network
	 * @throws Exception
	 */
	void handle(String remoteAddr, int remotePort, String message) throws Exception;
}
