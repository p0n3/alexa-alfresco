package org.alfresco.alexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.alfresco.alexa.AlfrescoVoiceSessionIntent;
import org.alfresco.alexa.model.SessionResponse;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.response.ResponseBuilder;

/**
 * Default handler for intents that use session attributes, slots
 * 
 * @author ltworek
 *
 */
public class DefaultSessionIntentHandler implements RequestHandler {

	String cardTitle = "Alfresco";

	String intentId;
	AlfrescoVoiceSessionIntent alfrescoVoiceIntent;

	public DefaultSessionIntentHandler(String intentId, AlfrescoVoiceSessionIntent alfrescoVoiceIntent) {
		this.intentId = intentId;
		this.alfrescoVoiceIntent = alfrescoVoiceIntent;
	}

	public DefaultSessionIntentHandler(String cardTitle, String intentId, AlfrescoVoiceSessionIntent alfrescoVoiceIntent) {
		this.cardTitle = cardTitle;
		this.intentId = intentId;
		this.alfrescoVoiceIntent = alfrescoVoiceIntent;
	}

	public String getIntentId() {
		return intentId;
	}

	private Map<String, String> getSlots(HandlerInput input) {
		Map<String, String> res = new HashMap<String, String>();

		Request req = input.getRequestEnvelope().getRequest();
		if (!(req instanceof IntentRequest) || ((IntentRequest) req).getIntent() == null) {
			return res;
		}
		Map<String, Slot> slotMaps = ((IntentRequest) req).getIntent().getSlots();
		if (slotMaps == null) {
			return res;
		}
		for (String key : slotMaps.keySet()) {
			res.put(key, slotMaps.get(key).getValue());
		}

		return res;
	}

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName(this.intentId));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {

		AttributesManager attributesManager = input.getAttributesManager();

		SessionResponse sessionResponse = alfrescoVoiceIntent.getResponse(attributesManager.getSessionAttributes(), this.getSlots(input));
		String speechText = sessionResponse.getSpeechText();

		if (sessionResponse.hasAttributes()) {
			attributesManager.setSessionAttributes(sessionResponse.getAttributes());
		}

		ResponseBuilder responseBuilder = input.getResponseBuilder();
		responseBuilder.withSpeech(speechText).withSimpleCard(this.cardTitle, speechText);
		responseBuilder.withShouldEndSession(sessionResponse.shouldEndSession());

		return responseBuilder.build();
	}

}
