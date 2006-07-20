/*
 * Created On: Jun 22, 2006 2:51:33 PM
 * $Id$
 */
package com.thinkparity.server.handler;

import java.util.UUID;

import org.jivesoftware.messenger.auth.UnauthorizedException;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.profile.ProfileModel;
import com.thinkparity.model.xmpp.IQReader;
import com.thinkparity.model.xmpp.IQWriter;

import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.container.ContainerModel;
import com.thinkparity.server.model.session.Session;

/**
 * <b>Title:</b>thinkParity Model Controller <br>
 * <b>Description:</b>An abstraction of an xmpp controller.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class AbstractController extends
        com.thinkparity.codebase.controller.AbstractController {

    /**
     * Obtain a controller log id.
     * 
     * @param controller
     *            A controller.
     * @return A controller log id.
     */
    protected static StringBuffer getControllerId(final String controller) {
        return new StringBuffer("[MODEL] [CONTROLLER] ").append(controller);
    }

    /** A thinkParity artifact model interface. */
    private ArtifactModel artifactModel;

    /** A thinkParity container model interface. */
    private ContainerModel containerModel;

    /** A thinkParity profile interface. */
    private ProfileModel profileModel;

    /** A custom iq reader. */
    private IQReader iqReader;

    /** A custom iq writer. */
    private IQWriter iqWriter;

    /** Create AbstractController. */
    public AbstractController(final String action) { super(action); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createReader(org.xmpp.packet.IQ)
     */
    public IQReader createReader(final IQ iq) {
        iqReader = new IQReader(iq);
        return iqReader;
    }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#createWriter(org.xmpp.packet.IQ)
     */
    public IQWriter createWriter(final IQ iq) {
        iqWriter = new IQWriter(iq);
        return iqWriter;
    }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#handleIQ(org.xmpp.packet.IQ)
     */
    public IQ handleIQ(final IQ iq) throws UnauthorizedException {
        final Session session = new Session() {
            final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(iq.getFrom().toString());
            public JabberId getJabberId() { return jabberId; }
            public JID getJID() { return iq.getFrom(); }
        };
        this.artifactModel = ArtifactModel.getModel(session);
        this.containerModel = ContainerModel.getModel(session);
        this.profileModel = ProfileModel.getModel(session);
        return super.handleIQ(iq);
    }

    /**
     * Read a unique id parameter.
     * 
     * @param name
     *            The parameter name.
     * @return The unique id.
     */
    public UUID readUUID(final String name) {
        return iqReader.readUUID(name);
    }

    /**
     * Obtain the artifact model.
     * 
     * @return The artifact model.
     * @see #handleIQ(IQ)
     */
    protected ArtifactModel getArtifactModel() { return artifactModel; }

    /**
     * Obtain the thinkParity profile interface.
     * 
     * @return A thinkParity profile interface.
     */
    protected ProfileModel getProfileModel() { return profileModel; }

    /**
     * Obtain the contianer model.
     * 
     * @return The container model.
     * @see #handleIQ(IQ)
     */
    protected ContainerModel getContainerModel() { return containerModel; }
}
