/*
 * Mar 8, 2005
 */
package com.thinkparity.model.parity.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.parity.api.note.Note;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.model.workspace.WorkspaceModel;
import com.thinkparity.model.parity.util.UUIDGenerator;
import com.thinkparity.model.parity.util.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.xml.XmlUtil;

import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * ParityXmlTranslator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class ParityXmlTranslator {

	/**
	 * Handle to an internal logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(ParityXmlTranslator.class);

	/**
	 * Handle to parity preferences.
	 */
	protected final Preferences preferences;

	/**
	 * Handle to parity workspace.
	 */
	protected final Workspace workspace;

	/**
	 * Alias used by the XStream library to resolve an object to an xml tag.
	 */
	private String xmlAlias;

	/**
	 * Alias class used by the XStream library to resolve a class given an xml
	 * alias.
	 */
	private Class xmlAliasClass;

	/**
	 * Create a ParityXmlTranslator
	 */
	protected ParityXmlTranslator(final String xmlAlias, final Class xmlAliasClass) {
		super();
		this.xmlAlias = xmlAlias;
		this.xmlAliasClass = xmlAliasClass;
		this.workspace = WorkspaceModel.getModel().getWorkspace();
		this.preferences = workspace.getPreferences();
	}

	/**
	 * Determine whether or not the xml translator can convert the given class
	 * to xml.
	 * @param convertTo <code>java.lang.Class</code>
	 * @return <code>boolean</code>
	 */
	public boolean canConvert(final Class convertTo) {
		return xmlAliasClass.equals(convertTo);
	}

	/**
	 * Log a recoverable error to the system logger.
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param cause
	 *            <code>java.lang.Throwable</code>
	 */
	protected final void error(final ParityObject parityObject,
			final String message, final Throwable cause) {
		final StringBuffer contextMessage = new StringBuffer(message)
			.append(Separator.SystemNewLine)
			.append((null == parityObject ? "<null/>" : parityObject.logMe()));
		logger.error(contextMessage, cause);
	}

	/**
	 * Log a non-recoverable error to the system logger, and throw an
	 * application exception.
	 * 
	 * @param parityObject
	 *            <code>org.kcs.projectmanager.client.api.ParityObject</code>
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param cause
	 *            <code>java.lang.Throwable</code>
	 */
	protected final void fatal(final ParityObject parityObject,
			final String message, final Throwable cause) {
		final StringBuffer contextMessage = new StringBuffer(message)
			.append(Separator.SystemNewLine)
			.append((null == parityObject ? "<null/>" : parityObject.logMe()));
		logger.fatal(contextMessage, cause);
		throw new MalformedXmlException(message, cause);
	}

	protected final void fatal(
			final ParityXmlSerializable parityXmlSerializable,
			final String message, final Throwable cause) {
		final StringBuffer messageBuffer = new StringBuffer(message)
			.append(Separator.SystemNewLine)
			.append((null == parityXmlSerializable ? "<null/>" : parityXmlSerializable.logMe()));
		logger.fatal(messageBuffer.toString(), cause);
		throw new MalformedXmlException(message, cause);
	}

	/**
	 * Obtain the xmlAlias for the given xml translator. This is set in each
	 * translator's call to the abstract constructor and cannot be changed.
	 * 
	 * @return <code>java.lang.String</code>
	 */
	public final String getXmlAlias() { return xmlAlias; }

	/**
	 * Obtain the xmlAliasClass for the given xml translator. This is set in
	 * each translator's call to the abstract constructor and cannot be changed.
	 * 
	 * @return <code>java.lang.Class</code>
	 */
	public final Class getXmlAliasClass() { return xmlAliasClass; }

	/**
	 * Read the creation username of a parity object. The creation username is
	 * required for all parity objects, and this cannot be overriden.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.lang.String</code>
	 */
	protected final String readCreatedBy(final HierarchicalStreamReader reader) {
		return reader.getAttribute("createdBy");
	}

	/**
	 * Read the creation date of a parity object. The creation date is requried
	 * for all parity objects and cannot be overridden.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.util.Calendar</code>
	 * @throws ParseException -
	 *             if the createdOn property cannot be parsed
	 */
	protected final Calendar readCreatedOn(final HierarchicalStreamReader reader)
			throws ParseException {
		reader.moveDown();								// <createdOn>
		reader.moveDown();									// <timestamp>
		final String timestamp = reader.getValue();
		reader.moveUp();									// </timestamp>
		reader.moveDown();									// <locale>
		reader.moveDown();										// <country>
		final String country = reader.getValue();
		reader.moveUp();										// </country>
		reader.moveDown();										// <language>
		final String language = reader.getValue();
		reader.moveUp();										// </language>
		reader.moveUp();									// </locale>
		reader.moveDown();									// <timeZone>
		final String timeZoneID = reader.getValue();
		reader.moveUp();									// </timeZone>
		reader.moveUp();								// </createdOn>
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
	 * 	<project name="project a">
	 * 		<userX>
	 * 			<name>parity documentation</name>
	 * 		</userX>
	 * 	</project>
	 * 
	 * @param parityObject
	 *            <code>org.kcs.projectmanager.client.api.ParityObject</code>
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchichalStreamReader</code>
	 */
	protected final void readCustomProperties(final ParityObject parityObject,
			final HierarchicalStreamReader reader) {
		if(reader.hasMoreChildren()) {
			reader.moveDown(); 									// <username>
			while(reader.hasMoreChildren()) {
				readCustomProperty(parityObject, reader);
			}
			reader.moveUp(); 									// </username>
		}
	}

	protected final void readCustomProperty(final ParityObject parityObject,
			final HierarchicalStreamReader reader) {
		reader.moveDown();											// <custom>
		final String key = reader.getNodeName();
		final String value = reader.getValue();
		parityObject.setCustomProperty(key, value);
		reader.moveUp();											// </custom>
	}

	/**
	 * Read the description of a parity object. The description is not required,
	 * however this can be changed by overloading this api.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.lang.String</code>
	 */
	protected String readDescription(final HierarchicalStreamReader reader) {
		reader.moveDown();
		final String description = reader.getValue();
		reader.moveUp();
		return description;
	}

	protected final File readDirectory(final HierarchicalStreamReader reader) {
		reader.moveDown();
		final String directoryAbsolutePath = reader.getValue();
		reader.moveUp();
		return new File(directoryAbsolutePath);
	}

	/**
	 * Read the id for the parity object.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.util.UUID</code>
	 */
	protected final UUID readId(final HierarchicalStreamReader reader) {
		reader.moveDown();											// <id>
		final String uuidVersion = reader.getAttribute("version");
		if(Long.parseLong(uuidVersion) != UUIDGenerator.getVersionId())
			throw new RuntimeException("Fatal exception.  UUID generator out of synch with project files.");
		final String id = reader.getValue();
		reader.moveUp();											// </id>
		return UUID.fromString(id);
	}

	protected final String readKeyHolder(final HierarchicalStreamReader reader) {
		return reader.getAttribute("keyHolder");
	}

	/**
	 * Read the name of a parity object. The name is required for all parity
	 * objects, and this cannot be overridden.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.lang.String</code>
	 */
	protected final String readName(final HierarchicalStreamReader reader) {
		return reader.getAttribute("name");
	}

	/**
	 * Read the name as a node value. This is done by the parity objects which
	 * provide custom attributes.
	 * 
	 * @param reader
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamReader</code>
	 * @return <code>java.lang.String</code>
	 */
	protected final String readNameNode(final HierarchicalStreamReader reader) {
		reader.moveDown();
		final String name = reader.getValue();
		reader.moveUp();
		return name;
	}

	protected final void readNote(final ParityObject parityObject,
			final HierarchicalStreamReader reader) {
		try {
			reader.moveDown();										// <note>
			final String subject = reader.getAttribute("subject");
			final String content = reader.getValue();
			parityObject.add(new Note(subject, content));
			reader.moveUp();										// </note>
		}
		catch(Exception x) { fatal(parityObject, "Could not read parity object's note.", x); }
	}

	protected final void readNotes(final ParityObject parityObject,
			final HierarchicalStreamReader reader) {
		try { 
			reader.moveDown();										// <notes>
			while(reader.hasMoreChildren()) { readNote(parityObject, reader); }
			reader.moveUp();										// </notes>
		}
		catch(Exception x) { fatal(parityObject, "Could not read parity object's notes.", x); }
	}

	protected final void readVersion(final ParityObject parityObject,
			final HierarchicalStreamReader reader) throws FileNotFoundException {
		reader.moveDown();	// <version>
		final File versionMetaDataFile = new File(reader.getValue());
		parityObject.add((ParityObjectVersion) XmlUtil
				.fromXml(new InputStreamReader(new FileInputStream(
						versionMetaDataFile))));
		reader.moveUp();	// </version>
	}

	protected final void readVersions(final ParityObject parityObject,
			final HierarchicalStreamReader reader) throws FileNotFoundException {
		reader.moveDown();	// <versions>
		while(reader.hasMoreChildren()) { readVersion(parityObject, reader); }
		reader.moveUp();	// </versions>
	}

	/**
	 * Write the creation username of a parity object. The creation username is
	 * required for all parity objects, and this cannot be overridden.
	 * 
	 * @param username
	 *            <code>java.lang.String</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeCreatedBy(final String username,
			final HierarchicalStreamWriter writer) {
		writer.addAttribute("createdBy", username);
	}

	/**
	 * Write the creation date of a parity object. The creation date is required
	 * for all parity objects, and this cannot be overridden.  The format for
	 * the creation date is as follows:
	 * yyyy-MM-dd HH:mm:ss.SSS;;
	 * 
	 * @param createdOn
	 *            <code>java.util.Calendar</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeCreatedOn(final Calendar createdOn,
			final HierarchicalStreamWriter writer) {
		final String timestamp = String.valueOf(createdOn.getTimeInMillis());
		final TimeZone timeZone = createdOn.getTimeZone();
		final Locale locale = preferences.getLocale();
		writer.startNode("createdOn"); 				// <createdOn>
		writer.startNode("timestamp"); 					// <timestamp>
		writer.setValue(timestamp);
		writer.endNode(); 								// </timestamp>
		writer.startNode("locale");						// <locale>
		writer.startNode("country");						// <country>
		writer.setValue(locale.getCountry());
		writer.endNode();									// </country>
		writer.startNode("language");						// <language>
		writer.setValue(locale.getLanguage());
		writer.endNode();									// </language>
		writer.endNode(); 								// </locale>
		writer.startNode("timeZone");					// <timeZone>
		writer.setValue(timeZone.getID());
		writer.endNode();								// </timeZone>
		writer.endNode(); 							// </createdOn>
	}


	/**
	 * Write the end node for the custom parity attributes. The custom
	 * attributes are located within tags identified by the username.
	 * 
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeCustomAttributesEnd(
			final HierarchicalStreamWriter writer) {
		writer.endNode();
	}

	/**
	 * Write the start node for the custom parity attributes. The custom
	 * attributes are located within tags identified by the username.
	 * 
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeCustomAttributesStart(
			final HierarchicalStreamWriter writer) {
		final String username = preferences.getUsername();
		writer.startNode(username);
	}

	protected final void writeCustomProperties(final ParityObject parityObject,
			final HierarchicalStreamWriter writer) {
		final Properties customProperties = parityObject.getCustomProperties();
		if(0 < customProperties.size()) {
			final String username = preferences.getUsername();
			writer.startNode(username); // <username>
			final Enumeration<?> customPropertyKeys = customProperties.propertyNames();
			while(customPropertyKeys.hasMoreElements()) {
				final String customPropertyKey = (String) customPropertyKeys
						.nextElement();
				final String customPropertyValue =
					customProperties.getProperty(customPropertyKey);
				writer.startNode(customPropertyKey); // <custom>
				if(null != customPropertyValue &&
						0 < customPropertyValue.length())
					writer.setValue(customPropertyValue);
				writer.endNode(); // </custom>
			}
			writer.endNode(); // </username>
		}
	}

	/**
	 * Write the description of a parity object. The description is not
	 * required; however this can be changed by overloading this api.
	 * 
	 * @param description
	 *            <code>java.lang.String</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected void writeDescription(final String description,
			final HierarchicalStreamWriter writer) {
		writer.startNode("description");
		if(null != description)
			writer.setValue(description);
		writer.endNode();
	}

	/**
	 * Write the directory uri of a parity object. The directory uri is
	 * required, so if it is null, a NullPointerException is thrown.
	 * 
	 * @param directory
	 *            <code>java.io.File</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeDirectory(final File directory,
			final HierarchicalStreamWriter writer) {
		writer.startNode("directory");
		writer.setValue(directory.getAbsolutePath());
		writer.endNode();
	}

	/**
	 * Write the id for the parity object.
	 * 
	 * @param uuid
	 *            <code>java.util.UUID</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeId(final UUID uuid,
			final HierarchicalStreamWriter writer) {
		writer.startNode("id");										// <id>
		writer.addAttribute("version", UUIDGenerator.getVersionId().toString());
		writer.setValue(uuid.toString());
		writer.endNode();											// </id>
	}
	protected final void writeKeyHolder(final ParityObject parityObject,
			final HierarchicalStreamWriter writer) {
		writer.addAttribute("keyHolder", parityObject.getKeyHolder());
	}

	/**
	 * Write the name of a parity object. The name is required for all parity
	 * objects, and this cannot be overridden.
	 * 
	 * @param name
	 *            <code>java.lang.String</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeName(final String name,
			final HierarchicalStreamWriter writer) {
		writer.addAttribute("name", name);
	}
	/**
	 * Write the name as a node rather than an attribute. This is used by parity
	 * objects which have custom name attributes.
	 * 
	 * @param name
	 *            <code>java.lang.String</code>
	 * @param writer
	 *            <code>com.thoughtworks.xstream.io.HierarchicalStreamWriter</code>
	 */
	protected final void writeNameNode(final String name,
			final HierarchicalStreamWriter writer) {
		writer.startNode("name");
		if(null != name)
			writer.setValue(name);
		writer.endNode();
	}
	private void writeNote(final Note note,
			final HierarchicalStreamWriter writer) {
		writer.startNode("note");									// <note>
		writer.addAttribute("subject", note.getSubject());
		writer.setValue(note.getContent());
		writer.endNode();											// </note>
	}
	protected final void writeNotes(final ParityObject parityObject,
			final HierarchicalStreamWriter writer) {
		writer.startNode("notes");									// <notes>
		for(Iterator<Note> iNotes = parityObject.getNotes().iterator();
			iNotes.hasNext();) {
			writeNote(iNotes.next(), writer);
		}		
		writer.endNode();											// </notes>
	}

	protected final void writeVersion(final ParityObject parityObject,
			final ParityObjectVersion version,
			final HierarchicalStreamWriter writer) {
		writer.startNode("version");								// <version>
		writer.addAttribute("versionId", version.getVersion());
		writer.setValue(version.getMetaDataFile(
				parityObject.getMetaDataDirectory()).getAbsolutePath());
		writer.endNode();										// </version>		
	}

	protected final void writeVersions(final ParityObject parityObject,
			final HierarchicalStreamWriter writer) {
		writer.startNode("versions");
		for (Iterator<ParityObjectVersion> i = parityObject.getVersions()
				.iterator(); i.hasNext();) {
			writeVersion(parityObject, i.next(), writer);
		}
		writer.endNode();
	}
}
