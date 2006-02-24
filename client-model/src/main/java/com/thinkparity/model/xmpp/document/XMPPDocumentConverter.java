/*
 * Nov 15, 2005
 */
package com.thinkparity.model.xmpp.document;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.zip.DataFormatException;

import com.thinkparity.codebase.CompressionUtil;

import com.thinkparity.model.parity.model.xml.XMLConstants;
import com.thinkparity.model.parity.model.xml.converter.XmlIOConverterErrorTranslator;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.xmpp.XMPPConverter;
import com.thinkparity.model.xmpp.XMPPConverterErrorTranslator;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPDocumentConverter extends XMPPConverter {

	/**
	 * Create a XMPPDocumentConverter.
	 */
	public XMPPDocumentConverter() { super(); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return type.equals(XMPPDocument.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final XMPPDocument xmppDocument = (XMPPDocument) source;
		logger.debug(xmppDocument);
		writeUniqueId(xmppDocument.getUniqueId(), writer, context);
		writeVersionId(xmppDocument.getVersionId(), writer, context);
		writeName(xmppDocument.getName(), writer, context);
		writeCreatedBy(xmppDocument.getCreatedBy(), writer, context);
		writeCreatedOn(xmppDocument.getCreatedOn(), writer, context);
		writeReceivedFrom(xmppDocument.getReceivedFrom(), writer, context);
		writeUpdatedBy(xmppDocument.getUpdatedBy(), writer, context);
		writeUpdatedOn(xmppDocument.getUpdatedOn(), writer, context);
		writeDescription(xmppDocument.getDescription(), writer, context);
		try { writeContent(xmppDocument.getContent(), writer, context); }
		catch(IOException iox) {
			logger.error("marshal(Object,HierarchicalStreamWriter,MarshallingContext)", iox);
			throw XMPPConverterErrorTranslator.translate(iox);
		}
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		final UUID uniqueId = readUniqueId(reader, context);
		final Long versionId = readVersionId(reader, context);
		final String name = readName(reader, context);
		final String createdBy = readCreatedBy(reader, context);
		final Calendar createdOn = readCreatedOn(reader, context);
		final String receivedFrom = readReceivedFrom(reader, context);
		final String updatedBy = readUpdatedBy(reader, context);
		final Calendar updatedOn = readUpdatedOn(reader, context);
		final String description = readDescription(reader, context);
		final byte[] content;
		try { content = readContent(reader, context); }
		catch(DataFormatException dfx) {
			logger.error("unmarshal(HierarchicalStreamReader,UnmarshallingContext)", dfx);
			throw XmlIOConverterErrorTranslator.translate(dfx);
		}
		catch(IOException iox) {
			logger.error("unmarshal(HierarchicalStreamReader,UnmarshallingContext)", iox);
			throw XmlIOConverterErrorTranslator.translate(iox);
		}
		return XMPPDocument.create(content, createdBy, createdOn, description,
				name, receivedFrom, uniqueId, updatedBy, updatedOn, versionId);
	}

	/**
	 * Write the content to the xStream xml writer.
	 * 
	 * @param content
	 *            The content to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 * @throws IOException
	 */
	private void writeContent(final byte[] content,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) throws IOException {
		writer.startNode("content");
		writer.addAttribute("encoding", Base64.getEncoder());
		writer.addAttribute("compressionlevel", XMLConstants.DEFAULT_COMPRESSION.toString());
		final byte[] compressedContent =
			CompressionUtil.compress(content, XMLConstants.DEFAULT_COMPRESSION);
		final String encodedContent = Base64.encodeBytes(compressedContent);
		writer.setValue(encodedContent);
		writer.endNode();		
	}

	/**
	 * Read the content from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The content.
	 */
	private byte[] readContent(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) throws DataFormatException,
			IOException {
		reader.moveDown();
		final String encodedCompressedContent = reader.getValue();
		reader.moveUp();
		return CompressionUtil.decompress(Base64.decodeBytes(encodedCompressedContent));
	}
}
