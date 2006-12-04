/*
 * Created On:  11-Nov-06 5:28:27 PM
 */
package com.thinkparity.codebase.model.util.xstream;

import java.io.Reader;
import java.io.Writer;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XStreamUtil {

    /**
     * A singleton instance of xstream util.  The XStream instance itself is
     * thread-safe; hence XStreamUtil will be as well.
     */
    private static final XStreamUtil INSTANCE;

    static {
        INSTANCE = new XStreamUtil();
    }

    public static XStreamUtil getInstance() {
        return INSTANCE;
    }

    private final XStream xstream;

    /**
     * Create XStreamUtil.
     *
     */
    private XStreamUtil() {
        super();
        this.xstream = new XStream();
    }

    /**
     * Deserialize an xmpp event from xml.
     * 
     * @param xml
     *            An xml <code>Reader</code>.
     * @return An <code>XMPPEvent</code>.
     */
    public XMPPEvent eventFromXML(final Reader xml, final XMPPEvent root) {
        return (XMPPEvent) xstream.fromXML(xml, root);
    }

    /**
     * Marshal an artifact receipt to an xstream writer.
     * 
     * @param artifactReceipt
     *            An <code>ArtifactReceipt</code>.
     * @param writer
     *            A <code>HierarchicalStreamWriter</code>.
     */
    public void marshal(final ArtifactReceipt artifactReceipt,
            final HierarchicalStreamWriter writer) {
        xstream.marshal(artifactReceipt, writer);
    }

    public void marshal(final Container container,
            final HierarchicalStreamWriter writer) {
        xstream.marshal(container, writer);
    }

    /**
     * Marshal a team member to an xstream writer.
     * 
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @param writer
     *            A <code>HierarchicalStreamWriter</code>.
     */
    public void marshal(final TeamMember teamMember,
            final HierarchicalStreamWriter writer) {
        xstream.marshal(teamMember, writer);
    }

    /**
     * Marshal a user to an xstream writer.
     * 
     * @param user
     *            A <code>User</code>.
     * @param writer
     *            A <code>HierarchicalStreamWriter</code>.
     */
    public void marshal(final User user, final HierarchicalStreamWriter writer) {
        xstream.marshal(user, writer);
    }

    /**
     * Marshall an xmpp event to an xstream writer.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     * @param writer
     *            An <code>HierarchicalStreamWriter</code>.
     */
    public void marshal(final XMPPEvent event,
            final HierarchicalStreamWriter writer) {
        xstream.marshal(event, writer);
    }

    /**
     * Create an xml stream from an xmpp event.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     * @param writer
     *            A java <code>Writer</code>.
     */
    public void toXML(final XMPPEvent event, final Writer writer) {
        xstream.toXML(event, writer);
    }

    public ArtifactReceipt unmarshalArtifactReceipt(
            final HierarchicalStreamReader xml, final ArtifactReceipt root) {
        return (ArtifactReceipt) xstream.unmarshal(xml, root);
    }

    public Container unmarshalContainer(final HierarchicalStreamReader xml,
            final Container root) {
        return (Container) xstream.unmarshal(xml, root);
    }

    /**
     * Unmarshal an xmpp event from an xtream reader.
     * 
     * @param xml
     *            A <code>HierarchicalStreamReader</code>.
     * @param root
     *            An <code>XMPPevent</code>.
     * @return An <code>XMPPevent</code>.
     */
    public XMPPEvent unmarshalEvent(final HierarchicalStreamReader xml,
            final XMPPEvent root) {
        return (XMPPEvent) xstream.unmarshal(xml, root);
    }

    /**
     * Unmarshal a team member from an xstream reader.
     * 
     * @param xml
     *            A <code>HierarchicalStreamReader</code>.
     * @param root
     *            A <code>TeamMember</code>.
     * @return A <code>TeamMember</code>.
     */
    public TeamMember unmarshalTeamMember(final HierarchicalStreamReader xml,
            final TeamMember root) {
        return (TeamMember) xstream.unmarshal(xml, root);
    }

    /**
     * Unmarshal a user from an xstream reader.
     * 
     * @param xml
     *            A <code>HierarchicalStreamReader</code>.
     * @param root
     *            A <code>User</code>.
     * @return A <code>User</code>.
     */
    public User unmarshalUser(final HierarchicalStreamReader xml, final User root) {
        return (User) xstream.unmarshal(xml, root);
    }
}
