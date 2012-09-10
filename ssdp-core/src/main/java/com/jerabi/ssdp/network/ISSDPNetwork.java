package com.jerabi.ssdp.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;

/**
 * 
 * Provides API for sending and receiving messages over network.
 * 
 * @author Sebastien Dionne
 */
public interface ISSDPNetwork {

	
	// SSDPPeriodicMessageSender ->
	/**
	 * Sends a multicast UDP message without the needs to join a group.
	 * 
	 * The multicast addresses are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
	 * inclusive. The address 224.0.0.0 is reserved and should not be used.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @throws Exception 
	 */
	void sendMulticastMessage(String message, SocketAddress address) throws Exception;
	
	/**
	 * Sends a multicast UDP message without the needs to join a group.
	 * 
	 * The multicast addresses are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
	 * inclusive. The address 224.0.0.0 is reserved and should not be used.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param ni NetworkInterface that will be used.
	 * @throws Exception 
	 */
	void sendMulticastMessage(String message, SocketAddress address, NetworkInterface ni) throws Exception;
	
	/**
	 * 
	 * Sends a multicast UDP message
	 * 
	 * The multicast addresses are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
	 * inclusive. The address 224.0.0.0 is reserved and should not be used.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param port destination port
	 * @throws Exception
	 */
	void sendMulticastMessage(String message, InetAddress address, int port) throws Exception;
	
	/**
	 * 
	 * Sends a multicast UDP message and wait for a response.  TTL defines the timeout for the response.
	 * If a response is received, it will be forward to the callback {@link ISSDPResponseHandler}.
	 * 
	 * The multicast addresses are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
	 * inclusive. The address 224.0.0.0 is reserved and should not be used.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param callbackHandler handle the response
	 * @param ttl Time to live (timeout)
	 * @throws Exception
	 */
	void sendMulticastMessage(String message, SocketAddress address, ISSDPResponseHandler callbackHandler, int ttl) throws Exception;
	
	/**
	 * 
	 * Sends a multicast UDP message and wait for a response.  TTL defines the timeout for the response.
	 * If a response is received, it will be forward to the callback {@link ISSDPResponseHandler}.
	 * 
	 * The multicast addresses are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
	 * inclusive. The address 224.0.0.0 is reserved and should not be used.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param port destination port
	 * @param callbackHandler handle the response
	 * @param ttl Time to live (timeout)
	 * @throws Exception
	 */
	void sendMulticastMessage(String message, InetAddress address, int port, ISSDPResponseHandler callbackHandler, int ttl) throws Exception;
	
	// SSDPDiscoverSender -> <-
	/**
	 * Creates a {@link IUDPSender} instance.  
	 * @return IUDPSender
	 * @throws Exception
	 */
	IUDPSender createUDPSender() throws Exception;
	
	/**
	 * Creates a {@link IUDPSender} instance.  
	 * 
	 * @param port port that will be used
	 * @return IUDPSender
	 * @throws Exception
	 */
	IUDPSender createUDPSender(int port) throws Exception;
	
	// SSDPMulticastListener <-
	/**
	 * Creates a {@link IMulticastListener} instance.
	 * 
	 * @param port listening port
	 * @param callbackHandler handle the response
	 * @return IMulticastListener
	 * @throws Exception
	 */
	IMulticastListener createMulticastListener(int port, ISSDPResponseHandler callbackHandler) throws Exception;
	
	/**
	 * Creates a {@link IMulticastListener} instance.
	 * 
	 * @param bindAddress listening address
	 * @param callbackHandler handle the response
	 * @return IMulticastListener
	 * @throws Exception
	 */
	IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler) throws Exception;
	
	/**
	 * Creates a {@link IMulticastListener} instance.
	 * 
	 * @param bindAddress listening address
	 * @param callbackHandler handle the response
	 * @param ni NetworkInterface that will be used.
	 * @return IMulticastListener
	 * @throws Exception
	 */
	IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler, NetworkInterface ni) throws Exception;
	
	
}
