/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PublishVersion extends AbstractHandler {

    /**
     * Create Publish.
     *
     */
    public PublishVersion() {
        super("container:publishversion");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        publish(provider, reader.readJabberId("userId"),
                reader.readContainerVersion("version"),
                reader.readDocumentVersionsStreamIds("documentVersions"),
                reader.readTeamMembers("teamMembers"),
                reader.readArtifactReceipts("receivedBy"),
                reader.readJabberId("publishedBy"),
                reader.readCalendar("publishedOn"),
                reader.readEMails("publishToEMails"),
                reader.readUsers("publishToUsers"));
    }

    private void publish(final ServiceModelProvider provider,
            final JabberId userId, final ContainerVersion version,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers,
            final List<ArtifactReceipt> receivedBy, final JabberId publishedBy,
            final Calendar publishedOn, final List<EMail> publishToEMails,
            final List<User> publishToUsers) {
        provider.getContainerModel().publishVersion(userId, version,
                documentVersions, teamMembers, receivedBy, publishedBy,
                publishedOn, publishToEMails, publishToUsers);
    }
}
