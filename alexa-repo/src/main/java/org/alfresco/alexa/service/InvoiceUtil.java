package org.alfresco.alexa.service;

import org.alfresco.alexa.model.InvoiceModel;
import org.alfresco.model.ContentModel;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.surf.exception.PlatformRuntimeException;

import java.util.List;

/**
 * Contains util methods for Invoices
 *
 * @author urosvukasinovic
 */
public class InvoiceUtil {

    private AlfrescoWrapper alfrescoWrapper;

    public List<NodeRef> getUnpaidDocuments(String name) {
        return alfrescoWrapper.getNodeRefs(getUnpaidFolder(), ContentModel.TYPE_CONTENT, name);
    }

    public List<NodeRef> getUnpaidDocuments() {
        return alfrescoWrapper.getNodeRefs(getUnpaidFolder(), ContentModel.TYPE_CONTENT);
    }

    public List<NodeRef> getPaidDocuments(String name) {
        return alfrescoWrapper.getNodeRefs(getPaidFolder(), ContentModel.TYPE_CONTENT, name);
    }

    public List<NodeRef> getPaidDocuments() {
        return alfrescoWrapper.getNodeRefs(getPaidFolder(), ContentModel.TYPE_CONTENT);
    }

    public void moveFromUnpaidToPaid(NodeRef paidDocumentNodeRef) throws FileNotFoundException {
        NodeRef paidFolder = getPaidFolder();
        alfrescoWrapper.getFileFolderService().move(paidDocumentNodeRef, paidFolder, null);
    }

    public boolean doesntExist(String nodeName) {
        return doesntExist(new NodeRef(nodeName));
    }

    public boolean doesntExist(NodeRef nodeRef) {
        return !isExists(nodeRef);
    }

    public boolean isExists(String nodeName) {
        return isExists(new NodeRef(nodeName));
    }

    public boolean isExists(NodeRef nodeRef) {
        return alfrescoWrapper.getNodeService().exists(nodeRef);
    }

    public NodeRef getPaidDocument(String documentName) {
        NodeRef paidFolder = getPaidFolder();
        NodeRef paidDocument = getChildByName(paidFolder, documentName);

        if (paidDocument == null) {
            throw new PlatformRuntimeException("Document " + documentName + " can't be found under paid folder " + InvoiceModel.PAID_FOLDER);
        }

        return paidDocument;
    }

    public NodeRef getPaidFolder() {
        NodeRef invoicesFolder = getInvoicesFolder();
        return getChildByName(invoicesFolder, InvoiceModel.PAID_FOLDER);
    }

    public NodeRef getUnpaidFolder() {
        NodeRef invoicesFolder = getInvoicesFolder();
        return getChildByName(invoicesFolder, InvoiceModel.UNPAID_FOLDER);
    }


    public NodeRef getInvoicesFolder() {
        NodeRef homeFolder = getHomeFolder();
        return getChildByName(homeFolder, InvoiceModel.INVOICES_FOLDER);
    }

    public NodeRef getHomeFolder() {
        String userName = AuthenticationUtil.getFullyAuthenticatedUser();

        NodeRef person = alfrescoWrapper.getPersonService().getPerson(userName);
        NodeRef homeFolder = (NodeRef) alfrescoWrapper.getNodeService().getProperty(person, ContentModel.PROP_HOMEFOLDER);
        return homeFolder;
    }

    public NodeRef getChildByName(NodeRef parentNodeRef, String childName) {
        return alfrescoWrapper.getNodeService().getChildByName(parentNodeRef, ContentModel.ASSOC_CONTAINS, childName);
    }

    public AlfrescoWrapper getAlfrescoWrapper() {
        return alfrescoWrapper;
    }

    public void setAlfrescoWrapper(AlfrescoWrapper alfrescoWrapper) {
        this.alfrescoWrapper = alfrescoWrapper;
    }
}
