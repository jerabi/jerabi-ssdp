package com.jerabi.ssdp.network;

import java.net.InetAddress;
import java.net.SocketAddress;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;

/**
 * Provides API for sending and receiving UDP messages over network.
 * 
 * This sender will not send multicast UDP.  If you need multicast UDP look {@link ISSDPNetwork}.
 * 
 * @author Sebastien Dionne
 */
public interface IUDPSender {
	
	/**
	 * Sends a UDP message and wait for a response.  TTL defines the timeout for the response.
	 * If a response is received, it will be forward to the callback {@link ISSDPResponseHandler}.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param callbackHandler handle the response
	 * @param ttl Time to live (timeout)
	 * @throws Exception
	 */
	void sendMessage(String message, SocketAddress address, ISSDPResponseHandler callbackHandler, int ttl) throws Exception;
	
	/**
	 * Sends a UDP message and wait for a response.  TTL defines the timeout for the response.
	 * If a response is received, it will be forward to the callback {@link ISSDPResponseHandler}.
	 * 
	 * @param message message to send
	 * @param address destination address
	 * @param port destination port
	 * @param callbackHandler handle the response
	 * @param ttl Time to live (timeout)
	 * @throws Exception
	 */
	void sendMessage(String message, InetAddress address, int port, ISSDPResponseHandler callbackHandler, int ttl) throws Exception;
	
}
