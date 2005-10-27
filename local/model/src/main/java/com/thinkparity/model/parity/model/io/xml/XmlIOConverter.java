/*
 * 27-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.api.MalformedXmlException;
import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.model.note.Note;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * An abstraction of the xstream xml converters for the parity objects.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XmlIOConverter implements Converter {

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
	 * Create a XmlIOConverter.
	 */
	protected XmlIOConverter() {
		super();
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.preferences = workspace.getPreferences();
	}

	/**
	 * Log a non-recoverable error to the system logger, and throw an
	 * application exception.
	 * 
	 * @param parityObject
	 *            Context information regarding the error. (Optional)
	 * @param message
	 *            A custom error message.
	 * @param cause
	 *            The cause of the fatal error.
	 */
	protected void fatal(final ParityObject parityObject,
			final String message, final Throwable cause) {
		final StringBuffer contextMessage = new StringBuffer(message)
			.append(Separator.SystemNewLine)
			.append((null == parityObject ? "<null/>" : parityObject.toString()));
		logger.fatal(contextMessage, cause);
		throw new MalformedXmlException(message, cause);
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
	protected Calendar readCreatedOn(
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) throws ParseException {
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
	protected void readCustomProperties(final ParityObject parityObject,
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
	 * Read the unique id for the parity object.
	 * 
	 * @param reader
	 *            The xStream xml reader
	 * @param context
	 *            The xStream xml reader context.
	 * @return The unique id for a parity object.
	 */
	protected UUID readId(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		final String id = reader.getAttribute("id");
		return UUID.fromString(id);
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
	 * Read notes from the xml reader and add them to the parity object.
	 * 
	 * @param parityObject
	 *            The parity object to add the notes to.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 */
	protected void readNotes(final ParityObject parityObject,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		try { 
			reader.moveDown();
			while(reader.hasMoreChildren()) {
				readNote(parityObject, reader, context);
			}
			reader.moveUp();
		}
		catch(Exception x) { fatal(parityObject, "Could not read parity object's notes.", x); }
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
	 * Write the creation date of a parity object. The format for the creation
	 * date is as follows: yyyy-MM-dd HH:mm:ss.SSS;;
	 * 
	 * @param createdOn
	 *            The parity object's creation date.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeCreatedOn(final Calendar createdOn,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		final String timestamp = String.valueOf(createdOn.getTimeInMillis());
		final TimeZone timeZone = createdOn.getTimeZone();
		final Locale locale = preferences.getLocale();
		writer.startNode("createdOn");
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
	protected void writeCustomProperties(final ParityObject parityObject,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		final Properties customProperties = parityObject.getCustomProperties();
		if(0 < customProperties.size()) {
			final String username = preferences.getUsername();
			writer.startNode(username);
			final Enumeration<?> customPropertyKeys = customProperties.propertyNames();
			while(customPropertyKeys.hasMoreElements()) {
				final String customPropertyKey = (String) customPropertyKeys
						.nextElement();
				final String customPropertyValue =
					customProperties.getProperty(customPropertyKey);
				writer.startNode(customPropertyKey);
				if(null != customPropertyValue &&
						0 < customPropertyValue.length())
					writer.setValue(customPropertyValue);
				writer.endNode();
			}
			writer.endNode();
		}
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
	 * Write the id for the parity object.
	 * 
	 * @param uuid
	 *            The parity object's unique id.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeId(final UUID uuid,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.addAttribute("id", uuid.toString());
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
	 * Write a series of notes for the parity object.
	 * 
	 * @param parityObject
	 *            The parity object containing the notes.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	protected void writeNotes(final ParityObject parityObject,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("notes");
		for(Note note : parityObject.getNotes()) {
			writeNote(note, writer, context);
		}		
		writer.endNode();
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
	private void readCustomProperty(final ParityObject parityObject,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		reader.moveDown();
		final String key = reader.getNodeName();
		final String value = reader.getValue();
		parityObject.setCustomProperty(key, value);
		reader.moveUp();
	}
	/**
	 * Read a note from the xml reader and add it to the parity object.
	 * 
	 * @param parityObject
	 *            The parity object to add the note to.
	 * @param reader
	 *            The xStream xml reader.
	 * @param context
	 *            The xStream xml reader context.
	 */
	private void readNote(final ParityObject parityObject,
			final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		try {
			reader.moveDown();
			final String subject = reader.getAttribute("subject");
			final String content = reader.getValue();
			parityObject.add(new Note(subject, content));
			reader.moveUp();
		}
		catch(Exception x) { fatal(parityObject, "Could not read parity object's note.", x); }
	}

	/**
	 * Write a note to the xml writer.
	 * 
	 * @param note
	 *            The note to write.
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeNote(final Note note,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		writer.startNode("note");
		writer.addAttribute("subject", note.getSubject());
		if(note.isSetContent())
			writer.setValue(note.getContent());
		writer.endNode();
	}

	
}

