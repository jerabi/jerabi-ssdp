package com.jerabi.ssdp.message;

import java.util.List;

import com.jerabi.ssdp.util.SSDPContants;

/**
 * Represents a "ssdp:discover" message (M-SEARCH).
 * 
 * When a Client (Control Point) want to search for device, it must send a discover message.
 * 
 * You can search for any device and a particular device/service with the parameter : ST.
 * 
 * @author Sebastien Dionne
 * 
 * @example.
 * 
 * <pre>
 * M-SEARCH * HTTP/1.1
 * HOST: 239.255.255.250:1900
 * ST: urn:schemas-upnp-org:device:MediaServer:1
 * MAN: "ssdp:discover"
 * MX: 2
 * X-AV-Client-Info: av=5.0; cn="Sony Computer Entertainment Inc."; mn="PLAYSTATION 3"; mv="1.0";
 * </pre>
 * 
 * @see DiscoverResponseMessage
 */
public class DiscoverMessage implements ISSDPMessage {
	
	/**
	 * First line of the message
	 */
	public static final String notify = "M-SEARCH * HTTP/1.1";
	/**
	 * Field for discover message (M-SEARCH)
	 */
	public static final String man = SSDPContants.NTS_DISCOVER;
	
	protected String message;
	protected String host = null;
	protected String port;
	protected String st = null;
	protected String mx = null;
	protected List<String> attributes = null;
	
	/**
	 * Default constructor
	 */
	public DiscoverMessage(){
		
	}
	
	/**
	 * Constructor 
	 * 
	 * @param host IP address that the device broadcast
	 * @param port that the device broadcast
	 * @param ttl Time-to-Live for this advertisement
	 * @param device which device/service that we searching for.
	 * @param attributes extra attributes for the DiscoverMessage
	 */
	public DiscoverMessage(String host, int port, int ttl, String device, List<String> attributes){
		this.host = host;
		this.port = Integer.toString(port);
		this.mx = Integer.toString(ttl);
		this.st = device;
		this.attributes = attributes;
	}
	
	/**
	 * Returns the raw message if parsed with {@link com.jerabi.ssdp.message.helper.SSDPMessageHelper}
	 * @return message not parsed
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the raw message if parsed with {@link com.jerabi.ssdp.message.helper.SSDPMessageHelper}
	 * or for debugging
	 * @param message original message not parsed
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Returns the host
	 * @return host
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * Sets the host
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * Returns the search target (ST)
	 * @return st URN target
	 */
	public String getSt() {
		return st;
	}
	
	/**
	 * Sets the search target (ST)
	 * @param st URN target
	 */
	public void setSt(String st) {
		this.st = st;
	}
	
	/**
	 * Returns the maximum wait time in seconds
	 * @return the "time-to-live" value
	 */
	public String getMx() {
		return mx;
	}
	
	/**
	 * Sets the maximum wait time in seconds
	 * @param mx the "time-to-live" value
	 */
	public void setMx(String mx) {
		this.mx = mx;
	}
	
	/**
	 * Returns the listen port
	 * @return port
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Sets the listen port
	 * @param port
	 */
	public void setPort(String port) {
		this.port = port;
	}
	
	/**
	 * Returns the extra attributes for the message
	 * @return extra attributes
	 */
	public List<String> getAttributes() {
		return attributes;
	}
	
	/**
	 * Sets extra attributes for the message
	 * @param attributes
	 */
	public void setAttributes(List<String> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		
		String host = getHost();
		
		if(getPort()!=null){
			host = host + ":" + getPort();
		} else {
			host = host + ":" + SSDPContants.DEFAULT_PORT;
		}
		
		sb.append(notify).append("\r\n");
		sb.append(SSDPContants.HOST + " " +  host).append("\r\n");
		sb.append(SSDPContants.MAN + " " + man).append("\r\n");
		sb.append(SSDPContants.MX + " " + mx).append("\r\n");
		sb.append(SSDPContants.ST + " " + st).append("\r\n");
		
		// custom attributes
		if(attributes!=null && attributes.size()>0){
			for (String attribute : attributes) {
				sb.append(attribute).append("\r\n");
			}
		}
		sb.append("\r\n");
		
		return sb.toString();
	}
}
