/*
 * Apr 5, 2006
 */
package com.thinkparity.model.smackx.packet.document;

import java.util.UUID;

import org.jivesoftware.smack.packet.IQ;
import org.xmlpull.v1.XmlPullParser;

import com.thinkparity.codebase.CompressionUtil;

import com.thinkparity.model.smack.provider.IQParityProvider;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQSendDocumentProvider extends IQParityProvider {

    /**
     * Create a IQSendDocumentProvider.
     */
    public IQSendDocumentProvider() { super(); }

    /**
     * @see com.thinkparity.model.smack.provider.IQParityProvider#parseIQ(org.xmlpull.v1.XmlPullParser)
     * 
     */
    public IQ parseIQ(final XmlPullParser parser) throws Exception {
        logger.info("[LMODEL] [XMPP] [PARSE DOCUMENT]");
        logger.debug(parser);
        final IQSendDocument iq = new IQSendDocument();

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

            if(XmlPullParser.START_TAG == eventType && "bytes".equals(name)) {
                parser.next();
                iq.setContent(CompressionUtil.decompress(Base64.decode(parser.getText())));
                parser.next();
            }
            else if(XmlPullParser.END_TAG == eventType && "bytes".equals(name)) {
                parser.next();
            }
            else if(XmlPullParser.START_TAG == eventType && "uuid".equals(name)) {
                parser.next();
                iq.setUniqueId(UUID.fromString(parser.getText()));
                parser.next();
            }
            else if(XmlPullParser.END_TAG == eventType && "uuid".equals(name)) {
                parser.next();
            }
            else if(XmlPullParser.START_TAG == eventType && "versionid".equals(name)) {
                parser.next();
                iq.setVersionId(Long.parseLong(parser.getText()));
                parser.next();
            }
            else if(XmlPullParser.END_TAG == eventType && "versionid".equals(name)) {
                parser.next();
            }
            else if(XmlPullParser.START_TAG == eventType && "name".equals(name)) {
                parser.next();
                iq.setName(parser.getText());
                parser.next();
            }
            else if(XmlPullParser.END_TAG == eventType && "name".equals(name)) {
                parser.next();
            }
            else { isComplete = Boolean.TRUE; }
        }
        return iq;
    }
}
