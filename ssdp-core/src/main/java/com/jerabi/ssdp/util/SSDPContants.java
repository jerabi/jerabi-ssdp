package com.jerabi.ssdp.util;

/**
 * 
 * Defines all SSDP Constants.
 * 
 * @author Sebastien Dionne
 *
 */
public class SSDPContants {
	
	/**
	 * Method for sending notifications and events.  First line of the message.
	 */
	public static final String NOTIFY = "NOTIFY:";
	
	/**
	 * Field value MUST have the max-age directive ("max-age=") followed by an integer that specifies the number of
	 * seconds the advertisement is valid
	 */
	public static final String CACHECONTROL = "CACHE-CONTROL:";
	/**
	 * Field value contains when response was generated
	 */
	public static final String DATE = "DATE:";
	/**
	 * Field value contains a URL to the UPnP description of the root device
	 */
	public static final String LOCATION = "LOCATION:";
	/**
	 * Server informations.  The format is : OS/version, UPnP/version, product/version
	 */
	public static final String SERVER = "SERVER:";
	/**
	 * Field value contains Search Target.  
	 */
	public static final String ST = "ST:";
	/**
	 * Backwards compatibility with UPnP 1.0. (Header field name only; no field value.)
	 */
	public static final String EXT = "EXT:";
	/**
	 * Field value contains Unique Service Name. Identifies a unique instance of a device or service
	 */
	public static final String USN = "USN:";
	/**
	 * Field value contains the byte count in the body
	 */
	public static final String CONTENTLENGTH = "CONTENT-LENGTH:";
	
	/**
	 * Field value contains multicast address and port reserved for SSDP by Internet Assigned Numbers Authority (IANA).
     * MUST be 239.255.255.250:1900. If the port number (":1900") is omitted, the receiver MUST assume the default SSDP port
     * number of 1900.
	 */
	public static final String HOST = "HOST:";
	/**
	 * Field value contains Notification Type.
	 */
	public static final String NT = "NT:";
	/**
	 * Field value contains Notification Sub Type
	 */
	public static final String NTS = "NTS:";
	/**
	 * HTTP Extension Framework. Unlike the NTS and ST field values, the field value of the MAN header field is
	 * enclosed in double quotes; it defines the scope (namespace) of the extension. MUST be "ssdp:discover".
	 */
	public static final String MAN = "MAN:";
	/**
	 * Field value contains maximum wait time in seconds. MUST be greater than or equal to 1 and SHOULD be less than
     * 5 inclusive. Device responses SHOULD be delayed a random duration between 0 and this many seconds to balance load for
     * the control point when it processes responses
	 */
	public static final String MX = "MX:";
	
	/**
	 * Default SSDP port
	 */
	public static final int DEFAULT_PORT = 1900;
	
	/**
	 * Default SSDP IP
	 */
	public static final String DEFAULT_IP = "239.255.255.250";
	
	/**
	 * Default sleep value for periodically messages in ms.
	 */
	public static final int DEFAULT_DELAY = 5000;
	
	/**
	 * Default value for non blocking socket timeout
	 */
	public static final int DEFAULT_SOCKET_TIMEOUT = 3000;
	
	/**
	 * NTS value for {@link com.jerabi.ssdp.message.AliveMessage}
	 */
	public static final String NTS_ALIVE = "ssdp:alive";
	/**
	 * NTS value for {@link com.jerabi.ssdp.message.DiscoverMessage}
	 */
	public static final String NTS_DISCOVER = "\"ssdp:discover\"";
	/**
	 * NTS value for {@link com.jerabi.ssdp.message.ByeByeMessage}
	 */
	public static final String NTS_BYEBYE = "ssdp:byebye";
	/**
	 * NTS value for {@link com.jerabi.ssdp.message.UpdateMessage}
	 */
	public static final String NTS_UPDATE = "ssdp:update";
	/**
	 * NTS value for discover all devices and services @see com.jerabi.ssdp.message.DiscoverMessage
	 */
	public static final String NTS_DISCOVER_ALL = "ssdp:all";
	
}
