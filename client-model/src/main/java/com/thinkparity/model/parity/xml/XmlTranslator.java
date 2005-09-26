/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.xml;

import com.thoughtworks.xstream.converters.Converter;

/**
 * XmlTranslator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XmlTranslator extends Converter {

	public String getXmlAlias();

	public Class getXmlAliasClass();
}
