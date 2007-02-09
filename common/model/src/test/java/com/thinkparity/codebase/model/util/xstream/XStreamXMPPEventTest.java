/*
 * Created On:  11-Nov-06 5:32:51 PM
 */
package com.thinkparity.codebase.model.util.xstream;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationAcceptedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeclinedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactInvitationExtendedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ContactUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import org.dom4j.Element;

import com.thoughtworks.xstream.io.xml.Dom4JReader;
import com.thoughtworks.xstream.io.xml.Dom4JWriter;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XStreamXMPPEventTest extends XStreamTestCase {

    /** The test name <code>String</code>. */
    private static final String NAME = "XStream XMPP Event Test";

    /** The test datum <code>Fixture</code>. */
    private List<Fixture> data;

    private EMail email;

    private JabberId jabberId;

    private List<JabberId> jabberIds;

    /**
     * Create ArtifactDraftCreatedEventTest.
     *
     */
    public XStreamXMPPEventTest() {
        super(NAME);
    }

    /**
     * Test to and from xml.
     *
     */
    public void testXML() {
        for (final Fixture datum : data) {
            final StringWriter writer = new StringWriter();
            XStreamUtil.getInstance().toXML(datum.event, writer);
            final StringReader reader = new StringReader(writer.toString());
            XStreamUtil.getInstance().eventFromXML(reader, datum.event2);
            XStreamTestCase.assertEquals(datum.event, datum.event2);
        }
    }

    public void testXMLMarshal() {
        for (final Fixture datum : data) {
            final Dom4JWriter writer = new Dom4JWriter();
            XStreamUtil.getInstance().marshal(datum.event, writer);

            final Dom4JReader reader = new Dom4JReader((Element) writer.getTopLevelNodes().get(0));
            XStreamUtil.getInstance().unmarshalEvent(reader, datum.event2);
            XStreamTestCase.assertEquals(datum.event, datum.event2);
        }        
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        data = new ArrayList<Fixture>();
        email = EMailBuilder.parse("junit@thinkparity.com");
        jabberId = JabberIdBuilder.parse("junit@thinkparity.dyndns.org/1209110");
        jabberIds = new ArrayList<JabberId>();
        jabberIds.add(JabberIdBuilder.parse("junit@thinkparity.dyndns.org/1209110"));
        data.add(setUp(new ArtifactDraftCreatedEvent(), new ArtifactDraftCreatedEvent()));
        data.add(setUp(new ArtifactDraftDeletedEvent(), new ArtifactDraftDeletedEvent()));
        data.add(setUp(new ArtifactPublishedEvent(), new ArtifactPublishedEvent()));
        data.add(setUp(new ArtifactReceivedEvent(), new ArtifactReceivedEvent()));
        data.add(setUp(new ArtifactTeamMemberAddedEvent(), new ArtifactTeamMemberAddedEvent()));
        data.add(setUp(new ArtifactTeamMemberRemovedEvent(), new ArtifactTeamMemberRemovedEvent()));

        data.add(setUp(new ContactDeletedEvent(), new ContactDeletedEvent()));
        data.add(setUp(new ContactInvitationAcceptedEvent(), new ContactInvitationAcceptedEvent()));
        data.add(setUp(new ContactInvitationDeclinedEvent(), new ContactInvitationDeclinedEvent()));
        data.add(setUp(new ContactInvitationDeletedEvent(), new ContactInvitationDeletedEvent()));
        data.add(setUp(new ContactInvitationExtendedEvent(), new ContactInvitationExtendedEvent()));
        data.add(setUp(new ContactUpdatedEvent(), new ContactUpdatedEvent()));
    }

    /**
     * @see com.thinkparity.codebase.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        data.clear();
        data = null;
        super.tearDown();
    }

    private Fixture setUp(final ArtifactDraftCreatedEvent event,
            final ArtifactDraftCreatedEvent event2) {
        event.setCreatedBy(jabberId);
        event.setCreatedOn(DateUtil.getInstance());
        event.setUniqueId(UUID.randomUUID());
        return new Fixture(event);
    }

    private Fixture setUp(final ArtifactDraftDeletedEvent event,
            final ArtifactDraftDeletedEvent event2) {
        event.setDeletedBy(jabberId);
        event.setDeletedOn(DateUtil.getInstance());
        event.setUniqueId(UUID.randomUUID());
        return new Fixture(event);
    }

    private Fixture setUp(final ArtifactPublishedEvent event,
            final ArtifactPublishedEvent event2) {
        event.setPublishedBy(jabberId);
        event.setPublishedOn(DateUtil.getInstance());
        event.setUniqueId(UUID.randomUUID());
        event.setVersionId(10L);
        return new Fixture(event);
    }

    private Fixture setUp(final ArtifactReceivedEvent event,
            final ArtifactReceivedEvent event2) {
        event.setPublishedOn(DateUtil.getInstance());
        event.setReceivedBy(jabberId);
        event.setReceivedOn(DateUtil.getInstance());
        event.setUniqueId(UUID.randomUUID());
        event.setVersionId(10L);
        return new Fixture(event);
    }

    private Fixture setUp(final ArtifactTeamMemberAddedEvent event,
            final ArtifactTeamMemberAddedEvent event2) {
        event.setJabberId(jabberId);
        event.setUniqueId(UUID.randomUUID());
        return new Fixture(event);
    }

    private Fixture setUp(final ArtifactTeamMemberRemovedEvent event,
            final ArtifactTeamMemberRemovedEvent event2) {
        event.setJabberId(jabberId);
        event.setUniqueId(UUID.randomUUID());
        return new Fixture(event);
    }

    private Fixture setUp(final ContactDeletedEvent event,
            final ContactDeletedEvent event2) {
        event.setDeletedBy(jabberId);
        event.setDeletedOn(DateUtil.getInstance());
        return new Fixture(event);
    }

    private Fixture setUp(final ContactInvitationAcceptedEvent event,
            final ContactInvitationAcceptedEvent event2) {
        event.setAcceptedBy(jabberId);
        event.setAcceptedOn(DateUtil.getInstance());
        return new Fixture(event);
    }

    private Fixture setUp(final ContactInvitationDeclinedEvent event,
            final ContactInvitationDeclinedEvent event2) {
        event.setDeclinedBy(jabberId);
        event.setDeclinedOn(DateUtil.getInstance());
        event.setInvitedAs(email);
        return new Fixture(event);
    }

    private Fixture setUp(final ContactInvitationDeletedEvent event,
            final ContactInvitationDeletedEvent event2) {
        event.setDeletedBy(jabberId);
        event.setDeletedOn(DateUtil.getInstance());
        event.setInvitedAs(email);
        return new Fixture(event);
    }

    private Fixture setUp(final ContactInvitationExtendedEvent event,
            final ContactInvitationExtendedEvent event2) {
        event.setInvitedAs(email);
        event.setInvitedBy(jabberId);
        event.setInvitedOn(DateUtil.getInstance());
        return new Fixture(event);
    }

    private Fixture setUp(final ContactUpdatedEvent event,
            final ContactUpdatedEvent event2) {
        event.setContactId(jabberId);
        event.setUpdatedOn(DateUtil.getInstance());
        return new Fixture(event);
    }

    private static final class Fixture extends XStreamTestCase.Fixture {
        private final XMPPEvent event;
        private XMPPEvent event2;
        private Fixture(final XMPPEvent event) {
            this.event = event;
            this.event.setDate(DateUtil.getInstance());
            this.event.setId(String.valueOf(System.nanoTime()));
            this.event2 = null;
        }
    }
}

