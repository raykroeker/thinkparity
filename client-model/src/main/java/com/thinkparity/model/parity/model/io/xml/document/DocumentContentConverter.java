/*
 * Nov 2, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.IOException;
import java.util.UUID;
import java.util.zip.DataFormatException;

import com.thinkparity.codebase.CompressionUtil;

import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.io.xml.IXmlIOConstants;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverterErrorTranslator;
import com.thinkparity.model.parity.util.Base64;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentContentConverter extends XmlIOConverter {

	/**
	 * Create a DocumentContentConverter.
	 */
	public DocumentContentConverter() { super(); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return type.equals(DocumentContent.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final DocumentContent content = (DocumentContent) source;
		logger.debug(content);
		writeDocumentId(content.getDocumentId(), writer, context);
		writeChecksum(content.getChecksum(), writer, context);
		try { writeContent(content.getContent(), writer, context); }
		catch(IOException iox) {
			logger.error("marshal(Object,HierarchicalStreamWriter,MarshallingContext)", iox);
			throw XmlIOConverterErrorTranslator.translate(iox);
		}
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		final UUID documentId = readDocumentId(reader, context);
		final String checksum = readChecksum(reader, context);
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
		return new DocumentContent(checksum, content, documentId);
	}

	/**
	 * Read the content's checksum from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The content checksum.
	 */
	private String readChecksum(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String checksum = reader.getValue();
		reader.moveUp();
		return checksum;
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

	/**
	 * Read the document id from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The document id.
	 */
	private UUID readDocumentId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return UUID.fromString(reader.getAttribute("documentId"));
	}

	/**
	 * Write the content checksum to the xStream xml writer.
	 * 
	 * @param checksum
	 *            The content checksum.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeChecksum(final String checksum,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("checksum");
		writer.setValue(checksum);
		writer.endNode();
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
		writer.addAttribute("compressionlevel", IXmlIOConstants.DEFAULT_COMPRESSION.toString());
		final byte[] compressedContent =
			CompressionUtil.compress(content, IXmlIOConstants.DEFAULT_COMPRESSION);
		final String encodedContent = Base64.encodeBytes(compressedContent);
		writer.setValue(encodedContent);
		writer.endNode();		
	}

	/**
	 * Write the document id to the xStream xml writer.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeDocumentId(final UUID documentId,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("documentId", documentId.toString());
	}
}
