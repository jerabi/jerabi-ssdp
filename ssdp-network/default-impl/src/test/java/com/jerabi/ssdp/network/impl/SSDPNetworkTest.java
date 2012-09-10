package com.jerabi.ssdp.network.impl;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.handler.ISSDPResponseHandler;
import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.network.IMulticastListener;
import com.jerabi.ssdp.network.ISSDPNetwork;
import com.jerabi.ssdp.network.IUDPSender;
import com.jerabi.ssdp.network.SSDPNetworkFactory;
import com.jerabi.ssdp.network.impl.SSDPNetworkImpl;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;

public class SSDPNetworkTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		assertNotNull(instance);
	}

	@Test
	public void testSendMulticastMessageSocketAddress() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		assertNotNull(instance);
		
		String message = "testSendMulticastMessageSocketAddress";
		
		try {
			instance.sendMulticastMessage(message, new InetSocketAddress(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT));
			instance.sendMulticastMessage(message, new InetSocketAddress(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT), null, 3);
			instance.sendMulticastMessage(message, new InetSocketAddress(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT), new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
					
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			}, 3);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		fail("doit ajouter un listener.. je dois etre sur de recevoir les UDP");
	}
	

	@Test
	public void testSendMulticastMessageInetAddress() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		assertNotNull(instance);
		
		String message = "testSendMulticastMessageInetAddress";
		
		try {
			instance.sendMulticastMessage(message, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT);
			instance.sendMulticastMessage(message, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, null, 3);
			instance.sendMulticastMessage(message, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
					
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			}, 3);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
	}

	@Test
	public void testCreateUDPSender() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		
		assertNotNull(instance);
		
		String msg = "testCreateUDPSender";
		
		try {
			IUDPSender sender1 = instance.createUDPSender();
			assertNotNull(sender1);
			
			sender1.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender1.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, null, 5);
			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			IUDPSender sender2 = instance.createUDPSender(SSDPContants.DEFAULT_PORT);
			assertNotNull(sender2);
			
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, null, 5);

			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
	}
	
	@Test
	public void testMulticastListener() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		
		assertNotNull(instance);
		
		final String msg = "testMulticastListener";
		
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		
		InetAddress group = null;
		
		Thread t1 = null;
		Thread t2 = null;
		
		try {
			
			group = InetAddress.getByName(SSDPContants.DEFAULT_IP);
			
			final IMulticastListener listener1 = instance.createMulticastListener(SSDPContants.DEFAULT_PORT, new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					if(msg.equals(message)){
						latch1.countDown();
					}
					
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			});
			assertNotNull(listener1);
			
			final IMulticastListener listener2 = instance.createMulticastListener(new InetSocketAddress(SSDPContants.DEFAULT_PORT+1), new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					if(msg.equals(message)){
						latch2.countDown();
					}
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			});
			assertNotNull(listener2);
			
			
			listener1.joinGroup(group);
			listener2.joinGroup(group);
			
			t1 = new Thread(new Runnable(){
				@Override
				public void run() {
					while(!Thread.interrupted()){
						try {
							listener1.receive(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}});
			
			t2 = new Thread(new Runnable(){
				@Override
				public void run() {
					while(!Thread.interrupted()){
						try {
							listener2.receive(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}});
			
			t1.start();
			t2.start();
			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			IUDPSender sender2 = instance.createUDPSender();
			assertNotNull(sender2);
			
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT+1, null, 5);

			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			latch1.await(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		t1.interrupt();
		t2.interrupt();
		
		assertEquals(0, latch1.getCount());
		assertEquals(0, latch2.getCount());
		
	}
	
	@Test
	public void testMulticastListenerNetworkInterface() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		ISSDPNetwork instance = SSDPNetworkFactory.getInstance();
		
		fail("Not yet implemented");
		
		assertNotNull(instance);
		
		final String msg = "testMulticastListener";
		
		final CountDownLatch latch1 = new CountDownLatch(1);
		final CountDownLatch latch2 = new CountDownLatch(1);
		
		InetAddress group = null;
		
		Thread t1 = null;
		Thread t2 = null;
		
		try {
			
			group = InetAddress.getByName(SSDPContants.DEFAULT_IP);
			
			final IMulticastListener listener1 = instance.createMulticastListener(SSDPContants.DEFAULT_PORT, new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					if(msg.equals(message)){
						latch1.countDown();
					}
					
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			});
			assertNotNull(listener1);
			
			final IMulticastListener listener2 = instance.createMulticastListener(new InetSocketAddress(SSDPContants.DEFAULT_PORT+1), new ISSDPResponseHandler() {
				
				@Override
				public void setSSDPControler(ISSDPControler controler) {
				}
				
				@Override
				public void handle(String message) throws Exception {
					
				}
				
				@Override
				public void handle(String remoteAddr, int remotePort, String message)
						throws Exception {
					if(msg.equals(message)){
						latch2.countDown();
					}
				}
				
				@Override
				public ISSDPControler getSSDPControler() {
					return null;
				}
			});
			assertNotNull(listener2);
			
			
			listener1.joinGroup(group);
			listener2.joinGroup(group);
			
			t1 = new Thread(new Runnable(){
				@Override
				public void run() {
					while(!Thread.interrupted()){
						try {
							listener1.receive(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}});
			
			t2 = new Thread(new Runnable(){
				@Override
				public void run() {
					while(!Thread.interrupted()){
						try {
							listener2.receive(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}});
			
			t1.start();
			t2.start();
			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			IUDPSender sender2 = instance.createUDPSender();
			assertNotNull(sender2);
			
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT, new SSDPDefaultResponseHandler(null) {}, 5);
			sender2.sendMessage(msg, InetAddress.getByName(SSDPContants.DEFAULT_IP), SSDPContants.DEFAULT_PORT+1, null, 5);

			
		} catch(Exception e){
			e.printStackTrace();
			fail("Shouldn't throw Exception");
		}
		
		try {
			latch1.await(4, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		t1.interrupt();
		t2.interrupt();
		
		assertEquals(0, latch1.getCount());
		assertEquals(0, latch2.getCount());
		
	}

}
