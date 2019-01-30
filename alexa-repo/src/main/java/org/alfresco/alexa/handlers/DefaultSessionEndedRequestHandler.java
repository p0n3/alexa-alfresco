package org.alfresco.alexa.handlers;

import static com.amazon.ask.request.Predicates.requestType;

import java.util.Optional;

import org.alfresco.alexa.AlfrescoVoiceSkill;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

/**
 * Default handler executed when session ends
 * 
 * @author ltworek
 *
 */
public class DefaultSessionEndedRequestHandler implements RequestHandler {

	private AlfrescoVoiceSkill alexaVoiceSkill;

	public DefaultSessionEndedRequestHandler() {
	}

	public DefaultSessionEndedRequestHandler(AlfrescoVoiceSkill alexaVoiceSkill) {
		this.alexaVoiceSkill = alexaVoiceSkill;
	}

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(requestType(SessionEndedRequest.class));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		if (this.alexaVoiceSkill != null) {
			this.alexaVoiceSkill.cleanup();
		}
		return input.getResponseBuilder().build();
	}

}
