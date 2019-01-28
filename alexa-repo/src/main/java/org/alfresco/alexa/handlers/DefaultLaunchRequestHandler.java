package org.alfresco.alexa.handlers;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class DefaultLaunchRequestHandler implements RequestHandler {

	private String cardTitle = "Alfresco";
	private String speechText = "Welcome to the Alfresco";
	
	public DefaultLaunchRequestHandler() {
	}
	
	public DefaultLaunchRequestHandler(String cardTitle) {
		this.cardTitle = cardTitle;
	}
	
	public DefaultLaunchRequestHandler(String cardTitle, String speechText) {
		this.cardTitle = cardTitle;
		this.speechText = speechText;
	}
	
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder().withSpeech(this.speechText).withSimpleCard(this.cardTitle, this.speechText).withReprompt(this.speechText).build();
    }

}
