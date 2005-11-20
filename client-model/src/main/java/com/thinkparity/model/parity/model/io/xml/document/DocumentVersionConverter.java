/*
 * May 30, 2005
 */
package com.thinkparity.model.parity.model.io.xml.document;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentAction;
import com.thinkparity.model.parity.model.document.DocumentActionData;
import com.thinkparity.model.parity.model.document.DocumentVersion;
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
	 * Simple wrapper used by writeAction\readAction.
	 */
	private class ActionWrapper {
		private final DocumentAction action;
		private final DocumentActionData actionData;
		private ActionWrapper(final DocumentAction action,
				final DocumentActionData actionData) {
			this.action = action;
			this.actionData = actionData;
		}
	}

	/**
	 * Handle to a document converter.
	 */
	private final DocumentConverter converter;

	/**
	 * Create a DocumentVersionConverter
	 */
	public DocumentVersionConverter() {
		this.converter = new DocumentConverter();
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
		writeVersionId(version.getVersionId(), writer, context);
		writeAction(new ActionWrapper(version.getAction(), version.getActionData()), writer, context);
		writeSnapshot(version.getSnapshot(), writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext)");
		final String versionId = readVersionId(reader, context);
		final ActionWrapper actionWrapper = readAction(reader, context);
		final Document snapshot = readSnapshot(reader, context);
		return new DocumentVersion(snapshot.getId(), versionId, snapshot,
				actionWrapper.action, actionWrapper.actionData);
	}

	/**
	 * Read an action into an action wrapper.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The action; and action data.
	 */
	private ActionWrapper readAction(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final DocumentAction action =
			DocumentAction.fromId(reader.getAttribute("id"));
		final DocumentActionData actionData;
		if(reader.hasMoreChildren()) {
			reader.moveDown();
			actionData = new DocumentActionData();
			while(reader.hasMoreChildren()) {
				readActionDataItem(actionData, reader, context);
			}
			reader.moveUp();
		}
		else { actionData = null; }
		reader.moveUp();
		return new ActionWrapper(action, actionData);
	}

	/**
	 * Read an extra action data item from the xml reader into the action data
	 * object.
	 * 
	 * @param actionData
	 *            The action data to set the data item within.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 */
	private void readActionDataItem(final DocumentActionData actionData,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		actionData.setDataItem(reader.getAttribute("key"), reader.getValue());
		reader.moveUp();
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
	private Document readSnapshot(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final Document document =
			(Document) converter.unmarshal(reader, context);
		reader.moveUp();
		return document;
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
	 * Write the action for the document version.
	 * 
	 * @param actionWrapper
	 *            The action\action data to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeAction(final ActionWrapper actionWrapper,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("action");
		writer.addAttribute("id", actionWrapper.action.getActionId());
		if(actionWrapper.actionData.containsData()) {
			for(String key : actionWrapper.actionData.keys()) {
				writeActionDataItem(key, actionWrapper.actionData
						.getDataItem(key), writer, context);
			}
		}
		writer.endNode();
	}

	/**
	 * Write an action data item to the writer.
	 * 
	 * @param key
	 *            The data item key.
	 * @param value
	 *            The data item value.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeActionDataItem(final String key, final String value,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("data");
		writer.addAttribute("key", key);
		writer.setValue(value);
		writer.endNode();
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
	private void writeSnapshot(final Document document,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("snapshot");
		converter.marshal(document, writer, context);
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
