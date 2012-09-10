package com.jerabi.ssdp.network;

import java.lang.reflect.InvocationTargetException;


/**
 * 
 * Provides API for sending and receiving messages over network.
 * 
 * This class is a dummy that doesn't implements any methods.
 * It used for compilation purpose.  An implementation must be
 * available in another package. 
 * 
 * @author Sebastien Dionne
 *
 */
public class SSDPNetworkFactory {
	
	private static final String DEFAULT_IMPL = "com.jerabi.ssdp.network.impl.SSDPNetworkImpl";
	private static ISSDPNetwork instance = null;
	
	/**
	 * Private Constructor.  You need to use getInstance()
	 * @see #getInstance()
	 */
	private SSDPNetworkFactory(){}
	
	/**
	 * Returns a ISSDPNetwork instance.
	 * 
	 * @return ISSDPNetwork
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 */
	public static ISSDPNetwork getInstance() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		
		if(instance==null){
			instance = (ISSDPNetwork) Class.forName(DEFAULT_IMPL).newInstance();
		}
		
		return instance;
		
	}
	

}
