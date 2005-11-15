/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.parity.model.io.xml.XmlIOConverter;
import com.thinkparity.model.parity.model.project.Project;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * ProjectConverter
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public class ProjectConverter extends XmlIOConverter {

	/**
	 * Create a ProjectConverter
	 */
	public ProjectConverter() { super(); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) { return type.equals(Project.class); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		logger.info("marshal(Object,HierarchicalStreamWriter,MarshallingContext)");
		final Project project = (Project) source;
		logger.debug(project);
		writeId(project.getId(), writer, context);
		writeName(project.getName(), writer, context);
		writeProjectId(project.getParentId(), writer, context);
		writeCreatedBy(project.getCreatedBy(), writer, context);
		writeCreatedOn(project.getCreatedOn(), writer, context);
		writeUpdatedBy(project.getUpdatedBy(), writer, context);
		writeUpdatedOn(project.getUpdatedOn(), writer, context);
		writeFlags(project.getFlags(), writer, context);
		writeDescription(project.getDescription(), writer, context);
		writeCustomProperties(project, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		logger.info("unmarshal(HierarchicalStreamReader,UnmarshallingContext");
		final UUID id = readId(reader, context);
		final String name = readName(reader, context);
		final UUID projectId = readProjectId(reader, context);
		final String createdBy = readCreatedBy(reader, context);
		final Calendar createdOn = readCreatedOn(reader, context);
		final String updatedBy = readUpdatedBy(reader, context);
		final Calendar updatedOn = readUpdatedOn(reader, context);
		final Collection<ParityObjectFlag> flags = readFlags(reader, context);
		final String description = readDescription(reader, context);
		final Project project = new Project(createdBy, createdOn, description,
				flags, id, name, projectId, updatedBy, updatedOn);
		readCustomProperties(project, reader, context);
		return project;
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
		if(null == projectId) { return null; }
		else { return UUID.fromString(projectId); }
	}

	/**
	 * Write the project id to the xml writer.
	 * 
	 * @param projectId
	 *            The project id. (Optional)
	 * @param writer
	 *            The xStream xml writer.
	 * @param context
	 *            The xStream xml writer context.
	 */
	private void writeProjectId(final UUID projectId,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		if(null != projectId) {
			writer.addAttribute("projectId", projectId.toString());
		}
	}
}
