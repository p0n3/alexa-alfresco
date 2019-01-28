package org.alfresco.alexa.model;

import org.alfresco.service.namespace.QName;

public abstract interface AlexaModel {
	public final static String NAMESPACE = "http://www.alfresco.org/alexa/model/content/1.0";
	
	public final static QName ASPECT_ALEXA_USER= QName.createQName(NAMESPACE, "alexaUser");
	public final static QName PROP_AUTH_TOKENS = QName.createQName(NAMESPACE, "authTokens");
	
}
