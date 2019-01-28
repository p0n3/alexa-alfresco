package org.alfresco.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DefaultFallbackIntentHandler implements RequestHandler {

	private String cardTitle = "Alfresco";
	private String speechText = "Sorry, I don't know that. You can say try saying help!";
	
	public DefaultFallbackIntentHandler() {
	}
	
	public DefaultFallbackIntentHandler(String cardTitle) {
		this.cardTitle = cardTitle;
	}
	
	public DefaultFallbackIntentHandler(String cardTitle, String speechText) {
		this.cardTitle = cardTitle;
		this.speechText = speechText;
	}
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.FallbackIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().withSpeech(this.speechText).withSimpleCard(this.cardTitle, this.speechText).withReprompt(this.speechText).build();
    }
}
