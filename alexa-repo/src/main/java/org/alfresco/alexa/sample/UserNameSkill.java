package org.alfresco.alexa.sample;

import java.util.Map;

import org.alfresco.alexa.AlfrescoVoiceSimpleIntent;
import org.alfresco.alexa.AlfrescoVoiceSkill;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;

public class UserNameSkill extends AlfrescoVoiceSkill implements AlfrescoVoiceSimpleIntent {

	private ServiceRegistry serviceRegistry;
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	@Override
	protected void registerIntents() {
		
		this.registerSimpleIntent("UserNameIntent", this);
		
	}

	@Override
	public String getResponse() {
		
		String userName = AuthenticationUtil.getFullyAuthenticatedUser();
		
		NodeRef person = this.serviceRegistry.getPersonService().getPerson(userName);
		String firstName = (String) this.serviceRegistry.getNodeService().getProperty(person, ContentModel.PROP_FIRSTNAME);
		
		return "Hello "+firstName;
	}

}
