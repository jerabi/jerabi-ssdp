package com.jerabi.ssdp.network.impl;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.network.IMulticastListener;
import com.jerabi.ssdp.network.ISSDPNetwork;
import com.jerabi.ssdp.network.IUDPSender;

/**
 * 
 * Provides API for sending and receiving messages over network.
 * 
 * This class is a dummy that doesn't implements any methods.
 * It used for compilation purpose.  An implementation must be
 * available in another package. 
 * 
 * @author Sebastien Dionne
 *
 */
public class SSDPNetwork implements ISSDPNetwork {
	
	/**
	 * Private Constructor.  You need to use getInstance()
	 * @see #getInstance()
	 */
	private SSDPNetwork(){}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMulticastListener createMulticastListener(int port, ISSDPResponseHandler callbackHandler) throws Exception {
		throw new RuntimeException("Not implemented");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler) throws Exception {
		throw new RuntimeException("Not implemented");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IMulticastListener createMulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler, NetworkInterface ni) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUDPSender createUDPSender() throws Exception {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUDPSender createUDPSender(int port) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String message, SocketAddress address) throws Exception {
		throw new RuntimeException("Not implemented");
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String message, InetAddress address, int port) throws Exception {
		throw new RuntimeException("Not implemented");
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMulticastMessage(String message, SocketAddress address, ISSDPResponseHandler callbackHandler, int ttl) throws Exception {
		throw new RuntimeException("Not implemented");
		
	}

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
	public void sendMulticastMessage(String message, SocketAddress address, NetworkInterface ni) throws Exception {
		throw new RuntimeException("Not implemented");
	}

	/**
	 * Returns a ISSDPNetwork instance.
	 * 
	 * The implementation class must implements this method.
	 *  
	 * @return ISSDPNetwork
	 */
	public static ISSDPNetwork getInstance() {
		throw new RuntimeException("Not implemented");
	}

}
