package com.jerabi.ssdp.message;

import com.jerabi.ssdp.util.SSDPContants;

/**
 * 
 * Represents a ssdp:byebye message.  
 * 
 * When a device and its services are going to be removed from the network, the device SHOULD multicast an ssdp:byebye message
 * corresponding to each of the ssdp:alive messages it multicasted that have not already expired. If the device is removed abruptly
 * from the network, it might not be possible to multicast a message. 
 * 
 * When the device starts a ssdp:byebye message must be sent before any other messages. (It's not mandatory but strongly suggested)
 * 
 * @author Sebastien Dionne
 * @example.
 *   <pre>
 *   NOTIFY * HTTP/1.1
 *   HOST: 239.255.255.250:1900
 *   NTS: ssdp:byebye
 *   USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1
 *   NT: urn:schemas-upnp-org:service:ContentDirectory:1
 *   CONTENT-LENGTH: 0
 *   </pre>
 *   
 *   @see AbstractSSDPNotifyMessage
 */
public class ByeByeMessage extends AbstractSSDPNotifyMessage {
	
	/**
	 * First line of the message
	 */
	public static final String notify = "NOTIFY * HTTP/1.1";
	
	/**
	 * NTS value
	 */
	public static final String nts = SSDPContants.NTS_BYEBYE;
	protected String contentLength;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNts() {
		return nts;
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
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		sb.append(getNotify()).append("\r\n");
		sb.append(SSDPContants.HOST + " " +  getHost()).append("\r\n");
		sb.append(SSDPContants.NT + " " +  getNt()).append("\r\n");
		sb.append(SSDPContants.NTS + " " +  getNts()).append("\r\n");
		sb.append(SSDPContants.USN + " " +  getUsn()).append("\r\n");
		sb.append(SSDPContants.CONTENTLENGTH + " " +  getContentLength()).append("\r\n");
		
		return sb.toString();
	}

}
