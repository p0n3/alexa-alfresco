package org.alfresco.alexa.model;

import java.util.HashMap;
import java.util.Map;

/**
 * SessionResponse object used in
 * {@link org.alfresco.alexa.AlfrescoVoiceSessionIntent}
 * 
 * @author ltworek
 *
 */
public class SessionResponse {

	private String speechText;
	private boolean shouldEndSession;
	private Map<String, Object> attributes;

	public SessionResponse() {
	}

	/**
	 * Set a text to say
	 * 
	 * @param speechText
	 */
	public void setSpeechText(String speechText) {
		this.speechText = speechText;
	}

	public String getSpeechText() {
		return speechText;
	}

	/**
	 * Set a session attributes
	 * 
	 * @param attributes
	 */
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttribute(String key, Object value) {
		if (this.attributes == null) {
			this.attributes = new HashMap<String, Object>();
		}
		this.attributes.put(key, value);
	}

	public void setShouldEndSession(boolean shouldEndSession) {
		this.shouldEndSession = shouldEndSession;
	}

	public boolean shouldEndSession() {
		return shouldEndSession;
	}

	public boolean hasAttributes() {
		return (this.attributes != null && this.attributes.size() > 0);
	}
}
