/*
 * Created On:  11-Nov-06 5:28:27 PM
 */
package com.thinkparity.codebase.model.util.xstream;

import java.io.Reader;
import java.io.Writer;

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

    /**
     * Unmarshal an xmpp event from an xtream reader.
     * 
     * @param xml
     *            An <code>HierarchicalStreamReader</code>.
     * @return An <code>XMPPevent</code>.
     */
    public XMPPEvent unmarshalEvent(final HierarchicalStreamReader xml,
            final XMPPEvent root) {
        return (XMPPEvent) xstream.unmarshal(xml, root);
    }
}
