/*
 * Nov 19, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import com.thinkparity.model.parity.model.document.DocumentContent;
import com.thinkparity.model.parity.model.document.DocumentVersionContent;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * Write a document version content to an xml stream.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionContentConverter extends XmlIOConverter {

	/**
	 * Handle to a document content converter.
	 */
	private final DocumentContentConverter contentConverter;

	/**
	 * Create a DocumentVersionContentConverter.
	 */
	public DocumentVersionContentConverter() {
		super();
		this.contentConverter = new DocumentContentConverter();
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return type.equals(DocumentVersionContent.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final DocumentVersionContent versionContent = (DocumentVersionContent) source;
		logger.debug(versionContent);
		writeVersionId(versionContent.getVersionId(), writer, context);
		writeContentSnapshot(versionContent.getSnapshot(), writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		final String versionId = readVersionId(reader, context);
		final DocumentContent documentContent = readContentSnapshot(reader, context);
		return new DocumentVersionContent(documentContent,
				documentContent.getDocumentId(), versionId);
	}

	/**
	 * Read the document content snapshot from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The document content.
	 */
	private DocumentContent readContentSnapshot(
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final DocumentContent content = (DocumentContent) contentConverter.unmarshal(
				reader, context);
		reader.moveUp();
		return content;
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
	 * Write the document content to the xStream xml writer.
	 * 
	 * @param content
	 *            The document content.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeContentSnapshot(final DocumentContent content,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("snapshot");
		contentConverter.marshal(content, writer, context);
		writer.endNode();
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
