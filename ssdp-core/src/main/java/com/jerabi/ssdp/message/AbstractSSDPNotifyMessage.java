package com.jerabi.ssdp.message;

import java.util.List;

/**
 * Contains the commons fields for notification {@link ISSDPMessage}.  
 * A parent class will extends this class and add the required fields that are missing.
 * 
 * @author Sebastien Dionne
 *
 */
public abstract class AbstractSSDPNotifyMessage implements ISSDPMessage {
	protected String message;
	protected String notify;
	protected String host;
	protected String port;
	protected String nt;
	protected String nts;
	protected String usn;
	protected List<String> attributes = null;
	
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
	 * Returns Notification Type (NT)
	 * @return Notification Type
	 */
	public String getNt() {
		return nt;
	}
	
	/**
	 * Sets Notification Type (NT)
	 * @param nt
	 */
	public void setNt(String nt) {
		this.nt = nt;
	}
	
	/**
	 * Sets Notification Sub Type (NTS).  It's the message type
	 * @see AliveMessage
	 * @see ByeByeMessage
	 * @see UpdateMessage
	 * @param nts
	 */
	public void setNts(String nts) {
		this.nts = nts;
	}
	
	/**
	 * Returns the Notify : the first line of the message
	 * @return notify
	 */
	public String getNotify() {
		return notify;
	}
	
	/**
	 * Sets the notify
	 * @param notify
	 */
	public void setNotify(String notify) {
		this.notify = notify;
	}
	
	/**
	 * Returns port
	 * @return port
	 */
	public String getPort() {
		return port;
	}
	
	/**
	 * Sets port
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
	 * Return this message formatted
	 * @return message formatted
	 */
	public abstract String toString();
	
	/**
	 * Returns Notification Sub Type (NTS)
	 * @return nts
	 */
	public abstract String getNts();
	
}
