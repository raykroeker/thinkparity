/*
 * Feb 23, 2006
 */
package com.thinkparity.model.log4j.or.parity.document;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.model.log4j.or.IRendererConstants;
import com.thinkparity.model.parity.model.document.history.HistoryItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryItemRenderer implements ObjectRenderer {

	private static final String DATE = ",date:";

	private static final String EVENT = ",event:";

	private static final String NAME = ",name:";

	private static final String PREFIX =
		HistoryItem.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String VERSION = ",version:";

	/**
	 * Create a HistoryItemRenderer.
	 * 
	 */
	public HistoryItemRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 * 
	 */
	public String doRender(final Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.PREFIX_SUFFIX)
				.toString();
		}
		else {
			final HistoryItem hi = (HistoryItem) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(hi.getDocumentId())
				.append(DATE).append(hi.getDate().getTimeInMillis())
				.append(EVENT).append(hi.getEvent().toString())
				.append(NAME).append(hi.getName())
				.append(VERSION).append(hi.getVersionId())
				.toString();
		}
	}
}
