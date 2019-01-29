package org.alfresco.alexa.service;

import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

import java.util.List;

public class InvoiceService {

    private final Logger logger = Logger.getLogger(InvoiceService.class);

    private InvoiceUtil invoiceUtil;

    public void moveFromUnpaidToPaid(NodeRef nodeRef) throws FileNotFoundException {
        logger.debug("Move document with nodeRef " + nodeRef.toString() + " from Unpaid to Paid folder");
        invoiceUtil.moveFromUnpaidToPaid(nodeRef);
        logger.debug("Document is moved to Paid Folder!");
    }

    public List<NodeRef> getUnpaidDocuments() {
        logger.debug("Get Unpaid Documents");
        List<NodeRef> unpaidDocuments = invoiceUtil.getUnpaidDocuments();
        logger.debug("Got Unpaid Documents with size: " + unpaidDocuments.size());
        return unpaidDocuments;
    }

    public List<NodeRef> getUnpaidDocuments(String name) {
        logger.debug("Get Unpaid Documents by name " + name);
        List<NodeRef> unpaidDocuments = invoiceUtil.getUnpaidDocuments(name);
        logger.debug("Got Unpaid Documents with size: " + unpaidDocuments.size());
        return unpaidDocuments;
    }

    public List<NodeRef> getPaidDocuments() {
        logger.debug("Get Paid Documents");
        List<NodeRef> paidDocuments = invoiceUtil.getPaidDocuments();
        logger.debug("Got Paid Documents with size: " + paidDocuments.size());
        return paidDocuments;
    }

    public List<NodeRef> getPaidDocuments(String name) {
        logger.debug("Get Paid Documents by name " + name);
        List<NodeRef> paidDocuments = invoiceUtil.getPaidDocuments(name);
        logger.debug("Got Paid Documents with size: " + paidDocuments.size());
        return paidDocuments;
    }

    public InvoiceUtil getInvoiceUtil() {
        return invoiceUtil;
    }

    public void setInvoiceUtil(InvoiceUtil invoiceUtil) {
        this.invoiceUtil = invoiceUtil;
    }
}
