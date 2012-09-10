package com.jerabi.ssdp.sender;

import java.net.NetworkInterface;
import java.util.List;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.message.ISSDPMessage;

/**
 * This class is a Sender that sends periodically {@link ISSDPMessage} and doesn't handle responses.  If 
 * a response is required, you will have to override {@link #sendMessage(String)}.
 * 
 * This class is a default implementation of {@link SSDPPeriodicMessageSender} with a dummy method {@link #getSSDPMessagesToSend()} that will allow you to custom
 * which messages to send. You can instantiate a new SSDPDefaultPeriodicMessageSender, unlike SSDPPeriodicMessageSender that is an abstract class.
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
 * sender = new SSDPDefaultPeriodicMessageSender(controler, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY) {
 * 			&#64;Override
 * 			public List&lt;ISSDPMessage> getSSDPMessagesToSend() {
 * 				List&lt;ISSDPMessage> list = new ArrayList&lt;ISSDPMessage>();
 * 				
 * 				// add 5 times the same message to the list
 * 				for(int i=0;i&lt;5;i++){
 * 					list.add(new ISSDPMessage(){
 * 						public String toString() {
 * 							return "messagetosend";
 * 						}});
 * 				}
 * 				
 * 				return list;
 * 			}
 * 		};
 * 		
 * 	...
 * </pre>
 * 
 *
 */
public class SSDPDefaultPeriodicMessageSender extends SSDPPeriodicMessageSender {

	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 */
	public SSDPDefaultPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort) {
		super(controler, ssdpHost, ssdpPort);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPDefaultPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay) {
		super(controler, ssdpHost, ssdpPort, delay);
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 */
	public SSDPDefaultPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPDefaultPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList, int delay) {
		super(controler, ssdpHost, ssdpPort, networkInterfaceList, delay);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISSDPMessage> getSSDPMessagesToSend() {
		return null;
	}

}
