package com.jerabi.ssdp.sender;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.network.IUDPSender;
import com.jerabi.ssdp.network.SSDPNetworkFactory;
import com.jerabi.ssdp.util.SSDPContants;

/**
 * This class in a {@link SSDPPeriodicMessageSender} that send DiscoverMessage and handle responses for the messages sent. 
 * 
 * The sender is a {@link IUDPSender} and it sends UDP instead of Multicast UDP. The responses will be handle by a {@link ISSDPResponseHandler}, if the handler is null, 
 * the responses will be ignored.
 *  
 * When you change the port, the {@link IUDPSender} will be set to null and recreated when you send a message.  
 * 
 * @author Sebastien Dionne
 * @example.
 * <pre>
 * ...
 * // you need a ISSDPControler to process the responses.
 * SSDPDiscoverSender discoverSender = new SSDPDiscoverSender(controler, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT);
 * discoverSender.setSSDPResponseHandler(new SSDPDiscoverResponseHandler(controler));
 * Thread t = new Thread(discoverSender);
 * t.start();
 * </pre>
 * @see DiscoverMessage
 * @see IUDPSender
 */
public class SSDPDiscoverSender extends SSDPPeriodicMessageSender {
	private Logger logger = Logger.getLogger(SSDPDiscoverSender.class.getName());
	
	private IUDPSender udpSender = null;
	private ISSDPResponseHandler handler = null;
	private int TTL = 3; 	// MX 
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * and creates a IUDPSender using ssdpPort
	 * 
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 */
	public SSDPDiscoverSender(ISSDPControler controler, String ssdpHost, int ssdpPort) {
		super(controler, ssdpHost, ssdpPort);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * and creates a IUDPSender using ssdpPort
	 * 
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPDiscoverSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay) {
		super(controler, ssdpHost, ssdpPort, delay);
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * and creates a IUDPSender using ssdpPort
	 * 
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 */
	public SSDPDiscoverSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * and creates a IUDPSender using ssdpPort
	 * 
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPDiscoverSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList, int delay) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList, delay);
	}
	
	/**
	 * Return a ISSDPHandler that will handle the discover responses.
	 * @return handler
	 */
	public ISSDPResponseHandler getSSDPResponseHandler() {
		return handler;
	}
	
	/**
	 * Sets a ISSDPHandler that will handle the discover responses.
	 * @param handler
	 */
	public void setSSDPResponseHandler(ISSDPResponseHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * {@inheritDoc}
	 * Sets IUDPSender to null. If will be recreate when a message is sent. 
	 */
	@Override
	public void setSSDPPort(int port) {
	    super.setSSDPPort(port);
	    udpSender=null;
	}
	
	/**
	 * {@inheritDoc}
	 * Creates a {@link DiscoverMessage} and add it twice to the list.
	 * It's strongly suggested to send DiscoverMessage twice. 
	 */
	@Override
	public List<ISSDPMessage> getSSDPMessagesToSend() {
		
		List<ISSDPMessage> list = new ArrayList<ISSDPMessage>();
		DiscoverMessage discoverMessage = new DiscoverMessage(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, TTL, "upnp:rootdevice", null);
		
		list.add(discoverMessage);
		list.add(discoverMessage);
		
		return list;
	}
	
	/**
	 * Sends the messages using {@link IUDPSender} and wait for a response until a timeout (Time-to-live).
	 * When a response is received, a {@link ISSDPResponseHandler} will handle it.  If the port changed since the
	 * last time you sent a message, a new IUDPSender will be created.
	 */
	@Override 
	public void sendMessage(String message) throws Exception {
		
		if(message==null){
			return;
		}
		
		logger.info("sending M-SEARCH \n" + message);
		
		if(udpSender==null){
			udpSender = SSDPNetworkFactory.getInstance().createUDPSender();
		}
		
		final String msg = message;
		threadPool.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				udpSender.sendMessage(msg, InetAddress.getByName(host), port, handler, TTL);
				return Boolean.TRUE;
			}
		});
		
	}

}
