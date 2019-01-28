package org.alfresco.alexa;

import java.util.Map;

import org.alfresco.alexa.model.SessionResponse;

public interface AlfrescoVoiceSessionIntent {

	public SessionResponse getResponse(Map<String, Object> attributes);
	
}
