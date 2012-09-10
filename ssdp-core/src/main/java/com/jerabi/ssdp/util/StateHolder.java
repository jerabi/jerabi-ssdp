package com.jerabi.ssdp.util;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that will hold a State {@link State}.
 * 
 * Uses {@link AtomicReference} with {@link ReentrantReadWriteLock}.
 * 
 * Notify listener when the state changes.
 * 
 * @see State
 * 
 * @author Sebastien Dionne
 * @param <E> State
 */
public class StateHolder<E> {
	private AtomicReference<E> state;
    
    private ReentrantReadWriteLock readWriteLock;
    private List<IStateListener<E>> stateListenerList = null;
    
    /**
     * Instantiates a new state holder.
     */
    public StateHolder(){
    	 state = new AtomicReference<E>();
         readWriteLock = new ReentrantReadWriteLock();
         stateListenerList = new LinkedList<IStateListener<E>>();
    }

    /**
     * Adds the state listener.
     * 
     * @param listener the listener
     */
    public void addStateListener(IStateListener<E> listener){
    	if(listener!=null){
    		stateListenerList.add(listener);
    	}
    }
    
    /**
     * Removes the state listener.
     * 
     * @param listener the listener
     */
    public void removeStateListener(IStateListener<E> listener){
    	if(listener!=null){
    		stateListenerList.remove(listener);
    	}
    }
    
	/**
	 * Gets the state.
	 * If the state is not setted, null value will be return
	 * 
	 * @return the state
	 * @see State
	 */
	public E getState() {
		readWriteLock.readLock().lock();
		
		E currentState = state.get();
		
		readWriteLock.readLock().unlock();
		
		return currentState;
	}

	/**
	 * Sets the state and notify all registered listeners.
	 * Used a {@link ReentrantReadWriteLock}
	 * 
	 * @param state the new state
	 * @see State
	 */
	public void setState(E state) {
		readWriteLock.writeLock().lock();
		
		this.state.set(state);
		
		// downgrading lock to read
		readWriteLock.readLock().lock();
		readWriteLock.writeLock().unlock();
		
		// notifyListener
		notifyListeners(state);
		
		// release lock
		readWriteLock.readLock().unlock();
	}
    
	/**
	 * Notify listeners.
	 * 
	 * @param state the state
	 */
	protected void notifyListeners(E state){
		for (IStateListener<E> listener : stateListenerList) {
			listener.notifyStateChange(state);
		}
	}
    
}
