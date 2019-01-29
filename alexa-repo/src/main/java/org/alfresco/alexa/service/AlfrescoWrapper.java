package org.alfresco.alexa.service;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PersonService;

public class AlfrescoWrapper {

    private ServiceRegistry serviceRegistry;

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
}
