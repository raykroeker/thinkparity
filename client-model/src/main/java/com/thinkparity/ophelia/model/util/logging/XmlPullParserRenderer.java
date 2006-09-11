/*
 * Dec 11, 2005
 */
package com.thinkparity.ophelia.model.util.logging;

import org.apache.log4j.or.ObjectRenderer;
import org.xmlpull.v1.XmlPullParser;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlPullParserRenderer implements ObjectRenderer {

	private static final String DEPTH = "depth:";

	private static final String INPUT_ENCODING = ",inputEncoding:";

	private static final String NAMESPACE = ",namespace:";

	private static final String PREFIX =
		XmlPullParser.class + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a XmlPullParserRenderer.
	 */
	public XmlPullParserRenderer() { super(); }

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
			final XmlPullParser xpp = (XmlPullParser) o;
			return new StringBuffer(PREFIX)
				.append(DEPTH).append(xpp.getDepth())
				.append(INPUT_ENCODING).append(xpp.getInputEncoding())
				.append(NAMESPACE).append(xpp.getNamespace())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}
}
