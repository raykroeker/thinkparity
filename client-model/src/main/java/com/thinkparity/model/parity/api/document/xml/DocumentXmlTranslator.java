/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.api.document.xml;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.CompressionUtil;
import com.thinkparity.codebase.CompressionUtil.Level;


import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityXmlTranslator;
import com.thinkparity.model.parity.api.document.Document;
import com.thinkparity.model.parity.util.Base64;
import com.thinkparity.model.parity.util.LoggerFactory;
import com.thinkparity.model.parity.xml.XmlTranslator;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import org.apache.log4j.Logger;

/**
 * DocumentXmlTranslator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentXmlTranslator extends ParityXmlTranslator implements
		XmlTranslator {

	/**
	 * Compression level to use when writing the content to the version file.
	 */
	private static final Level compressionLevel = Level.Nine;

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		LoggerFactory.createInstance(DocumentXmlTranslator.class);

	/**
	 * Create a DocumentXmlTranslator
	 */
	DocumentXmlTranslator() { super("document", Document.class); }

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		final Document document = (Document) source;
		writeName(document.getName(), writer);
		writeCreatedBy(document.getCreatedBy(), writer);
		writeKeyHolder(document, writer);
		writeId(document.getId(), writer);
		writeCreatedOn(document.getCreatedOn(), writer);
		writeDescription(document.getDescription(), writer);
		writeDirectory(document.getDirectory(), writer);
		try { writeContent(document, writer); }
		catch(IOException iox) {
			fatal((ParityObject) source, "Could not serialize file content.", iox);
		}
		writeNotes(document, writer);
		writeVersions(document, writer);
		writeCustomProperties(document, writer);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		Document document = null;
		try {
			final String name = readName(reader);
			final String createdBy = readCreatedBy(reader);
			final String keyHolder = readKeyHolder(reader);
			final UUID id = readId(reader);
			final Calendar createdOn = readCreatedOn(reader);
			final String description = readDescription(reader);
			final File directory = readDirectory(reader);
			final Content content = readContent(reader);
			document = new Document(name, createdOn, createdBy, keyHolder,
					description, directory, id, content.content, content.contentChecksum);
			readNotes(document, reader);
			readVersions(document, reader);
			readCustomProperties(document, reader);
		}
		catch(Exception x) { fatal(document, "An unknown error occured parsing document xml.", x); }
		return document;
	}

	private Content readContent(final HierarchicalStreamReader reader)
			throws IOException {
		reader.moveDown();										// <content>
		final String encoding = reader.getAttribute("encoding");
		final String compressionLevel = reader.getAttribute("compressionlevel");
		final String byteContentChecksum = reader.getAttribute("checksum");
		final String encodedCompressedContent =
			reader.getValue();
		final byte[] decodedContent = Base64.decodeBytes(encodedCompressedContent);
		final byte[] byteContent = CompressionUtil.decompress(decodedContent);
		reader.moveUp();										// </content>
		final Content content = new Content();
		content.content = byteContent;
		content.contentChecksum = byteContentChecksum;
		return content;
	}

	private void writeContent(final Document document,
			final HierarchicalStreamWriter writer) throws IOException {
		writer.startNode("content");								// <content>
		writer.addAttribute("encoding", Base64.getEncoder());
		writer.addAttribute("compressionlevel", compressionLevel.toString());
		writer.addAttribute("checksum", document.getContentChecksum());
		final byte[] content = document.getContent();
		final byte[] compressedContent =
			CompressionUtil.compress(content, compressionLevel);
		final String encodedContent = Base64.encodeBytes(compressedContent);
		writer.setValue(encodedContent);
		writer.endNode();											// </content>
	}

	private class Content {
		private byte[] content;
		private String contentChecksum;
	}
}
