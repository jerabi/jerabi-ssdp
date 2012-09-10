package com.jerabi.ssdp.message;

import java.util.List;

import com.jerabi.ssdp.util.SSDPContants;



/**
 * Represents a response to a {@link DiscoverMessage}.
 * 
 * @author Sebastien Dionne
 * 
 * @example.
 * <pre>
 * HTTP/1.1 200 OK
 * CACHE-CONTROL: max-age=1200
 * DATE: Tue, 05 May 2009 13:31:51 GMT
 * LOCATION: http://142.225.35.55:5001/description/fetch
 * SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11
 * ST: upnp:rootdevice
 * EXT: 
 * USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::upnp:rootdevice
 * Content-Length: 0
 * </pre>
 * 
 * @see DiscoverMessage
 */
public class DiscoverResponseMessage implements ISSDPMessage {
	
	/**
	 * First line of the message
	 */
	public static final String notify = "HTTP/1.1 200 OK";
	
	protected String message;
	protected String cacheControl;
	protected String date;
	protected String location;
	protected String server;
	protected String st;
	protected String ext;
	protected String usn;
	protected String contentLength;
	protected List<String> attributes = null;
	
	/**
	 * Returns the Notify : the first line of the message
	 * @return notify
	 */
	public String getNotify() {
		return notify;
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
	 * Returns when response was generated
	 * @return date
	 */
	public String getDate() {
		return date;
	}
	
	/**
	 * Sets the date when the response was generated
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
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
	 * Returns EXT value 
	 * Backwards compatibility with UPnP 1.0. (Header field name only; no field value.)
	 * @return EXT value
	 */
	public String getExt() {
		return ext;
	}
	
	/**
	 * Backwards compatibility with UPnP 1.0. (Header field name only; no field value.)
	 * @param ext new value
	 */
	public void setExt(String ext) {
		this.ext = ext;
	}
	
	/**
	 * Returns the bytes in the body
	 * @return body length
	 */
	public String getContentLength() {
		return contentLength;
	}
	
	/**
	 * Sets the byte length available in the body 
	 * @param contentLength
	 */
	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
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
	 * Returns the (USN)
	 * @see com.jerabi.ssdp.message.USNInfo
	 * @return (USN) Unique Service Name
	 */
	public String getUsn() {
		return usn;
	}
	
	/**
	 * Sets the (USN) 
	 * @see com.jerabi.ssdp.message.USNInfo
	 * @param usn Unique Service Name
	 */
	public void setUsn(String usn) {
		this.usn = usn;
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
	/*
	 * @return a notify response from theses attributes
	 */
	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		
		// the message is different based on the type
		
		sb.append(getNotify()).append("\r\n");
		sb.append(SSDPContants.CACHECONTROL + " " +  getCacheControl()).append("\r\n");
		sb.append(SSDPContants.DATE + " " +  getDate()).append("\r\n");
		sb.append(SSDPContants.LOCATION + " " +  getLocation()).append("\r\n");
		sb.append(SSDPContants.SERVER + " " +  getServer()).append("\r\n");
		sb.append(SSDPContants.ST + " " +  getSt()).append("\r\n");
		sb.append(SSDPContants.EXT + " " +  getExt()).append("\r\n");
		sb.append(SSDPContants.USN + " " +  getUsn()).append("\r\n");
		sb.append(SSDPContants.CONTENTLENGTH + " " +  getContentLength()).append("\r\n");
		sb.append("\r\n");
		
		return sb.toString();
	}
}
