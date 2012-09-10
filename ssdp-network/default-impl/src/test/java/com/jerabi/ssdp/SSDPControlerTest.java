package com.jerabi.ssdp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.SSDPControler;
import com.jerabi.ssdp.handler.ISSDPMessageHandler;
import com.jerabi.ssdp.handler.SSDPDefaultMessageHandler;
import com.jerabi.ssdp.listener.SSDPMulticastListener;
import com.jerabi.ssdp.message.AbstractSSDPNotifyMessage;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.USNInfo;
import com.jerabi.ssdp.message.UpdateMessage;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;
import com.jerabi.ssdp.sender.SSDPDiscoverSender;
import com.jerabi.ssdp.sender.SSDPPeriodicMessageSender;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;

public class SSDPControlerTest {

	private static SSDPControler controler = null;
	private static ISSDPMessage aliveMessage = null;
	private static ISSDPMessage byebyeMessage = null;
	private static ISSDPMessage updateMessage = null;
	private static ISSDPMessage discoverMessage = null;
	private static ISSDPMessage discoverResponseMessage = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		aliveMessage = SSDPMessageHelper.getSSDPMessage(createAliveMessage());
		byebyeMessage = SSDPMessageHelper.getSSDPMessage(createByeByeMessage());
		updateMessage = SSDPMessageHelper.getSSDPMessage(createUpdateMessage());
		discoverMessage = SSDPMessageHelper.getSSDPMessage(createDiscoverMessage());
		discoverResponseMessage = SSDPMessageHelper.getSSDPMessage(createDiscoverResponseMessage());
		
	}

	@Before
	public void setUp() throws Exception {
		controler = new SSDPControler();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMessageListener() {
		
		assertNotNull(controler.getMessageHandlerList());
		assertTrue(controler.getMessageHandlerList().size()==0);
		
		ISSDPMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPAliveMessage(AliveMessage ssdpMessage)
					throws Exception {
			}
			@Override
			public void processSSDPByeByeMessage(ByeByeMessage ssdpMessage)
					throws Exception {
			}
			@Override
			public void processSSDPDiscoverMessage(String remoteAddr,
					int remotePort, DiscoverMessage ssdpMessage)
					throws Exception {
			}
			@Override
			public void processSSDPDiscoverResponseMessage(
					DiscoverResponseMessage ssdpMessage) throws Exception {
			}
			@Override
			public void processSSDPUpdateMessage(UpdateMessage ssdpMessage)
					throws Exception {
			}};
			
		
		controler.addMessageHandler(listener);
		assertTrue(controler.getMessageHandlerList().size()==1);
			
		controler.removeMessageHandler(null);
		assertTrue(controler.getMessageHandlerList().size()==1);
		
		
		controler.removeMessageHandler(listener);
		
		assertNotNull(controler.getMessageHandlerList());
		assertTrue(controler.getMessageHandlerList().size()==0);
		
	}

	@Test
	public void testPeriodicSender() {
		
		boolean enabled = controler.getPeriodicSenderEnabled();
		
		controler.setPeriodicSenderEnabled(!enabled);
		assertTrue(controler.getPeriodicSenderEnabled()==!enabled);
		
		controler.setPeriodicSenderEnabled(enabled);
		assertTrue(controler.getPeriodicSenderEnabled()==enabled);
		
		
		SSDPPeriodicMessageSender sender = controler.getPeriodicMessageSender();
		assertNotNull(sender);
		
		controler.setPeriodicMessageSender(null);
		assertTrue(controler.getPeriodicMessageSender()==null);
		
		controler.setPeriodicMessageSender(sender);
		assertEquals(sender, controler.getPeriodicMessageSender());
		
	}

	@Test
	public void testDiscoverSender() {
		
		boolean enabled = controler.getDiscoverSenderEnabled();
		
		controler.setDiscoverSenderEnabled(!enabled);
		assertTrue(controler.getDiscoverSenderEnabled()==!enabled);
		
		controler.setDiscoverSenderEnabled(enabled);
		assertTrue(controler.getDiscoverSenderEnabled()==enabled);
		
		
		SSDPDiscoverSender sender = controler.getDiscoverSender();
		assertNotNull(sender);
		
		controler.setDiscoverSender(null);
		assertTrue(controler.getDiscoverSender()==null);
		
		controler.setDiscoverSender(sender);
		assertEquals(sender, controler.getDiscoverSender());
	}

	@Test
	public void testMulticastListener() {
		boolean enabled = controler.getMulticastListenerEnabled();
		
		controler.setMulticastListenerEnabled(!enabled);
		assertTrue(controler.getMulticastListenerEnabled()==!enabled);
		
		controler.setMulticastListenerEnabled(enabled);
		assertTrue(controler.getMulticastListenerEnabled()==enabled);
		
		
		SSDPMulticastListener sender = controler.getMulticastListener();
		assertNotNull(sender);
		
		controler.setMulticastListener(null);
		assertTrue(controler.getMulticastListener()==null);
		
		controler.setMulticastListener(sender);
		assertEquals(sender, controler.getMulticastListener());
	}

	@Test
	public void testServiceInfo() {
		
		assertNotNull(controler.getServiceInfoList());
		assertTrue(controler.getServiceInfoList().size()==0);
		
		// add device
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "upnp:rootdevice","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","upnp:rootdevice")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:service:ConnectionManager:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:service:ConnectionManager:1")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:service:ContentDirectory:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:service:ContentDirectory:1")));
		controler.getServiceInfoList().add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "urn:schemas-upnp-org:device:MediaServer:1","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","schemas-upnp-org:device:MediaServer:1")));

		assertEquals(4, controler.getServiceInfoList().size());
		
		ServiceInfo serviceInfo = new ServiceInfo();
		controler.addServiceInfo(serviceInfo);
		
		assertEquals(5, controler.getServiceInfoList().size());
		
		controler.removeServiceInfo(serviceInfo);
		assertEquals(4, controler.getServiceInfoList().size());
		
		List<ServiceInfo> list = new ArrayList<ServiceInfo>();
		list.add(new ServiceInfo(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, "upnp:rootdevice","http://142.225.35.55:5001/description/fetch", new USNInfo("9dcf6222-fc4b-33eb-bf49-e54643b4f416","upnp:rootdevice")));
		
		controler.setServiceInfoList(list);
		assertEquals(1, controler.getServiceInfoList().size());
	}

	@Test
	public void testStartandStopAll() {
		try {
			controler.getDiscoverSender().setDelay(300000);
			controler.getPeriodicMessageSender().setDelay(300000);
			controler.getMulticastListener().setTimeout(300000);
			
			controler.start();
		} catch (Exception e) {
			
		}
		
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			latch.await(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.SLEEP, controler.getDiscoverSender().getState());
		assertEquals(State.SLEEP, controler.getPeriodicMessageSender().getState());
		assertEquals(State.STARTED, controler.getMulticastListener().getState());
		
		try {
			controler.stop();
		} catch (Exception e) {
			
		}
		
		latch = new CountDownLatch(1);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());
		
		// if in blocking it won't be stopped until a socket message is received. 
		// assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
	}
	
	@Test
	public void testStartandStopDiscoverOnly() {
		try {
			controler.setPeriodicSenderEnabled(false);
			controler.setDiscoverSenderEnabled(true);
			controler.setMulticastListenerEnabled(false);
			
			controler.getDiscoverSender().setDelay(300000);
			controler.getPeriodicMessageSender().setDelay(300000);
			controler.getMulticastListener().setTimeout(300000);
			
			controler.start();
		} catch (Exception e) {
			
		}
		
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			latch.await(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.SLEEP, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());
		assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
		try {
			controler.stop();
		} catch (Exception e) {
			
		}
		
		latch = new CountDownLatch(1);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());
		
		// if in blocking it won't be stopped until a socket message is received. 
		// assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
	}
	
	@Test
	public void testStartandStopPeriodicOnly() {
		try {
			controler.setPeriodicSenderEnabled(true);
			controler.setDiscoverSenderEnabled(false);
			controler.setMulticastListenerEnabled(false);
			
			controler.getDiscoverSender().setDelay(300000);
			controler.getPeriodicMessageSender().setDelay(300000);
			controler.getMulticastListener().setTimeout(300000);
			
			controler.start();
		} catch (Exception e) {
			
		}
		
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			latch.await(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.SLEEP, controler.getPeriodicMessageSender().getState());
		assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
		try {
			controler.stop();
		} catch (Exception e) {
			
		}
		
		latch = new CountDownLatch(1);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());

		// if in blocking it won't be stopped until a socket message is received. 
		// assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
	}
	
	@Test
	public void testStartandStopMulticastListenerOnlyNonBlocking() {
		try {
			controler.setPeriodicSenderEnabled(false);
			controler.setDiscoverSenderEnabled(false);
			controler.setMulticastListenerEnabled(true);
			
			controler.getDiscoverSender().setDelay(300000);
			controler.getPeriodicMessageSender().setDelay(300000);
			controler.getMulticastListener().setTimeout(300000);
			
			controler.start();
		} catch (Exception e) {
			
		}
		
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			latch.await(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());
		assertEquals(State.STARTED, controler.getMulticastListener().getState());
		
		try {
			controler.stop();
		} catch (Exception e) {
			
		}
		
		latch = new CountDownLatch(1);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());

		// if in blocking it won't be stopped until a socket message is received. 
		// assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
	}
	
	@Test
	public void testStartandStopMulticastListenerOnlyBlocking() {
		try {
			controler.setPeriodicSenderEnabled(false);
			controler.setDiscoverSenderEnabled(false);
			controler.setMulticastListenerEnabled(true);
			
			controler.getDiscoverSender().setDelay(300000);
			controler.getPeriodicMessageSender().setDelay(300000);
			controler.getMulticastListener().setTimeout(300000);
			controler.getMulticastListener().setBlocking(false);
			
			controler.start();
		} catch (Exception e) {
			
		}
		
		CountDownLatch latch = new CountDownLatch(1);
		
		try {
			latch.await(15, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());
		assertEquals(State.STARTED, controler.getMulticastListener().getState());
		
		try {
			controler.stop();
		} catch (Exception e) {
			
		}
		
		latch = new CountDownLatch(1);
		
		try {
			latch.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			
		}
		
		assertEquals(State.STOPPED, controler.getDiscoverSender().getState());
		assertEquals(State.STOPPED, controler.getPeriodicMessageSender().getState());

		// if in blocking it won't be stopped until a socket message is received. 
		 assertEquals(State.STOPPED, controler.getMulticastListener().getState());
		
	}
	
	@Test
	public void testProcessSSDPMessageISSDPMessage() {
		final CountDownLatch aliveLatch = new CountDownLatch(2);
		final CountDownLatch byebyeLatch = new CountDownLatch(2);
		final CountDownLatch discoverLatch = new CountDownLatch(2);
		final CountDownLatch discoverResponseLatch = new CountDownLatch(2);
		final CountDownLatch updateLatch = new CountDownLatch(2);
		
		ISSDPMessageHandler listener = new SSDPDefaultMessageHandler(){
			@Override
			public void processSSDPAliveMessage(AliveMessage ssdpMessage)
					throws Exception {
				aliveLatch.countDown();
			}
			@Override
			public void processSSDPByeByeMessage(ByeByeMessage ssdpMessage)
					throws Exception {
				byebyeLatch.countDown();
			}
			@Override
			public void processSSDPDiscoverMessage(String remoteAddr,
					int remotePort, DiscoverMessage ssdpMessage)
					throws Exception {
				discoverLatch.countDown();
			}
			@Override
			public void processSSDPDiscoverResponseMessage(
					DiscoverResponseMessage ssdpMessage) throws Exception {
				discoverResponseLatch.countDown();
			}
			@Override
			public void processSSDPUpdateMessage(UpdateMessage ssdpMessage)
					throws Exception {
				updateLatch.countDown();
			}};
		
		
		assertNotNull(controler.getMessageHandlerList());
			
		controler.addMessageHandler(listener);
		controler.addMessageHandler(null);
		
		assertTrue(controler.getMessageHandlerList().size()==1);

		
		// processing messages
		try {
			controler.processSSDPMessage(null);
			controler.processSSDPMessage(aliveMessage);
			controler.processSSDPMessage(byebyeMessage);
			controler.processSSDPMessage(updateMessage);
			controler.processSSDPMessage(discoverMessage);
			controler.processSSDPMessage(discoverResponseMessage);
			
			controler.processSSDPMessage(null, -1, aliveMessage);
			controler.processSSDPMessage(null, -1, byebyeMessage);
			controler.processSSDPMessage(null, -1, updateMessage);
			controler.processSSDPMessage(null, -1, discoverMessage);
			controler.processSSDPMessage(null, -1, discoverResponseMessage);
			
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}
		
		assertEquals(0, aliveLatch.getCount());
		assertEquals(0, byebyeLatch.getCount());
		assertEquals(0, discoverLatch.getCount());
		assertEquals(0, discoverResponseLatch.getCount());
		assertEquals(0, updateLatch.getCount());
		
		
		try {
			controler.processSSDPMessage(new ISSDPMessage(){@Override
				public String toString() {
					return "Custom ISSDPMessage";
				}} );
			controler.processSSDPMessage(new AbstractSSDPNotifyMessage() {
				
				@Override
				public String toString() {
					return "";
				}
				
				@Override
				public String getNts() {
					return "";
				}
			});
			
			
			controler.processSSDPMessage(null, -1, new ISSDPMessage(){@Override
				public String toString() {
					return "Custom ISSDPMessage";
				}} );
			controler.processSSDPMessage(null, -1, new AbstractSSDPNotifyMessage() {
				
				@Override
				public String toString() {
					return "";
				}
				
				@Override
				public String getNts() {
					return "";
				}
			});
		} catch (Exception e) {
			fail("Shouldn't throw Exception");
		}

	}

	public static String createByeByeMessage(){
		
		/*
		NOTIFY * HTTP/1.1
		HOST: 239.255.255.250:1900
		NTS: ssdp:byebye
		USN: uuid:0b1f697a-a0fa-5181-010f-8edcc5a1a3e8::upnp:rootdevice
		NT: upnp:rootdevice
		CONTENT-LENGTH: 0
		*/
		StringBuffer sb = new StringBuffer();
		
		sb.append("NOTIFY * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250:1900").append("\n");
		sb.append("NTS: ssdp:byebye").append("\n");
		sb.append("USN: uuid:0b1f697a-a0fa-5181-010f-8edcc5a1a3e8::upnp:rootdevice").append("\n");
		sb.append("NT: upnp:rootdevice").append("\n");
		sb.append("CONTENT-LENGTH: 0").append("\n");
		
		return sb.toString();
	}

	public static String createDiscoverMessage(){
		
		/*
		M-SEARCH * HTTP/1.1
		HOST: 239.255.255.250:1900
		ST: urn:schemas-upnp-org:device:MediaServer:1
		MAN: "ssdp:discover"
		MX: 2
		X-AV-Client-Info: av=5.0; cn="Sony Computer Entertainment Inc."; mn="PLAYSTATION 3"; mv="1.0";
		*/
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("M-SEARCH * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250:1900").append("\n");
		sb.append("ST: urn:schemas-upnp-org:device:MediaServer:1").append("\n");
		sb.append("MAN: \"ssdp:discover\"").append("\n");
		sb.append("MX: 2").append("\n");
		sb.append("X-AV-Client-Info: av=5.0; cn=\"Sony Computer Entertainment Inc.\"; mn=\"PLAYSTATION 3\"; mv=\"1.0\";").append("\n");
		
		return sb.toString();
	}

	public static String createDiscoverResponseMessage(){
		
		/*
		HTTP/1.1 200 OK
		CACHE-CONTROL: max-age=1200
		DATE: Tue, 05 May 2009 13:31:51 GMT
		LOCATION: http://142.225.35.55:5001/description/fetch
		SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11
		ST: upnp:rootdevice
		EXT: 
		USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::upnp:rootdevice
		Content-Length: 0
	 */
		StringBuffer sb = new StringBuffer();
		
		sb.append("HTTP/1.1 200 OK").append("\n");
		sb.append("CACHE-CONTROL: max-age=1200").append("\n");
		sb.append("DATE: Tue, 05 May 2009 13:31:51 GMT").append("\n");
		sb.append("LOCATION: http://142.225.35.55:5001/description/fetch").append("\n");
		sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
		sb.append("ST: upnp:rootdevice").append("\n");
		sb.append("EXT: ").append("\n");
		sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::upnp:rootdevice").append("\n");
		sb.append("Content-Length: 0").append("\n");
		
		return sb.toString();
	}

	public static String createUpdateMessage(){
		/*
		NOTIFY * HTTP/1.1
		HOST: 239.255.255.250:1900
		NT: urn:schemas-upnp-org:service:ContentDirectory:1
		NTS: ssdp:update
		LOCATION: http://142.225.35.55:5001/description/fetch
		USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1
		CACHE-CONTROL: max-age=1800
		SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11
		 */
		StringBuffer sb = new StringBuffer();
		
		sb.append("NOTIFY * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250:1900").append("\n");
		sb.append("NT: urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("NTS: ssdp:update").append("\n");
		sb.append("LOCATION: http://142.225.35.55:5001/description/fetch").append("\n");
		sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("CACHE-CONTROL: max-age=1800").append("\n");
		sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
		
		return sb.toString();
	}

	public static String createAliveMessage(){
		/*
		NOTIFY * HTTP/1.1
		HOST: 239.255.255.250:1900
		NT: urn:schemas-upnp-org:service:ContentDirectory:1
		NTS: ssdp:alive
		LOCATION: http://142.225.35.55:5001/description/fetch
		USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1
		CACHE-CONTROL: max-age=1800
		SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11
		 */
		StringBuffer sb = new StringBuffer();
		
		sb.append("NOTIFY * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250:1900").append("\n");
		sb.append("NT: urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("NTS: ssdp:alive").append("\n");
		sb.append("LOCATION: http://142.225.35.55:5001/description/fetch").append("\n");
		sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("CACHE-CONTROL: max-age=1800").append("\n");
		sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
		
		
		return sb.toString();
	}
	
	

}
