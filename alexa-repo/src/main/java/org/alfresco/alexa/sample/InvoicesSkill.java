package org.alfresco.alexa.sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.alfresco.alexa.AlfrescoVoiceSessionIntent;
import org.alfresco.alexa.AlfrescoVoiceSkill;
import org.alfresco.alexa.model.SessionResponse;
import org.alfresco.alexa.service.InvoiceService;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;

public class InvoicesSkill extends AlfrescoVoiceSkill implements AlfrescoVoiceSessionIntent {

	public static final String ATTR_STAGE = "stage";
	public static final String ATTR_INVOICE = "invoice";
	public static final String ATTR_INVOICES = "invoices";
	
	public static final String STAGE_CONFIRM = "confirm";
	public static final String STAGE_CHOICE = "choice";

	public static final String SLOT_INVOICE_TYPE = "InvoiceType";
	public static final String SLOT_NUMBER = "Number";
	

	private ServiceRegistry serviceRegistry;
	private InvoiceService invoiceService;
	
	@Override
	public SessionResponse getResponse(Map<String, Object> attributes, Map<String, String> slots) {
		
		SessionResponse sessionResponse = new SessionResponse();
		NodeService ns = this.serviceRegistry.getNodeService();
		
		String stage = (String) attributes.get(ATTR_STAGE);
		if(stage == null) {
			String invoiceType = slots.get(SLOT_INVOICE_TYPE);
			
			
			List<NodeRef> docs;
			
			if(invoiceType != null) {
				docs = invoiceService.getUnpaidDocuments(invoiceType);
			}
			else {
				docs = invoiceService.getUnpaidDocuments();
				
			}
			
			if(docs == null || docs.size() == 0) {
				sessionResponse.setShouldEndSession(true);
				String msg = "";
				if(invoiceType != null) {
					msg += invoiceType + " ";
				}
				msg += "unpaid document not found";
				sessionResponse.setSpeechText(msg);
				return sessionResponse;
			}
			else if(docs.size() == 1) {
				sessionResponse.setAttribute(ATTR_STAGE, STAGE_CONFIRM);
				sessionResponse.setAttribute(ATTR_INVOICE, docs.get(0).toString());
				sessionResponse.setShouldEndSession(false);
				sessionResponse.setSpeechText("do you want to mark the "+(invoiceType != null ? invoiceType+" " : "")+"invoice as paid?");
				return sessionResponse;
			}
			else {
				String text = "Which one do you want to mark as paid? ";
				
				List<String> invoices = new ArrayList<String>();
				int ind = 0;
				
				for(NodeRef doc : docs) {
					ind++;
					
					String fileName = (String) ns.getProperty(doc, ContentModel.PROP_NAME);
					text += ind + " - the " + fileName.substring(0, fileName.indexOf("-")).toLowerCase() + "invoice, ";
					invoices.add(doc.toString());
				}
				
				sessionResponse.setAttribute(ATTR_STAGE, STAGE_CHOICE);
				sessionResponse.setAttribute(ATTR_INVOICES, String.join(",", invoices));
				sessionResponse.setShouldEndSession(false);
				sessionResponse.setSpeechText(text);
				return sessionResponse;
				
			}
		}
		else if(stage.equals("choice")) {
			String invoicesStr = (String) attributes.get(ATTR_INVOICES);
			int number = Integer.parseInt((String) slots.get(SLOT_NUMBER))-1;
			
			String invoiceNodeRef = invoicesStr.split(",")[number];
			String fileName = (String) ns.getProperty(new NodeRef(invoiceNodeRef), ContentModel.PROP_NAME);
			
			sessionResponse.setAttribute(ATTR_STAGE, STAGE_CONFIRM);
			sessionResponse.setAttribute(ATTR_INVOICE, invoiceNodeRef);
			sessionResponse.setShouldEndSession(false);
			sessionResponse.setSpeechText("Are you sure, you want to mark the "+fileName.substring(0, fileName.indexOf("-")).toLowerCase()+"invoice as paid?");
			return sessionResponse;
		}
		else if(stage.equals(STAGE_CONFIRM)) {
			String invoice = (String) attributes.get(ATTR_INVOICE);
			String confirmStatus = slots.get("Confirm");
			if(confirmStatus != null && confirmStatus.equals("yes")) {
				
				try {
					
					this.invoiceService.moveFromUnpaidToPaid(new NodeRef(invoice));
					
				} catch (FileNotFoundException e) {
					sessionResponse.setShouldEndSession(true);
					sessionResponse.setSpeechText("The invoice not found");
					return sessionResponse;
				}
				
				sessionResponse.setShouldEndSession(true);
				sessionResponse.setSpeechText("The invoice was mark as paid");
				return sessionResponse;
			}
		}
		
		return null;
	}

	@Override
	protected void registerIntents() {
		this.registerSessionIntent("MarkAsPaidIntent", this);
		
	}
	
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
	
	public void setInvoiceService(InvoiceService invoiceService) {
		this.invoiceService = invoiceService;
	}

}
