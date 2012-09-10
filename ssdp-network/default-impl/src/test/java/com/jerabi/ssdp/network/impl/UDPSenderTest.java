package com.jerabi.ssdp.network.impl;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.network.impl.MulticastListener;
import com.jerabi.ssdp.network.impl.UDPSender;
import com.jerabi.ssdp.util.SSDPContants;

public class UDPSenderTest {

	private static UDPSender sender = null;
	private static UDPSender sender2 = null;
	
	private static final String HOST = SSDPContants.DEFAULT_IP;
	private static final int PORT = 5009;
	private static InetAddress group = null;
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			sender = new UDPSender();
			sender2 = new UDPSender(PORT);
			
			group = InetAddress.getByName(HOST);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUDPSender() {
		MulticastListener listener = null;
		MulticastListener listener2 = null;
		MulticastListener listener3 = null;
		
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		
		try {
			listener = new MulticastListener(PORT, new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch1.countDown();
					new UDPSender().sendMessage(message, InetAddress.getByName(remoteAddr), remotePort, null, 2);
				}
			});
			
			listener.joinGroup(group);
			
			listener2 = new MulticastListener(new InetSocketAddress(PORT), new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch2.countDown();
					new UDPSender().sendMessage(message, InetAddress.getByName(remoteAddr), remotePort, null, 2);
				}
			});
			
			listener2.joinGroup(group);
			
			listener3 = new MulticastListener(new InetSocketAddress(PORT), null);
			listener3.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		Thread t1 = new Thread(new UDPServer(listener, true));
		Thread t2 = new Thread(new UDPServer(listener2, true));
		Thread t3 = new Thread(new UDPServer(listener3, false));
		
		t1.start();
		t2.start();
		t3.start();
		
		String msg = "UDP sent";
		
		try {
			sender.sendMessage(msg, InetAddress.getByName(HOST), PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender.sendMessage(msg, InetAddress.getByName(HOST), PORT, null, 5);
		} catch (SocketTimeoutException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			latch1.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		t1.interrupt();
		t2.interrupt();
		t3.interrupt();
		
		assertEquals(0, latch1.getCount());
		assertEquals(0, latch2.getCount());
		
		try {
			latch1.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
	}

	@Test
	public void testUDPSenderUsingINetAddress() {
		MulticastListener listener = null;
		MulticastListener listener2 = null;
		
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		
		try {
			listener = new MulticastListener(PORT, new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch1.countDown();
					new UDPSender().sendMessage(message, InetAddress.getByName(remoteAddr), remotePort, null, 2);
				}
			});
			
			listener.joinGroup(group);
			
			listener2 = new MulticastListener(new InetSocketAddress(PORT), new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch2.countDown();
					new UDPSender().sendMessage(message, InetAddress.getByName(remoteAddr), remotePort, null, 2);
				}
			});
			
			listener2.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		Thread t1 = new Thread(new UDPServer(listener, true));
		Thread t2 = new Thread(new UDPServer(listener2, true));
		
		t1.start();
		t2.start();
		
		String msg = "UDP sent with InetAddress";
		
		try {
			sender2.sendMessage(msg, InetAddress.getByName(HOST), PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender2.sendMessage(msg, InetAddress.getByName(HOST), PORT, null, 5);
		} catch (SocketTimeoutException e) {
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			latch1.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		t1.interrupt();
		t2.interrupt();
		
		assertEquals(0, latch1.getCount());
		assertEquals(0, latch2.getCount());
		
		try {
			latch1.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
	}
	
	@Test
	public void testUDPSenderUsingSocketAddress() {
		MulticastListener listener = null;
		MulticastListener listener2 = null;
		
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		
		try {
			listener = new MulticastListener(PORT+1, new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch1.countDown();
					new UDPSender().sendMessage(message, new InetSocketAddress(InetAddress.getByName(remoteAddr), remotePort), null, 2);
				}
			});
			
			listener.joinGroup(group);
			
			listener2 = new MulticastListener(new InetSocketAddress(PORT+1), new SSDPDefaultResponseHandler(null) {
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					latch2.countDown();
					new UDPSender().sendMessage(message, new InetSocketAddress(InetAddress.getByName(remoteAddr), remotePort), null, 2);
				}
			});
			
			listener2.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		Thread t1 = new Thread(new UDPServer(listener, true));
		Thread t2 = new Thread(new UDPServer(listener2, true));
		
		t1.start();
		t2.start();
		
		String msg = "UDP sent with SocketAddress";
		
		final CountDownLatch callbackLatch = new CountDownLatch(1);
		try {
			sender.sendMessage(msg, new InetSocketAddress(InetAddress.getByName(HOST), PORT+1) , new SSDPDefaultResponseHandler(null) {
				@Override
				public void handle(String message) throws Exception {
					callbackLatch.countDown();
				}
				@Override
				public void handle(String remoteAddr, int remotePort,
						String message) throws Exception {
					callbackLatch.countDown();
				}
			}, 4);
			sender.sendMessage(msg, new InetSocketAddress(InetAddress.getByName(HOST), PORT+1) , null, 4);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			latch1.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		t1.interrupt();
		t2.interrupt();
		
		assertEquals(0, latch1.getCount());
		assertEquals(0, latch2.getCount());
		
		try {
			latch1.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
	}

}
