package com.jerabi.ssdp.handler;

import java.util.logging.Logger;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;

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
 * generated from the raw message received by this ISSDPHandler.  Once a ISSDPMessage is generated, it's passed to the {@link com.jerabi.ssdp.ISSDPControler}.
 * 
 * @author Sebastien Dionne
 * @see ISSDPMessageHandler 
 */
public class SSDPDefaultResponseHandler implements ISSDPResponseHandler {
	private Logger logger = Logger.getLogger(SSDPDefaultResponseHandler.class.getName());
	
	protected ISSDPControler controler = null;
	
	/**
	 * Constructor
	 * @param controler that will process {@link ISSDPMessage}
	 */
	public SSDPDefaultResponseHandler(ISSDPControler controler) {
		this.controler = controler;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ISSDPControler getSSDPControler() {
		return controler;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setSSDPControler(ISSDPControler controler) {
		this.controler = controler;
	}

	/**
	 * {@inheritDoc}
	 * The message received will be converted to ISSDPMessage 
	 * {@link SSDPMessageHelper#getSSDPMessage(String)}. 
	 * 
	 * The ISSDPMessage will be process by the ISSDPControler
	 */
	public void handle(String message) throws Exception {
		
		logger.info("\n" + message);
		
		ISSDPMessage ssdpMessage = SSDPMessageHelper.getSSDPMessage(message);
		
		if(ssdpMessage!=null && controler!=null){
			controler.processSSDPMessage(ssdpMessage);
		} 
		
	}

	/**
	 * {@inheritDoc}
	 * The message received will be converted to ISSDPMessage 
	 * {@link SSDPMessageHelper#getSSDPMessage(String)}. 
	 * 
	 * The ISSDPMessage will be process by the ISSDPControler
	 */
	@Override
	public void handle(String remoteAddr, int remotePort, String message) throws Exception {
		logger.info("\n" + message);
		
		ISSDPMessage ssdpMessage = SSDPMessageHelper.getSSDPMessage(message);
		
		if(ssdpMessage!=null && controler!=null){
			controler.processSSDPMessage(remoteAddr, remotePort, ssdpMessage);
		} 
		
	}

}
