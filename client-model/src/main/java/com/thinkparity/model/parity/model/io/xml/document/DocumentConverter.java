/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.util.Base64;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * The document converter is a class that translates parity documents to xml
 * using the xStream framework.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentConverter extends XmlIOConverter {

	/**
	 * Simple wrapper class for document content.  Since the document content
	 * xml tags contain a content and a checksum, the wrapper makes it easy
	 * to transfer between apis.
	 */
	private class ContentWrapper {

		/**
		 * Content bytes.
		 */
		private final byte[] bytes;

		/**
		 * Content cheksum.
		 */
		private final String checksum;

		/**
		 * Create a ContentWrapper.
		 * 
		 * @param bytes
		 *            The content bytes.
		 * @param checksum
		 *            The content checksum.
		 */
		private ContentWrapper(final byte[] bytes, final String checksum) {
			this.bytes = new byte[bytes.length];
			System.arraycopy(bytes, 0, this.bytes, 0, bytes.length);
			this.checksum = checksum;
		}

		/**
		 * Create a ContentWrapper.
		 * 
		 * @param document
		 *            The document to extract the content from.
		 */
		private ContentWrapper(final Document document) {
			this(document.getContent(), document.getContentChecksum());
		}
	}

	/**
	 * Compression level to use when writing the bytes to the version file.
	 */
	private static final Level compressionLevel = Level.Nine;

	/**
	 * Create a DocumentConverter
	 */
	public DocumentConverter() { super(); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(final Class type) {
		return type.equals(Document.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final Document document = (Document) source;
		logger.debug(document);
		final Project parent = document.getParent();
		logger.debug(parent);
		writeId(document.getId(), writer, context);
		writeName(document.getName(), writer, context);
		writeProjectId(parent.getId(), writer, context);
		writeCreatedBy(document.getCreatedBy(), writer, context);
		writeCreatedOn(document.getCreatedOn(), writer, context);
		writeDescription(document.getDescription(), writer, context);
		try { writeContent(new ContentWrapper(document), writer, context); }
		catch(IOException iox) {
			fatal((ParityObject) source, "Could not serialize file bytes.", iox);
		}
		writeNotes(document, writer, context);
		writeCustomProperties(document, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		Document document = null;
		try {
			final UUID id = readId(reader, context);
			final String name = readName(reader, context);
			final UUID projectId = readProjectId(reader, context);
			final String createdBy = readCreatedBy(reader, context);
			final Calendar createdOn = readCreatedOn(reader, context);
			final String description = readDescription(reader, context);
			final ContentWrapper content = readContent(reader, context);
			document = new Document(name, createdOn, createdBy, description,
					id, content.bytes, content.checksum);
			readNotes(document, reader, context);
			readCustomProperties(document, reader, context);
		}
		catch(Exception x) { fatal(document, "An unknown error occured parsing document xml.", x); }
		return document;
	}

	/**
	 * Read a document's bytes from xml. All bytes is base 64 encoded in the
	 * xml and compressed; so we decode it then decompress it.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xSream xml reader context.
	 * @return The bytes in a wrapper.
	 * @throws IOException
	 */
	private ContentWrapper readContent(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) throws IOException {
		reader.moveDown();
		final String byteContentChecksum = reader.getAttribute("checksum");
		final String encodedCompressedContent = reader.getValue();
		reader.moveUp();
		return new ContentWrapper(CompressionUtil.decompress(Base64
				.decodeBytes(encodedCompressedContent)), byteContentChecksum);
	}

	/**
	 * Read the project id from the xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The project id.
	 */
	private UUID readProjectId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		final String projectId = reader.getAttribute("projectId");
		return UUID.fromString(projectId);
	}

	/**
	 * Write a document's bytes to xml.  All bytes is compressed and base 64
	 * encoded before writing it to xml.
	 * 
	 * @param bytes
	 *            The bytes wrapper (contains bytes & checksum)
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 * @throws IOException
	 */
	private void writeContent(final ContentWrapper content,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) throws IOException {
		writer.startNode("content");
		writer.addAttribute("encoding", Base64.getEncoder());
		writer.addAttribute("compressionlevel", compressionLevel.toString());
		writer.addAttribute("checksum", content.checksum);
		final byte[] compressedContent =
			CompressionUtil.compress(content.bytes, compressionLevel);
		final String encodedContent = Base64.encodeBytes(compressedContent);
		writer.setValue(encodedContent);
		writer.endNode();
	}

	/**
	 * Write the project id to the xml writer.
	 * 
	 * @param projectId
	 *            The project id.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeProjectId(final UUID projectId,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("projectId", projectId.toString());
	}
}
