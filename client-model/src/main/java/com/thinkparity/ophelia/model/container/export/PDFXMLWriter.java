/*
 * Created On: 11-Oct-06 9:07:55 AM
 */
package com.thinkparity.ophelia.model.container.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.FileSystem;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thoughtworks.xstream.XStream;

/**
 * <b>Title:</b>thinkParity OpheliaModel PDF XML Writer<br>
 * <b>Description:</b>An xml writer used to serialize the data to xml before
 * creating the export pdf.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
final class PDFXMLWriter {

    /** An instance of <code>BytesFormat</code>. */
    private final BytesFormat bytesFormat;

    /** A <code>Container</code>. */
    private Container container;

    /** A created by <code>User</code>. */
    private User createdBy;

    /**
     * A <code>Map</code> of all of the <code>ContainerVersion</code>s to
     * their <code>List</code> of respective <code>DocumentVersion</code>s.
     */
    private final Map<ContainerVersion, List<DocumentVersion>> documents;

    /**
     * A <code>Map</code> of all of the <code>DocumentVersion</code>s to
     * their respective sizes <code>Long</code>.
     */
    private final Map<DocumentVersion, Long> documentsSize;

    /** The export <code>FileSystem</code>. */
    private final FileSystem exportFileSystem;

    /**
     * A <code>Map</code> of all of the <code>ContainerVersion</code>s to
     * their <code>List</code> of respective published to
     * <code>ArtifactReceipt</code>s.
     */
    private final Map<ContainerVersion, List<ArtifactReceipt>> publishedTo;

    /** A <code>Map</code> of named resource <code>File</code>s. */
    private final Map<String, File> resources;

    /** A collection of <code>Statistics</code>. */
    private Statistics statistics;

    /** A <code>List</code> of <code>ContainerVersion</code>s to export. */
    private final List<ContainerVersion> versions;

    /**
     * A <code>Map</code> of all of the <code>ContainerVersion</code>s to
     * their respective published by <code>User</code>.
     */
    private final Map<ContainerVersion, User> versionsPublishedBy;

    /**
     * Create XMLWriter.
     * 
     * @param exportFileSystem
     *            An export <code>FileSystem</code>.
     */
    public PDFXMLWriter(final FileSystem exportFileSystem) {
       super();
       this.bytesFormat = new BytesFormat();
       this.documents = new HashMap<ContainerVersion, List<DocumentVersion>>();
       this.documentsSize = new HashMap<DocumentVersion, Long>();
       this.publishedTo = new HashMap<ContainerVersion, List<ArtifactReceipt>>();
       this.resources = new HashMap<String, File>();
       this.versions = new ArrayList<ContainerVersion>();
       this.versionsPublishedBy = new HashMap<ContainerVersion, User>();
       this.exportFileSystem = exportFileSystem;
    }

    /**
     * Write the pdf xml. Build an in-memory map of all of the model objects to
     * temporary xml serialization objects then use xstream xml serialization to
     * store the data.
     * 
     * @param path
     *            The xml file path <code>String</code> within the export file
     *            system.
     * @param resources
     *            A <code>Map</code> of named resource <code>File</code>s.
     * @param container
     *            A <code>Container</code>.
     * @param createdBy
     *            A created by <code>User</code>.
     * @param versions
     *            A <code>List</code> of <code>ContainerVersion</code>s to
     *            export.
     * @param versionsPublishedBy
     *            A <code>Map</code> of all of the
     *            <code>ContainerVersion</code>s to their respective
     *            published by <code>User</code>.
     * @param documents
     *            A <code>Map</code> of all of the
     *            <code>ContainerVersion</code>s to their <code>List</code>
     *            of respective <code>DocumentVersion</code>s.
     * @param documentsSize
     *            A <code>Map</code> of all of the
     *            <code>DocumentVersion</code>s to their respective sizes
     *            <code>Long</code>.
     * @param publishedTo
     *            A <code>Map</code> of all of the
     *            <code>ContainerVersion</code>s to their <code>List</code>
     *            of respective published to <code>ArtifactReceipt</code>s.
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
        this.createdBy = containerCreatedBy;
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
        xstream.alias("export", PDFXMLExport.class);
        xstream.alias("resource", PDFXMLResource.class);
        xstream.alias("container", PDFXMLContainer.class);
        xstream.alias("version", PDFXMLContainerVersion.class);
        xstream.alias("document", PDFXMLDocument.class);
        xstream.alias("user", PDFXMLUser.class);

        final FileWriter fileWriter = newFileWriter(path);
        try {
            xstream.toXML(createPDFExportXML(xstream), fileWriter);
        } finally {
            fileWriter.close();
        }
    }

    /**
     * Create the container xml.
     * 
     * @return An instance of <code>PDFXMLContainer</code>.
     */
    private PDFXMLContainer createPDFContainerXML() {
        final PDFXMLContainer pdfContainerXML = new PDFXMLContainer();
        pdfContainerXML.createdBy = createdBy.getName();
        pdfContainerXML.createdOn = format(container.getCreatedOn());
        pdfContainerXML.documentSum = format(statistics.documentSum);
        pdfContainerXML.name = container.getName();
        pdfContainerXML.userSum = format(statistics.usersSum);
        pdfContainerXML.versions = createPDFXMLVersions();
        pdfContainerXML.versionSum = format(statistics.versionSum);
        return pdfContainerXML;
    }

    /**
     * Create the export xml.
     * 
     * @param xstream
     *            An <code>XStream</code> reference.
     * @return An instance of <code>PDFXMLExport</code>.
     */
    private PDFXMLExport createPDFExportXML(final XStream xstream) {
        final PDFXMLExport pdfExportXML = new PDFXMLExport();
        pdfExportXML.container = createPDFContainerXML();
        pdfExportXML.resources = createPDFResourcesXML(xstream);
        return pdfExportXML;
    }

    /**
     * Create the resources xml. Each resource string will become an alias for
     * the resource path field.
     * 
     * @param xstream
     *            An <code>XStream</code> reference.
     * @return An <code>List</code> instance of <code>PDFXMLResource</code>.
     */
    private List<PDFXMLResource> createPDFResourcesXML(final XStream xstream) {
        final List<PDFXMLResource> resources = new ArrayList<PDFXMLResource>();
        PDFXMLResource resource;
        for (final Entry<String, File> entry : this.resources.entrySet()) {
            xstream.aliasField(entry.getKey(), PDFXMLResource.class, "path");
            resource = new PDFXMLResource();
            resource.path = entry.getValue().getAbsolutePath();
            resources.add(resource);
        }
        return resources;
    }

    /**
     * Create the document xml.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An instance of <code>PDFXMLDocument</code>.
     */
    private PDFXMLDocument createPDFXMLDocument(final DocumentVersion version) {
        final PDFXMLDocument pdfXML = new PDFXMLDocument();
        pdfXML.name = version.getArtifactName();
        pdfXML.size = bytesFormat.format(documentsSize.get(version));
        return pdfXML;
    }

    /**
     * Create the document list xml for a container version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A <code>List</code> of <code>PDFXMLDocument</code>.
     */
    private List<PDFXMLDocument> createPDFXMLDocuments(final ContainerVersion version) {
        final List<PDFXMLDocument> pdfXML = new ArrayList<PDFXMLDocument>(documents.size());
        for (final DocumentVersion documentVersion : documents.get(version)) {
            pdfXML.add(createPDFXMLDocument(documentVersion));
        }
        return pdfXML;
    }

    /**
     * Create the user xml.
     * 
     * @param receipt
     *            An <code>ArtifactReceipt</code>.
     * @return An instance of <code>PDFXMLUser</code>.
     */
    private PDFXMLUser createPDFXMLUser(final ArtifactReceipt receipt) {
        final PDFXMLUser pdfXML = new PDFXMLUser();
        pdfXML.name = receipt.getUser().getName();
        if (receipt.isSetReceivedOn()) {
            pdfXML.receivedOn = format(receipt.getReceivedOn());
        } else {
            pdfXML.receivedOn = "";
        }
        return pdfXML;
    }

    /**
     * Create the user xml.
     * 
     * @param version
     *            A <code>ContaienrVersion</code>.
     * @return A <code>List</code> of <code>PDFXMLUser</code>.
     */
    private List<PDFXMLUser> createPDFXMLUsers(final ContainerVersion version) {
        final List<ArtifactReceipt> publishedTo = this.publishedTo.get(version);
        final List<PDFXMLUser> pdfXML = new ArrayList<PDFXMLUser>(publishedTo.size());
        for (final ArtifactReceipt receipt : publishedTo) {
            pdfXML.add(createPDFXMLUser(receipt));
        }
        return pdfXML;
    }

    /**
     * Create the container version xml.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param versionId
     *            The container version id <code>Integer</code> relative to
     *            the user.
     * @return An instance of <code>PDFXMLContainerVersion</code>.
     */
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

    /**
     * Create the container version xml.
     * 
     * @return A <code>List</code> of <code>PDFXMLContainerVersion</code>.
     */
    private List<PDFXMLContainerVersion> createPDFXMLVersions() {
        final List<PDFXMLContainerVersion> pdfXML = new ArrayList<PDFXMLContainerVersion>(versions.size());
        for (int i = 0; i < versions.size(); i++) {
            pdfXML.add(createPDFXMLVersion(versions.get(i), versions.size() - i));
        }
        return pdfXML;
    }

    /**
     * Format a calendar as a string.
     * 
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A formatted <code>String</code>.
     */
    private String format(final Calendar calendar) {
        return MessageFormat.format("{0,date,MMMM d, yyyy h:mm a}",
                calendar.getTime());
    }

    /**
     * Format an integer as a number string.
     * 
     * @param integer
     *            An <code>Integer</code>.
     * @return A formatted <code>String</code>.
     */
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

    /**
     * Create an xml file writer at the given path.
     * 
     * @param path
     *            A path <code>String</code> within the export file system.
     * @return A <code>FileWriter</code>.
     * @throws IOException
     */
    private FileWriter newFileWriter(final String path) throws IOException {
        if (null == exportFileSystem.find(path)) {
            return new FileWriter(exportFileSystem.createFile(path));
        } else {
            return new FileWriter(exportFileSystem.findFile(path));
        }
    }

    /**
     * <b>Title:</b>Output Statistics<br>
     * <b>Description:</b>A series of output statistics.<br>
     */
    private class Statistics {
        private final Map<ContainerVersion, Integer> documentsPerVersion = new HashMap<ContainerVersion, Integer>();
        private Integer documentSum;
        private final Map<ContainerVersion, Integer> usersPerVersion = new HashMap<ContainerVersion, Integer>();
        private Integer usersSum;
        private Integer versionSum;
    }
}
