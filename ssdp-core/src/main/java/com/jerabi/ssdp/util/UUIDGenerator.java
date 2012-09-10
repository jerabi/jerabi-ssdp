package com.jerabi.ssdp.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.UUID;

/**
*  
*  Provide API to generate {@link java.util.UUID}.
*  
*  A UUID is an acronym for :  Universally Unique Identifier
*  
*	<pre> 
*    UUID                   = &lt;time_low> - &lt;time_mid> - &lt;time_high_and_version> - &lt;clock_seq_and_reserved> &lt;clock_seq_low> - &lt;node>
*    
*    time_low               = 4*&lt;hexOctet>
*    time_mid               = 2*&lt;hexOctet>
*    time_high_and_version  = 2*&lt;hexOctet>
*    clock_seq_and_reserved = &lt;hexOctet>
*    clock_seq_low          = &lt;hexOctet>
*    node                   = 6*&lt;hexOctet
*    hexOctet               = &lt;hexDigit> &lt;hexDigit>
*
*    hexDigit = 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | a | b | c | d | e | f | A | B | C | D | E | F
*    
*    The following is an example of the string representation of a UUID:
*
*    f81d4fae-7dec-11d0-a765-00a0c91e6bf6
*	</pre>
*	
*	@see java.util.UUID
*	 
*	@author Sebastien Dionne
*
*/
public class UUIDGenerator {
	
	/**
	 * Generates a random UUID 
	 * 
	 * @return UUID
	 * @see java.util.UUID 
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * Generates a UUID based on the MAC address.
	 * If the NetworkInterface is null, null value will be return
	 * 
	 * @param ni NetworkInterface used
	 * @return a UUID based on the MAC address of the NetworkInterface
	 * @throws SocketException
	 * @see java.util.UUID 
	 */
	public static String getUUID(NetworkInterface ni) throws SocketException {
		
		if(ni==null){
			return null;
		}
		
		byte[] mac = ni.getHardwareAddress();
		
		return UUID.nameUUIDFromBytes(mac).toString();
	}
	
}
