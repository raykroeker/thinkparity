/*
 * 21-Oct-2005
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.document.DocumentVersion;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Document Version Renderer<br>
 * <b>Description:</b>A Log4J renderer of thinkParity document versions.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DocumentVersionRenderer implements ObjectRenderer {

	/**
	 * Create DocumentVersionRenderer.
     * 
	 */
	public DocumentVersionRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(final Object o) {
		if(null == o) {
			return Separator.Null.toString();
		}
		else {
			final DocumentVersion dv = (DocumentVersion) o;
			return StringUtil.toString(DocumentVersion.class,
                    "getArtifactUniqueId()", dv.getArtifactUniqueId(),
                    "getVersionId", dv.getVersionId());
		}
	}
}
