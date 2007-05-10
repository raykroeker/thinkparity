/*
 * Created On: 11-Oct-06 9:07:55 AM
 */
package com.thinkparity.ophelia.model.container.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thoughtworks.xstream.XStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PDFXMLWriter {

    /** An empty resources xml tag. */
    private static final String EMPTY_RESOURCES_XML;

    /** A resource begin xml tag. */
    private static final String RESOURCE_XML_BEGIN;

    /** A resources begin xml tag. */
    private static final String RESOURCES_XML_BEGIN;

    /** A resources end xml tag. */
    private static final String RESOURCES_XML_END;

    /** An xml begin close tag. */
    private static final String TAG_XML_BEGIN_CLOSE;

    /** An xml end tag. */
    private static final char TAG_XML_END;

    static {
        EMPTY_RESOURCES_XML = "<resources/>";
        RESOURCES_XML_BEGIN = "<resources>";
        RESOURCES_XML_END = new StringBuilder(16)
            .append(Separator.SystemNewLine.toString())
            .append("</resources>")
            .append(Separator.SystemNewLine.toString())
            .toString();
        RESOURCE_XML_BEGIN = new StringBuilder(5)
            .append(Separator.SystemNewLine.toString())
            .append("  <")
            .toString();
        TAG_XML_END = '>';
        TAG_XML_BEGIN_CLOSE = "</";
    }

    private Container container;

    private User containerCreatedBy;

    private final Map<ContainerVersion, List<DocumentVersion>> documents;

    private final Map<DocumentVersion, Long> documentsSize;

    private final FileSystem exportFileSystem;

    private final Map<ContainerVersion, List<ArtifactReceipt>> publishedTo;

    private final Map<String, File> resources;

    private Statistics statistics;

    private final List<ContainerVersion> versions;

    private final Map<ContainerVersion, User> versionsPublishedBy;

    /**
     * Create XMLWriter.
     *
     */
    public PDFXMLWriter(final FileSystem exportFileSystem) {
       super();
       this.documents = new HashMap<ContainerVersion, List<DocumentVersion>>();
       this.documentsSize = new HashMap<DocumentVersion, Long>();
       this.publishedTo = new HashMap<ContainerVersion, List<ArtifactReceipt>>();
       this.resources = new HashMap<String, File>();
       this.versions = new ArrayList<ContainerVersion>();
       this.versionsPublishedBy = new HashMap<ContainerVersion, User>();
       this.exportFileSystem = exportFileSystem;
    }

    /**
     * Write a pdf.
     * @param path
     * @param container
     * @param containerCreatedBy
     * @param versions
     * @param versionsPublishedBy
     * @param documents
     * @param documentsSize
     * @param publishedTo
     * @param sharedWith
     * @throws IOException
     * @throws TransformerException
     */
    void write(final String path, final Map<String, File> resources,
            final Container container, final User containerCreatedBy,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, User> versionsPublishedBy,
            final Map<ContainerVersion, List<DocumentVersion>> documents,
            final Map<DocumentVersion, Long> documentsSize,
            final Map<ContainerVersion, List<ArtifactReceipt>> publishedTo)
            throws IOException, TransformerException {
        this.container = container;
        this.containerCreatedBy = containerCreatedBy;
        this.documents.clear();
        this.documents.putAll(documents);
        this.documentsSize.clear();
        this.documentsSize.putAll(documentsSize);
        this.publishedTo.clear();
        this.publishedTo.putAll(publishedTo);
        this.resources.clear();
        this.resources.putAll(resources);
        this.versions.clear();
        this.versions.addAll(versions);
        this.versionsPublishedBy.clear();
        this.versionsPublishedBy.putAll(versionsPublishedBy);
        generateStatistics();

        final XStream xstream = new XStream();
        xstream.alias("container", PDFXMLContainer.class);
        xstream.alias("version", PDFXMLContainerVersion.class);
        xstream.alias("document", PDFXMLDocument.class);
        xstream.alias("user", PDFXMLUser.class);

        final FileWriter fileWriter = newFileWriter(path);
        try {
            streamPDFResourcesXML(fileWriter);
            xstream.toXML(createPDFContainerXML(), fileWriter);
        } finally {
            fileWriter.close();
        }
    }

    private PDFXMLContainer createPDFContainerXML() {
        final PDFXMLContainer pdfContainerXML = new PDFXMLContainer();
        pdfContainerXML.createdBy = containerCreatedBy.getName();
        pdfContainerXML.createdOn = format(container.getCreatedOn());
        pdfContainerXML.documentSum = format(statistics.documentSum);
        pdfContainerXML.name = container.getName();
        pdfContainerXML.userSum = format(statistics.usersSum);
        pdfContainerXML.versions = createPDFXMLVersions();
        pdfContainerXML.versionSum = format(statistics.versionSum);
        return pdfContainerXML;
    }

    private PDFXMLDocument createPDFXMLDocument(final DocumentVersion version) {
        final PDFXMLDocument pdfXML = new PDFXMLDocument();
        pdfXML.name = version.getName();
        pdfXML.size = FileUtil.formatSize(documentsSize.get(version));
        return pdfXML;
    }

    private List<PDFXMLDocument> createPDFXMLDocuments(final ContainerVersion version) {
        final List<PDFXMLDocument> pdfXML = new ArrayList<PDFXMLDocument>(documents.size());
        for (final DocumentVersion documentVersion : documents.get(version)) {
            pdfXML.add(createPDFXMLDocument(documentVersion));
        }
        return pdfXML;
    }

    private PDFXMLUser createPDFXMLUser(final ArtifactReceipt receipt) {
        final PDFXMLUser pdfXML = new PDFXMLUser();
        pdfXML.name = receipt.getUser().getName();
        if (receipt.isSetReceivedOn())
            pdfXML.receivedOn = format(receipt.getReceivedOn());
        else
            pdfXML.receivedOn = "";
        return pdfXML;
    }

    private List<PDFXMLUser> createPDFXMLUsers(final ContainerVersion version) {
        final List<ArtifactReceipt> publishedTo = this.publishedTo.get(version);
        final List<PDFXMLUser> pdfXML = new ArrayList<PDFXMLUser>(publishedTo.size());
        for (final ArtifactReceipt receipt : publishedTo) {
            pdfXML.add(createPDFXMLUser(receipt));
        }
        return pdfXML;
    }

    private PDFXMLContainerVersion createPDFXMLVersion(
            final ContainerVersion version, final Integer versionId) {
        final PDFXMLContainerVersion pdfXML = new PDFXMLContainerVersion();
        pdfXML.documents = createPDFXMLDocuments(version);
        pdfXML.documentSum = format(statistics.documentsPerVersion.get(version));
        pdfXML.publishedBy = versionsPublishedBy.get(version).getName();
        pdfXML.publishedOn = format(version.getUpdatedOn());
        pdfXML.users = createPDFXMLUsers(version);
        pdfXML.userSum = format(statistics.usersPerVersion.get(version));
        pdfXML.versionId = format(versionId);
        return pdfXML;
    }

    private List<PDFXMLContainerVersion> createPDFXMLVersions() {
        final List<PDFXMLContainerVersion> pdfXML = new ArrayList<PDFXMLContainerVersion>(versions.size());
        for (int i = 0; i < versions.size(); i++) {
            pdfXML.add(createPDFXMLVersion(versions.get(i), versions.size() - i));
        }
        return pdfXML;
    }

    private String format(final Calendar calendar) {
        return MessageFormat.format("{0,date,MMMM d, yyyy h:mm a}", calendar.getTime());
    }

    private String format(final Integer integer) {
        return MessageFormat.format("{0}", integer);
    }

    /**
     * Generate container statistics.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     */
    private void generateStatistics() {
        this.statistics = new Statistics();
        this.statistics.documentsPerVersion.clear();
        this.statistics.documentSum = 0;
        this.statistics.usersSum = 0;
        this.statistics.versionSum = versions.size();

        for (final Entry<ContainerVersion, List<DocumentVersion>> entry :
                documents.entrySet()) {
            this.statistics.documentSum += entry.getValue().size();
            this.statistics.documentsPerVersion.put(
                    entry.getKey(), entry.getValue().size());
        }
        for (final ContainerVersion version : publishedTo.keySet()) {
            this.statistics.usersPerVersion.put(version, publishedTo.get(version).size());
            this.statistics.usersSum += publishedTo.get(version).size();
        }
    }

    private FileWriter newFileWriter(final String path) throws IOException {
        if (null == exportFileSystem.find(path)) {
            return new FileWriter(exportFileSystem.createFile(path));
        } else {
            return new FileWriter(exportFileSystem.findFile(path));
        }
    }

    private void streamPDFResourcesXML(final Writer writer) throws IOException {
        boolean first = true;
        boolean didWrite = false;
        for (final Entry<String, File> entry : resources.entrySet()) {
            didWrite = true;

            if (first) {
                writer.write(RESOURCES_XML_BEGIN);
                first = false;
            }
            writer.write(RESOURCE_XML_BEGIN);
            writer.write(entry.getKey());
            writer.write(TAG_XML_END);
            writer.write(entry.getValue().getAbsolutePath());
            writer.write(TAG_XML_BEGIN_CLOSE);
            writer.write(entry.getKey());
            writer.write(TAG_XML_END);
        }
        if (didWrite) {
            writer.write(RESOURCES_XML_END);
        } else {
            writer.write(EMPTY_RESOURCES_XML);
        }
    }

    private class Statistics {
        private final Map<ContainerVersion, Integer> documentsPerVersion = new HashMap<ContainerVersion, Integer>();
        private Integer documentSum;
        private final Map<ContainerVersion, Integer> usersPerVersion = new HashMap<ContainerVersion, Integer>();
        private Integer usersSum;
        private Integer versionSum;
    }
}
