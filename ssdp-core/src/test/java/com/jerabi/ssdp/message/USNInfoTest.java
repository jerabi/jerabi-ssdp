package com.jerabi.ssdp.message;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import com.jerabi.ssdp.message.USNInfo;

public class USNInfoTest {

	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	@SuppressWarnings("unused")
	public void testUSNInfoNullUSN() {
		
		boolean exception = false;
		
		try {
			USNInfo usnInfo = new USNInfo(null);
		} catch (Exception e) {
			exception = true;
		}
		
		if(!exception){
			fail("Should had thrown Exception");
		}
	}

	@Test
	@SuppressWarnings("unused")
	public void testUSNInfoInvalidUSN() {
		
		boolean exception = false;
		
		try {
			USNInfo usnInfo = new USNInfo("");
		} catch (Exception e) {
			exception = true;
		}
		
		if(!exception){
			fail("Should had thrown Exception");
		}
		
		exception = false;
		
		try {
			USNInfo usnInfo = new USNInfo("uuid:abc:urn:def");
		} catch (Exception e) {
			exception = true;
		}
		
		if(!exception){
			fail("Should had thrown Exception");
		}
		
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testUSNInfoValidUSN() {
		
		String usn = "uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1";
		
		boolean exception = false;
		
		try {
			USNInfo usnInfo = new USNInfo(usn);
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
		
		exception = false;
		
		try {
			USNInfo usnInfo = new USNInfo("::");
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
		
		try {
			USNInfo usnInfo = new USNInfo("abc::");
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
		
		try {
			USNInfo usnInfo = new USNInfo("abc","def");
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
		
		try {
			USNInfo usnInfo = new USNInfo(null, null);
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
		
		try {
			USNInfo usnInfo = new USNInfo(null, null);
			usnInfo.setUuid("uuid1");
			usnInfo.setUrn("urn1");
			
			assertEquals("uuid:uuid1::urn1", usnInfo.toString());
		} catch (Exception e) {
			exception = true;
		}
		
		if(exception){
			fail();
		}
	}

	@Test
	public void testToString() {
		
		String usn = "uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1";
		
		
		try {
			USNInfo usnInfo = new USNInfo(usn);
			
			assertEquals(usn, usnInfo.toString());
			assertEquals("uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416", usnInfo.getUuid());
			assertEquals("urn:schemas-upnp-org:service:ContentDirectory:1", usnInfo.getUrn());
			
		} catch (Exception e) {
			fail();
		}
		
		try {
			USNInfo usnInfo = new USNInfo("uuid:uuid1", null);
			
			assertEquals("uuid:uuid1", usnInfo.toString());
			assertEquals("uuid:uuid1", usnInfo.getUuid());
			assertEquals(null, usnInfo.getUrn());
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void testEquals() {
		String usn1 = "uuid:uuid1::urn:urn1";
		String usn2 = "uuid:uuid1::urn:urn1";
		
		try {
			USNInfo usnInfo1 = new USNInfo(usn1);
			USNInfo usnInfo2 = new USNInfo(usn2);
			
			assertEquals(usnInfo1, usnInfo1);
			assertEquals(usnInfo1, usnInfo2);
			assertEquals(usnInfo2, usnInfo1);
			
			assertFalse(usnInfo1.equals(null));
			assertFalse(usnInfo1.equals("string"));
			
			USNInfo usnInfo3 = new USNInfo(null, "urn1");
			assertTrue(usnInfo3.equals(new USNInfo(null, "urn1")));
			
			
		} catch (Exception e) {
			fail();
		}
		
	}
	
	@Test
	public void testNotEquals() {
		String usn1 = "uuid:uuid1::urn:urn1";
		String usn2 = "uuid:uuid2::urn:urn2";
		
		try {
			USNInfo usnInfo1 = new USNInfo(usn1);
			USNInfo usnInfo2 = new USNInfo(usn2);
			
			assertFalse(usnInfo1.equals(usnInfo2));
			assertFalse(usnInfo2.equals(usnInfo1));
			
			USNInfo usnInfo3 = new USNInfo(null, null);
			assertFalse(usnInfo3.equals(usnInfo1));
			
			USNInfo usnInfo4 = new USNInfo(null, "urn");
			assertFalse(usnInfo4.equals(usnInfo1));
			
			USNInfo usnInfo5 = new USNInfo(null, "urn1");
			assertFalse(usnInfo5.equals(usnInfo1));
			
			USNInfo usnInfo6 = new USNInfo("uuid1", null);
			assertFalse(usnInfo6.equals(usnInfo1));
			
			USNInfo usnInfo7 = new USNInfo("uuid1", null);
			assertFalse(usnInfo7.equals(new USNInfo("uuid2", null)));
			
			USNInfo usnInfo8 = new USNInfo(null, "urn1");
			assertFalse(usnInfo8.equals(new USNInfo("uuid2", "urn1")));
			
			USNInfo usnInfo9 = new USNInfo(null, "urn1");
			assertFalse(usnInfo9.equals(new USNInfo(null, "urn2")));
			
			USNInfo usnInfo10 = new USNInfo(null, "urn1");
			assertFalse(usnInfo10.equals(new USNInfo(null, null)));
			
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCompare() {
		
		try {
			USNInfo usnInfo1 = new USNInfo("uuid:uuid1::urn:urn1");
			USNInfo usnInfo2 = new USNInfo("uuid:uuid3::urn:urn2");
			USNInfo usnInfo3 = new USNInfo("uuid:uuid2::urn:urn2");
			USNInfo usnInfo4 = new USNInfo("uuid:uuid2::urn:urn1");
			USNInfo usnInfo5 = new USNInfo("uuid:uuid1::urn:urn0");
			
			Map<USNInfo,USNInfo> map = new TreeMap<USNInfo,USNInfo>();
			
			map.put(usnInfo1, usnInfo1);
			map.put(usnInfo2, usnInfo2);
			map.put(usnInfo3, usnInfo3);
			map.put(usnInfo4, usnInfo4);
			map.put(usnInfo5, usnInfo5);
			
			// the order should be 5,1,4,3,2
			int i=0;
			for (USNInfo item : map.values()) {
				
				switch (i) {
					case 0:
						assertEquals(usnInfo5, item);
						break;
					case 1:
						assertEquals(usnInfo1, item);
						break;
					case 2:
						assertEquals(usnInfo4, item);
						break;
					case 3:
						assertEquals(usnInfo3, item);
						break;
					case 4:
						assertEquals(usnInfo2, item);
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
		
		try {
			USNInfo usnInfo1 = new USNInfo("uuid:uuid1::urn:urn1");
			USNInfo usnInfo2 = new USNInfo("uuid:uuid3::urn:urn2");
			USNInfo usnInfo3 = new USNInfo(null,null);
			
			Map<USNInfo,USNInfo> map = new HashMap<USNInfo,USNInfo>();
			
			map.put(usnInfo1, usnInfo1);
			map.put(usnInfo1, usnInfo1);
			map.put(usnInfo1, usnInfo1);
			map.put(usnInfo2, usnInfo2);
			map.put(usnInfo3, usnInfo3);
			
			assertEquals(3, map.size());
			
		} catch (Exception e) {
			fail();
		}
	}
	
}
