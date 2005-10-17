/*
 * Oct 16, 2005
 */
package com.thinkparity.model.xmpp;

import com.thinkparity.model.xstream.XStreamSerializable;

/**
 * XMPPSerializable defines all XMPP artifacts that are to be streamed across
 * the network.  It derives from XStreamSerializable because it uses the XStream
 * library to generate the XML for the XMPPSerializable object. 
 * @author raykroeker@gmail.com
 * @version 1.0
 * @see XStreamSerializable
 */
public interface XMPPSerializable extends XStreamSerializable {}
