/*
 * Nov 2, 2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.ophelia.model.document.DocumentContent;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentContentRenderer implements ObjectRenderer {

	private static final String CHECKSUM = ",checksum:";
	private static final String DOCUMENT_ID = "document id:";
	private static final String PREFIX =
		DocumentContent.class.getName() + IRendererConstants.PREFIX_SUFFIX;
	private static final String SIZE = ",size[bytes]:";

	/**
	 * Create a DocumentContentRenderer.
	 */
	public DocumentContentRenderer() { super(); }

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
			final DocumentContent dc = (DocumentContent) o;
			return new StringBuffer(PREFIX)
				.append(DOCUMENT_ID).append(dc.getDocumentId())
				.append(CHECKSUM).append(dc.getChecksum())
				.append(SIZE).append(dc.getContent().length)
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}

}
