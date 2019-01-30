/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.alfresco.alexa.webscript;

import static com.amazon.ask.servlet.ServletConstants.DEFAULT_TOLERANCE_MILLIS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.alexa.service.AlexaService;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.repo.security.authentication.AuthenticationUtil.RunAsWork;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.amazon.ask.Skill;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.servlet.util.ServletUtils;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.servlet.verifiers.SkillRequestTimestampVerifier;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import com.amazon.ask.util.JacksonSerializer;

/**
 * Alexa Endpoind Webscript. It process request from alexa and invokes suitable
 * skill
 * 
 * @author ltworek
 *
 */
public class EndpointWebScript extends DeclarativeWebScript {

	private static Log logger = LogFactory.getLog(EndpointWebScript.class);

	private AlexaService alexaService;

	private List<SkillServletVerifier> verifiers;

	private final Serializer serializer = new JacksonSerializer();

	public EndpointWebScript() {

		List<SkillServletVerifier> defaultVerifiers = new ArrayList<>();
		if (!ServletUtils.isRequestSignatureCheckSystemPropertyDisabled()) {
			defaultVerifiers.add(new SkillRequestSignatureVerifier());
		}
		Long timestampToleranceProperty = ServletUtils.getTimeStampToleranceSystemProperty();
		defaultVerifiers.add(new SkillRequestTimestampVerifier(timestampToleranceProperty != null ? timestampToleranceProperty : DEFAULT_TOLERANCE_MILLIS));

		this.verifiers = defaultVerifiers;

	}

	public void setAlexaService(AlexaService alexaService) {
		this.alexaService = alexaService;
	}

	protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
		Map<String, Object> model = new HashMap<String, Object>();

		logger.debug("Endpoint webscript was called!");

		RequestEnvelope requestEnvelope;
		try {
			requestEnvelope = serializer.deserialize(req.getContent().getContent(), RequestEnvelope.class);

			String skillId = requestEnvelope.getSession().getApplication().getApplicationId();
			String accessToken = requestEnvelope.getSession().getUser().getAccessToken();

			Skill skill = this.alexaService.getSkillById(skillId);
			String userName = this.alexaService.validateAccessToken(accessToken, skillId);

			ResponseEnvelope skillResponse = null;
			if (userName == null) {
				// no auth
				skillResponse = skill.invoke(requestEnvelope);
			} else {
				skillResponse = AuthenticationUtil.runAs(new RunAsWork<ResponseEnvelope>() {
					@Override
					public ResponseEnvelope doWork() throws Exception {
						return skill.invoke(requestEnvelope);
					}

				}, userName);
			}

			String res = serializer.serialize(skillResponse);
			model.put("alexaRes", res);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return model;
	}

}