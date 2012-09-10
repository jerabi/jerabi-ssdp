package com.jerabi.ssdp.network.impl;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;

import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.network.IMulticastListener;

/**
 * 
 * @author Sebastien Dionne
 * @see MulticastSocket
 */
public class MulticastListener implements IMulticastListener {
	
	private MulticastSocket ssdpUniSock = null;
	private ISSDPResponseHandler callbackHandler = null;
	private int timeout = 3000;
	
	public MulticastListener(int port, ISSDPResponseHandler callbackHandler) throws Exception {
		ssdpUniSock = new MulticastSocket(port); 
		this.callbackHandler = callbackHandler;
	}
	
	public MulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler) throws Exception {
		ssdpUniSock = new MulticastSocket(bindAddress); 
		this.callbackHandler = callbackHandler;
	}
	
	public MulticastListener(SocketAddress bindAddress, ISSDPResponseHandler callbackHandler, NetworkInterface ni) throws Exception {
		ssdpUniSock = new MulticastSocket(bindAddress); 
		this.callbackHandler = callbackHandler;
		ssdpUniSock.setNetworkInterface(ni);
	}
	
	@Override
	public void joinGroup(InetAddress group) throws Exception {
		ssdpUniSock.joinGroup(group);

	}
	
	@Override
	public void joinGroup(SocketAddress socketAddress, NetworkInterface ni) throws Exception {
		ssdpUniSock.joinGroup(socketAddress, ni);
	}

	@Override
	public void leaveGroup(SocketAddress socketAddress, NetworkInterface ni) throws Exception {
		ssdpUniSock.leaveGroup(socketAddress, ni);
	}

	@Override
	public void leaveGroup(InetAddress group) throws Exception {
		ssdpUniSock.leaveGroup(group);
	}

	@Override
	public void receive() throws Exception {
		receive(false);
	}
	
	@Override
	public void receive(boolean blocking) throws Exception {
		byte ssdvRecvBuf[] = new byte[1024];
		DatagramPacket dgmPacket2 = new DatagramPacket(ssdvRecvBuf, 1024);
		
		if(!blocking){
			ssdpUniSock.setSoTimeout(timeout);
		}
		ssdpUniSock.receive(dgmPacket2);
		
		int packetLen = dgmPacket2.getLength();
		String packetData = new String(dgmPacket2.getData(), 0, packetLen);
		
		// TODO en gros.. si la taille retournee est la meme que le buffer,
		// il y a de grosse chance qu'il reste du data cote serveur..
		// donc faire une boucle
		
		if(callbackHandler!=null){
			callbackHandler.handle(dgmPacket2.getAddress().getHostAddress(), dgmPacket2.getPort(), packetData);
		}

	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
