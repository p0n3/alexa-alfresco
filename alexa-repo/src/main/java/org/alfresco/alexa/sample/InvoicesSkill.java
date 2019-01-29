package org.alfresco.alexa.sample;

import java.util.List;
import java.util.Map;

import org.alfresco.alexa.AlfrescoVoiceSessionIntent;
import org.alfresco.alexa.AlfrescoVoiceSkill;
import org.alfresco.alexa.model.SessionResponse;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;

public class InvoicesSkill extends AlfrescoVoiceSkill implements AlfrescoVoiceSessionIntent {

	public static final String ATTR_STAGE = "stage";
	public static final String ATTR_INVOICE = "invoice";
	
	public static final String STAGE_CONFIRM = "confirm";
	public static final String STAGE_CHOICE = "choice";

	private ServiceRegistry serviceRegistry;
	
	@Override
	public SessionResponse getResponse(Map<String, Object> attributes, Map<String, String> slots) {
		
		
		
		SessionResponse sessionResponse = new SessionResponse();
		
		String stage = (String) attributes.get(ATTR_STAGE);
		if(stage == null) {
			String invoiceType = slots.get("InvoiceType");
			
			NodeService ns = this.serviceRegistry.getNodeService();
			
			String userName = AuthenticationUtil.getFullyAuthenticatedUser();
			NodeRef person = this.serviceRegistry.getPersonService().getPerson(userName);
			NodeRef homeFolder = (NodeRef) ns.getProperty(person, ContentModel.PROP_HOMEFOLDER);
			
			StoreRef store = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
			String homeStr = ns.getPath(homeFolder).toPrefixString(this.serviceRegistry.getNamespaceService());
			
			String query = "TYPE:\"cm:content\" AND PATH:\""+homeStr+"/cm:Invoices/cm:Unpaid/*\"";
			if(invoiceType != null) {
				query += " @cm\\:name:"+invoiceType;
			}
			ResultSet rs = this.serviceRegistry.getSearchService().query(store, SearchService.LANGUAGE_LUCENE, query);

			List<NodeRef> docs = rs.getNodeRefs();
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
				//TODO
			}

			
			//this.serviceRegistry.getSearchService().query
			//ns.getChildAssocs(unpaidFolder, childNodeTypeQNames)
			//stage = "choice";
			
		}
		else if(stage.equals("choice")) {
			//String invoiceList = (String) attributes.get("invoiceList");
			//TODO
		}
		else if(stage.equals(STAGE_CONFIRM)) {
			String invoice = (String) attributes.get(ATTR_INVOICE);
			String confirmStatus = slots.get("Confirm");
			if(confirmStatus != null && confirmStatus.equals("yes")) {
				// mark the invoice as paid
				NodeService ns = this.serviceRegistry.getNodeService();
				
				NodeRef inv = new NodeRef(invoice);
				
				String userName = AuthenticationUtil.getFullyAuthenticatedUser();
				NodeRef person = this.serviceRegistry.getPersonService().getPerson(userName);
				NodeRef homeFolder = (NodeRef) ns.getProperty(person, ContentModel.PROP_HOMEFOLDER);
				NodeRef invoicesFolder = ns.getChildByName(homeFolder, ContentModel.ASSOC_CONTAINS, "Invoices");
//				NodeRef unpaidFolder = ns.getChildByName(invoicesFolder, ContentModel.ASSOC_CONTAINS, "cm:Unpaid");				
				NodeRef paidFolder = ns.getChildByName(invoicesFolder, ContentModel.ASSOC_CONTAINS, "Paid");				
				
				ns.moveNode(inv, paidFolder, ContentModel.ASSOC_CONTAINS, ns.getPrimaryParent(inv).getQName());
				
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

}
