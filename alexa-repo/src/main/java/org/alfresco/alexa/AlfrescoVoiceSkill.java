package org.alfresco.alexa;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.alfresco.alexa.handlers.DefaultCancelIntentHandler;
import org.alfresco.alexa.handlers.DefaultFallbackIntentHandler;
import org.alfresco.alexa.handlers.DefaultHelpIntentHandler;
import org.alfresco.alexa.handlers.DefaultLaunchRequestHandler;
import org.alfresco.alexa.handlers.DefaultSessionEndedRequestHandler;
import org.alfresco.alexa.handlers.DefaultSessionIntentHandler;
import org.alfresco.alexa.handlers.DefaultSimpleIntentHandler;
import org.alfresco.alexa.handlers.DefaultStopIntentHandler;
import org.alfresco.alexa.service.AlexaService;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.GenericRequestHandler;

/**
 * Abstract class for all alfresco skills
 * 
 * @author ltworek
 *
 */
public abstract class AlfrescoVoiceSkill {

	private AlexaService alexaService;

	private Skill skill;

	protected String skillName;
	protected String skillId;

	protected String helpText;
	protected String welcomeText;

	protected List<GenericRequestHandler<HandlerInput, Optional<Response>>> handlers = new ArrayList<GenericRequestHandler<HandlerInput, Optional<Response>>>();

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public void setSkillId(String skillId) {
		this.skillId = skillId;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	public void setWelcomeText(String welcomeText) {
		this.welcomeText = welcomeText;
	}

	public String getSkillId() {
		return skillId;
	}

	public void setAlexaService(AlexaService alexaService) {
		this.alexaService = alexaService;
	}

	protected RequestHandler getStopIntentHandler() {
		return new DefaultStopIntentHandler(this.skillName);
	}

	protected RequestHandler getCancelIntentHandler() {
		return new DefaultCancelIntentHandler(this.skillName);
	}

	protected RequestHandler getHelpIntentHandler() {
		if (this.helpText != null) {
			return new DefaultHelpIntentHandler(this.skillName, this.helpText);
		}
		return new DefaultHelpIntentHandler(this.skillName);
	}

	protected RequestHandler getFallbackIntentHandler() {
		return new DefaultFallbackIntentHandler(this.skillName);
	}

	protected RequestHandler getSessionEndedRequestHandler() {
		return new DefaultSessionEndedRequestHandler(this);
	}

	protected RequestHandler getLaunchIntentHandler() {
		if (this.welcomeText != null) {
			return new DefaultLaunchRequestHandler(this.skillName, this.welcomeText);
		}
		return new DefaultLaunchRequestHandler(this.skillName);
	}

	protected void registerSimpleIntent(String intentId, AlfrescoVoiceSimpleIntent intent) {
		handlers.add(new DefaultSimpleIntentHandler(this.skillName, intentId, intent));
	}

	protected void registerSessionIntent(String intentId, AlfrescoVoiceSessionIntent intent) {
		handlers.add(new DefaultSessionIntentHandler(this.skillName, intentId, intent));
	}

	public void cleanup() {
		// TODO cleanup after skills end
	}

	protected void init() {
		this.registerIntents();
		this.alexaService.registerSkill(this);
	}

	protected abstract void registerIntents();

	public Skill buildSkill() {

		handlers.add(getStopIntentHandler());
		handlers.add(getCancelIntentHandler());
		handlers.add(getHelpIntentHandler());
		handlers.add(getFallbackIntentHandler());
		handlers.add(getLaunchIntentHandler());
		handlers.add(getSessionEndedRequestHandler());

		this.skill = Skills.standard().addRequestHandlers(handlers).withSkillId(this.skillId).build();

		return this.skill;

	}
}
