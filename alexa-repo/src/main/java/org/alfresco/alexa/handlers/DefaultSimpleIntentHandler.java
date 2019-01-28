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

import org.alfresco.alexa.AlfrescoVoiceSimpleIntent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

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
