package com.jerabi.ssdp.network.impl;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.util.logging.Logger;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.network.IMulticastListener;
import com.jerabi.ssdp.network.ISSDPNetwork;
import com.jerabi.ssdp.network.IUDPSender;
import com.jerabi.ssdp.network.impl.SSDPNetworkImpl;

/**
 * test default implementation ssdp network
 * @author Sebastien Dionne
 *
 */
public class SSDPNetworkImpl implements ISSDPNetwork {
	
	private static final Logger logger = Logger.getLogger(SSDPNetworkImpl.class.getName());
	
	// SSDPPeriodicMessageSender ->
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String msg, SocketAddress address) throws Exception {
		try {

			MulticastSocket ssdpUniSock = new MulticastSocket(null);
			ssdpUniSock.setReuseAddress(true);
			
			logger.info("sending message \n" + msg);
			
			DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), address);
			
			ssdpUniSock.send(dgmPacket);

			ssdpUniSock.close();

		} catch (Throwable e) {
		}
		
	}
	
	// SSDPPeriodicMessageSender ->
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String msg, InetAddress address, int port) throws Exception {
		try {

			MulticastSocket ssdpUniSock = new MulticastSocket(null);
			ssdpUniSock.setReuseAddress(true);
			
			logger.info("sending message \n" + msg);
			
			DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
			
			ssdpUniSock.send(dgmPacket);

			ssdpUniSock.close();

		} catch (Throwable e) {
		}
		
	}
	
	// SSDPPeriodicMessageSender ->
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String message, SocketAddress address, ISSDPResponseHandler callbackHandler, int ttl) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	// SSDPPeriodicMessageSender ->
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String message, InetAddress address, int port, ISSDPResponseHandler callbackHandler, int ttl) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMulticastListener createMulticastListener(int port, ISSDPResponseHandler callbackHandler) throws Exception {
		MulticastListener listener = new MulticastListener(port, callbackHandler);
		return listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler) throws Exception {
		MulticastListener listener = new MulticastListener(bindAddress, callbackHandler);
		return listener;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUDPSender createUDPSender(int port) throws Exception {
		UDPSender sender = new UDPSender(port);
		return sender;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUDPSender createUDPSender() throws Exception {
		UDPSender sender = new UDPSender();
		return sender;
	}

	@Override
	public IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler, NetworkInterface ni) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	@Override
	public void sendMulticastMessage(String message, SocketAddress address, NetworkInterface ni) throws Exception {
		throw new RuntimeException("Not implemented");
	}


}
