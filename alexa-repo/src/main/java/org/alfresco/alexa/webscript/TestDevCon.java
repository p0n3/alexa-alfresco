package org.alfresco.alexa.webscript;

import org.alfresco.alexa.service.InvoiceService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestDevCon extends DeclarativeWebScript {

    private InvoiceService invoiceService;
    private final Logger logger = Logger.getLogger(InvoiceService.class);

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {
        logger.debug("Test DevCon...");

        Map<String, Object> model = new HashMap<>();


        String unpaidDocuments = getUnpaidDocuments();
        String paidDocuments = getPaidDocuments();

        model.put("unpaidDocuments", unpaidDocuments);
        model.put("paidDocuments", paidDocuments);

        model.put("success", "false");
        return model;
    }

    public String getUnpaidDocuments() {
        List<NodeRef> unpaidDocuments = invoiceService.getUnpaidDocuments();
        return unpaidDocuments.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    public String getPaidDocuments() {
        List<NodeRef> paidDocuments = invoiceService.getPaidDocuments();
        return paidDocuments.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

}
