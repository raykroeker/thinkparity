/*
 * 19-Oct-2005
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.document.Document;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Log4J Document Renderer<br>
 * <b>Description:</b>A Log4J thinkParity document renderer.<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DocumentRenderer implements ObjectRenderer {

	/**
	 * Create DocumentRenderer.
     * 
	 */
	public DocumentRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     * 
	 */
	public String doRender(final Object o) {
		if(null == o) {
			return Separator.Null.toString();
		}
		else {
			final Document o2 = (Document) o;
			return StringUtil.toString(o2.getClass(),
                    "getId()", o2.getId(),
                    "getName()", o2.getName(),
                    "getUniqueId()", o2.getUniqueId(),
                    "hashCode()", Integer.toHexString(o2.hashCode()));
		}
	}
}
