package com.jerabi.ssdp.sender;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.network.SSDPNetworkFactory;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;
import com.jerabi.ssdp.util.StateHolder;

/**
 * This class is a Sender that sends periodically {@link ISSDPMessage} and doesn't handle responses.  If 
 * a response is required, you will have to override {@link #sendMessage(String)}.
 * 
 * You need to provide an implementation of {@link #getSSDPMessagesToSend()} that will allow you to custom
 * which messages to send. 
 * 
 * Once this sender is stopped it can't be restarted.  You need to create a new instance.
 * 
 * This sender will send the message and enter in sleep mode for the delay used and return to started mode until 
 * the sender is stopped by the controller.
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
 * 				for(int i=0;i<5;i++){
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
public abstract class SSDPPeriodicMessageSender implements Runnable {

	private Logger logger = Logger.getLogger(SSDPPeriodicMessageSender.class.getName());
	
	protected StateHolder<State> stateHolder = new StateHolder<State>();
	
	protected int delay = SSDPContants.DEFAULT_DELAY;
	protected String host = null;;
	protected int port;
	protected ISSDPControler controler = null;
	protected InetSocketAddress socketAddress = null;
	protected List<NetworkInterface> networkInterfaceList = null;
	
	/**
	 * Default Thread Pool (called ExecutorService) to send messages.
	 */
	protected ExecutorService threadPool;
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort){
		this(controler, ssdpHost, ssdpPort, SSDPContants.DEFAULT_DELAY);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay){
		this(controler, ssdpHost, ssdpPort, delay, Executors.newFixedThreadPool(5));
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param delay delay before the next batch of messages are sent
	 * @param threadPool ExecutorService that will send messages
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, int delay, ExecutorService threadPool){
		this(controler, ssdpHost, ssdpPort, null, delay, threadPool);
	}
	
	/**
	 * Constructor that will use the default delay see com.jerabi.ssdp.util.SSDPContants.DEFAULT_DELAY value
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList){
		this(controler, ssdpHost, ssdpPort, SSDPContants.DEFAULT_DELAY);
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param delay delay before the next batch of messages are sent
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList, int delay){
		this(controler, ssdpHost, ssdpPort, delay, Executors.newFixedThreadPool(5));
	}
	
	/**
	 * Constructor with a custom default delay in ms.
	 * @param controler ISSDPControler
	 * @param ssdpHost multicast IP
	 * @param ssdpPort multicast port
	 * @param networkInterfaceList NetworkInterfaces that will be used
	 * @param delay delay before the next batch of messages are sent
	 * @param threadPool ExecutorService that will send messages
	 */
	public SSDPPeriodicMessageSender(ISSDPControler controler, String ssdpHost, int ssdpPort, List<NetworkInterface> networkInterfaceList, int delay, ExecutorService threadPool){
		this.controler = controler;
		this.host = ssdpHost;
		this.port = ssdpPort;
		this.networkInterfaceList = networkInterfaceList;
		this.delay = delay;
		
		socketAddress = new InetSocketAddress(host, port);
		
		setState(State.STOPPED);
		
		this.threadPool = threadPool;
	}
	
	/**
	 * Returns the current state of this listener
	 * @return the current state
	 */
	public State getState() {
		return stateHolder.getState();
	}
	
	/**
	 * Sets a new state for this listener
	 * @param state new State
	 */
	public void setState(State state) {
		this.stateHolder.setState(state);
	}
	
	/**
	 * Returns the delay of the sleep mode
	 * @return the delay in ms.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Sets the delay for the sleep mode
	 * @param delay the delay in ms.
	 */
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	/**
	 * Returns the Multicast Address on which this listener listen
	 * @return the IP.
	 */
	public String getSSDPHost() {
		return host;
	}
	
	/**
	 * Sets the Multicast Address on which this listener will listen
	 * @param host the IP
	 */
	public void setSSDPHost(String host) {
		this.host = host;
	}
	
	/**
	 * Returns the Multicast Port on which this listener listen
	 * @return the port.
	 */
	public int getSSDPPort() {
		return port;
	}
	
	/**
	 * Sets the Multicast Port on which this listener will listen
	 * @param port the port
	 */
	public void setSSDPPort(int port) {
		this.port = port;
	}

	/**
	 * Sends the multicast message.  This method doesn't wait for a response.  
	 * If you want to wait for a response, you have to override this method.
	 * 
	 * @param message message to send
	 * @throws Exception
	 */
	public void sendMessage(String message) throws Exception {
		
		logger.info("sendMessage");
		
		if(message==null){
			return;
		}
		
		// TODO devrait avoir une boucle pour lire les messages
		// entrant.. testcase : 3 serveurs qui écouteraient et répondrait en meme temps
		// mettre un runnable car on se fou du resultat ici
		
		final String msg = message;
		threadPool.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				if(networkInterfaceList!=null && !networkInterfaceList.isEmpty()){
					for (NetworkInterface ni : networkInterfaceList) {
						SSDPNetworkFactory.getInstance().sendMulticastMessage(msg, socketAddress, ni);
					}
				} else {
					SSDPNetworkFactory.getInstance().sendMulticastMessage(msg, InetAddress.getByName(host), port);
				}
				return Boolean.TRUE;
			}
		});
		
	}
	
	/**
	 * Send messages in a loop until the {@link State} change to STOPPED.
	 * After the messages are send, the State change to SLEEP for a period (delay).
	 * After the delay, it returns to STARTED and send new messages. 
	 */
	@Override
	public void run() {
		
		stateHolder.setState(State.STARTED);
		
		while (!Thread.interrupted() && stateHolder.getState() != State.STOPPED) {

			if (stateHolder.getState() != State.SLEEP) {
				logger.info("Running normally");
				try {
					
					List<ISSDPMessage> list = getSSDPMessagesToSend();
					
					if(list!=null){
						for (ISSDPMessage message : list) {
							sendMessage(message.toString());
						}
					}
					// enter sleep mode
					enterSleepMode();
				} catch(InterruptedException ie){
					stateHolder.setState(State.STOPPED);
				} catch(Exception e){
					logger.log(Level.WARNING, "Exception while sending messages", e);
					stateHolder.setState(State.SLEEP);
				}
			} else {
				logger.info("in Pause mode.. ");
				// exit sleep mode
				exitSleepMode();
			}
		}
	}
	
	/**
	 * Changes State to SLEEP and sleeps for some time (equal to delay) in ms.
	 * @throws InterruptedException
	 */
	protected void enterSleepMode() throws InterruptedException {
		stateHolder.setState(State.SLEEP);
		
		CountDownLatch latch = new CountDownLatch(1);
 
		latch.await(delay, TimeUnit.MILLISECONDS);
		
	}
	
	/**
	 * Wait for some time (delay) and change the status to STARTED
	 */
	protected void exitSleepMode(){
		CountDownLatch latch = new CountDownLatch(1);
 
		try {
			latch.await(delay, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
		} 
		
		stateHolder.setState(State.STARTED);
	}
	
	/**
	 * Returns the ISSDPMessage List to send by this Sender
	 * @return a list of ISSDPMessage ready to send
	 */
	public abstract List<ISSDPMessage> getSSDPMessagesToSend();
	
}
