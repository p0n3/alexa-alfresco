package org.alfresco.alexa.webscript;

import org.alfresco.alexa.service.InvoiceService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.lang.StringUtils;
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

        populateModelWithDocuments(model);

        populateModelWithDocuments(model, request.getParameter("documentName"));

        return model;
    }

    private void populateModelWithDocuments(Map<String, Object> model, String documentName) {

        if (documentName == null || StringUtils.isBlank(documentName)) {
            model.put("unpaidDocumentsByName", "");
            model.put("paidDocumentsByName", "");
            return;
        }

        String unpaidDocuments = getUnpaidDocuments(documentName);
        String paidDocuments = getPaidDocuments(documentName);

        model.put("unpaidDocumentsByName", unpaidDocuments);
        model.put("paidDocumentsByName", paidDocuments);
    }

    private void populateModelWithDocuments(Map<String, Object> model) {
        String unpaidDocuments = getUnpaidDocuments();
        String paidDocuments = getPaidDocuments();

        model.put("unpaidDocuments", unpaidDocuments);
        model.put("paidDocuments", paidDocuments);
    }

    public String getUnpaidDocuments() {
        return convertToString(invoiceService.getUnpaidDocuments());
    }

    public String getUnpaidDocuments(String documentName) {
        return convertToString(invoiceService.getUnpaidDocuments(documentName));
    }

    public String getPaidDocuments() {
        return convertToString(invoiceService.getPaidDocuments());
    }

    public String getPaidDocuments(String documentName) {
        return convertToString(invoiceService.getPaidDocuments(documentName));
    }

    public String convertToString(List<NodeRef> list) {
        return list.stream().map(Object::toString)
                .collect(Collectors.joining(", "));
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

}
