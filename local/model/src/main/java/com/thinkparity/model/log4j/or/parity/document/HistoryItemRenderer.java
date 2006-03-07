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

	private static final String ARTIFACT_VERSION_ID = ",artifactVersionId:";

	private static final String DATE = ",eventDate:";

	private static final String EVENT = ",eventText:";

	private static final String PREFIX =
		HistoryItem.class.getName() + IRendererConstants.PREFIX_SUFFIX;

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
			final StringBuffer buffer = new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(hi.getDocumentId());
			if(hi.isSetVersionId()) {
				buffer.append(ARTIFACT_VERSION_ID)
					.append(hi.getVersionId());
			}
			return buffer.append(DATE).append(hi.getDate().getTimeInMillis())
				.append(EVENT).append(hi.getEvent())
				.toString();
		}
	}
}
