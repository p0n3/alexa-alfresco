package org.alfresco.alexa;

import java.util.Map;

import org.alfresco.alexa.model.SessionResponse;

/**
 * Interface for intent that use session attributes, slots and which want to use shouldEndSession value
 * 
 * @author ltworek
 *
 */
public interface AlfrescoVoiceSessionIntent {

	/**
	 * Method is executed when intent is invoked
	 * 
	 * @param attributes	map of session attributes
	 * @param slots	map of parametrs (slots)
	 * @return	SessionResponse object
	 */
	public SessionResponse getResponse(Map<String, Object> attributes, Map<String, String> slots);
	
}
