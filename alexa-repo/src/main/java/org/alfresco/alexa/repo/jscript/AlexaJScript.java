package org.alfresco.alexa.repo.jscript;

import org.alfresco.alexa.AlfrescoVoiceScriptSkill;
import org.alfresco.alexa.service.AlexaService;
import org.alfresco.repo.jscript.BaseScopableProcessorExtension;
import org.alfresco.repo.jscript.ScriptNode;

public class AlexaJScript extends BaseScopableProcessorExtension  {

	private AlexaService alexaService;
	private AlfrescoVoiceScriptSkill alfrescoVoiceScriptSkill;
	
	public void setAlexaService(AlexaService alexaService) {
		this.alexaService = alexaService;
	}
	
	public void setAlfrescoVoiceScriptSkill(AlfrescoVoiceScriptSkill alfrescoVoiceScriptSkill) {
		this.alfrescoVoiceScriptSkill = alfrescoVoiceScriptSkill;
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
	
	private void rebuildSkill() {
		alexaService.unregisterSkill(alfrescoVoiceScriptSkill.getSkillId());
		alexaService.registerSkill(alfrescoVoiceScriptSkill);
		
	}
	
	public void registerScriptIntent(ScriptNode jsNode) {
		alfrescoVoiceScriptSkill.registerScriptIntent(jsNode.getNodeRef());
		this.rebuildSkill();
	}
	
	public void unregisterScriptIntent(String intentId) {
		alfrescoVoiceScriptSkill.unregisterScriptIntentIfExist(intentId);
		this.rebuildSkill();
	}
	
}
