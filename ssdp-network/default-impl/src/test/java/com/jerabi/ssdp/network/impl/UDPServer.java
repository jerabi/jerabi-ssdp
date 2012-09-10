package com.jerabi.ssdp.network.impl;

import com.jerabi.ssdp.network.impl.MulticastListener;

public class UDPServer implements Runnable {

	private MulticastListener listener = null;
	private boolean blocking = false;
	
	public UDPServer(MulticastListener listener, boolean blocking){
		this.listener = listener;
		this.blocking = blocking;
	}
	
	@Override
	public void run() {

		while(!Thread.interrupted()){
			try {
				if(blocking){
					listener.receive(blocking);
				} else {
					listener.receive();
				}
			} catch (Exception e) {
				
			}
		}
		
	}

}
