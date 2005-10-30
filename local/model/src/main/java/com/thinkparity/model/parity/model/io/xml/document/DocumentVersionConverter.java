/*
 * May 30, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.util.UUID;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.document.DocumentVersionBuilder;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * The document version converter is a class that translates parity document
 * versions to and from xml using the xStream framework.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionConverter extends XmlIOConverter {

	/**
	 * Handle to a document converter.
	 */
	private DocumentConverter documentConverter;

	/**
	 * Create a DocumentVersionConverter
	 */
	public DocumentVersionConverter() {
		this.documentConverter = new DocumentConverter();
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(final Class type) {
		return type.equals(DocumentVersion.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final DocumentVersion version = (DocumentVersion) source;
		logger.debug(version);
		final Document document = version.getDocument();
		logger.debug(document);
		writeVersionId(version.getVersion(), writer, context);
		writeDocumentId(document.getId(), writer, context);
		writeDocument(version.getDocument(), writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		final String version = readVersionId(reader, context);
		final UUID documentId = readDocumentId(reader, context);
		final Document snapshot = readDocument(reader, context);
		return DocumentVersionBuilder.getVersion(version, snapshot);
	}

	/**
	 * Read the document from the xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The document.
	 */
	private Document readDocument(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final Document document =
			(Document) documentConverter.unmarshal(reader, context);
		reader.moveUp();
		return document;
	}

	/**
	 * Read the document id from the xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The document id.
	 */
	private UUID readDocumentId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		final String documentId = reader.getAttribute("documentId");
		return UUID.fromString(documentId);
	}

	/**
	 * Read the version id from the xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The version id.
	 */
	private String readVersionId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return reader.getAttribute("id");
	}

	/**
	 * Write the document to the xml writer.
	 * 
	 * @param document
	 *            The document to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeDocument(final Document document,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("document");
		documentConverter.marshal(document, writer, context);
		writer.endNode();
	}

	/**
	 * Write the document id to the xml writer.
	 * 
	 * @param documentId
	 *            The document id.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeDocumentId(final UUID documentId,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("documentId", documentId.toString());
	}

	/**
	 * Write the version id to the xml writer.
	 * 
	 * @param id
	 *            The version id.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeVersionId(final String id,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("id", id);
	}
}
