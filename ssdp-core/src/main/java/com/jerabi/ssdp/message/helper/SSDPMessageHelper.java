package com.jerabi.ssdp.message.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.jerabi.ssdp.message.AbstractSSDPNotifyMessage;
import com.jerabi.ssdp.message.AliveMessage;
import com.jerabi.ssdp.message.ByeByeMessage;
import com.jerabi.ssdp.message.DiscoverMessage;
import com.jerabi.ssdp.message.DiscoverResponseMessage;
import com.jerabi.ssdp.message.ISSDPMessage;
import com.jerabi.ssdp.message.ServiceInfo;
import com.jerabi.ssdp.message.UpdateMessage;
import com.jerabi.ssdp.util.SSDPContants;

/**
 * This helper provides API to create and parse {@link ISSDPMessage}.
 * 
 * You can create a ISSDPMessage using a {@link ServiceInfo}.
 * 
 * @example.
 * <pre>
 * //You could received a message over the network and want to convert it to ISSDPMessage.
 * 
 * StringBuffer sb = new StringBuffer();
 * sb.append("HTTP/1.1 200 OK").append("\n");
 * sb.append("CACHE-CONTROL: max-age=1200").append("\n");
 * sb.append("DATE: Tue, 05 May 2009 13:31:51 GMT").append("\n");
 * sb.append("LOCATION: http://142.225.35.55:5001/description/fetch").append("\n");
 * sb.append("SERVER: Windows_XP-x86-5.1, UPnP/1.0, PMS/1.11").append("\n");
 * sb.append("ST: upnp:rootdevice").append("\n");
 * sb.append("EXT: ").append("\n");
 * sb.append("USN: uuid:9dcf6222-fc4b-33eb-bf49-e54643b4f416::upnp:rootdevice").append("\n");
 * sb.append("Content-Length: 0").append("\n");
 *	
 * ISSDPMessage message = SSDPMessageHelper.getSSDPMessage(sb.toString());
 *	</pre>		
 * @author Sebastien Dionne
 * 
 * @see AliveMessage
 * @see ByeByeMessage
 * @see DiscoverMessage
 * @see DiscoverResponseMessage
 * @see UpdateMessage
 */
public class SSDPMessageHelper {
	
	private static final Logger logger = Logger.getLogger(SSDPMessageHelper.class.getName());
	

	/**
	 * Parse the message and returns a ISSDPMessage if the message is supported.
	 * If the message is not recognized a null value will be return.
	 * 
	 * Some extra attributes could be received within the message.  Even if there are not
	 * part of the SSDP specs, and they be parsed and kept into the property : Attributes. 
	 * 
	 * @param message message to parse
	 * @return ISSDPMessage if a valid message is received or null if it's a unknown format
	 */
	public static ISSDPMessage getSSDPMessage(String message){
		
		if(message==null || message.trim().length()==0){
			return null;
		}
		
		String[] split = message.split("\n");
		
		String notify = null;
		String cacheControl = null;
		String date = null;
		String location = null;
		String server = null;
		String st = null;
		String ext = null;
		String usn = null;
		String contentLength = null;
		String host = null;
		String port = null;
		String nt = null;
		String nts = null;
		String mx = null;
		@SuppressWarnings("unused")
		String man = null;
		List<String> othersAttributes = new ArrayList<String>();
		
		for (int i = 0; i < split.length; i++) {
			
			String line = split[i];
			
			if(i==0){
				notify = line.trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.CACHECONTROL)){
				cacheControl = line.substring(SSDPContants.CACHECONTROL.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.DATE)){
				date = line.substring(SSDPContants.DATE.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.LOCATION)){
				location = line.substring(SSDPContants.LOCATION.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.SERVER)){
				server = line.substring(SSDPContants.SERVER.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.ST)){
				st = line.substring(SSDPContants.ST.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.EXT)){
				ext = line.substring(SSDPContants.EXT.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.USN)){
				usn = line.substring(SSDPContants.USN.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.CONTENTLENGTH)){
				contentLength = line.substring(SSDPContants.CONTENTLENGTH.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.HOST)){
				host = line.substring(SSDPContants.HOST.length()).trim();
				
				// extract port if found
				int portIndex = host.indexOf(":");
				if(portIndex>0){
					port = host.substring(portIndex+1);
					host = host.substring(0,portIndex);
				} else {
					// use default port
					port = Integer.toString(SSDPContants.DEFAULT_PORT);
				}
				
			} else if(line.toUpperCase().startsWith(SSDPContants.NT)){
				nt = line.substring(SSDPContants.NT.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.NTS)){
				nts = line.substring(SSDPContants.NTS.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.MX)){
				mx = line.substring(SSDPContants.MX.length()).trim();
			} else if(line.toUpperCase().startsWith(SSDPContants.MAN)){
				man = line.substring(SSDPContants.MAN.length()).trim();
			} else {
				othersAttributes.add(line);
			}
			
		}
		
		// is it a M-SEARCH ?
		if(notify!=null && notify.toUpperCase().startsWith("M-SEARCH")){
			
			DiscoverMessage ssdpMessage = new DiscoverMessage(); 
			
			ssdpMessage.setMessage(message);
			ssdpMessage.setHost(host);
			ssdpMessage.setPort(port);
			ssdpMessage.setMx(mx);
			ssdpMessage.setSt(st);
			ssdpMessage.setAttributes(othersAttributes);
			
			return ssdpMessage;
			
		} else if(notify!=null && notify.equalsIgnoreCase("HTTP/1.1 200 OK")){
			// probably a M-SEARCH response
			DiscoverResponseMessage ssdpMessage = new DiscoverResponseMessage();
			
			ssdpMessage.setMessage(message);
			//ssdpMessage.setNotify(notify);
			ssdpMessage.setCacheControl(cacheControl);
			ssdpMessage.setDate(date);
			ssdpMessage.setLocation(location);
			ssdpMessage.setServer(server);
			ssdpMessage.setSt(st);
			ssdpMessage.setExt(ext);
			ssdpMessage.setUsn(usn);
			ssdpMessage.setContentLength(contentLength);
			ssdpMessage.setAttributes(othersAttributes);
			
			return ssdpMessage;
		} else if(nts!=null && nts.trim().length()>0){
			// assign values now
			
			AbstractSSDPNotifyMessage ssdpMessage = null;
			
			if(SSDPContants.NTS_ALIVE.equals(nts)){
				ssdpMessage  = new AliveMessage();
				
				((AliveMessage)ssdpMessage ).setCacheControl(cacheControl);
				((AliveMessage)ssdpMessage ).setLocation(location);
				((AliveMessage)ssdpMessage ).setServer(server);
				
			} else if(SSDPContants.NTS_UPDATE.equals(nts)){
				ssdpMessage = new UpdateMessage();
				
				((UpdateMessage)ssdpMessage ).setCacheControl(cacheControl);
				((UpdateMessage)ssdpMessage ).setLocation(location);
				((UpdateMessage)ssdpMessage ).setServer(server);

			} else if(SSDPContants.NTS_BYEBYE.equals(nts)){
				ssdpMessage = new ByeByeMessage();
				
				((ByeByeMessage)ssdpMessage ).setContentLength(contentLength);

			} else {
				logger.info("SSDPNotifyMessage not reconized : \n" + message);
				return null;
			}
			// commons fields
			
			ssdpMessage.setMessage(message);
			ssdpMessage.setNotify(notify);
			ssdpMessage.setHost(host);
			ssdpMessage.setPort(port);
			ssdpMessage.setNt(nt);
			ssdpMessage.setNts(nts);
			ssdpMessage.setUsn(usn);
			ssdpMessage.setAttributes(othersAttributes);
			
			return ssdpMessage;
		}  else {
			logger.finest("Message not recognized : \n" + message);
		}
		
		return null;
	}
	
	/**
	 * Creates a {@link AliveMessage} from a {@link ServiceInfo}.  
	 * 
	 * @param deviceInfo info on your device
	 * @return AliveMessage
	 */
	public static AliveMessage createSSDPAliveMessage(ServiceInfo deviceInfo) {
		AliveMessage message =  new AliveMessage();
		
		message.setNotify(AliveMessage.notify);
		message.setHost(deviceInfo.getHost() + ":" + deviceInfo.getPort());
		message.setNts(AliveMessage.nts);
		message.setNt(deviceInfo.getNt());
		message.setLocation(deviceInfo.getLocation());
		message.setUsn(deviceInfo.getUsn().toString());
		
		return message;
	}
	
	/**
	 * Creates a {@link UpdateMessage} from a {@link ServiceInfo}.  
	 * 
	 * @param deviceInfo info on your device
	 * @return UpdateMessage
	 */
	public static UpdateMessage createSSDPUpdateMessage(ServiceInfo deviceInfo) {
		UpdateMessage message =  new UpdateMessage();
		
		message.setNotify(UpdateMessage.notify);
		message.setHost(deviceInfo.getHost() + ":" + deviceInfo.getPort());
		message.setNts(UpdateMessage.nts);
		message.setNt(deviceInfo.getNt());
		message.setUsn(deviceInfo.getUsn().toString());
		
		return message;
	}
	
	/**
	 * Creates a {@link ByeByeMessage} from a {@link ServiceInfo}.  
	 * 
	 * @param deviceInfo info on your device
	 * @return ByeByeMessage
	 */
	public static ByeByeMessage createSSDPByeByeMessage(ServiceInfo deviceInfo) {
		ByeByeMessage message =  new ByeByeMessage();
		
		message.setNotify(ByeByeMessage.notify);
		message.setHost(deviceInfo.getHost() + ":" + deviceInfo.getPort());
		message.setNts(ByeByeMessage.nts);
		message.setNt(deviceInfo.getNt());
		message.setUsn(deviceInfo.getUsn().toString());
		
		return message;
	}
	
	/**
	 * Creates a {@link DiscoverMessage} from the info on your device.
	 *   
	 * @param host on which host the device is located
	 * @param port on which port the device is listening
	 * @param ttl the time to live value
	 * @param device device URN ex : urn:schemas-upnp-org:device:MediaServer:1
	 * @param attributes add extra attributes on the message
	 * @return DiscoverMessage
	 */
	public static DiscoverMessage createSSDPDiscoverMessage(String host, int port, int ttl, String device, List<String> attributes) {
		DiscoverMessage message = new DiscoverMessage(host, port, ttl, device, attributes);
		
		return message;
	}
	
}
