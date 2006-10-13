/*
 * 19-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import org.jivesoftware.smack.packet.Message;


/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class MessageRenderer implements ObjectRenderer {

	private static final String BODY = ",body:";
	private static final String FROM = ",from:";
	private static final String PREFIX =
		Message.class.getName() + IRendererConstants.PREFIX_SUFFIX;
	private static final String SUBJECT = ",subject:";
	private static final String THREAD = ",thread:";
	private static final String TO = ",to:";

	/**
	 * Create a MessageRenderer.
	 */
	public MessageRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
		else {
			final Message m = (Message) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(m.getPacketID())
				.append(TO).append(m.getTo())
				.append(FROM).append(m.getFrom())
				.append(THREAD).append(m.getThread())
				.append(SUBJECT).append(m.getSubject())
				.append(BODY).append(m.getBody())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
