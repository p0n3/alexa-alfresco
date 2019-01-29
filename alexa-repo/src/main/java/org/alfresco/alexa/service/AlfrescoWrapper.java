package org.alfresco.alexa.service;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchParameters;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.security.PersonService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

import java.util.List;

public class AlfrescoWrapper {

    private final Logger logger = Logger.getLogger(InvoiceService.class);

    private ServiceRegistry serviceRegistry;

    public List<NodeRef> getNodeRefs(NodeRef parentNodeRef, QName type, String name) {

        String query = "PRIMARYPARENT: '" + parentNodeRef + "' AND =TYPE:'" + type + "'"
                + " AND @cm\\:name:" + '*' + name + '*';

        logger.debug("Executing search with the query: " + query);

        List<NodeRef> nodeRefs = getResults(query).getNodeRefs();

        logger.debug("Got nodeRefs: " + nodeRefs);

        return nodeRefs;
    }

    public List<NodeRef> getNodeRefs(NodeRef parentNodeRef, QName type) {
        String query = "PRIMARYPARENT: '" + parentNodeRef + "' AND =TYPE:'" + type + "'";

        logger.debug("Executing search with the query: " + query);

        List<NodeRef> nodeRefs = getResults(query).getNodeRefs();

        logger.debug("Got nodeRefs: " + nodeRefs);

        return nodeRefs;
    }

    private ResultSet getResults(String query) {
        return getSearchService().query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_FTS_ALFRESCO, query);
    }

    private ResultSet getResults(SearchParameters sp) {
        return getSearchService().query(sp);
    }

    public NodeService getNodeService() {
        return serviceRegistry.getNodeService();
    }

    public FileFolderService getFileFolderService() {
        return serviceRegistry.getFileFolderService();
    }

    public ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    public void setServiceRegistry(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public PersonService getPersonService() {
        return serviceRegistry.getPersonService();
    }

    public SearchService getSearchService() {
        return serviceRegistry.getSearchService();
    }
}
