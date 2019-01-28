/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package org.alfresco.alexa.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

import org.alfresco.alexa.AlfrescoVoiceSessionIntent;
import org.alfresco.alexa.model.SessionResponse;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.response.ResponseBuilder;

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
    

	@Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName(this.intentId));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
       
    	AttributesManager attributesManager = input.getAttributesManager();
    	
    	SessionResponse sessionResponse = alfrescoVoiceIntent.getResponse(attributesManager.getSessionAttributes());
    	//input.getRequestEnvelope().getSession().getAttributes()
    	String speechText = sessionResponse.getSpeechText();
    	
    	if(sessionResponse.hasAttributes()) {
    		attributesManager.setSessionAttributes(sessionResponse.getAttributes());
    	}
    	
    	ResponseBuilder responseBuilder = input.getResponseBuilder();
    	responseBuilder.withSpeech(speechText).withSimpleCard(this.cardTitle, speechText);
    	responseBuilder.withShouldEndSession(sessionResponse.shouldEndSession());
    	
    	
        return responseBuilder.build();
    }

}
