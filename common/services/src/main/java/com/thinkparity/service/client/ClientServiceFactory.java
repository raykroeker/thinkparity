/*
 * Created On:  3-Jun-07 8:39:41 PM
 */
package com.thinkparity.service.client;

import javax.jws.WebService;

import com.thinkparity.service.*;
import com.thinkparity.service.client.http.HttpProxyFactory;

/**
 * <b>Title:</b>thinkParity Service Client Service Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ClientServiceFactory implements ServiceFactory {

    /** A singleton instance of the service factory. */
    private static final ClientServiceFactory SINGLETON;

    static {
        SINGLETON = new ClientServiceFactory();
    }

    /**
     * Obtain an instance of the service factory.
     * 
     * @return A <code>ServiceFactory</code>.
     */
    public static ClientServiceFactory getInstance() {
        return SINGLETON;
    }

    /** A service proxy factory. */
    private final ServiceProxyFactory proxyFactory;

    /**
     * Create ServiceFactory.
     *
     */
    private ClientServiceFactory() {
        super();
        this.proxyFactory = HttpProxyFactory.getInstance();
    }

    /**
     * Obtain an artifact service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public ArtifactService getArtifactService() {
        return (ArtifactService) create(ArtifactService.class);
    }

    /**
     * Obtain a backup service.
     * 
     * @return An instance of <code>BackupService</code>.
     */
    public BackupService getBackupService() {
        return (BackupService) create(BackupService.class);
    }

    /**
     * Obtain a contact service.
     * 
     * @return An instance of <code>ContactService</code>.
     */
    public ContactService getContactService() {
        return (ContactService) create(ContactService.class);
    }

    /**
     * Obtain a container service.
     * 
     * @return An instance of <code>ContactService</code>.
     */
    public ContainerService getContainerService() {
        return (ContainerService) create(ContainerService.class);
    }

    /**
     * Obtain a crypto service.
     * 
     * @return An instance of <code>CryptoService</code>.
     */
    public CryptoService getCryptoService() {
        return (CryptoService) create(CryptoService.class);
    }

    /**
     * Obtain a migrator service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public MigratorService getMigratorService() {
        return (MigratorService) create(MigratorService.class);
    }

    /**
     * Obtain a profile service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public ProfileService getProfileService() {
        return (ProfileService) create(ProfileService.class);
    }

    /**
     * Obtain a queue service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public QueueService getQueueService() {
        return (QueueService) create(QueueService.class);
    }

    /**
     * Obtain a rule service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public RuleService getRuleService() {
        return (RuleService) create(RuleService.class);
    }

    /**
     * Obtain a session service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public SessionService getSessionService() {
        return (SessionService) create(SessionService.class);
    }

    /**
     * Obtain a stream service.
     * 
     * @return An instance of <code>StreamService</code>.
     */
    public StreamService getStreamService() {
        return (StreamService) create(StreamService.class);
    }

    /**
     * Obtain a system service.
     * 
     * @return An instance of <code>SystemService</code>.
     */
    public SystemService getSystemService() {
        return (SystemService) create(SystemService.class);
    }

    /**
     * Obtain an user service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public UserService getUserService() {
        return (UserService) create(UserService.class);
    }

    /**
     * Create the service proxy implementation.
     * 
     * @param serviceClass
     *            A service <code>Class</code>.
     * @return A service proxy implementation.
     */
    private Object create(final Class<?> serviceClass) {
        return proxyFactory.createProxy(serviceClass, newService(serviceClass));
    }

    /**
     * Create an instance of a service.
     * 
     * @param serviceClass
     *            A service <code>Class</code>.
     * @return A <code>Service</code>.
     */
    private Service newService(final Class<?> serviceClass) {
        return new Service() {
            public String getId() {
                return serviceClass.getAnnotation(WebService.class).name();
            }
        };
    }
}
