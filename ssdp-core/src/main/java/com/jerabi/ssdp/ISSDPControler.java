package com.jerabi.ssdp;

import java.util.List;

import com.jerabi.ssdp.handler.ISSDPMessageHandler;
import com.jerabi.ssdp.listener.SSDPMulticastListener;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.sender.SSDPDiscoverSender;
import com.jerabi.ssdp.sender.SSDPPeriodicMessageSender;

/**
 * Provides API to send and handle SSDPMessage.
 * 
 * This is the main Interface for a controler.  Can be used to implements
 * a SSDP client and SSDP server.
 * 
 * @author Sebastien Dionne
 *
 */
public interface ISSDPControler {

	/**
	 * Adds a listener into the ISSDPMessageHandler List
	 * @param handler Will handle the messages received 
	 * @throws Exception
	 */
	void addMessageHandler(ISSDPMessageHandler handler) throws Exception;

	/**
	 * Removes a listener from the ISSDPMessageHandler List
	 * @param handler Will handle the messages received
	 * @throws Exception
	 */
	void removeMessageHandler(ISSDPMessageHandler handler) throws Exception;

	/**
	 * Returns the list of ISSDPMessageHandler that will handle the messages received.
	 * @return ISSDPMessageHandler list
	 * @throws Exception
	 */
	List<ISSDPMessageHandler> getMessageHandlerList() throws Exception;

	/**
	 * Process the ISSDPMessage received.
	 * 
	 * @param message ISSDPMessage received from the network
	 * @throws Exception
	 */
	void processSSDPMessage(ISSDPMessage message) throws Exception;

	/**
	 * Process the ISSDPMessage received.  This method have informations about the sender (address and port) to send a
	 * response if required.
	 * 
	 * @param remoteAddr sender IP address
	 * @param remotePort sender port
	 * @param message ISSDPMessage received from the network
	 * @throws Exception
	 */
	void processSSDPMessage(String remoteAddr, int remotePort, ISSDPMessage message) throws Exception;

	/**
	 * Starts the controler and all the senders and listeners that are enabled.
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * Stops the controler and all the senders and listeners.  
	 * Once it's stopped, it can't not restarted.
	 * @throws Exception
	 */
	void stop() throws Exception;

	/**
	 * Returns a list of ServiceInfo
	 * @return ServiceInfo list
	 */
	List<ServiceInfo> getServiceInfoList();

	/**
	 * Sets the list of ServiceInfo
	 * @param serviceList
	 */
	void setServiceInfoList(List<ServiceInfo> serviceList);

	/**
	 * Adds a ServiceInfo into the ServiceInfoList
	 * @param serviceInfo new ServiceInfo to add
	 */
	void addServiceInfo(ServiceInfo serviceInfo);

	/**
	 * Removes a ServiceInfo from the ServiceInfoList
	 * @param serviceInfo new ServiceInfo to remove
	 * @return if the operation is successful 
	 */
	boolean removeServiceInfo(ServiceInfo serviceInfo);

	/**
	 * Returns true if enabled
	 * @return enabled or disabled
	 */
	boolean getPeriodicSenderEnabled();

	/**
	 * Enabled or Disabled the PeriodicSender
	 * @param enabled or disabled
	 */
	void setPeriodicSenderEnabled(boolean enabled);

	/**
	 * Returns the {@link SSDPPeriodicMessageSender}
	 * @return SSDPPeriodicMessageSender
	 */
	SSDPPeriodicMessageSender getPeriodicMessageSender();

	/**
	 * Sets a {@link SSDPPeriodicMessageSender}
	 * 
	 * @param periodicSender Sender that will send {@link com.jerabi.ssdp.message.AliveMessage}
	 */
	void setPeriodicMessageSender(SSDPPeriodicMessageSender periodicSender);

	/**
	 * Returns true if enabled
	 * @return enabled or disabled
	 */
	boolean getDiscoverSenderEnabled();

	/**
	 * Enabled or Disabled the DiscoverSender
	 * @param enabled or disabled
	 */
	void setDiscoverSenderEnabled(boolean enabled);

	/**
	 * Returns the {@link SSDPDiscoverSender}
	 * @return SSDPDiscoverSender
	 */
	SSDPDiscoverSender getDiscoverSender();

	/**
	 * Sets a {@link SSDPDiscoverSender}
	 * 
	 * @param discoverSender Sender that will send {@link com.jerabi.ssdp.message.DiscoverMessage} (M-SEARCH)
	 */
	void setDiscoverSender(SSDPDiscoverSender discoverSender);

	/**
	 * Returns true if enabled
	 * @return enabled or disabled
	 */
	boolean getMulticastListenerEnabled();

	/**
	 * Enabled or Disabled the MulticastListener
	 * @param enabled or disabled
	 */
	void setMulticastListenerEnabled(boolean enabled);

	/**
	 * Returns the {@link SSDPMulticastListener}
	 * @return SSDPMulticastListener
	 */
	SSDPMulticastListener getMulticastListener();

	/**
	 * Sets a {@link SSDPDiscoverSender}
	 * 
	 * @param listener that will listen and handle multicast messages received 
	 */
	void setMulticastListener(SSDPMulticastListener listener);
}
