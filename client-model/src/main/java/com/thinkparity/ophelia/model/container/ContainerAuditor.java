/*
 * Created On: Jun 29, 2006 8:30:14 AM
 */
package com.thinkparity.ophelia.model.container;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.model.InternalModelFactory;
import com.thinkparity.ophelia.model.audit.AbstractAuditor;
import com.thinkparity.ophelia.model.audit.event.PublishEvent;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
final class ContainerAuditor extends AbstractAuditor {

    /**
     * Create ContainerAuditor.
     * 
     * @param modelFactory
     *            A thinkParity <code>InternalModelFactory</code>.
     */
    ContainerAuditor(final InternalModelFactory modelFactory) {
        super(modelFactory);
    }

    void create(final Container container) {}

    void publish(final Container container, final ContainerDraft draft,
            final ContainerVersion version, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        final PublishEvent publishEvent = new PublishEvent();
        publishEvent.setArtifactId(container.getId());
        publishEvent.setArtifactVersionId(version.getVersionId());
        publishEvent.setCreatedBy(publishedBy);
        publishEvent.setCreatedOn(publishedOn);
        publishEvent.setPublishedBy(publishedBy);
        publishEvent.setPublishedOn(publishedOn);
        getAuditModel().audit(publishEvent);
    }
}
