package com.jerabi.ssdp.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.util.UUIDGenerator;

public class UUIDGeneratorTest {

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
	public void testGetUUID() {
		
		Map<String,String> map = new HashMap<String,String>();
		
		for(int i=0;i<100;i++){
			String uuid = UUIDGenerator.getUUID();
			if(map.containsKey(uuid)){
				fail("UUID already exist");
			}
			map.put(uuid, uuid);
		}
		
		
	}
	
	@Test
	public void testGetUUIDWithMAC() {
		
		String initialUUID = null;
		
		try {
			initialUUID = UUIDGenerator.getUUID(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
		} catch (Exception e) {
			fail("Should not throws Exception");
		}
		
		for(int i=0;i<100;i++){
			String uuid = null;
			try {
				uuid = UUIDGenerator.getUUID(NetworkInterface.getByInetAddress(InetAddress.getLocalHost()));
			} catch (Exception e) {
				fail("Should not throws Exception");
			}
			
			assertNotNull(uuid);
			assertEquals(initialUUID, uuid);
		}
		
	}
	
	@Test
	public void testGetUUIDWithNullNetworkInterface() {
		
		String initialUUID = null;
		
		try {
			initialUUID = UUIDGenerator.getUUID(null);
		} catch (Exception e) {
			fail("Should not throws Exception");
		}
		
		assertNull(initialUUID);
		
	}

}
