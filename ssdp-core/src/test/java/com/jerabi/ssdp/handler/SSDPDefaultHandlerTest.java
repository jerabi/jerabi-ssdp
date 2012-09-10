package com.jerabi.ssdp.handler;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.ISSDPControler;
import com.jerabi.ssdp.SSDPControler;
import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.USNInfo;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;
import com.jerabi.ssdp.util.SSDPContants;

public class SSDPDefaultHandlerTest {

	private ServiceInfo deviceInfo = new ServiceInfo("localhost",1999,"nt","location", new USNInfo("uuid", "urn"));
	
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
	public void testSetSSDPControler() throws Exception{
		ISSDPControler controler = new SSDPControler(){
			@Override
			public void processSSDPMessage(ISSDPMessage message)
					throws Exception {
			}
		};
		
		SSDPDefaultResponseHandler handler = new SSDPDefaultResponseHandler(null);
		
		assertNull(handler.getSSDPControler());
		
		handler.setSSDPControler(controler);
		
		assertEquals(controler, handler.getSSDPControler());
	}
	
	@Test
	public void testHandleMessage() throws Exception {
		
		final CountDownLatch countDown = new CountDownLatch(5);
		
		ISSDPControler controler = new SSDPControler(){
			@Override
			public void processSSDPMessage(ISSDPMessage message)
					throws Exception {
				countDown.countDown();
			}
		};
		
		SSDPDefaultResponseHandler handler = new SSDPDefaultResponseHandler(controler);
		
		try {
			handler.handle(SSDPMessageHelper.createSSDPAliveMessage(deviceInfo).toString());
			handler.handle(SSDPMessageHelper.createSSDPByeByeMessage(deviceInfo).toString());
			handler.handle(SSDPMessageHelper.createSSDPUpdateMessage(deviceInfo).toString());
			handler.handle(SSDPMessageHelper.createSSDPDiscoverMessage(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, 3, "upnp:rootdevice", null).toString());
			
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
			
			handler.handle( SSDPMessageHelper.getSSDPMessage(sb.toString()).toString());
			
			
			handler.handle(null, -1, SSDPMessageHelper.createSSDPAliveMessage(deviceInfo).toString());
			
			handler.setSSDPControler(null);
			handler.handle(SSDPMessageHelper.createSSDPAliveMessage(deviceInfo).toString());
			
			
			
		} catch (Exception e) {
			fail("Should not throws Exception");
		}
		
		assertEquals(0, countDown.getCount());
		
	}

}
