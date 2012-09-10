package com.jerabi.ssdp.message.helper;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.message.AbstractSSDPNotifyMessage;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.UpdateMessage;
import com.jerabi.ssdp.message.helper.SSDPMessageHelper;
import com.jerabi.ssdp.util.SSDPContants;

public class SSDPMessageHelperTest {

	private static String aliveMessage = null;
	private static String byeByeMessage = null;
	private static String discoverMessage = null;
	private static String discoverResponseMessage = null;
	private static String updateMessage = null;
	
	
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
	
	@BeforeClass
	public static void setUp() throws Exception {
		
		aliveMessage = createAliveMessage();
		byeByeMessage = createByeByeMessage();
		discoverMessage = createDiscoverMessage();
		discoverResponseMessage = createDiscoverResponseMessage();
		updateMessage = createUpdateMessage();
		
	}

	@Test
	public void testGetSSDPMessageEmpty() {
		Assert.assertNull(SSDPMessageHelper.getSSDPMessage(null));
		Assert.assertNull(SSDPMessageHelper.getSSDPMessage(""));
	}
	
	@Test
	public void testGetSSDPMessageInvalid() {
		Assert.assertNull(SSDPMessageHelper.getSSDPMessage("_"));
		
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("NOTIFY * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250:1900").append("\n");
		sb.append("NT: urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("NTS: unknown").append("\n");
		sb.append("LOCATION: http://142.225.35.55:5001/description/fetch").append("\n");
		sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("CACHE-CONTROL: max-age=1800").append("\n");
		sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
		
		Assert.assertNull(SSDPMessageHelper.getSSDPMessage("NOTIFY * HTTP/1.1"));
		
		Assert.assertNull(SSDPMessageHelper.getSSDPMessage(sb.toString()));
	}
	
	@Test
	public void testGetSSDPMessageNoPort() {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("NOTIFY * HTTP/1.1").append("\n");
		sb.append("HOST: 239.255.255.250").append("\n");
		sb.append("NT: urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("NTS: ssdp:alive").append("\n");
		sb.append("LOCATION: http://142.225.35.55/description/fetch").append("\n");
		sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1").append("\n");
		sb.append("CACHE-CONTROL: max-age=1800").append("\n");
		sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
		
		
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(sb.toString());
		
		assertNotNull(message);
		assertTrue(message instanceof AliveMessage);
		
		AliveMessage msg = (AliveMessage)message;
		
		assertEquals("1900", msg.getPort());
		assertEquals("http://142.225.35.55/description/fetch", msg.getLocation());
	}
	
	
	@Test
	public void testGetSSDPMessageAliveMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(aliveMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof AliveMessage);
		
		AliveMessage msg = (AliveMessage)message;
		
		assertEquals("max-age=1800", msg.getCacheControl());
		assertEquals("239.255.255.250", msg.getHost());
		assertEquals("http://142.225.35.55:5001/description/fetch", msg.getLocation());
		assertEquals("NOTIFY * HTTP/1.1", msg.getNotify());
		assertEquals("urn:schemas-upnp-org:service:ContentDirectory:1", msg.getNt());
		assertEquals("ssdp:alive", msg.getNts());
		assertEquals("1900", msg.getPort());
		assertEquals("Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11", msg.getServer());
		assertEquals("uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1", msg.getUsn());
		
		assertEquals(0, msg.getAttributes().size());
		assertEquals(aliveMessage, msg.getMessage());
		
	}
	
	@Test
	public void testGetSSDPMessageByeByeMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(byeByeMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof ByeByeMessage);
		
		ByeByeMessage msg = (ByeByeMessage)message;
		
		assertEquals("0", msg.getContentLength());
		assertEquals("239.255.255.250", msg.getHost());
		assertEquals("NOTIFY * HTTP/1.1", msg.getNotify());
		assertEquals("upnp:rootdevice", msg.getNt());
		assertEquals("ssdp:byebye", msg.getNts());
		assertEquals("1900", msg.getPort());
		assertEquals("uuid:0b1f697a-a0fa-5181-010f-8edcc5a1a3e8::upnp:rootdevice", msg.getUsn());
		
		assertEquals(0, msg.getAttributes().size());
		assertEquals(byeByeMessage, msg.getMessage());
		
	}
	
	@Test
	public void testGetSSDPMessageUnkownMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(byeByeMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof ByeByeMessage);
		
		ByeByeMessage msg = (ByeByeMessage)message;
		msg.setNts("Unknown");
		
		assertEquals("0", msg.getContentLength());
		assertEquals("239.255.255.250", msg.getHost());
		assertEquals("NOTIFY * HTTP/1.1", msg.getNotify());
		assertEquals("upnp:rootdevice", msg.getNt());
		assertEquals("ssdp:byebye", msg.getNts());
		assertEquals("1900", msg.getPort());
		assertEquals("uuid:0b1f697a-a0fa-5181-010f-8edcc5a1a3e8::upnp:rootdevice", msg.getUsn());
		
		assertEquals(0, msg.getAttributes().size());
		assertEquals(byeByeMessage, msg.getMessage());
		
	}
	
	@Test
	public void testGetSSDPMessageUpdateMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(updateMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof UpdateMessage);
		
		UpdateMessage msg = (UpdateMessage)message;
		
		assertEquals("max-age=1800", msg.getCacheControl());
		assertEquals("239.255.255.250", msg.getHost());
		assertEquals("http://142.225.35.55:5001/description/fetch", msg.getLocation());
		assertEquals("NOTIFY * HTTP/1.1", msg.getNotify());
		assertEquals("urn:schemas-upnp-org:service:ContentDirectory:1", msg.getNt());
		assertEquals("ssdp:update", msg.getNts());
		assertEquals("1900", msg.getPort());
		assertEquals("Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11", msg.getServer());
		assertEquals("uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1", msg.getUsn());
		
		assertEquals(0, msg.getAttributes().size());
		assertEquals(updateMessage, msg.getMessage());
		
	}
	
	@Test
	public void testGetSSDPMessageDiscoverMessageMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(discoverMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof DiscoverMessage);
		
		DiscoverMessage msg = (DiscoverMessage)message;
		
		assertEquals("239.255.255.250", msg.getHost());
		assertEquals("urn:schemas-upnp-org:device:MediaServer:1", msg.getSt());
		assertEquals("2", msg.getMx());
		assertEquals("1900", msg.getPort());
		
		assertEquals(1, msg.getAttributes().size());
		assertEquals(discoverMessage, msg.getMessage());
		
		// check if the attribute if found 
		assertTrue(msg.toString().indexOf(msg.getAttributes().get(0))>-1);
		
		msg.setPort(null);
		assertNull(msg.getPort());
		
		// check if default port is returned
		assertTrue(msg.toString().indexOf(SSDPContants.HOST + " " + msg.getHost() + ":" + SSDPContants.DEFAULT_PORT)>-1);
		
	}
	
	@Test
	public void testGetSSDPMessageDiscoverResponseMessage(){
		ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(discoverResponseMessage);
		
		assertNotNull(message);
		assertTrue(message instanceof DiscoverResponseMessage);
		
		DiscoverResponseMessage msg = (DiscoverResponseMessage)message;
		
		assertEquals("max-age=1200", msg.getCacheControl());
		assertEquals("Tue, 05 May 2009 13:31:51 GMT", msg.getDate());
		assertEquals("http://142.225.35.55:5001/description/fetch", msg.getLocation());
		assertEquals("HTTP/1.1 200 OK", msg.getNotify());
		assertEquals("upnp:rootdevice", msg.getSt());
		assertEquals("", msg.getExt());
		assertEquals("Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11", msg.getServer());
		assertEquals("uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::upnp:rootdevice", msg.getUsn());
		assertEquals("0", msg.getContentLength());
		
		assertEquals(0, msg.getAttributes().size());
		assertEquals(discoverResponseMessage, msg.getMessage());
		
	}

}
