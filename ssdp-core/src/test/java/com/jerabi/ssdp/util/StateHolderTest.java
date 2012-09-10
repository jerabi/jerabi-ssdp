package com.jerabi.ssdp.util;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jerabi.ssdp.util.IStateListener;
import com.jerabi.ssdp.util.State;
import com.jerabi.ssdp.util.StateHolder;

public class StateHolderTest {

	private static StateHolder<State> stateHolder = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		stateHolder = new StateHolder<State>();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testStateHolderChangeStates() {

		assertNull(stateHolder.getState());

		stateHolder.setState(State.STARTED);
		assertEquals(State.STARTED, stateHolder.getState());

		stateHolder.setState(State.STARTED);
		assertEquals(State.STARTED, stateHolder.getState());

		stateHolder.setState(State.STOPPED);
		assertEquals(State.STOPPED, stateHolder.getState());

		stateHolder.setState(State.SUSPENDED);
		assertEquals(State.SUSPENDED, stateHolder.getState());

		final CountDownLatch latch = new CountDownLatch(2);
		
		IStateListener<State> listener = new IStateListener<State>() {
			
			public void notifyStateChange(State state) {
				latch.countDown();
			}
		};
		
		stateHolder.addStateListener(listener);
		
		stateHolder.setState(State.STOPPED);
		stateHolder.setState(State.STARTED);
		
		assertEquals(0, latch.getCount());
		
		stateHolder.removeStateListener(listener);
		
		stateHolder.setState(State.SLEEP);
		
		stateHolder.addStateListener(null);
		stateHolder.removeStateListener(null);

	}

}
