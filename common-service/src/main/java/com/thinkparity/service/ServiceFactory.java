/*
 * Created On:  3-Jun-07 8:39:41 PM
 */
package com.thinkparity.service;

/**
 * <b>Title:</b>thinkParity Service Factory<br>
 * <b>Description:</b>A thinkParity service factory definition.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceFactory {

    /**
     * Obtain an artifact service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public ArtifactService getArtifactService();

    /**
     * Obtain a backup service.
     * 
     * @return An instance of <code>BackupService</code>.
     */
    public BackupService getBackupService();

    /**
     * Obtain a contact service.
     * 
     * @return An instance of <code>ContactService</code>.
     */
    public ContactService getContactService();

    /**
     * Obtain a container service.
     * 
     * @return An instance of <code>ContactService</code>.
     */
    public ContainerService getContainerService();

    /**
     * Obtain a crypto service.
     * 
     * @return An instance of <code>CryptoService</code>.
     */
    public CryptoService getCryptoService();

    /**
     * Obtain a migrator service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public MigratorService getMigratorService();

    /**
     * Obtain a profile service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public ProfileService getProfileService();

    /**
     * Obtain a queue service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public QueueService getQueueService();

    /**
     * Obtain a rule service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public RuleService getRuleService();

    /**
     * Obtain a session service.
     * 
     * @return An instance of <code>SessionService</code>.
     */
    public SessionService getSessionService();

    /**
     * Obtain a stream service.
     * 
     * @return An instance of <code>StreamService</code>.
     */
    public StreamService getStreamService();

    /**
     * Obtain a system service.
     * 
     * @return An instance of <code>SystemService</code>.
     */
    public SystemService getSystemService();

    /**
     * Obtain an user service.
     * 
     * @return An instance of <code>ArtifactService</code>.
     */
    public UserService getUserService();
}
