/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * The document converter is a class that translates parity documents to xml
 * using the xStream framework.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
public class DocumentConverter extends XmlIOConverter {

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
		writeId(document.getId(), writer, context);
		writeName(document.getName(), writer, context);
		writeProjectId(document.getParentId(), writer, context);
		writeCreatedBy(document.getCreatedBy(), writer, context);
		writeCreatedOn(document.getCreatedOn(), writer, context);
		writeUpdatedBy(document.getUpdatedBy(), writer, context);
		writeUpdatedOn(document.getUpdatedOn(), writer, context);
		writeFlags(document.getFlags(), writer, context);
		writeDescription(document.getDescription(), writer, context);
		writeNotes(document, writer, context);
		writeCustomProperties(document, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		final UUID id = readId(reader, context);
		final String name = readName(reader, context);
		final UUID projectId = readProjectId(reader, context);
		final String createdBy = readCreatedBy(reader, context);
		final Calendar createdOn = readCreatedOn(reader, context);
		final String updatedBy = readUpdatedBy(reader, context);
		final Calendar updatedOn = readUpdatedOn(reader, context);
		final Collection<ParityObjectFlag> flags = readFlags(reader, context);
		final String description = readDescription(reader, context);
		final Document document = new Document(createdBy, createdOn,
				description, flags, id, name, projectId, updatedBy, updatedOn);
		readNotes(document, reader, context);
		readCustomProperties(document, reader, context);
		return document;
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
