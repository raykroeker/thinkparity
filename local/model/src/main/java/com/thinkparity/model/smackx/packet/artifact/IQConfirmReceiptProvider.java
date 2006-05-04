/*
 * Apr 8, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.model.smack.provider.IQParityProvider;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQConfirmReceiptProvider extends IQParityProvider {

    /**
     * Create a IQConfirmReceiptProvider.
     * 
     */
    public IQConfirmReceiptProvider() { super(); }

    /**
     * @see com.thinkparity.model.smack.provider.IQParityProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
     * 
     */
    public IQ parseIQ(final XmlPullParser parser) throws Exception {
        logger.info("[LMODEL] [XMPP] [PARSE CONFIRMATION RECEIPT]");
        logger.debug(parser);
        final IQConfirmArtifactReceipt iq = new IQConfirmArtifactReceipt();

        Integer attributeCount, depth, eventType;
        String name, namespace, prefix, text;
        Boolean isComplete = Boolean.FALSE;
        while(Boolean.FALSE == isComplete) {
            eventType = parser.next();
            attributeCount = parser.getAttributeCount();
            depth = parser.getDepth();
            name = parser.getName();
            prefix = parser.getPrefix();
            namespace = parser.getNamespace(prefix);
            text = parser.getText();

            logger.debug(attributeCount);
            logger.debug(depth);
            logger.debug(eventType);
            logger.debug(name);
            logger.debug(namespace);
            logger.debug(prefix);
            logger.debug(text);

            if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
                parser.next();
                iq.setArtifactUUID(UUID.fromString(parser.getText()));
                parser.next();
            }
            else if(XmlPullParser.START_TAG == eventType && "versionid".equals(name)) {
                parser.next();
                iq.setArtifactVersionId(Long.parseLong(parser.getText()));
                parser.next();
            }
            else if(XmlPullParser.START_TAG == eventType && "jid".equals(name)) {
                parser.next();
                iq.setRecievedFrom(JabberIdBuilder.parseQualifiedJabberId(parser.getText()));
                parser.next();
            }
            else { isComplete = Boolean.TRUE; }
        }
        return iq;
    }

}
