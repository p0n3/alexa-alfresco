package org.alfresco.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

/**
 * Default cancel intent invoked when skill is cancelled
 * 
 * @author ltworek
 *
 */
public class DefaultCancelIntentHandler implements RequestHandler {

	private String cardTitle = "Alfresco";
	private String speechText = "Goodbye";

	public DefaultCancelIntentHandler() {
	}

	public DefaultCancelIntentHandler(String cardTitle) {
		this.cardTitle = cardTitle;
	}

	public DefaultCancelIntentHandler(String cardTitle, String speechText) {
		this.cardTitle = cardTitle;
		this.speechText = speechText;
	}

	@Override
	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("AMAZON.CancelIntent"));
	}

	@Override
	public Optional<Response> handle(HandlerInput input) {
		return input.getResponseBuilder().withSpeech(speechText).withSimpleCard(this.cardTitle, this.speechText).build();
	}
}
