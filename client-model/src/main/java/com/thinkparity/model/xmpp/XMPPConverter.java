/*
 * 15-Nov-2005
 */
package com.thinkparity.model.xmpp;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * An abstraction of the xStream xml converter for xmpp objects.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.1
 */
public abstract class XMPPConverter implements Converter {

	/**
	 * Handle to an internal logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(getClass());

	/**
	 * Handle to parity preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to parity workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Create a XMPPConverter.
	 */
	protected XMPPConverter() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.preferences = workspace.getPreferences();
	}

	/**
	 * Read the creation username of a parity object. 
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The parity object's creator.
	 */
	protected String readCreatedBy(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String createdBy = reader.getValue();
		reader.moveUp();
		return createdBy;
	}

	/**
	 * Read the creation date of a parity object.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The creation date.
	 * @throws ParseException
	 */
	protected Calendar readCreatedOn(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return readCalendar(reader, context);
	}

	/**
	 * Read any remaining custom properties from the xml file. The custom
	 * properties are expected to all be listed beneath a single tag which is
	 * the current user's name. They are also expected to each be a separate tag
	 * where the tag name is the custom property's name. For example, the name
	 * of a parity object can be over-ridden by using a custom property. The xml
	 * for userX would look like this:
	 * <code>
	 * 	<project name="project a">
	 * 		<userX>
	 * 			<name>parity documentation</name>
	 * 		</userX>
	 *	</project>
	 * </code>
	 * 
	 * @param parityObject
	 *            The object within which the custom properties are to be set.
	 * @param reader
	 *            The xStream xml reader
	 * @param context
	 *            The xStream xml reader context.
	 */
	protected void readCustomProperties(final Artifact parityObject,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		if(reader.hasMoreChildren()) {
			reader.moveDown();
			while(reader.hasMoreChildren()) {
				readCustomProperty(parityObject, reader, context);
			}
			reader.moveUp();
		}
	}

	/**
	 * Read the description of a parity object.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return <code>java.lang.String</code>
	 */
	protected String readDescription(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String description = reader.getValue();
		reader.moveUp();
		return description;
	}

	/**
	 * Read the list of flags from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The list of flags read from the xml reader.
	 */
	protected Collection<ArtifactFlag> readFlags(
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final Collection<ArtifactFlag> flags = new Vector<ArtifactFlag>(1);
		while(reader.hasMoreChildren()) {
			readFlag(flags, reader, context);
		}
		reader.moveUp();
		return flags;
	}

	/**
	 * Read the name of a parity object.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 * @return The parity object's name.
	 */
	protected String readName(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return reader.getAttribute("name");
	}

	/**
	 * Read the unique id for the parity object.
	 * 
	 * @param reader
	 *            The xStream xml reader
	 * @param context
	 *            The xStream xml reader context.
	 * @return The unique id for a parity object.
	 */
	protected UUID readUniqueId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		final String id = reader.getAttribute("uniqueId");
		return UUID.fromString(id);
	}

	/**
	 * Read the updator from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The updator.
	 */
	protected String readUpdatedBy(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String updatedBy = reader.getValue();
		reader.moveUp();
		return updatedBy;
	}

	/**
	 * Read the update date from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return The update date.
	 */
	protected Calendar readUpdatedOn(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return readCalendar(reader, context);
	}

	/**
	 * Write the creation username of a parity object. The creation username is
	 * required for all parity objects, and this cannot be overridden.
	 * 
	 * @param username
	 *            The creator's username.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeCreatedBy(final String username,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("createdBy");
		writer.setValue(username);
		writer.endNode();
	}

	/**
	 * Write the creation date to the xStream xml writer.
	 * 
	 * @param createdOn
	 *            The creation date.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeCreatedOn(final Calendar createdOn,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		context.put("attribute", "createdOn");
		writeCalendar(createdOn, writer, context);
	}

	/**
	 * Write the custom properties of the parity object to xml.
	 * 
	 * @param parityObject
	 *            The parity object containing the custom properties.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeCustomProperties(final Artifact parityObject,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		logger.warn("NO CUSTOM PROPERTY SERIALIZATION");
	}

	/**
	 * Write the description of a parity object.
	 * 
	 * @param description
	 *            The parity object's descriptioni.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeDescription(final String description,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("description");
		if(null != description)
			writer.setValue(description);
		writer.endNode();
	}

	/**
	 * Write the list of flags to the xStream xml writer.
	 * 
	 * @param flags
	 *            The list of flags to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	protected void writeFlags(final Collection<ArtifactFlag> flags,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("flags");
		for(ArtifactFlag flag : flags) {
			writer.startNode("flag");
			writer.setValue(flag.toString());
			writer.endNode();
		}
		writer.endNode();
	}

	/**
	 * Write the name of a parity object. The name is required for all parity
	 * objects, and this cannot be overridden.
	 * 
	 * @param name
	 *            The parity object's name.
	 * @param writer
	 *            The xStream xml writer
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeName(final String name,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("name", name);
	}

	/**
	 * Write a unique id to the xstream writer.
	 * 
	 * @param uniqueId
	 *            The unique id.
	 * @param writer
	 *            The xstream writer.
	 * @param context
	 *            The xstream unmarshalling context.
	 */
	protected void writeUniqueId(final UUID uniqueId,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("uniqueId", uniqueId.toString());
	}

	/**
	 * Write the updated by to the xStream xml writer.
	 * 
	 * @param updatedBy
	 *            The updator.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	protected void writeUpdatedBy(final String updatedBy,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("updatedBy");
		writer.setValue(updatedBy);
		writer.endNode();
	}
	/**
	 * Write the update date to the xStream xml writer.
	 * 
	 * @param updatedOn
	 *            The update date.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	protected void writeUpdatedOn(final Calendar updatedOn,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		context.put("attribute", "updatedOn");
		writeCalendar(updatedOn, writer, context);
	}

	/**
	 * Read a parity calendar from the xStream xml reader.
	 * 
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 * @return A java calendar.
	 */
	private Calendar readCalendar(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		reader.moveDown();
		final String timestamp = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		reader.moveDown();
		final String country = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		final String language = reader.getValue();
		reader.moveUp();
		reader.moveUp();
		reader.moveDown();
		final String timeZoneID = reader.getValue();
		reader.moveUp();
		reader.moveUp();
		final Locale locale = new Locale(country, language);
		final TimeZone timeZone = TimeZone.getTimeZone(timeZoneID);
		return DateUtil.getInstance(new Long(timestamp), timeZone, locale);
	}

	/**
	 * Read a custom property from the xml reader and add it to the parity
	 * object.
	 * 
	 * @param parityObject
	 *            The parity object to add the custom property to.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 */
	private void readCustomProperty(final Artifact parityObject,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String key = reader.getNodeName();
		final String value = reader.getValue();
		parityObject.setCustomProperty(key, value);
		reader.moveUp();
	}

	/**
	 * Read an individual flag into the list of flags.
	 * 
	 * @param flags
	 *            The list of existing flags.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader's context.
	 */
	private void readFlag(final Collection<ArtifactFlag> flags,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		flags.add(ArtifactFlag.valueOf(reader.getValue()));
		reader.moveUp();
	}

	/**
	 * Write a calendar to the xStream xml writer. Note that the "attribute"
	 * data must be set within the context. This attribute is used to generate
	 * the primary tag.
	 * 
	 * @param calendar
	 *            The calendar to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer's context.
	 */
	private void writeCalendar(final Calendar calendar,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		final String timestamp = String.valueOf(calendar.getTimeInMillis());
		final TimeZone timeZone = calendar.getTimeZone();
		final Locale locale = preferences.getLocale();
		writer.startNode((String) context.get("attribute"));
		writer.startNode("timestamp");
		writer.setValue(timestamp);
		writer.endNode();
		writer.startNode("locale");
		writer.startNode("country");
		writer.setValue(locale.getCountry());
		writer.endNode();
		writer.startNode("language");
		writer.setValue(locale.getLanguage());
		writer.endNode();
		writer.endNode();
		writer.startNode("timeZone");
		writer.setValue(timeZone.getID());
		writer.endNode();
		writer.endNode();
	}
}

