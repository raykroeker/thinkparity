/*
 * 19-Oct-2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.codebase.model.document.Document;



/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class DocumentRenderer implements ObjectRenderer {

	private static final String NAME = ",name:";
	private static final String PREFIX =
		Document.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a DocumentRenderer.
	 */
	public DocumentRenderer() { super(); }

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
			final Document d = (Document) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(d.getId())
				.append(NAME).append(d.getName())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
