package org.alfresco.alexa;

import java.util.Optional;

import org.alfresco.alexa.handlers.DefaultSessionIntentHandler;
import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.GenericRequestHandler;

public class AlfrescoVoiceScriptSkill extends AlfrescoVoiceSkill {

	private ServiceRegistry serviceRegistry;

	// private Map<String,NodeRef> scriptSkills;

	@Override
	protected void registerIntents() {
		// TODO register all scripts from some folders

	}
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	/**
	 * Register or update script intent
	 * 
	 * @param jsNode
	 */
	public void registerScriptIntent(NodeRef jsNode) {

		String intentId = this.getIntentIdFromName(jsNode);
		// Remove handler if exist
		this.unregisterScriptIntentIfExist(intentId);
		
		// create intent
		AlfrescoVoiceSessionIntent intent = new AlfrescoVoiceScriptIntent(jsNode, this.serviceRegistry.getScriptService(), this.serviceRegistry.getContentService());
		handlers.add(new DefaultSessionIntentHandler(this.skillName, intentId, intent));
	}
	
	public String getIntentIdFromName(NodeRef node) {
		String intentId = (String) this.serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_NAME);

		// cut extension from name
		intentId = intentId.substring(0, intentId.lastIndexOf("."));
		return intentId;
	}

	public void unregisterScriptIntentIfExist(String intentId) {
		for (GenericRequestHandler<HandlerInput, Optional<Response>> handler : this.handlers) {
			if (((DefaultSessionIntentHandler) handler).getIntentId().equals(intentId)) {
				this.handlers.remove(handler);
				break;
			}
		}
	}

}