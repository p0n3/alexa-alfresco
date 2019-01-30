package org.alfresco.alexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.alfresco.alexa.AlfrescoVoiceSimpleIntent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

/**
 * Default handler for simple intents (without session parameters)
 * 
 * @author ltworek
 *
 */
public class DefaultSimpleIntentHandler implements RequestHandler {

	String cardTitle = "Alfresco";

	String intentId;
	AlfrescoVoiceSimpleIntent alfrescoVoiceIntent;

	
	public DefaultSimpleIntentHandler(String intentId, AlfrescoVoiceSimpleIntent alfrescoVoiceIntent) {
		this.intentId = intentId;
		this.alfrescoVoiceIntent = alfrescoVoiceIntent;
	}
	
    public DefaultSimpleIntentHandler(String cardTitle, String intentId, AlfrescoVoiceSimpleIntent alfrescoVoiceIntent) {
		this.cardTitle = cardTitle;
		this.intentId = intentId;
		this.alfrescoVoiceIntent = alfrescoVoiceIntent;
	}
    

	@Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(this.intentId));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
       String speechText = alfrescoVoiceIntent.getResponse();
       return input.getResponseBuilder().withSpeech(speechText).withSimpleCard(this.cardTitle, speechText).build();
    }

}
