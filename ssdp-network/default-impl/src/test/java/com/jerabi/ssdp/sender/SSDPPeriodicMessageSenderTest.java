package com.jerabi.ssdp.sender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.sender.SSDPDefaultPeriodicMessageSender;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;

public class SSDPPeriodicMessageSenderTest {

	private static SSDPDefaultPeriodicMessageSender sender = null;
	private static ExecutorService threadPool = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		sender = new SSDPDefaultPeriodicMessageSender(null, SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY) {
			@Override
			public List<ISSDPMessage> getSSDPMessagesToSend() {
				List<ISSDPMessage> list = new ArrayList<ISSDPMessage>();
				
				for(int i=0;i<5;i++){
					list.add(new ISSDPMessage(){
					@Override
					public String toString() {
						return "messagetosend";
					}});
				}
				
				return list;
			}
		};
		threadPool = Executors.newFixedThreadPool(5);

		threadPool.execute(sender);

		CountDownLatch latch = new CountDownLatch(1);
		latch.await(1, TimeUnit.SECONDS);
	}

	@Test
	public void testGetState() {
		assertNotNull(sender.getState());

	}

	@Test
	public void testSetState() {
		sender.setState(State.SUSPENDED);
		assertEquals(State.SUSPENDED, sender.getState());

		sender.setState(State.STOPPED);
		assertEquals(State.STOPPED, sender.getState());
	}

	@Test
	public void testGetDelay() {
		assertTrue(sender.getDelay() > -1);
	}

	@Test
	public void testSetDelay() {
		sender.setDelay(1234);

		assertEquals(1234, sender.getDelay());
	}

	@Test
	public void testGetSSDPHost() {
		assertEquals(SSDPContants.DEFAULT_IP, sender.getSSDPHost());
	}

	@Test
	public void testSetSSDPHost() {
		sender.setSSDPHost("localhost");
		assertEquals("localhost", sender.getSSDPHost());
	}

	@Test
	public void testGetSSDPPort() {
		assertEquals(SSDPContants.DEFAULT_PORT, sender.getSSDPPort());
	}

	@Test
	public void testSetSSDPPort() {
		sender.setSSDPPort(1234);
		assertEquals(1234, sender.getSSDPPort());
	}

	@Test
	public void testSendMessage() {
		try {
			sender.sendMessage("message1");
			sender.sendMessage((String)null);
		} catch (Exception e) {
			fail("Shouldn't throws Exception");
		}
	}
	
	@Test
	public void testSendMessageInvalidHost() {
		SSDPDefaultPeriodicMessageSender sender2 = new SSDPDefaultPeriodicMessageSender(null, "-1", -1, SSDPContants.DEFAULT_DELAY) {
			@Override
			public List<ISSDPMessage> getSSDPMessagesToSend() {
				List<ISSDPMessage> list = new ArrayList<ISSDPMessage>();
				
				for(int i=0;i<5;i++){
					list.add(new ISSDPMessage(){
					@Override
					public String toString() {
						return "messagetosend";
					}});
				}
				
				return list;
			}
		};
		threadPool = Executors.newFixedThreadPool(5);

		threadPool.execute(sender2);

		CountDownLatch latch = new CountDownLatch(1);
		try {
			latch.await(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		
		threadPool.shutdownNow();
		
	}

}
