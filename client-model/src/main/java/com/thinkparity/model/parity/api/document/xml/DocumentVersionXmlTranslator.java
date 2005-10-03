/*
 * May 30, 2005
 */
package com.thinkparity.model.parity.api.document.xml;

import com.thinkparity.model.parity.api.ParityXmlTranslator;
import com.thinkparity.model.parity.api.document.Document;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.document.DocumentVersionBuilder;
import com.thinkparity.model.parity.xml.XmlTranslator;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/**
 * DocumentVersionXmlTranslator
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentVersionXmlTranslator extends ParityXmlTranslator implements
		XmlTranslator {

	private DocumentXmlTranslator documentXmlTranslator;

	/**
	 * Create a DocumentVersionXmlTranslator
	 */
	public DocumentVersionXmlTranslator() {
		super(DocumentVersion.class.getSimpleName().toLowerCase(), DocumentVersion.class);
		this.documentXmlTranslator = new DocumentXmlTranslator();
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object, com.thoughtworks.xstream.io.HierarchicalStreamWriter, com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		final DocumentVersion documentVersion = (DocumentVersion) source;
		writeVersion(documentVersion, writer);
		writeDocument(documentVersion, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader, com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		final String version = readVersion(reader);
		final Document document = readDocument(reader, context);
		return DocumentVersionBuilder.newDocumentVersion(document, version);
	}

	private Document readDocument(final HierarchicalStreamReader reader,
			final UnmarshallingContext context) {
		return (Document) documentXmlTranslator.unmarshal(reader, context);
	}

	private String readVersion(final HierarchicalStreamReader reader) {
		return reader.getAttribute("version");
	}

	/**
	 * Write the document to the writer.
	 * @param documentVersion <code>DocumentVersion</code>
	 * @param writer <code>HierarchicalStreamWriter</code>
	 */
	private void writeDocument(final DocumentVersion documentVersion,
			final HierarchicalStreamWriter writer,
			final MarshallingContext context) {
		documentXmlTranslator.marshal(documentVersion.getDocument(), writer,
				context);
	}

	private void writeVersion(final DocumentVersion documentVersion,
			final HierarchicalStreamWriter writer) {
		writer.addAttribute("version", documentVersion.getVersion());
	}

}
