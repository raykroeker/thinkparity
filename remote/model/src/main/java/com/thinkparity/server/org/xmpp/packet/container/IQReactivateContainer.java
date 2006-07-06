/*
 * Created On: Jul 6, 2006 10:10:29 AM
 */
package com.thinkparity.server.org.xmpp.packet.container;

import java.util.Calendar;
import java.util.List;

import org.xmpp.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.xmpp.ElementBuilder;

import com.thinkparity.server.org.dom4j.NamespaceName;
import com.thinkparity.server.org.xmpp.packet.IQParity.Action;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class IQReactivateContainer extends IQContainer {

    /** Create IQReactivateContainer. */
    public IQReactivateContainer() {
        super(Action.CONTAINERREACTIVATE, NamespaceName.IQ_CONTAINER_REACTIVATE);
        setType(IQ.Type.set);
    }

    /**
     * Set reactivated by.
     * 
     * @param reactivatedBy
     *            Who reactivated the container.
     */
    public void setReactivatedBy(final JabberId reactivatedBy) {
        ElementBuilder.addElement(rootElement, "reactivatedby", reactivatedBy);
    }

    /**
     * Set reactivated on.
     * 
     * @param reactivatedOn
     *            When the container was reactivated.
     */
    public void setReactivatedOn(final Calendar reactivatedOn) {
        ElementBuilder.addElement(rootElement, "reactivatedon", reactivatedOn);
    }

    /**
     * Set team.
     * 
     * @param team
     *            The team.
     */
    public void setTeam(final List<JabberId> team) {
        ElementBuilder.addJabberIdElements(rootElement, "team", "teammember", team);
    }
}
