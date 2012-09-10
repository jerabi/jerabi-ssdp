package com.jerabi.ssdp.handler;

import java.util.logging.Logger;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;

/**
 * Provides a API to handle raw messages received but don't process DiscoverMessage (M-SEARCH).
 * 
 * Normally the handle methods call {@link com.jerabi.ssdp.ISSDPControler} to process
 * the message and it call be used to filter the messages received.
 * 
 * It should be used when you sending DiscoverMessage and don't want to handle the DiscoverMessage that you just sent.
 * 
 * If you want to receive theses DiscoverMessage or change the filters to exclude messages, you could use {@link SSDPDefaultResponseHandler} instead. 
 * 
 * This class look like {@link ISSDPMessageHandler} but ISSDPMessageHandler actually process a {@link com.jerabi.ssdp.message.ISSDPMessage} 
 * generated from the raw message received by this ISSDPHandler.  Once a ISSDPMessage is generated, it's passed to the {@link com.jerabi.ssdp.ISSDPControler}.
 * 
 * @author Sebastien Dionne
 * @see SSDPDefaultResponseHandler 
 */
public class SSDPDiscoverResponseHandler extends SSDPDefaultResponseHandler {
	private Logger logger = Logger.getLogger(SSDPDiscoverResponseHandler.class.getName());
	
	/**
	 * Constructor
	 * @param controler ISSDPControler that will process the {@link ISSDPMessage}.
	 */
	public SSDPDiscoverResponseHandler(ISSDPControler controler) {
		super(controler);
	}
	
	/**
	 * {@inheritDoc}
	 * Exclude DiscoverMessage
	 */
	@Override
	public void handle(String message) throws Exception {
		
		// we shouldn't received M_SEARCH.. but just in case, we skip it.
		if(message!=null && !message.startsWith("M-SEARCH")){
			logger.info("\n" + message);
			
			ISSDPMessage ssdpMessage = SSDPMessageHelper.getSSDPMessage(message);
			
			if(ssdpMessage!=null && controler!=null){
				controler.processSSDPMessage(ssdpMessage);
			} 
			
		}
		
	}
}
