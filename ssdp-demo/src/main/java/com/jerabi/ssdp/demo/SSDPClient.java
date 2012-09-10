package com.jerabi.ssdp.demo;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jerabi.ssdp.SSDPControler;
import com.jerabi.ssdp.handler.SSDPDefaultMessageHandler;
import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.handler.SSDPDiscoverResponseHandler;

public class SSDPClient {

	private static final Logger s_logger = Logger.getLogger(SSDPClient.class.getName());
	
	public SSDPControler controler = null;
	
	public void start() throws Exception {
		
		// will dispatch the responses from the handlers to the listener
		controler = new SSDPControler();
		
		// pas besoin pour le client
		controler.setPeriodicSenderEnabled(false);
		
		// on cherche les serveurs
		controler.setDiscoverSenderEnabled(true);
		
		// le client peut-etre interesse par les alive des serveurs et byebye
		controler.setMulticastListenerEnabled(false);
		
		// will handle the responses
		controler.addMessageHandler(new SSDPDefaultMessageHandler());
		
		// this will handle the messages received by the multicastListener 
		controler.getMulticastListener().setSSDPResponseHandler(new SSDPDefaultResponseHandler(controler));
		
		// this will handle discover response
		controler.getDiscoverSender().setSSDPResponseHandler(new SSDPDiscoverResponseHandler(controler));
		
		
		controler.getDiscoverSender().setDelay(5000);
		
		s_logger.fine("starting controler");
		
    	controler.start();
	}
	
	public void stop() throws Exception {
		controler.stop();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		SSDPClient client = new SSDPClient();
		
		client.start();
	}
}
