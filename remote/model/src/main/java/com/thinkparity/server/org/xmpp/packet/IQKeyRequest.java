/*
 * Dec 7, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.xmpp.packet.IQ;

import com.thinkparity.server.org.dom4j.ElementBuilder;
import com.thinkparity.server.org.dom4j.ElementName;
import com.thinkparity.server.org.dom4j.NamespaceName;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyRequest extends IQArtifact {

	/**
	 * Create a IQKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQKeyRequest(final UUID artifactUUID) {
		super(Action.REQUESTKEY, artifactUUID);
		setType(IQ.Type.get);
		final Element queryElement = setChildElement(
				ElementName.QUERY.getName(),
				NamespaceName.IQ_KEY_REQUEST.getName());

		// add the uuid
		final String uuidElementText = getArtifactUUID().toString();
		ElementBuilder.addElement(queryElement, ElementName.UUID, uuidElementText);
	}

	/**
	 * @see org.xmpp.packet.Packet#getElement()
	 */
	public Element getElement() {
		logger.info("getElement()");
		final Throwable t = new Throwable();
		try { throw t; }
		catch(Throwable t2) {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			t2.printStackTrace(pw);
			logger.debug(sw.toString());
		}
		final Element element = super.getElement();
		logger.debug(element);
		final Namespace ns = element.getNamespace();
		logger.debug(ns.getURI());
		return element;
	}


}
