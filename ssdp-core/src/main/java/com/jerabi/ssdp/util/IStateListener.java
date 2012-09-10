package com.jerabi.ssdp.util;

/**
 * Provides the notification mechanism for state changes.
 * 
 * @author Sebastien Dionne
 *
 * @param <E> State
 */
public interface IStateListener<E> {

	/**
	 * Notify the listener that the state changed.
	 * 
	 * @param state the new current state
	 */
	void notifyStateChange(E state);
}
