/*
 * 21-Oct-2005
 */
package com.thinkparity.model.log4j.or.parity.document;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.model.log4j.or.IRendererConstants;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * Write the document version object to the apache log.
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class DocumentVersionRenderer implements ObjectRenderer {

	private static final String DOCUMENT_ID = "document id:";
	private static final String PREFIX =
		DocumentVersion.class.getName() + IRendererConstants.PREFIX_SUFFIX;
	private static final String VERSION = ",version:";

	/**
	 * Create a DocumentVersionRenderer.
	 */
	public DocumentVersionRenderer() { super(); }

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
			final DocumentVersion dv = (DocumentVersion) o;
			return new StringBuffer(PREFIX)
				.append(DOCUMENT_ID).append(dv.getArtifactId())
				.append(VERSION).append(dv.getVersionId())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
