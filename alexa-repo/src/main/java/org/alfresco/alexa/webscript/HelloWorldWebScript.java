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

import org.alfresco.alexa.handlers.DefaultStopIntentHandler;
import org.alfresco.alexa.handlers.HelloWorldIntentHandler;
import org.alfresco.alexa.handlers.DefaultHelpIntentHandler;
import org.alfresco.alexa.handlers.DefaultLaunchRequestHandler;
import org.alfresco.alexa.handlers.DefaultSessionEndedRequestHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.servlet.SkillServlet;
import com.amazon.ask.servlet.util.ServletUtils;
import com.amazon.ask.servlet.verifiers.SkillRequestSignatureVerifier;
import com.amazon.ask.servlet.verifiers.SkillRequestTimestampVerifier;
import com.amazon.ask.servlet.verifiers.SkillServletVerifier;
import com.amazon.ask.util.JacksonSerializer;

/**
 * A demonstration Java controller for the Hello World Web Script.
 *
 * @author martin.bergljung@alfresco.com
 * @since 2.1.0
 */
public class HelloWorldWebScript extends DeclarativeWebScript {
    private static Log logger = LogFactory.getLog(HelloWorldWebScript.class);
    
    private static final Logger log = LoggerFactory.getLogger(SkillServlet.class);
    private static final long serialVersionUID = 3257254794185762002L;

    private transient  Skill skill;
    private transient final List<SkillServletVerifier> verifiers;
    private transient final Serializer serializer = new JacksonSerializer();

    public HelloWorldWebScript() {
		// TODO Auto-generated constructor stub
	

        List<SkillServletVerifier> defaultVerifiers = new ArrayList<>();
        if (!ServletUtils.isRequestSignatureCheckSystemPropertyDisabled()) {
            defaultVerifiers.add(new SkillRequestSignatureVerifier());
        }
        Long timestampToleranceProperty = ServletUtils.getTimeStampToleranceSystemProperty();
        defaultVerifiers.add(new SkillRequestTimestampVerifier(timestampToleranceProperty != null
                ? timestampToleranceProperty : DEFAULT_TOLERANCE_MILLIS));
        this.skill = HelloWorldWebScript.getSkill();
        this.verifiers = defaultVerifiers;
    
    }
    
    protected Map<String, Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        Map<String, Object> model = new HashMap<String, Object>();
//        model.put("fromJava", "HelloFromJava");

        logger.debug("Your 'Hello World' Web Script was called!");
        
        //byte[] serializedRequestEnvelope = IOUtils.toByteArray(req.getInputStream());

        RequestEnvelope deserializedRequestEnvelope;
		try {
			deserializedRequestEnvelope = serializer.deserialize(req.getContent().getContent(), RequestEnvelope.class);

	        ResponseEnvelope skillResponse = skill.invoke(deserializedRequestEnvelope);
	        
	        String res = serializer.serialize(skillResponse);
	        model.put("alexaRes", res);

	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        final RequestEnvelope deserializedRequestEnvelope = serializer.deserialize(IOUtils.toString(
//                serializedRequestEnvelope, ServletConstants.CHARACTER_ENCODING), RequestEnvelope.class);

        // Verify the authenticity of the request by executing configured verifiers.
//        for (SkillServletVerifier verifier : verifiers) {
//            verifier.verify(request, serializedRequestEnvelope, deserializedRequestEnvelope);
//        }
        
        
        

        return model;
    }
    
    
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new DefaultStopIntentHandler(),
                        new HelloWorldIntentHandler(),
                        new DefaultHelpIntentHandler(),
                        new DefaultLaunchRequestHandler(),
                        new DefaultSessionEndedRequestHandler())
                // Add your skill id below
                //.withSkillId("")
                .build();
    }
    
}