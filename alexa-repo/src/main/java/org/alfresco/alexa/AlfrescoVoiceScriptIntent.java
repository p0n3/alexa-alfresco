package org.alfresco.alexa;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.alexa.model.SessionResponse;
import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.ScriptService;

/**
 * Intent for JS Scripts
 * 
 * @author ltworek
 *
 */
public class AlfrescoVoiceScriptIntent implements AlfrescoVoiceSessionIntent {

	private ScriptService scriptService;
	private ContentService contentService;

	private NodeRef scriptNode;

	public AlfrescoVoiceScriptIntent(NodeRef script, ScriptService scriptService, ContentService contentService) {
		this.scriptNode = script;
		this.scriptService = scriptService;
		this.contentService = contentService;
	}

	public AlfrescoVoiceScriptIntent(NodeRef script) {
		this.scriptNode = script;
	}

	public void setScriptService(ScriptService scriptService) {
		this.scriptService = scriptService;
	}

	public void setContentService(ContentService contentService) {
		this.contentService = contentService;
	}

	private String getScriptFromFile() {

		ContentReader cr = this.contentService.getReader(this.scriptNode, ContentModel.PROP_CONTENT);
		if (cr == null || cr.exists() == false) {
			return null;
		}

		// get the content of the file
		return cr.getContentString();
	}

	@Override
	public SessionResponse getResponse(Map<String, Object> attributes, Map<String, String> slots) {

		String script = getScriptFromFile();

		SessionResponse sessionResponse = new SessionResponse();
		Map<String, Object> responseAttributes = new HashMap<String, Object>();

		Map<String, Object> model = new HashMap<String, Object>();

		model.put("requestAttributes", attributes);
		model.put("responseAttributes", responseAttributes);
		model.put("slots", slots);

		String response = (String) scriptService.executeScriptString(script, model);

		// TODO setShouldEndSession should be set in js script
		sessionResponse.setShouldEndSession(true);
		sessionResponse.setSpeechText(response);
		sessionResponse.setAttributes(responseAttributes);

		return sessionResponse;

	}

}
