package com.jerabi.ssdp;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.jerabi.ssdp.handler.ISSDPMessageHandler;
import com.jerabi.ssdp.handler.SSDPDiscoverResponseHandler;
import com.jerabi.ssdp.listener.SSDPMulticastListener;
import com.jerabi.ssdp.message.AbstractSSDPNotifyMessage;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.UpdateMessage;
import com.jerabi.ssdp.sender.SSDPDefaultPeriodicMessageSender;
import com.jerabi.ssdp.sender.SSDPDiscoverSender;
import com.jerabi.ssdp.sender.SSDPPeriodicMessageSender;
import com.jerabi.ssdp.util.SSDPContants;

/**
 * This is the Main class.  Provides an implementation of SSDP (Simple Service Discovery Protocol).
 * 
 * <pre>
 * SSDP provides a mechanism which network clients can use to discover network services. Clients can use SSDP with little or no static configuration.
 * 
 * SSDP uses UDP unicast and multicast packets to advertise available services. The multicast address used is 239.255.255.250 in IPv4.
 * SSDP uses port 1900.
 * 
 * There are 3 major parts in the protocol.
 * 
 *    - Discover device/service   
 *    - Advertise device/service
 *    - Handle notification and response messages
 * 
 * To Discover device/service a {@link SSDPDiscoverSender} is used.  This Sender sends {@link DiscoverMessage} (M-SEARCH).
 * When a device received a DiscoverMessage and the device offers the service requested, the device will send a response
 * {@link DiscoverResponseMessage} to the client.
 * 
 * To Advertise device/service a {@link SSDPPeriodicMessageSender} is used.  This Sender sends {@link AliveMessage}. 
 * It's possible to send other notification message like {@link UpdateMessage} and  {@link ByeByeMessage}.  
 * 
 * To receive theses messages a {@link SSDPMulticastListener} is required.  This Listener listen and handle messages received. 
 * Theses messages will be process by this {@link ISSDPControler}.  
 * 
 * The messages received will be forwarded to all {@link ISSDPMessageHandler} subscribe to this ISSDPControler.  All
 * the ISSDPMessageHandler will receive the messages, because it's possible that the client that send the message close 
 * the connection the SSDPControler should consider that possibility.  To avoid too much trouble, one ISSDPMessageHandler
 * should process the messages and send a response if required, and the others handler could be for logging.  
 * 
 * Note that when the controler is stopped, it can't not be restarted. 
 * 
 * A SSDP client and SSDP server mostly differs in which Sender and Listener enabled.
 * By default all the Sender and Listener are Enabled.
 * 
 * </pre>
 * 
 * @author Sebastien Dionne
 *
 * @example.
 * <pre>
 * A SSDP Server most of the time doesn't need to discover device, so the
 * DiscoverSender will be disabled.
 * 
 * <b>here a sample for SSDP Server</b>
 * 
 * SSDPControler controler = new SSDPControler();
 *
 * // a device doesn't need to look for others devices
 * controler.setDiscoverSenderEnabled(false);
 *
 * // will handle the responses
 * controler.addMessageHandler(new SSDPDefaultMessageHandler());
 *
 * // this will handle the messages received by the MulticastListener
 * controler.getMulticastListener().setSSDPHandler(new SSDPDefaultHandler(controler));
 *
 * // this will handle discover response sent by clients
 * controler.getDiscoverSender().setSSDPHandler(new SSDPDiscoverResponseHandler(controler));
 *
 * // send notification messages
 * controler.setPeriodicMessageSender(new SSDPPeriodicMessageSender(controler, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT) {
 *     &#64;Override
 *     public List&lt;ISSDPMessage> getSSDPMessagesToSend() {
 *         List&lt;ISSDPMessage> list = new ArrayList&lt;ISSDPMessage>();
 *
 *         for (ServiceInfo deviceInfo : controler.getServiceInfoList()) {
 *
 *             AliveMessage message = SSDPMessageHelper.createSSDPAliveMessage(deviceInfo);
 *
 *             message.setCacheControl("max-age=1800");
 *             message.setServer("Sebastien Dionne SSDP Demo");
 *
 *             list.add(message);
 *         }
 *
 *         return list;
 *     }
 *});
 *
 * // sends messages each 30 seconds.
 * controler.getPeriodicMessageSender().setDelay(30000);
 *
 * // add device and service to be advertised
 * controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, 
 *                                                    SSDPContants.DEFAULT_PORT, 
 *                                                    "upnp:rootdevice", 
 *                                                    "http://127.0.0.1:9000/config", 
 *                                                    new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416", "upnp:rootdevice")));
 *
 * controler.start();
 *
 * <b>here a sample for SSDP Client</b>
 *  
 * SSDPControler controler = new SSDPControler();
 *
 * // doesn't need to send notification messages
 * controler.setPeriodicSenderEnabled(false);
 *
 * // will handle the responses
 * controler.addMessageHandler(new SSDPDefaultMessageHandler());
 *
 * // this will handle the messages received by the MulticastListener 
 * controler.getMulticastListener().setSSDPHandler(new SSDPDefaultHandler(controler));
 *
 * // send DiscoverMessages each 60 seconds.
 * controler.getDiscoverSender().setDelay(60000);
 *
 * controler.start();
 *   
 * </pre>
 */
public class SSDPControler implements ISSDPControler {

	private static final Logger logger = Logger.getLogger(SSDPControler.class.getName());

	protected String ssdpHost;
	protected int ssdpPort;

	protected List<ISSDPMessageHandler> messageHandlerList = null;

	protected SSDPDiscoverSender discoverSender = null;
	protected SSDPPeriodicMessageSender periodicSender = null;
	protected SSDPMulticastListener multicastListener = null;

	protected boolean periodicSenderEnabled = true;
	protected boolean discoverSenderEnabled = true;
	protected boolean multicastListenerEnabled = true;

	/**
	 * Default Thread Pool (called ExecutorService).If not set, and instance
	 * of the DefaultThreadPool will be created.
	 */
	protected ExecutorService threadPool;

	protected List<ServiceInfo> serviceInfoList = null;

	/**
	 * Constructor that used the default settings
	 * @throws Exception 
	 */
	public SSDPControler() throws Exception {
		this(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT);
	}
	
	/**
	 * Constructor that used the default settings
	 * @param networkInterfaceList NetworkInterfaces that will be used 
	 * @throws Exception 
	 */
	public SSDPControler(List<NetworkInterface> networkInterfaceList) throws Exception {
		this(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, networkInterfaceList);
	}

	/**
	 * Constructor with custom IP and Port 
	 * @param ssdpHost multicast IP used
	 * @param ssdpPort multicast port used
	 * @throws Exception 
	 */
	public SSDPControler(String ssdpHost, int ssdpPort) throws Exception {
		this(ssdpHost, ssdpPort, Executors.newFixedThreadPool(5));
	}
	
	/**
	 * Constructor with custom IP and Port 
	 * @param ssdpHost multicast IP used
	 * @param ssdpPort multicast port used
	 * @param networkInterfaceList NetworkInterfaces that will be used 
	 * @throws Exception 
	 */
	public SSDPControler(String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList) throws Exception {
		this(ssdpHost, ssdpPort, networkInterfaceList, Executors.newFixedThreadPool(5));
	}
	
	/**
	 * Constructor with custom IP and Port and ExecutorService
	 * @param ssdpHost multicast IP used
	 * @param ssdpPort multicast port used
	 * @param threadPool ExecutorService that will handle Threading
	 * @throws Exception 
	 */
	public SSDPControler(String ssdpHost, int ssdpPort, ExecutorService threadPool) throws Exception {
		this(ssdpHost, ssdpPort, null, Executors.newFixedThreadPool(5));
	}
	
	/**
	 * Constructor with custom IP and Port and ExecutorService
	 * @param ssdpHost multicast IP used
	 * @param ssdpPort multicast port used
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 * @param threadPool ExecutorService that will handle Threading
	 * @throws Exception 
	 */
	public SSDPControler(String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList, ExecutorService threadPool) throws Exception {

		this.ssdpHost = ssdpHost;
		this.ssdpPort = ssdpPort;
		this.threadPool = threadPool;

		messageHandlerList = new ArrayList<ISSDPMessageHandler>();

		serviceInfoList = new ArrayList<ServiceInfo>();

		discoverSender = new SSDPDiscoverSender(this, ssdpHost, ssdpPort, networkInterfaceList);
		discoverSender.setSSDPResponseHandler(new SSDPDiscoverResponseHandler(this));

		periodicSender = new SSDPDefaultPeriodicMessageSender(this, ssdpHost, ssdpPort, networkInterfaceList);

		multicastListener = new SSDPMulticastListener(ssdpHost, ssdpPort, networkInterfaceList);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMessageHandler(ISSDPMessageHandler handler) {
		if (handler != null) {
			messageHandlerList.add(handler);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ISSDPMessageHandler> getMessageHandlerList() {
		return messageHandlerList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMessageHandler(ISSDPMessageHandler handler) {
		if (handler != null) {
			messageHandlerList.remove(handler);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getPeriodicSenderEnabled() {
		return periodicSenderEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPeriodicSenderEnabled(boolean enabled) {
		this.periodicSenderEnabled = enabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getDiscoverSenderEnabled() {
		return discoverSenderEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDiscoverSenderEnabled(boolean enabled) {
		this.discoverSenderEnabled = enabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean getMulticastListenerEnabled() {
		return multicastListenerEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMulticastListenerEnabled(boolean multicastListenerEnabled) {
		this.multicastListenerEnabled = multicastListenerEnabled;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSDPDiscoverSender getDiscoverSender() {
		return discoverSender;
	}

	@Override
	public void setDiscoverSender(SSDPDiscoverSender discoverSender) {
		this.discoverSender = discoverSender;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSDPPeriodicMessageSender getPeriodicMessageSender() {
		return periodicSender;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPeriodicMessageSender(SSDPPeriodicMessageSender periodicSender) {
		this.periodicSender = periodicSender;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SSDPMulticastListener getMulticastListener() {
		return multicastListener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setMulticastListener(SSDPMulticastListener listener) {
		this.multicastListener = listener;
	}

	/**
	 * {@inheritDoc}
	 * Starts DiscoverSender, PeriodicSender and MulticastListener 
	 * that are enabled.
	 */
	@Override
	public void start() throws Exception {

		// runner that send periodic messages
		if (discoverSenderEnabled) {
			threadPool.execute(discoverSender);
		}
		if (periodicSenderEnabled) {
			threadPool.execute(periodicSender);
		}
		if (multicastListenerEnabled) {
			threadPool.execute(multicastListener);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop() throws Exception {
		threadPool.shutdownNow();
	}

	@Override
	public void processSSDPMessage(ISSDPMessage message) throws Exception {
		processSSDPMessage(null, -1, message);
	}

	@Override
	public void processSSDPMessage(String remoteAddr, int remotePort, ISSDPMessage message) throws Exception {

		if (message instanceof DiscoverMessage) {
			for (ISSDPMessageHandler listener : messageHandlerList) {
				listener.processSSDPDiscoverMessage(remoteAddr, remotePort, (DiscoverMessage) message);
			}
		} else if (message instanceof AbstractSSDPNotifyMessage) {

			AbstractSSDPNotifyMessage notifyMessage = (AbstractSSDPNotifyMessage) message;
			if (SSDPContants.NTS_ALIVE.equals(notifyMessage.getNts())) {
				for (ISSDPMessageHandler listener : messageHandlerList) {
					listener.processSSDPAliveMessage((AliveMessage) notifyMessage);
				}
			} else if (SSDPContants.NTS_UPDATE.equals(notifyMessage.getNts())) {
				for (ISSDPMessageHandler listener : messageHandlerList) {
					listener.processSSDPUpdateMessage((UpdateMessage) notifyMessage);
				}
			} else if (SSDPContants.NTS_BYEBYE.equals(notifyMessage.getNts())) {
				for (ISSDPMessageHandler listener : messageHandlerList) {
					listener.processSSDPByeByeMessage((ByeByeMessage) notifyMessage);
				}
			} else {
				logger.info("SSDPNotifyResponse not reconized : " + notifyMessage);
			}
		} else if (message instanceof DiscoverResponseMessage) {
			for (ISSDPMessageHandler listener : messageHandlerList) {
				listener.processSSDPDiscoverResponseMessage((DiscoverResponseMessage) message);
			}
		} else {
			logger.info("ISSDPMessage not reconized : " + message);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addServiceInfo(ServiceInfo serviceInfo) {
		serviceInfoList.add(serviceInfo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeServiceInfo(ServiceInfo serviceInfo) {
		return serviceInfoList.remove(serviceInfo);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ServiceInfo> getServiceInfoList() {
		return serviceInfoList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setServiceInfoList(List<ServiceInfo> serviceList) {
		this.serviceInfoList = serviceList;
	}

}
