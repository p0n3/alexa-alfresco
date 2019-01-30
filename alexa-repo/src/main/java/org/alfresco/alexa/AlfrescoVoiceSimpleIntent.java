package org.alfresco.alexa;

/**
 *  Interface for intent that only response simple text
 * 
 * @author ltworek
 *
 */
public interface AlfrescoVoiceSimpleIntent {

	/**
	 * Method is executed when intent is invoked
	 * 
	 * @return response text to say
	 */
	public String getResponse();
	
}
