package com.jerabi.ssdp.sender;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;

/**
 * This class is a Sender that sends periodically {@link AliveMessage} and doesn't handle responses.  If 
 * a response is required, you will have to override {@link #sendMessage(String)}.
 * 
 * This class is a implementation of {@link SSDPDefaultPeriodicMessageSender}.
 * 
 * Once this sender is stopped it can't be restarted.  You need to create a new instance.
 * 
 * This sender will send the message and enter in sleep mode for the delay used and return to started mode until 
 * the sender is stopped by the controller.
 * 
 * 
 * @author Sebastien Dionne
 * @example.
 * <pre>
 * 
 *  // a simple example that will send 5 times the same message each time.
 * ...
 * sender = new AliveMessageSender(controler, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY, "max-age=1800", "Demo Server", 5);
 * 		
 * 	...
 * </pre>
 * 
 *
 */
public class AliveMessageSender extends SSDPDefaultPeriodicMessageSender {

	protected String cacheControl = "";
	protected String serverName = "";
	protected int multipleTimeToSend = 1;
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, String cacheControl, String serverName) {
		super(controler, ssdpHost, ssdpPort);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param multipleTimeToSend number time each messages will be sent each time
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, String cacheControl, String serverName, int multipleTimeToSend) {
		super(controler, ssdpHost, ssdpPort);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
		this.multipleTimeToSend = multipleTimeToSend; 
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay, String cacheControl, String serverName) {
		super(controler, ssdpHost, ssdpPort, delay);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param multipleTimeToSend number time each messages will be sent each time
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay, String cacheControl, String serverName, int multipleTimeToSend) {
		super(controler, ssdpHost, ssdpPort, delay);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
		this.multipleTimeToSend = multipleTimeToSend; 
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, String cacheControl, String serverName, List<NetworkInterface> networkInterfaceList) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param multipleTimeToSend number time each messages will be sent each time
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, String cacheControl, String serverName, List<NetworkInterface> networkInterfaceList, int multipleTimeToSend) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
		this.multipleTimeToSend = multipleTimeToSend; 
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay, String cacheControl, String serverName, List<NetworkInterface> networkInterfaceList) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList, delay);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 * @param cacheControl cache-control value
	 * @param serverName the name or description of the server
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param multipleTimeToSend number time each messages will be sent each time
	 */
	public AliveMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay, String cacheControl, String serverName, List<NetworkInterface> networkInterfaceList, int multipleTimeToSend) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList, delay);
		this.cacheControl = cacheControl;
		this.serverName = serverName;
		this.multipleTimeToSend = multipleTimeToSend; 
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISSDPMessage> getSSDPMessagesToSend() {
		List<ISSDPMessage> list = new ArrayList<ISSDPMessage>();
		
		for (ServiceInfo deviceInfo : controler.getServiceInfoList()) {
		
			AliveMessage message = SSDPMessageHelper.createSSDPAliveMessage(deviceInfo);
			
			message.setCacheControl(cacheControl);
			message.setServer(serverName);
			
			for(int i=0;i<multipleTimeToSend;i++){
				list.add(message);
			}
		}
		
		return list;
	}
	
	

}
