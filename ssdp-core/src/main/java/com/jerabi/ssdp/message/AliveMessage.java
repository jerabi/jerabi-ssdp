package com.jerabi.ssdp.message;

import com.jerabi.ssdp.util.SSDPContants;

/**
 * 
 * Represents a ssdp:alive message.  This message is sent periodically as a keep-alive like and for broadcasting
 * the service that the device offers.  The server must sent this message for advertising his services. 
 * 
 * @author Sebastien Dionne
 * @example.
 *   <pre>
 *   NOTIFY * HTTP/1.1
 *   HOST: 239.255.255.250:1900
 *   NT: urn:schemas-upnp-org:service:ContentDirectory:1
 *   NTS: ssdp:alive
 *   LOCATION: http://142.225.35.55:5001/description/fetch
 *   USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1
 *   CACHE-CONTROL: max-age=1800
 *   SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11
 *   </pre>
 *   
 *   @see AbstractSSDPNotifyMessage
 */
public class AliveMessage extends AbstractSSDPNotifyMessage {
	
	/**
	 * First line of the message
	 */
	public static final String notify = "NOTIFY * HTTP/1.1";
	
	/**
	 * NTS value
	 */
	public static final String nts = SSDPContants.NTS_ALIVE;

	protected String cacheControl;
	protected String location;
	protected String server;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNts() {
		return nts;
	}
	
	/**
	 * Returns a duration for which the advertisement is valid
	 * @return cache-control value
	 */
	public String getCacheControl() {
		return cacheControl;
	}
	/**
	 * Sets the duration for which the advertisement is valid
	 * @param cacheControl
	 */
	public void setCacheControl(String cacheControl) {
		this.cacheControl = cacheControl;
	}
	
	/**
	 * Returns the URL for retrieving more information about the device.  
	 * The device most respond to this URL.
	 * 
	 * @return device location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the URL on which the device will publish more information
	 * @param location 
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * Returns the server informations.
	 * Format is : OS/version, UPnP/version, product/version
	 * @return server informations
	 */
	public String getServer() {
		return server;
	}
	
	/**
	 * Sets the server informations.
	 * Format must be : OS/version, UPnP/version, product/version
	 * @param server informations
	 */
	public void setServer(String server) {
		this.server = server;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getNotify()).append("\r\n");
		sb.append(SSDPContants.HOST + " " +  getHost()).append("\r\n");
		sb.append(SSDPContants.NT + " " +  getNt()).append("\r\n");
		sb.append(SSDPContants.NTS + " " +  getNts()).append("\r\n");
		sb.append(SSDPContants.LOCATION + " " +  getLocation()).append("\r\n");
		sb.append(SSDPContants.USN + " " +  getUsn()).append("\r\n");
		sb.append(SSDPContants.CACHECONTROL + " " +  getCacheControl()).append("\r\n");
		sb.append(SSDPContants.SERVER + " " +  getServer()).append("\r\n");
		sb.append("\r\n");
		
		return sb.toString();
	}

}
