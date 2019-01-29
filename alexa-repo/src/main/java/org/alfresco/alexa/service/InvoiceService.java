package org.alfresco.alexa.service;

import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

public class InvoiceService {

    private final Logger logger = Logger.getLogger(InvoiceService.class);

    private InvoiceUtil invoiceUtil;

    public void moveFromUnpaidToPaid(NodeRef nodeRef) throws FileNotFoundException {
        logger.debug("Move document with nodeRef " + nodeRef.toString() + " from Unpaid to Paid folder");
        invoiceUtil.moveFromUnpaidToPaid(nodeRef);
        logger.debug("Document is moved to Paid Folder!");
    }

    public InvoiceUtil getInvoiceUtil() {
        return invoiceUtil;
    }

    public void setInvoiceUtil(InvoiceUtil invoiceUtil) {
        this.invoiceUtil = invoiceUtil;
    }
}
