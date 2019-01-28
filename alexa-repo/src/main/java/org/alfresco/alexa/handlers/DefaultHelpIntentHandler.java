package org.alfresco.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class DefaultHelpIntentHandler implements RequestHandler {

	private String cardTitle = "Alfresco";
	private String helpText = "I don't know";
	
	public DefaultHelpIntentHandler() {
	}
	
	public DefaultHelpIntentHandler(String cardTitle) {
		this.cardTitle = cardTitle;
	}
	
	public DefaultHelpIntentHandler(String cardTitle, String helpText) {
		this.cardTitle = cardTitle;
		this.helpText = helpText;
	}
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().withSpeech(this.helpText).withSimpleCard(this.cardTitle, this.helpText).withReprompt(this.helpText).build();
    }
}
