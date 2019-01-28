package org.alfresco.alexa;

import org.alfresco.alexa.service.AlexaService;

public class TestTest {

	public static void main(String[] args) {
		AlexaService alexaService = new AlexaService();
		
		String clientId = "amzn1.ask.skill.6aac0bad-4288-4c3f-9575-4c4064eda0ce";
		String token = "d3c7b28b-2199-4f02-b69d-b0899564331a";
		
		String hashedToken = alexaService.hashToken(token, clientId);
		
		System.out.println(hashedToken);

	}

}
