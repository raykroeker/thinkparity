/*
 * Feb 21, 2005
 */
package com.thinkparity.model.xstream;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;

import com.thinkparity.codebase.FileUtil;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityXmlSerializable;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.parity.util.ParityUtil;

import com.thoughtworks.xstream.XStream;

/**
 * XStreamUtil
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XStreamUtil {

	/**
	 * Handle to the main XStream class for the client application.
	 */
	private static final XStream xStream;

	static {
		xStream = new XStream();
		XStreamRegistry.createRegistry(xStream);
	}

	public static Boolean delete(final File parentDirectory,
			final ParityXmlSerializable parityXmlSerializable) {
		return FileUtil.delete(parityXmlSerializable.getMetaDataFile(parentDirectory));
	}

	/**
	 * Delete the meta-data file which represents the parity object.
	 * @param pariObject <code>org.kcs.projectmanager.client.api.ParityObject</code>
	 * @return <code>java.lang.Boolean</code>
	 */
	public static Boolean delete(ParityObject pariObject) {
		return FileUtil.delete(pariObject.getMetaDataFile());
	}

	/**
	 * Read an xstream serialilzable object from the xml reader.
	 * 
	 * @param xml
	 *            The reader to read xml from.
	 * @return The xstream serializable object.
	 * @see XStream#fromXML(java.io.Reader)
	 */
	public static XStreamSerializable fromXML(final Reader xml) {
		return (XStreamSerializable) xStream.fromXML(xml);
	}
	/**
	 * Read an xstream serializable object from the xml.
	 * 
	 * @param xml
	 *            The xml to read.
	 * @return The xstream serializable object.
	 * @see XStream#fromXML(java.lang.String)
	 */
	public static XStreamSerializable fromXML(final String xml) {
		return (XStreamSerializable) xStream.fromXML(xml);
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
	 * Convert an x stream serializable object into an xml string.
	 * 
	 * @param serializable
	 *            The serializable object.
	 * @return The xml string.
	 */
	public static String toXML(final XStreamSerializable serializable) {
		return xStream.toXML(serializable);
	}

	public static void write(final File parentDirectory,
			final ParityXmlSerializable parityXmlSerializable)
			throws IOException {
		final String xStreamXml = XStreamUtil.toXML(parityXmlSerializable).toString();
		// encode the xml using Base64
		FileUtil.writeFile(
				parityXmlSerializable.getMetaDataFile(parentDirectory),
				encodeMetaData(xStreamXml).getBytes());		
	}

	public static void write(ParityObject pariObject) throws IOException {
		final String xStreamXml = XStreamUtil.toXML(pariObject).toString();
		// encode the xml using Base64
		FileUtil.writeFile(
				pariObject.getMetaDataFile(),
				encodeMetaData(xStreamXml).getBytes());
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
	 * @deprecated Use XStreamUtil.toXML(XStreamSerializable) instead
	 * @param parityObject
	 * @return
	 */
	private static StringBuffer toXML(final ParityObject parityObject) {
		return new StringBuffer(xStream.toXML(parityObject));
	}

	/**
	 * Create an XStreamUtil [Singleton]
	 */
	private XStreamUtil() { super(); }
}
