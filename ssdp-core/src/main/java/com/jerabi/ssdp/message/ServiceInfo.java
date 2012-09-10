package com.jerabi.ssdp.message;


/**
 * Represents a device/service for notification over the network. 
 * 
 * @author Sebastien Dionne
 * @example.
 * <pre>
 * new ServiceInfo(SSDPContants.DEFAULT_IP, 
 *                 SSDPContants.DEFAULT_PORT, 
 *                 "upnp:rootdevice",
 *                 "http://127.0.0.1:9000/config", 
 *                 new USNInfo("1acf6222-fc4b-33eb-bf49-e54643b4f416","upnp:rootdevice"));
 * </pre>
 * @see com.jerabi.ssdp.message.helper.SSDPMessageHelper
 */
public class ServiceInfo implements Comparable<ServiceInfo> {
	
	private String host;
	private int port;
	private String nt;
	private String location;
	private USNInfo usn;
	
	/**
	 * Default constructor
	 */
	public ServiceInfo(){
		
	}
	
	/**
	 * Constructor with custom values
	 * @param host multicast IP
	 * @param port multicast Port
	 * @param nt Notification Type (NT)
	 * @param location URL on which the device will publish more information
	 * @param usn Unique Service Name (USN)
	 */
	public ServiceInfo(String host, int port, String nt, String location, USNInfo usn){
		this.host = host;
		this.port = port;
		this.nt = nt;
		this.location = location;
		this.usn = usn;
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
	 * Returns the URL for retrieving more informations about the device.  
	 * The device most respond to this URL.
	 * 
	 * @return device location
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the URL on which the device will publish more informations
	 * @param location 
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * Returns the (USN)
	 * @return (USN) Unique Service Name
	 */
	public USNInfo getUsn() {
		return usn;
	}
	
	/**
	 * Sets the (USN)
	 * @param usn Unique Service Name
	 */
	public void setUsn(USNInfo usn) {
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
	 * Returns the listen port
	 * @return port
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets the listen port
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	/**
	 * Returns the root path of the location
	 * @return "http://" + server + port(if present)
	 */
	public String getLocationPath(){
		if(location==null){
			return null;
		}
		
		String path = null;
		
		if(location.toLowerCase().startsWith("http://")){
			int index = location.toLowerCase().indexOf("/", "http://".length());
			
			if(index>0){
				path = location.substring(index);
			} else {
				// if there isn't path, use blank
				// ex : http://localhost:8080
				path = "";
			}
			
		}
		
		return path;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((nt == null) ? 0 : nt.hashCode());
		result = prime * result + port;
		result = prime * result + ((usn == null) ? 0 : usn.hashCode());
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceInfo other = (ServiceInfo) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (nt == null) {
			if (other.nt != null)
				return false;
		} else if (!nt.equals(other.nt))
			return false;
		if (port != other.port)
			return false;
		if (usn == null) {
			if (other.usn != null)
				return false;
		} else if (!usn.equals(other.usn))
			return false;
		return true;
	}
	
	/**
	 * @return Returns this ServiceInfo as a String
	 */
	public String toString(){
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("host=").append(host).append("\n");
		sb.append("port=").append(port).append("\n");
		sb.append("nt=").append(nt).append("\n");
		sb.append("location=").append(location).append("\n");
		sb.append("usn=").append(usn).append("\n");
		
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(ServiceInfo o) {
		return this.toString().compareToIgnoreCase(o.toString());
	}
	
}
