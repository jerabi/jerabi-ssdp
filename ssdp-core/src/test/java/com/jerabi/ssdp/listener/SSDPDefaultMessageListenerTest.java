package com.jerabi.ssdp.listener;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.handler.SSDPDefaultMessageHandler;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.UpdateMessage;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;
import com.jerabi.ssdp.message.helper.SSDPMessageHelperTest;

public class SSDPDefaultMessageListenerTest {

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
	public void testProcessSSDPAliveMessage() {
		
		final CountDownLatch latch = new CountDownLatch(1); 
		
		SSDPDefaultMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPAliveMessage(AliveMessage ssdpMessage)
					throws Exception {
				latch.countDown();
			}
		};
		
		try {
			listener.processSSDPAliveMessage((AliveMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createAliveMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPByeByeMessage((ByeByeMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createByeByeMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPUpdateMessage((UpdateMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createUpdateMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPDiscoverMessage(null, -1, (DiscoverMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createDiscoverMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPDiscoverResponseMessage((DiscoverResponseMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createDiscoverResponseMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, latch.getCount());
		
	}

	@Test
	public void testProcessSSDPByeByeMessage() {
		final CountDownLatch latch = new CountDownLatch(1); 
		
		SSDPDefaultMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPByeByeMessage(ByeByeMessage ssdpMessage)
					throws Exception {
				latch.countDown();
			}
		};
		
		try {
			listener.processSSDPByeByeMessage((ByeByeMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createByeByeMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPAliveMessage((AliveMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createAliveMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, latch.getCount());
	}

	@Test
	public void testProcessSSDPDiscoverMessage() {
		final CountDownLatch latch = new CountDownLatch(1); 
		
		SSDPDefaultMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPDiscoverMessage(String remoteAddr,
					int remotePort, DiscoverMessage ssdpMessage)
					throws Exception {
				latch.countDown();
			}
		};
		
		try {
			listener.processSSDPDiscoverMessage(null, -1, (DiscoverMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createDiscoverMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPAliveMessage((AliveMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createAliveMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, latch.getCount());
	}
	
	@Test
	public void testProcessSSDPDiscoverResponseMessage() {
		final CountDownLatch latch = new CountDownLatch(1); 
		
		SSDPDefaultMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPDiscoverResponseMessage(
					DiscoverResponseMessage ssdpMessage) throws Exception {
				latch.countDown();
			}
		};
		
		try {
			listener.processSSDPDiscoverResponseMessage((DiscoverResponseMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createDiscoverResponseMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPAliveMessage((AliveMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createAliveMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, latch.getCount());
	}

	@Test
	public void testProcessSSDPUpdateMessage() {
		final CountDownLatch latch = new CountDownLatch(1); 
		
		SSDPDefaultMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPUpdateMessage(UpdateMessage ssdpMessage)
					throws Exception {
				latch.countDown();
			}
		};
		
		try {
			listener.processSSDPUpdateMessage((UpdateMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createUpdateMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		try {
			listener.processSSDPAliveMessage((AliveMessage)SSDPMessageHelper.getSSDPMessage(SSDPMessageHelperTest.createAliveMessage()));
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, latch.getCount());
	}

}
