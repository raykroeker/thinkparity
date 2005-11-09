/*
 * Nov 8, 2005
 */
package com.thinkparity.model.parity.model.io.xml.index;

import java.io.File;
import java.util.UUID;

import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IndexConverter extends XmlIOConverter {

	/**
	 * Create a IndexConverter.
	 */
	public IndexConverter() { super(); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return type.equals(Index.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter)");
		final Index index = (Index) source;
		logger.debug(index);
		writeIndex(index, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		final Index index = new Index();
		readIndex(index, reader, context);
		return index;
	}

	/**
	 * Read the index information into the index.
	 * 
	 * @param index
	 *            The index to add the information to.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 */
	private void readIndex(final Index index,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		while(reader.hasMoreChildren()) {
			reader.moveDown();
			index.addLookup(
					UUID.fromString(reader.getAttribute("id")),
					new File(reader.getAttribute("xmlFile")));
			reader.moveUp();
		}
	}

	/**
	 * Write all of the index information tied to an id.
	 * 
	 * @param index
	 *            The index.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeIndex(final Index index,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		for(UUID id : index.getIds()) {
			writer.startNode("entry");
			writer.addAttribute("id", id.toString());
			writer.addAttribute("xmlFile", index.lookupXmlFile(id).getAbsolutePath());
			writer.endNode();
		}
	}
}
