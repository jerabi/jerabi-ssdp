package com.jerabi.ssdp.listener;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.network.IMulticastListener;
import com.jerabi.ssdp.network.SSDPNetworkFactory;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;
import com.jerabi.ssdp.util.StateHolder;

/**
 * Provides API to listen for multicast messages over network.
 * 
 * @author Sebastien Dionne
 * @example.
 * <pre>
 * // You need pass a ISSDPHandler to handle the multicast messages received.
 * SSDPMulticastListener listener = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, true);
 * listener.setSSDPHandler(new SSDPDefaultHandler(controler)); 
 * t = new Thread(listener);
 * t.start();
 * 
 * // using multiples Network Interfaces
 * 
 // You need pass a ISSDPHandler to handle the multicast messages received.
 * List<NetworkInterface> networkInterfaceList = new ArrayList<NetworkInterface>();
 * networkInterfaceList.add(NetworkInterface.getByName("eth0"));
 * SSDPMulticastListener listener = new SSDPMulticastListener(new InetSocketAddress(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT), networkInterfaceList);
 * listener.setSSDPHandler(new SSDPDefaultHandler(controler)); 
 * t = new Thread(listener);
 * t.start();  
 * </pre>
 * 
 */
public class SSDPMulticastListener implements Runnable {
	private static final Logger logger = Logger.getLogger(SSDPMulticastListener.class.getName());
	
	protected StateHolder<State> stateHolder = new StateHolder<State>();
	
	protected int timeout = SSDPContants.DEFAULT_SOCKET_TIMEOUT;
	protected String host = null;
	protected int port;
	protected InetSocketAddress socketAddress = null;
	protected List<NetworkInterface> networkInterfaceList = null;
	private ISSDPResponseHandler handler = null;
	protected boolean blocking = true;
	
	/**
	 * Constructor with default delay and listener in blocking mode
	 * @param host Multicast IP
	 * @param port Multicast Port
	 */
	public SSDPMulticastListener(String host, int port) {
		this(host, port, SSDPContants.DEFAULT_SOCKET_TIMEOUT, true);
	}
	
	/**
	 * Constructor with listener in blocking mode
	 * @param host Multicast IP
	 * @param port Multicast Port
	 * @param timeout timeout for the non blocking mode 
	 */
	public SSDPMulticastListener(String host, int port, int timeout) {
		this(host, port, timeout, true);
	}
	
	/**
	 * Constructor with custom values
	 * @param host Multicast IP
	 * @param port Multicast Port
	 * @param timeout timeout for the non blocking mode 
	 * @param blocking is it in blocking mode
	 */
	public SSDPMulticastListener(String host, int port, int timeout, boolean blocking) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.blocking = blocking;
		
		setState(State.STOPPED);
	}
	
	/**
	 * Constructor with default delay and listener in blocking mode
	 * @param host Multicast IP
	 * @param port Multicast Port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 */
	public SSDPMulticastListener(String host, int port, List<NetworkInterface> networkInterfaceList) {
		this(host, port, networkInterfaceList, SSDPContants.DEFAULT_SOCKET_TIMEOUT, true);
	}
	
	/**
	 * Constructor with listener in blocking mode
	 * @param host Multicast IP
	 * @param port Multicast Port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 * @param timeout timeout for the non blocking mode 
	 */
	public SSDPMulticastListener(String host, int port, List<NetworkInterface> networkInterfaceList, int timeout) {
		this(host, port, networkInterfaceList, timeout, true);
	}
	
	/**
	 * Constructor with custom values
	 * @param host Multicast IP
	 * @param port Multicast Port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 * @param timeout timeout for the non blocking mode 
	 * @param blocking is it in blocking mode
	 */
	public SSDPMulticastListener(String host, int port, List<NetworkInterface> networkInterfaceList, int timeout, boolean blocking) {
		this.host = host;
		this.port = port;
		this.networkInterfaceList = networkInterfaceList;
		this.timeout = timeout;
		this.blocking = blocking;
		
		socketAddress = new InetSocketAddress(host, port);
		
		setState(State.STOPPED);
	}
	
	/**
	 * Constructor with default delay and listener in blocking mode
	 * @param socketAddress Multicast IP and port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 */
	public SSDPMulticastListener(InetSocketAddress socketAddress, List<NetworkInterface> networkInterfaceList) {
		this(socketAddress, networkInterfaceList, SSDPContants.DEFAULT_SOCKET_TIMEOUT, true);
	}
	
	/**
	 * Constructor with default delay and listener in blocking mode
	 * @param socketAddress Multicast IP and port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 * * @param timeout timeout for the non blocking mode
	 */
	public SSDPMulticastListener(InetSocketAddress socketAddress, List<NetworkInterface> networkInterfaceList, int timeout) {
		this(socketAddress, networkInterfaceList, timeout, true);
	}
	
	/**
	 * Constructor with default delay and listener in blocking mode
	 * @param socketAddress Multicast IP and port
	 * @param networkInterfaceList NetworkInterfaces that will be used to listen
	 * @param timeout timeout for the non blocking mode
	 * @param blocking is it in blocking mode
	 */
	public SSDPMulticastListener(InetSocketAddress socketAddress, List<NetworkInterface> networkInterfaceList, int timeout, boolean blocking) {
		this.socketAddress = socketAddress;
		this.networkInterfaceList = networkInterfaceList;
		this.timeout = timeout;
		this.blocking = blocking;
		
		setState(State.STOPPED);
	}

	/**
	 * Returns the ISSDPHandler that handle the multicast messages received
	 * @return the handler
	 */
	public ISSDPResponseHandler getSSDPResponseHandler() {
		return handler;
	}

	/**
	 * Sets the ISSDPHandler that will handle the multicast messages received
	 * @param handler
	 */
	public void setSSDPResponseHandler(ISSDPResponseHandler handler) {
		this.handler = handler;
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
	 * Returns the timeout of the non blocking mode
	 * @return timeout in ms.
	 */
	public int getTimeout() {
		return timeout;
	}
	
	/**
	 * Sets the timeout for the non blocking mode
	 * @param timeout value in ms.
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Returns the Multicast Address on which this listener listen
	 * @return the IP.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Sets the Multicast Address on which this listener will listen
	 * @param host the IP
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * Returns the Multicast Port on which this listener listen
	 * @return the port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Sets the Multicast Port on which this listener will listen
	 * @param port the port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Sets this listener in blocking mode or non blocking.
	 * <pre>
	 * In blocking mode this listener will block until a message is received.
	 * In non blocking mode this listener will wait until the timeout value and loop back again.
	 * In non blocking mode this listener can be stopped when the timeout expired, and in blocking mode,
	 * the listener need to receive a message before changing state.
	 * </pre>
	 * @param blocking is blocking mode enabled
	 */
	public void setBlocking(boolean blocking){
		this.blocking = blocking;
	}
	
	/**
	 * Returns the blocking mode of this listener
	 * <pre>
	 * In blocking mode this listener will block until a message is received.
	 * In non blocking mode this listener will wait until the timeout value and loop back again.
	 * In non blocking mode this listener can be stopped when the timeout expired, and in blocking mode,
	 * the listener need to receive a message before changing state.
	 * </pre>
	 * @return is it blocking mode enabled
	 */
	public boolean getBlocking(){
		return blocking;
	}
	
	@Override
	public void run() {
		
		stateHolder.setState(State.STARTED);
		
		IMulticastListener listener = null;
		InetAddress group = null;
		
		try {
			
			// if NetworkInterface are used, we can join on multiple groups
			if(networkInterfaceList!=null && !networkInterfaceList.isEmpty()){
				//listener = SSDPNetworkFactory.getInstance().createMulticastListener(socketAddress, handler);
				listener = SSDPNetworkFactory.getInstance().createMulticastListener(socketAddress.getPort(), handler);
				for (NetworkInterface ni : networkInterfaceList) {
					listener.joinGroup(socketAddress, ni);
				}
			} else {
				group = InetAddress.getByName(host);
				listener = SSDPNetworkFactory.getInstance().createMulticastListener(port, handler);
				listener.joinGroup(group);
			}
			
			listener.setTimeout(timeout);
			
			while (!Thread.interrupted() && stateHolder.getState() != State.STOPPED) {
				try {
					listener.receive(blocking);
				} catch (SocketTimeoutException e) {
					// does nothing
				}
			}
		} catch(Exception e){
			logger.log(Level.WARNING, e.getMessage(), e);
		} finally {
			stateHolder.setState(State.STOPPED);
			if(listener!=null){
				if(group!=null){
					try {
						listener.leaveGroup(group);
					} catch (Exception e) {
					}
				} else if(networkInterfaceList!=null && !networkInterfaceList.isEmpty()){
					for (NetworkInterface ni : networkInterfaceList) {
						try {
							listener.leaveGroup(socketAddress, ni);
						} catch (Exception e) {
						}
					}
				}
			}
		}
		
		
	}

}
