package org.alfresco.alexa.repo.jscript;

import org.alfresco.alexa.service.AlexaService;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;

public class AlexaJScript extends BaseScopableProcessorExtension  {

	private AlexaService alexaService;
	
	public void setAlexaService(AlexaService alexaService) {
		this.alexaService = alexaService;
	}
	
	// for testing purposes
	public AlexaService getAlexaService() {
		return alexaService;
	}
	
	public String generateToken() {
		return alexaService.generateRandomToken();
	}
	
	public String hashToken(String token, String clientId) {
		return alexaService.hashToken(token, clientId);
	}
	
	public void sendNotification(String text) {
		alexaService.sendNotification(text);
	}
}
