package org.alfresco.alexa.webscript;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.alexa.model.AlexaModel;
import org.alfresco.alexa.service.AlexaService;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptException;
import org.springframework.extensions.webscripts.WebScriptRequest;

/**
 * Webscript created for linking alexa account with alfresco
 * 
 * @author ltworek
 *
 */
public class AuthorizationWebScript extends DeclarativeWebScript {

	private PersonService personService;
	private NodeService nodeService;
	private AlexaService alexaService;

	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public void setAlexaService(AlexaService alexaService) {
		this.alexaService = alexaService;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {

		Map<String, Object> res = new HashMap<String, Object>();

		// Parse the JSON, if supplied
		JSONObject json = null;
		String contentType = req.getContentType();
		if (contentType != null && contentType.indexOf(';') != -1) {
			contentType = contentType.substring(0, contentType.indexOf(';'));
		}

		if (MimetypeMap.MIMETYPE_JSON.equals(contentType)) {
			JSONParser parser = new JSONParser();
			try {
				json = (JSONObject) parser.parse(req.getContent().getContent());
			} catch (IOException io) {
				throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Invalid JSON: " + io.getMessage());
			} catch (ParseException pe) {
				throw new WebScriptException(Status.STATUS_BAD_REQUEST, "Invalid JSON: " + pe.getMessage());
			}
		}

		String personId = AuthenticationUtil.getFullyAuthenticatedUser();
		NodeRef person = personService.getPerson(personId);

		if (!nodeService.hasAspect(person, AlexaModel.ASPECT_ALEXA_USER)) {
			nodeService.addAspect(person, AlexaModel.ASPECT_ALEXA_USER, null);
		}

		@SuppressWarnings("unchecked")
		List<String> tokens = (List<String>) nodeService.getProperty(person, AlexaModel.PROP_AUTH_TOKENS);
		if (tokens == null) {
			tokens = new ArrayList<String>();
		}

		String token = alexaService.generateRandomToken();
		String clientId = (String) json.get("client_id");

		String hashedToken = alexaService.hashToken(token, clientId);
		String accessToken = alexaService.generateAccessToken(token, personId);

		tokens.add(hashedToken);

		nodeService.setProperty(person, AlexaModel.PROP_AUTH_TOKENS, (Serializable) tokens);

		res.put("accessToken", accessToken);
		return res;
	}

}
