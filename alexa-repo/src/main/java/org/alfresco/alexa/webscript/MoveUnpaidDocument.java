package org.alfresco.alexa.webscript;

import org.alfresco.alexa.service.InvoiceService;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.HashMap;
import java.util.Map;

public class MoveUnpaidDocument extends DeclarativeWebScript {

    private InvoiceService invoiceService;
    private final Logger logger = Logger.getLogger(InvoiceService.class);


    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {
        logger.debug("Move Unpaid Document is called...");
        String documentName = request.getParameter("documentName");

        Map<String, Object> model = new HashMap<>();

        try {
            invoiceService.moveFromUnpaidToPaid(new NodeRef(documentName));
            model.put("success", "true");
            return model;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        model.put("success", "false");
        return model;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

}
