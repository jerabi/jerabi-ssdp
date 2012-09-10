package com.jerabi.ssdp.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jerabi.ssdp.SSDPControler;
import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.handler.SSDPDefaultMessageHandler;
import com.jerabi.ssdp.handler.SSDPDiscoverResponseHandler;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.USNInfo;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;
import com.jerabi.ssdp.sender.SSDPPeriodicMessageSender;
import com.jerabi.ssdp.util.SSDPContants;

public class SSDPServer {

	private static final Logger s_logger = Logger.getLogger(SSDPServer.class.getName());
	
	public SSDPControler controler = null;
	
	
	public void start() throws Exception {
		// will dispatch the responses from the handlers to the listener
		controler = new SSDPControler();
		
		// pas besoin pour le client
		controler.setPeriodicSenderEnabled(true);
		
		// on cherche les serveurs
		controler.setDiscoverSenderEnabled(false);
		
		// le client peut-etre interesse par les alive des serveurs et byebye
		controler.setMulticastListenerEnabled(true);
		
		// will handle the responses
		controler.addMessageHandler(new SSDPDefaultMessageHandler());
		
		// this will handle the messages received by the multicastListener 
		controler.getMulticastListener().setSSDPResponseHandler(new SSDPDefaultResponseHandler(controler));
		
		// this will handle discover response
		controler.getDiscoverSender().setSSDPResponseHandler(new SSDPDiscoverResponseHandler(controler));
		
		
		controler.setPeriodicMessageSender(new SSDPPeriodicMessageSender(controler, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT) {
			
			@Override
			public List<ISSDPMessage> getSSDPMessagesToSend() {
				List<ISSDPMessage> list = new ArrayList<ISSDPMessage>();
				
				for (ServiceInfo deviceInfo : controler.getServiceInfoList()) {
				
					AliveMessage message = SSDPMessageHelper.createSSDPAliveMessage(deviceInfo);
					
					message.setCacheControl("max-age=1800");
					message.setServer("Sebastien Dionne SSDP Demo");
					
					list.add(message);
				}
				
				return list;
			}
		});
		
		controler.getPeriodicMessageSender().setDelay(30000);

		// add device
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "upnp:rootdevice","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","upnp:rootdevice")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:service:ConnectionManager:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:service:ConnectionManager:1")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:service:ContentDirectory:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:service:ContentDirectory:1")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:device:MediaServer:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:device:MediaServer:1")));

		
		
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
		
		SSDPServer server = new SSDPServer();
    	server.start();
		
	}
}
