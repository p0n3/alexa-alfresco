package org.alfresco.alexa.webscript;

import org.alfresco.alexa.service.InvoiceService;
import org.alfresco.alexa.service.InvoiceUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import java.util.HashMap;
import java.util.Map;

public class GetHomeFolder extends DeclarativeWebScript {

    private InvoiceUtil invoiceUtil;
    private final Logger logger = Logger.getLogger(InvoiceService.class);

    @Override
    protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {
        logger.debug("Get Home Folder is called");

        Map<String, Object> model = new HashMap<>();

        NodeRef homeFolder = invoiceUtil.getHomeFolder();
        logger.debug("Got Home Folder: " + homeFolder);

        NodeRef paidFolder = invoiceUtil.getPaidFolder();
        logger.debug("Got Paid Folder: " + paidFolder);

        NodeRef unpaidFolder = invoiceUtil.getUnpaidFolder();
        logger.debug("Got Unpaid Folder: " + paidFolder);

        model.put("homeFolder", homeFolder.toString());
        model.put("paidFolder", paidFolder.toString());
        model.put("unpaidFolder", unpaidFolder.toString());

        return model;
    }

    public InvoiceUtil getInvoiceUtil() {
        return invoiceUtil;
    }

    public void setInvoiceUtil(InvoiceUtil invoiceUtil) {
        this.invoiceUtil = invoiceUtil;
    }

}
