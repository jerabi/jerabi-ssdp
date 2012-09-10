package com.jerabi.ssdp.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

/**
 * Provides API to listen for incoming multicast UDP, with additional capabilities for
 * joining "groups" of other multicast hosts on the Internet.
 * 
 * A multicast group is specified by a class D IP address
 * and by a standard UDP port number. Class D IP addresses
 * are in the range <CODE>224.0.0.0</CODE> to <CODE>239.255.255.255</CODE>,
 * inclusive. The address 224.0.0.0 is reserved and should not be used.
 * 
 * To join a multicast group you need to invoke <CODE>joinGroup(InetAddress groupAddr)</CODE>.
 * To leave a multicast group you need to invoke <CODE>leaveGroup(InetAddress groupAddr)</CODE>.
 * 
 * When a multicast message is sent, <b>all</b> the subscribers will receive the message (within the ttl range).
 * 
 * It's possible to send a multicast message without the needs to join a group.
 * 
 * @author Sebastien Dionne
 *
 * @example. 
 * 
 * 
 */
public interface IMulticastListener {
	
	/**
	 * Reads for incoming message.  This method use the default timeout.
	 * If you need a blocking reader look {@link #receive(boolean)}
	 * @throws Exception
	 */
	void receive() throws Exception;
	

	/**
	 * Reads for incoming message.  This method is blocking until a message is received.
	 * If you need a non blocking reader look {@link #receive()}
	 * 
	 * @param blocking in blocking mode is set to true
	 * @throws Exception
	 */
	void receive(boolean blocking) throws Exception;
	
	/**
	 * Joins a multicast group
	 * @param group the new to join
	 * @throws Exception
	 */
	void joinGroup(InetAddress group) throws Exception;
	
	/**
	 * Leaves a multicast group
	 * @param group group to leave
	 * @throws Exception
	 */
	void leaveGroup(InetAddress group) throws Exception;
	
	/**
	 * Joins a multicast group
	 * @param socketAddress the new to join
	 * @param ni NetworkInterface that will be used.
	 * @throws Exception
	 */
	void joinGroup(SocketAddress socketAddress, NetworkInterface ni) throws Exception;
	
	/**
	 * Leaves a multicast group
	 * @param socketAddress the new to join
	 * @param ni NetworkInterface that will be used.
	 * @throws Exception
	 */
	void leaveGroup(SocketAddress socketAddress, NetworkInterface ni) throws Exception;
	
	/**
	 * Sets the timeout for non blocking socket
	 * @param timeout value in ms.
	 */
	void setTimeout(int timeout);
	
	/**
	 * Returns the timeout for non blocking socket
	 * @return timeout value in ms.
	 */
	int getTimeout();
	
}
