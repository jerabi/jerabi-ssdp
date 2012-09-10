package com.jerabi.ssdp.message;

/**
 * 
 *  Unique Service Name (USN) - An identifier that is unique across all
 *  services for all time. It is used to uniquely identify a particular
 *  service in order to allow services of identical service type to be
 *  differentiated.
 *  <pre>
 *  A USN is formed by two parts : UUID and URN and they are not mandatory.  
 *  
 *  UUID : Universally Unique Identifier {@link com.jerabi.ssdp.util.UUIDGenerator}
 *  URN  : Uniform Resource Name.
 *      
 *  A URN must respect this format : 

 *  urn:schemas-upnp-org:device:deviceType:ver
 *  or
 *  urn:domain-name:device:deviceType:ver
 *  </pre>
 *  <pre>
 *  Example : 
 *  
 *     USN: uuid:9dcf6abc-fc5b-33eb-bf49-e54643b4f416::upnp:rootdevice
 *     USN: uuid:9dcf6abc-fc5b-33eb-bf49-e54643b4f416::urn:schemas-upnp-org:service:ContentDirectory:1
 *  </pre>
 *
 * @author Sebastien Dionne
 */
public class USNInfo implements Comparable<USNInfo> {
	
	protected String uuid;
	protected String urn;
	
	/**
	 * Constructor with from a USN
	 * @param usn must contains a UUID and URN separated by "::" 
	 * @throws Exception
	 */
	public USNInfo(String usn) throws Exception {
		
		int separator = usn.indexOf("::");
		
		if(separator<0){
			throw new Exception("Invalid USN");
		}
		
		uuid = usn.substring(0,separator);
		urn = usn.substring(separator+2);
		
	}
	
	/**
	 * Constructor from UUID and URN.
	 * The values can be null or empty
	 * 
	 * @param uuid is not mandatory
	 * @param urn is not mandatory
	 */
	public USNInfo(String uuid, String urn){
		this.uuid = uuid;
		this.urn = urn;
	}
	
	/**
	 * Returns UUID
	 * @return uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * Sets UUID
	 * @param uuid new UUID
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	/**
	 * Returns URN
	 * @return urn
	 */
	public String getUrn() {
		return urn;
	}
	/**
	 * Sets URN
	 * @param urn new URN
	 */
	public void setUrn(String urn) {
		this.urn = urn;
	}
	
	/**
	 * Returns a USN from UUID and URN.
	 * If URN is not null or empty, the USN will contains "::" as separator
	 * @return usn 
	 */
	public String toString(){
		// be sure that uuid doesn't already contains uuid:
		String uuidValue = uuid;
		if(uuid==null || !uuid.toLowerCase().startsWith("uuid:")){
			uuidValue = "uuid:" + uuidValue;
		}
		
		if(urn!=null && urn.length()>0){
			return uuidValue + "::" + urn;
		} else {
			return uuidValue;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((urn == null) ? 0 : urn.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		USNInfo other = (USNInfo) obj;
		if (urn == null) {
			if (other.urn != null)
				return false;
		} else if (!urn.equals(other.urn))
			return false;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(USNInfo obj) {
		return this.toString().compareToIgnoreCase(obj.toString());
	}
	
}
