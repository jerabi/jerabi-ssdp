package com.jerabi.ssdp.listener;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.*;

import com.jerabi.ssdp.handler.SSDPDefaultResponseHandler;
import com.jerabi.ssdp.listener.SSDPMulticastListener;
import com.jerabi.ssdp.network.IMulticastListener;
import com.jerabi.ssdp.network.SSDPNetworkFactory;
import com.jerabi.ssdp.network.impl.SSDPNetworkImpl;
import com.jerabi.ssdp.util.SSDPContants;
import com.jerabi.ssdp.util.State;

public class SSDPMulticastListenerTest {

	private static SSDPMulticastListener listener = null;
	private static Thread t = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		listener = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY);
		t = new Thread(listener);
	}

	@After
	public void tearDown() throws Exception {
		if (t != null) {
			try {
				t.interrupt();
			} catch (Exception e) {
			}
		}

	}

	private void sendUDP(String msg) throws IOException {
		DatagramSocket ssdpUniSock = new DatagramSocket(null);

		ssdpUniSock.setReuseAddress(true);
		
		//InetAddress inetAddr = InetAddress.getByAddress("239.255.255.250".getBytes());
		InetAddress inetAddr = InetAddress.getByName("239.255.255.250");

		DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.length(), inetAddr, 1900);
		
		ssdpUniSock.send(dgmPacket);
		ssdpUniSock.close();
	}

	@Test
	public void testSSDPMulticastSetter() throws IOException {
		listener.setSSDPResponseHandler(null);
		
		assertNull(listener.getSSDPResponseHandler());
		assertEquals(SSDPContants.DEFAULT_IP, listener.getHost());
		assertEquals(SSDPContants.DEFAULT_PORT, listener.getPort());
		assertEquals(SSDPContants.DEFAULT_DELAY, listener.getTimeout());
		
		listener.setHost("127.0.0.1");
		listener.setPort(12);
		listener.setTimeout(1234);
		listener.setBlocking(false);
		
		assertEquals("127.0.0.1", listener.getHost());
		assertEquals(12, listener.getPort());
		assertEquals(1234, listener.getTimeout());
		assertEquals(false, listener.getBlocking());
		
		listener.setBlocking(true);
		assertEquals(true, listener.getBlocking());
		
		try {
	        IMulticastListener listener2 = SSDPNetworkFactory.getInstance().createMulticastListener(11, null);
	        listener2.setTimeout(1122);
	        assertEquals(1122, listener2.getTimeout());
        } catch (Exception e) {
	        fail("Shouldn't throw Exception");
        }
	}
	
	@Test
	public void testSSDPMulticastInvalidHost() throws IOException {
		listener.setSSDPResponseHandler(new SSDPDefaultResponseHandler(null) {
			@Override
			public void handle(String message) throws Exception {
			}

			@Override
			public void handle(String remoteAddr, int remotePort, String message)
					throws Exception {
			}

		});
		
		assertNotNull(listener.getSSDPResponseHandler());

		listener.setHost("-1");
		
		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	@Test
	public void testSSDPMulticastListenerHelloMessage() throws IOException {

		final String msg = "Hello from SSPDMulticastListenerTest";
		final CountDownLatch countDown = new CountDownLatch(2);
		
		listener.setSSDPResponseHandler(null);
		
		assertNull(listener.getSSDPResponseHandler());
		assertEquals(SSDPContants.DEFAULT_IP, listener.getHost());
		assertEquals(SSDPContants.DEFAULT_PORT, listener.getPort());
		assertEquals(SSDPContants.DEFAULT_DELAY, listener.getTimeout());
		
		

		listener.setSSDPResponseHandler(new SSDPDefaultResponseHandler(null) {
			@Override
			public void handle(String message) throws Exception {
				if (msg.equals(message)) {
					countDown.countDown();
				}
			}

			@Override
			public void handle(String remoteAddr, int remotePort, String message)
					throws Exception {
				if (msg.equals(message)) {
					countDown.countDown();
				}
			}

		});
		
		assertNotNull(listener.getSSDPResponseHandler());

		t.start();

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		sendUDP(msg);
		sendUDP(msg);

		try {
			countDown.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t.interrupt();
		
		try {
			countDown.await(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(0, countDown.getCount());

	}
	
	@Test
	public void testSSDPMulticastListenerSSDPHandlerNull() throws IOException {

		final String msg = "Hello from SSPDMulticastListenerTest";
		final CountDownLatch countDown = new CountDownLatch(2);
		
		listener.setSSDPResponseHandler(null);

		assertNull(listener.getSSDPResponseHandler());

		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		sendUDP(msg);
		sendUDP(msg);

		try {
			countDown.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		assertEquals(2, countDown.getCount());

	}
	
	@Test
	public void testSSDPMulticastListenerInvalidHost() throws IOException {

		final String msg = "Hello from SSPDMulticastListenerTest";
		final CountDownLatch countDown = new CountDownLatch(2);
		
		listener.setHost(null);
		listener.setSSDPResponseHandler(null);

		assertNull(listener.getSSDPResponseHandler());

		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		sendUDP(msg);
		sendUDP(msg);

		try {
			countDown.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(2, countDown.getCount());

	}
	
	@Test
	public void testSSDPMulticastListenerIMulticastListenerNull() throws IOException {

		final String msg = "Hello from SSPDMulticastListenerTest";
		final CountDownLatch countDown = new CountDownLatch(2);
		
		listener.setSSDPResponseHandler(new SSDPDefaultResponseHandler(null) {
			@Override
			public void handle(String message) throws Exception {
				if (msg.equals(message)) {
					countDown.countDown();
				}
			}

			@Override
			public void handle(String remoteAddr, int remotePort, String message)
					throws Exception {
				if (msg.equals(message)) {
					countDown.countDown();
				}
			}

		});
		
		assertNotNull(listener.getSSDPResponseHandler());

		t.start();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		sendUDP(msg);
		sendUDP(msg);

		try {
			countDown.await(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(0, countDown.getCount());
		
		//fail();

	}
	
	@Test
	public void testSSDPMulticastListenerInterrupted() throws IOException {

		final CountDownLatch countDown = new CountDownLatch(2);
		
		SSDPMulticastListener listener2 = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY);
		listener2.setSSDPResponseHandler(null);
		listener2.setBlocking(false);
		listener2.setTimeout(10);
		
		Thread t2 = new Thread(listener2);
		
		t2.start();

		try {
			countDown.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t2.interrupt();
		
		try {
			countDown.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(State.STOPPED, listener2.getState());
		
	}
	
	@Test
	public void testSSDPMulticastListenerNetworkInterface() throws IOException {
		
		fail("Not yet implemented");
		
		List<SSDPMulticastListener> list = new ArrayList<SSDPMulticastListener>();
		
		while (NetworkInterface.getNetworkInterfaces().hasMoreElements()) {
	        NetworkInterface ni = (NetworkInterface) NetworkInterface.getNetworkInterfaces().nextElement();
	        
	        SSDPMulticastListener listenerTmp = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY);
	        
        }
		
		final CountDownLatch countDown = new CountDownLatch(2);
		
		SSDPMulticastListener listener2 = new SSDPMulticastListener(SSDPContants.DEFAULT_IP, SSDPContants.DEFAULT_PORT, SSDPContants.DEFAULT_DELAY);
		listener2.setSSDPResponseHandler(null);
		listener2.setBlocking(false);
		listener2.setTimeout(10);
		
		Thread t2 = new Thread(listener2);
		
		t2.start();

		try {
			countDown.await(3, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		t2.interrupt();
		
		try {
			countDown.await(5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		assertEquals(State.STOPPED, listener2.getState());
		
	}

}
