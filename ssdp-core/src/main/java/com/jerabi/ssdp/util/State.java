package com.jerabi.ssdp.util;

/**
 * Defines available SSDP states.
 * 
 * @author Sebastien Dionne
 *
 */
public enum State {
    /**
     * Used when the process is stopped
     */
    STOPPED, 
    /**
     * Used when the process is started
     */
    STARTED, 
    /**
     * Used when the process is sleeping
     */
    SLEEP, 
    /**
     * Used when the process is supended
     */
    SUSPENDED
}
