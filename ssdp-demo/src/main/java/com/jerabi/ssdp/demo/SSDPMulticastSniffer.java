package com.jerabi.ssdp.demo;


import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;

import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.listener.SSDPMulticastListener;
import com.jerabi.ssdp.util.SSDPContants;

public class SSDPMulticastSniffer {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		//SSDPMulticastListener listener = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT);
		
		List<NetworkInterface> networkInterfaceList = new ArrayList<NetworkInterface>();
		
		networkInterfaceList.add(NetworkInterface.getByName("net5"));
		networkInterfaceList.add(NetworkInterface.getByName("eth13"));
		
		SSDPMulticastListener listener = new SSDPMulticastListener(new InetSocketAddress(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT), networkInterfaceList);
		listener.setSSDPResponseHandler(new SSDPDefaultResponseHandler(null));
		listener.setBlocking(false);
		
		System.out.println("Starting MulticastListener");
		new Thread(listener).start();
		 
		//System.out.println("Waiting 30 sec. before quitting");
		//Thread.sleep(30000);
	}

}
