package org.alfresco.alexa.sample;

import org.alfresco.alexa.AlfrescoVoiceSimpleIntent;
import org.alfresco.alexa.AlfrescoVoiceSkill;

/**
 * Example skill
 * 
 * @author ltworek
 *
 */
public class HelloSkill extends AlfrescoVoiceSkill implements AlfrescoVoiceSimpleIntent {

	@Override
	protected void registerIntents() {

		this.registerSimpleIntent("HelloWorldIntent", this);

	}

	@Override
	public String getResponse() {
		return "Hello alfresco 123";
	}

}
