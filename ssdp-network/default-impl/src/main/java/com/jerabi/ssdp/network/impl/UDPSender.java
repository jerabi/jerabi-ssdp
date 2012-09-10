package com.jerabi.ssdp.network.impl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.network.IUDPSender;

/**
 * 
 * @author Sebastien Dionne
 *
 */
public class UDPSender implements IUDPSender {
	private static final Logger logger = Logger.getLogger(UDPSender.class.getName());
	
	private DatagramSocket ssdpUniSock = null;
	
	public UDPSender() throws Exception {

		ssdpUniSock = new DatagramSocket(null);
		ssdpUniSock.setReuseAddress(true);
	}
	
	public UDPSender(int port) throws Exception {

		ssdpUniSock = new DatagramSocket(null);
		ssdpUniSock.setReuseAddress(true);
		ssdpUniSock.bind(new InetSocketAddress(port));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(String msg, SocketAddress address, ISSDPResponseHandler callbackHandler, int ttl) throws Exception {
		
		DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), address);
		
		ssdpUniSock.send(dgmPacket);
		
		ssdpUniSock.setSoTimeout(ttl*1000); // convert to seconds
		
		byte buf[] = new byte[1024];
		DatagramPacket dgmPacket2 = new DatagramPacket(buf, buf.length);
		ssdpUniSock.receive(dgmPacket2);
		
		int packetLen = dgmPacket2.getLength();
		String packetData = new String(dgmPacket2.getData(), 0, packetLen);
		
		logger.finest("discover response : \n" + packetData);
		
		if(callbackHandler!=null){
			callbackHandler.handle(dgmPacket2.getAddress().getHostAddress(), dgmPacket2.getPort(), packetData);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(String msg, InetAddress address, int port, ISSDPResponseHandler callbackHandler, int ttl) throws Exception {
		
		DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), address, port);
		
		ssdpUniSock.setSoTimeout(ttl*1000); // convert to seconds
		ssdpUniSock.send(dgmPacket);
		
		try {
			byte buf[] = new byte[1024];
			DatagramPacket dgmPacket2 = new DatagramPacket(buf, buf.length);
			ssdpUniSock.receive(dgmPacket2);
			
			int packetLen = dgmPacket2.getLength();
			String packetData = new String(dgmPacket2.getData(), 0, packetLen);
			
			logger.finest("discover response : \n" + packetData);
			
			if(callbackHandler!=null){
				callbackHandler.handle(dgmPacket2.getAddress().getHostAddress(), dgmPacket2.getPort(), packetData);
			}
		} catch (SocketTimeoutException e) {
			// does nothing
		}

	}

}
