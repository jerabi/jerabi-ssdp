package com.jerabi.ssdp.message;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.USNInfo;

public class ServiceInfoTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testServiceInfo() {
		
		String host = "host";
		int port = 10;
		String nt = "nt";
		String location = "http://location:xxxx/path";
		USNInfo usn = new USNInfo("uuid", "urn");
		
		ServiceInfo serviceInfo = new ServiceInfo(host, port, nt, location, usn);
		
		assertEquals(host, serviceInfo.getHost());
		assertEquals(port, serviceInfo.getPort());
		assertEquals(nt, serviceInfo.getNt());
		assertEquals(location, serviceInfo.getLocation());
		assertEquals("/path", serviceInfo.getLocationPath());
		assertEquals(usn, serviceInfo.getUsn());
		
		ServiceInfo serviceInfo2 = new ServiceInfo();
		
		serviceInfo2.setHost(serviceInfo.getHost());
		serviceInfo2.setLocation(serviceInfo.getLocation());
		assertEquals("/path", serviceInfo2.getLocationPath());
		serviceInfo2.setNt(serviceInfo.getNt());
		serviceInfo2.setPort(serviceInfo.getPort());
		serviceInfo2.setUsn(usn);
		
		assertEquals(serviceInfo, serviceInfo2);
		
		assertNotNull(serviceInfo.toString());
		
	}
	
	@Test
	public void testLocationPath() {
		ServiceInfo serviceInfo = new ServiceInfo();
		
		assertNull(serviceInfo.getLocationPath());
		
		serviceInfo.setLocation("/path");
		assertNull(serviceInfo.getLocationPath());
		
		serviceInfo.setLocation("http://localhost:1111/path");
		assertEquals("/path", serviceInfo.getLocationPath());
		
		serviceInfo.setLocation("http://localhost/path");
		assertEquals("/path", serviceInfo.getLocationPath());
		
		serviceInfo.setLocation("http://localhost");
		assertEquals("", serviceInfo.getLocationPath());
		
		serviceInfo.setLocation("http://localhost/");
		assertEquals("/", serviceInfo.getLocationPath());
		
	}
	
	@Test
	public void testEquals() {
		String host = "host";
		int port = 10;
		String nt = "nt";
		String location = "http://location:xxxx/path";
		USNInfo usn = new USNInfo("uuid", "urn");
		
		try {
			
			ServiceInfo serviceInfo1 = new ServiceInfo(host, port, nt, location, usn);
			ServiceInfo serviceInfo2 = new ServiceInfo(host, port, nt, location, usn);
			ServiceInfo serviceInfo3 = new ServiceInfo();
			
			assertEquals(serviceInfo1, serviceInfo1);
			assertEquals(serviceInfo1, serviceInfo2);
			assertEquals(serviceInfo2, serviceInfo1);
			
			assertTrue(serviceInfo3.equals(new ServiceInfo(null, 0, null, null, null)));
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void testNotEquals() {
		String host = "host";
		int port = 10;
		String nt = "nt";
		String location = "http://location:xxxx/path";
		USNInfo usn = new USNInfo("uuid", "urn");
		
		try {
			ServiceInfo serviceInfo1 = new ServiceInfo(host, port, nt, location, usn);
			ServiceInfo serviceInfo2 = new ServiceInfo();
			
			assertFalse(serviceInfo1.equals(null));
			assertFalse(serviceInfo2.equals("string"));
			
			ServiceInfo serviceInfo3 = new ServiceInfo();
			assertFalse(serviceInfo3.equals(serviceInfo1));
			
			assertFalse(serviceInfo1.equals(serviceInfo3));
			
			assertFalse(serviceInfo2.equals(new ServiceInfo(host, 0, null, null, null)));
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, 0, null, "", null)));
			
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, 0, null, null, usn)));
			
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, 0, "", null, null)));
			
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, port, null, null, null)));
			assertFalse(serviceInfo1.equals(new ServiceInfo(host, port, "", null, null)));
			assertFalse(serviceInfo1.equals(new ServiceInfo(host, port, nt, location, null)));
			assertFalse(serviceInfo1.equals(new ServiceInfo(host, port, null, location, null)));
			
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, port, "", null, usn)));
			assertFalse(serviceInfo2.equals(new ServiceInfo(null, port, "", null, new USNInfo("uuid1", "urn1"))));
			
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCompare() {
		String host = "host";
		int port = 10;
		String nt = "nt";
		String location = "http://location:xxxx/path";
		USNInfo usn = new USNInfo("uuid", "urn");
		
		try {
			ServiceInfo serviceInfo1 = new ServiceInfo(host+1, port, nt, location, usn);
			ServiceInfo serviceInfo2 = new ServiceInfo(host+4, port, nt, location, usn);
			ServiceInfo serviceInfo3 = new ServiceInfo(host+3, port, nt, location, usn);
			ServiceInfo serviceInfo4 = new ServiceInfo(host+2, port, nt, location, usn);
			ServiceInfo serviceInfo5 = new ServiceInfo(host+0, port, nt, location, usn);
			
			Map<ServiceInfo,ServiceInfo> map = new TreeMap<ServiceInfo,ServiceInfo>();
			
			map.put(serviceInfo1, serviceInfo1);
			map.put(serviceInfo2, serviceInfo2);
			map.put(serviceInfo3, serviceInfo3);
			map.put(serviceInfo4, serviceInfo4);
			map.put(serviceInfo5, serviceInfo5);
			
			// the order should be 5,1,4,3,2
			int i=0;
			for (ServiceInfo item : map.values()) {
				
				switch (i) {
					case 0:
						assertEquals(serviceInfo5, item);
						break;
					case 1:
						assertEquals(serviceInfo1, item);
						break;
					case 2:
						assertEquals(serviceInfo4, item);
						break;
					case 3:
						assertEquals(serviceInfo3, item);
						break;
					case 4:
						assertEquals(serviceInfo2, item);
						break;
				}
				i++;
			}
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testHashCode() {
		String host = "host";
		int port = 10;
		String nt = "nt";
		String location = "http://location:xxxx/path";
		USNInfo usn = new USNInfo("uuid", "urn");
		
		try {
			ServiceInfo serviceInfo1 = new ServiceInfo(host, port, nt, location, usn);
			ServiceInfo serviceInfo2 = new ServiceInfo(host+2, port, nt, location, usn);
			ServiceInfo serviceInfo3 = new ServiceInfo();
			
			Map<ServiceInfo,ServiceInfo> map = new HashMap<ServiceInfo,ServiceInfo>();
			
			map.put(serviceInfo1, serviceInfo1);
			map.put(serviceInfo1, serviceInfo1);
			map.put(serviceInfo1, serviceInfo1);
			map.put(serviceInfo2, serviceInfo2);
			map.put(serviceInfo3, serviceInfo3);
			
			assertEquals(3, map.size());
			
		} catch (Exception e) {
			fail();
		}
	}

}
