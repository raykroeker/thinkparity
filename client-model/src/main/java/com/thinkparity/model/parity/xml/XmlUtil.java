/*
 * Feb 21, 2005
 */
package com.thinkparity.model.parity.xml;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.thinkparity.codebase.FileUtil;


import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityXmlSerializable;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.parity.util.LoggerFactory;
import com.thinkparity.model.parity.util.ParityUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;

import org.apache.log4j.Logger;

/**
 * XmlUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlUtil {

	/**
	 * Handle to the xml util's logger.
	 */
	private static final Logger logger =
		LoggerFactory.createInstance(XmlUtil.class);

	/**
	 * Handle to the main XStream class for the client application.
	 */
	private static final XStream xStream = new XStream();

	/**
	 * Register a converter for byte arrays.
	 */
	static {
		XmlUtil.xStream.registerConverter(new EncodedByteArrayConverter());
	}

	/**
	 * Decode the xml using a base64 encoding.
	 * @param encodedXml <code>byte[]</code>
	 * @return <code>byte[]</code>
	 */
	private static byte[] decodeMetaData(final byte[] metaDataContent) {
		if(ParityUtil.isDevelopmentMachine())
			return metaDataContent;
		return Base64.decodeBytes(new String(metaDataContent));
	}

	/**
	 * Encode the xml using a base64 encoding.
	 * @param xml <code>byte[]</code>
	 * @return <code>byte[]</code>
	 */
	private static String encodeMetaData(final String metaDataXml) {
		if(ParityUtil.isDevelopmentMachine())
			return metaDataXml;
		return Base64.encodeBytes(metaDataXml.getBytes());
	}

	/**
	 * Delete the meta-data file which represents the parity object.
	 * @param pariObject <code>org.kcs.projectmanager.client.api.ParityObject</code>
	 * @return <code>java.lang.Boolean</code>
	 */
	public static Boolean delete(ParityObject pariObject) {
		return FileUtil.delete(pariObject.getMetaDataFile());
	}
	public static Boolean delete(final File parentDirectory,
			final ParityXmlSerializable parityXmlSerializable) {
		return FileUtil.delete(parityXmlSerializable.getMetaDataFile(parentDirectory));
	}

	public static ParityObject read(final File metaDataFile) throws IOException {
		// read the xml using a default character-set then a base64 decoding
		// into a string
		final byte[] xmlBytes = decodeMetaData(FileUtil.readFile(metaDataFile));
		final String xmlString = new String(xmlBytes, Charset.defaultCharset().name());
		// use the xml serialization library to load the object
		// from the xml
		return (ParityObject) xStream.fromXML(xmlString);
	}
	public static ParityXmlSerializable read2(final File metaDataFile) throws IOException {
		// read the xml using a default character-set then a base64 decoding
		// into a string
		final byte[] xmlBytes = decodeMetaData(FileUtil.readFile(metaDataFile));
		final String xmlString = new String(xmlBytes, Charset.defaultCharset().name());
		// use the xml serialization library to load the object
		// from the xml
		return (ParityXmlSerializable) xStream.fromXML(xmlString);
	}


	/**
	 * Register an XStream converter for the client application.  The
	 * implementation of the singleton xml object should register the translator
	 * within a static initializer before any reading\writing of the object.
	 * @param xmlTranslator
	 * <code>org.kcs.projectmanager.client.xml.XmlTranslator</code>
	 */
	public static void registerTranslator(XmlTranslator xmlTranslator) {
		XmlUtil.xStream.registerConverter(xmlTranslator);
		XmlUtil.xStream.alias(xmlTranslator.getXmlAlias(), xmlTranslator.getXmlAliasClass());
	}

	public static void write(ParityObject pariObject) throws IOException {
		final String xStreamXml = XmlUtil.toXml(pariObject).toString();
		// encode the xml using Base64
		FileUtil.writeFile(
				pariObject.getMetaDataFile(),
				encodeMetaData(xStreamXml).getBytes());
	}
	public static void write(final File parentDirectory,
			final ParityXmlSerializable parityXmlSerializable)
			throws IOException {
		final String xStreamXml = XmlUtil.toXml(parityXmlSerializable).toString();
		// encode the xml using Base64
		FileUtil.writeFile(
				parityXmlSerializable.getMetaDataFile(parentDirectory),
				encodeMetaData(xStreamXml).getBytes());		
	}

	/**
	 * Simply convert the parity object to an xml StringBuffer.
	 * @param parityObject <code>org.kcs.projectmanager.client.api.ParityObject</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public static StringBuffer toXml(final ParityObject parityObject) {
		return new StringBuffer(xStream.toXML(parityObject));
	}
	public static StringBuffer toXml(
			final ParityXmlSerializable parityXmlSerializable) {
		return new StringBuffer(xStream.toXML(parityXmlSerializable));
	}

	/**
	 * Convert parityXmlSerializable to xml then encode it.
	 * @param parityXmlSerializable - the parity serializable object to convert
	 * to extension xml.
	 * @return encoded extension xml
	 */
	public static StringBuffer toExtensionXml(
			final ParityXmlSerializable parityXmlSerializable) {
		final StringBuffer toXml = XmlUtil.toXml(parityXmlSerializable);
		return new StringBuffer(Base64.encodeBytes(toXml.toString().getBytes()));
	}

	public static ParityXmlSerializable fromExtensionXml(
			final String extensionXml)
			throws UnsupportedEncodingException {
		final byte[] decodedXmlBytes = Base64.decodeBytes(extensionXml);
		final String decodedXmlString =
			new String(decodedXmlBytes, Charset.defaultCharset().name());
		return (ParityXmlSerializable) xStream.fromXML(decodedXmlString);
	}

	public static Object fromXml(final Reader reader) {
		return xStream.fromXML(reader);
	}
	/**
	 * Create an XmlUtil [Singleton]
	 */
	private XmlUtil() { super(); }
}
