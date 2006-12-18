/*
 * Created On:  16-Dec-06 3:24:27 PM
 */
package com.thinkparity.desdemona.util.service;

import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.container.ContainerModel;
import com.thinkparity.desdemona.model.profile.ProfileModel;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.UserModel;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceModelProvider {

    /**
     * Obtain an archive model.
     * 
     * @return An <code>ArchiveModel</code>.
     */
    public ArchiveModel getArchiveModel();

    /**
     * Obtain an artifact model.
     * 
     * @return An <code>ArtifactModel</code>.
     */
    public ArtifactModel getArtifactModel();

    /**
     * Obtain an backup model.
     * 
     * @return A <code>BackupModel</code>.
     */
    public BackupModel getBackupModel();

    /**
     * Obtain a contact model.
     * 
     * @return An <code>ContactModel</code>.
     */
    public ContactModel getContactModel();

    /**
     * Obtain a container model.
     * 
     * @return A <code>ContainerModel</code>.
     */
    public ContainerModel getContainerModel();

    /**
     * Obtain a profile model.
     * 
     * @return A <code>ProfileModel</code>.
     */
    public ProfileModel getProfileModel();

    /**
     * Obtain a queue model.
     * 
     * @return A <code>QueueModel</code>.
     */
    public QueueModel getQueueModel();

    /**
     * Obtain a stream model.
     * 
     * @return A <code>StreamModel</code>.
     */
    public StreamModel getStreamModel();

    /**
     * Obtain a user model.
     * 
     * @return A <code>UserModel</code>.
     */
    public UserModel getUserModel();
}
