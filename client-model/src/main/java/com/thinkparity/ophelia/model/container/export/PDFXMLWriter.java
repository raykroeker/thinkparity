/*
 * Created On: 11-Oct-06 9:07:55 AM
 */
package com.thinkparity.ophelia.model.container.export;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
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

    /** A localization bundle. */
    private static final ResourceBundle BUNDLE;

    static {
        BUNDLE = ResourceBundle.getBundle("localization/AuditReport_Messages");
    }

    /** A <code>Container</code>. */
    private Container container;

    /**
     * A <code>Map</code> of all of the
     * <code>ContainerVersion</code>s to their <code>Map</code>
     * of <code>DocumentVersion</code> to <code>Delta</code>s.
     */
    private final Map<ContainerVersion, Map<DocumentVersion, Delta>> deltas;

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

    /** The first version <code>ContainerVersion</code>. */
    private ContainerVersion firstVersion;

    /** A <code>FuzzyDateFormat</code>. */
    private static final FuzzyDateFormat FUZZY_DATE_FORMAT;

    /** The latest version <code>ContainerVersion</code>. */
    private ContainerVersion latestVersion;

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

    /** A <code>List</code> of <code>TeamMember</code>s. */
    private final List<TeamMember> teamMembers;

    /** A <code>List</code> of <code>ContainerVersion</code>s to export. */
    private final List<ContainerVersion> versions;

    /**
     * A <code>Map</code> of all of the <code>ContainerVersion</code>s to
     * their respective published by <code>User</code>.
     */
    private final Map<ContainerVersion, User> versionsPublishedBy;

    static {
        FUZZY_DATE_FORMAT = new FuzzyDateFormat();
    }

    /**
     * Create XMLWriter.
     * 
     * @param exportFileSystem
     *            An export <code>FileSystem</code>.
     */
    public PDFXMLWriter(final FileSystem exportFileSystem) {
       super();
       this.deltas = new HashMap<ContainerVersion, Map<DocumentVersion, Delta>>();
       this.documents = new HashMap<ContainerVersion, List<DocumentVersion>>();
       this.documentsSize = new HashMap<DocumentVersion, Long>();
       this.publishedTo = new HashMap<ContainerVersion, List<ArtifactReceipt>>();
       this.resources = new HashMap<String, File>();
       this.teamMembers = new ArrayList<TeamMember>();
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
     * @param containerCreatedBy
     *            A created by <code>User</code>.
     * @param latestVersion
     *            A latest version <code>ContainerVersion</code>.   
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
     * @param deltas
     *            A <code>Map</code> of all of the
     *            <code>ContainerVersion</code>s to their <code>Map</code>
     *            of <code>DocumentVersion</code> to <code>Delta</code>s.
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>s.
     * @throws IOException
     * @throws TransformerException
     */
    void write(final String path, final Map<String, File> resources,
            final Container container, final User containerCreatedBy,
            final ContainerVersion latestVersion,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, User> versionsPublishedBy,
            final Map<ContainerVersion, List<DocumentVersion>> documents,
            final Map<DocumentVersion, Long> documentsSize,
            final Map<ContainerVersion, List<ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> deltas,
            final List<TeamMember> teamMembers)
            throws IOException, TransformerException {
        Assert.assertTrue(versions.size()>0, "Unable to export container with zero versions.");
        this.container = container;
        this.firstVersion = versions.get(versions.size()-1);
        this.latestVersion = latestVersion;
        this.deltas.clear();
        this.deltas.putAll(deltas);
        this.documents.clear();
        this.documents.putAll(documents);
        this.documentsSize.clear();
        this.documentsSize.putAll(documentsSize);
        this.publishedTo.clear();
        this.publishedTo.putAll(publishedTo);
        this.resources.clear();
        this.resources.putAll(resources);
        this.teamMembers.clear();
        this.teamMembers.addAll(teamMembers);
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
        xstream.alias("versionSummary", PDFXMLContainerVersionSummary.class);
        xstream.alias("document", PDFXMLDocument.class);
        xstream.alias("documentSummary", PDFXMLDocumentSummary.class);
        xstream.alias("user", PDFXMLUser.class);
        xstream.alias("teamMember", PDFXMLTeamMember.class);
        xstream.alias("localization", PDFXMLLocalization.class);

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
        pdfContainerXML.localization = createPDFXMLLocalization();
        pdfContainerXML.documentSummaries = createPDFXMLDocumentSummaries(latestVersion);
        pdfContainerXML.documentSum = format(documents.get(latestVersion).size());
        pdfContainerXML.firstPublished = format(firstVersion.getCreatedOn());
        pdfContainerXML.lastPublished = format(latestVersion.getCreatedOn());
        pdfContainerXML.name = container.getName();
        pdfContainerXML.teamMembers = createPDFXMLTeamMembers(teamMembers);
        pdfContainerXML.teamMemberSum = format(teamMembers.size());
        pdfContainerXML.userSum = format(statistics.usersSum);
        pdfContainerXML.versions = createPDFXMLVersions();
        pdfContainerXML.versionSummaries = createPDFXMLVersionSummaries();
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
            resource = new PDFXMLResource();
            resource.name = entry.getKey();
            final StringBuffer file = new StringBuffer("file:").append(entry.getValue().getAbsolutePath());
            resource.path = file.toString();
            resources.add(resource);
        }
        return resources;
    }

    /**
     * Create the document xml.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @param versionDeltas
     *            A <code>Map</code>of <code>DocumentVersion</code> to <code>Delta</code>.   
     * @return An instance of <code>PDFXMLDocument</code>.
     */
    private PDFXMLDocument createPDFXMLDocument(final DocumentVersion version,
            final Map<DocumentVersion, Delta> versionDeltas) {
        final PDFXMLDocument pdfXML = new PDFXMLDocument();
        pdfXML.name = version.getArtifactName();
        final Delta delta = versionDeltas.get(version);
        if (Delta.NONE == delta) {
            pdfXML.delta = "";
        } else {
            final StringBuffer buffer = new StringBuffer(" ").append(getString(delta.toString()));
            pdfXML.delta = buffer.toString();
        }
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
        final Map<DocumentVersion, Delta> versionDeltas = deltas.get(version);
        for (final DocumentVersion documentVersion : documents.get(version)) {
            pdfXML.add(createPDFXMLDocument(documentVersion, versionDeltas));
        }
        return pdfXML;
    }

    /**
     * Create the document summary xml.
     * 
     * @param version
     *            A <code>DocumentVersion</code>.
     * @return An instance of <code>PDFXMLDocument</code>.
     */
    private PDFXMLDocumentSummary createPDFXMLDocumentSummary(final DocumentVersion version) {
        final PDFXMLDocumentSummary pdfXML = new PDFXMLDocumentSummary();
        pdfXML.name = version.getArtifactName();
        return pdfXML;
    }

    /**
     * Create the document summary list xml for a container.
     * 
     * @param latestVersion
     *            A latest version <code>ContainerVersion</code>.
     * @return A <code>List</code> of <code>PDFXMLDocumentSummary</code>.
     */
    private List<PDFXMLDocumentSummary> createPDFXMLDocumentSummaries(final ContainerVersion latestVersion) {
        final List<PDFXMLDocumentSummary> pdfXML = new ArrayList<PDFXMLDocumentSummary>(documents.size());
        for (final DocumentVersion documentVersion : documents.get(latestVersion)) {
            pdfXML.add(createPDFXMLDocumentSummary(documentVersion));
        }
        return pdfXML;
    }

    /**
     * Create the localization xml.
     */
    private PDFXMLLocalization createPDFXMLLocalization() {
        final PDFXMLLocalization pdfXML = new PDFXMLLocalization();
        pdfXML.firstPublished = getString("FirstPublished");
        pdfXML.lastPublished = getString("LastPublished");
        pdfXML.teamMembers = getString("TeamMembers");
        pdfXML.documents = getString("Documents");
        pdfXML.versions = getString("Versions");
        pdfXML.versionPublishedOn = getString("VersionPublishedOn");
        pdfXML.versionPublishedBy = getString("VersionPublishedBy");
        pdfXML.versionPublishedTo = getString("VersionPublishedTo");
        pdfXML.versionDocuments = getString("VersionDocuments");
        pdfXML.versionNote = getString("VersionNote");
        return pdfXML;
    }

    /**
     * Create the team member xml.
     * 
     * @param teamMember
     *            A <code>TeamMember</code>.
     * @return A <code>PDFXMLTeamMember</code>.
     */
    private PDFXMLTeamMember createPDFXMLTeamMember(final TeamMember teamMember) {
        final PDFXMLTeamMember pdfXML = new PDFXMLTeamMember();
        pdfXML.name = teamMember.getName();
        pdfXML.organization = teamMember.getOrganization();
        pdfXML.title = teamMember.getTitle();
        return pdfXML;
    }

    /**
     * Create the team member xml.
     * 
     * @param teamMembers
     *            A <code>List</code> of <code>TeamMember</code>.
     * @return A <code>List</code> of <code>PDFXMLTeamMember</code>.
     */
    private List<PDFXMLTeamMember> createPDFXMLTeamMembers(final List<TeamMember> teamMembers) {
        final List<PDFXMLTeamMember> pdfXML = new ArrayList<PDFXMLTeamMember>(teamMembers.size());
        for (final TeamMember teamMember : teamMembers) {
            pdfXML.add(createPDFXMLTeamMember(teamMember));
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
            final StringBuffer buffer = new StringBuffer(" ");
            buffer.append(getString("UserReceived", new Object[] {format(receipt.getReceivedOn())}));
            pdfXML.receivedOn = buffer.toString();
        } else {
            final StringBuffer buffer = new StringBuffer(" ").append(getString("UserDidNotReceive"));
            pdfXML.receivedOn = buffer.toString();
        }
        return pdfXML;
    }

    /**
     * Create the user xml.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
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
        pdfXML.name = getVersionName(version);
        pdfXML.note = getVersionComment(version);
        pdfXML.publishedBy = versionsPublishedBy.get(version).getName();
        pdfXML.publishedOn = format(version.getCreatedOn());
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
     * Create the container version summary xml.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param versionId
     *            The container version id <code>Integer</code> relative to
     *            the user.
     * @return An instance of <code>PDFXMLContainerVersionSummary</code>.
     */
    private PDFXMLContainerVersionSummary createPDFXMLVersionSummary(
            final ContainerVersion version, final Integer versionId) {
        final PDFXMLContainerVersionSummary pdfXML = new PDFXMLContainerVersionSummary();
        pdfXML.name = getVersionName(version);
        pdfXML.publishedBy = versionsPublishedBy.get(version).getName();
        pdfXML.publishedOn = format(version.getCreatedOn());
        pdfXML.versionId = format(versionId);
        return pdfXML;
    }

    /**
     * Create the container version summary xml.
     * 
     * @return A <code>List</code> of <code>PDFXMLContainerVersionSummary</code>.
     */
    private List<PDFXMLContainerVersionSummary> createPDFXMLVersionSummaries() {
        final List<PDFXMLContainerVersionSummary> pdfXML = new ArrayList<PDFXMLContainerVersionSummary>(versions.size());
        for (int i = 0; i < versions.size(); i++) {
            pdfXML.add(createPDFXMLVersionSummary(versions.get(i), versions.size() - i));
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
        return FUZZY_DATE_FORMAT.format(calendar, "Long");
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
     * Get a localized string.
     * 
     * @param key
     *            A <code>String</code> key.
     * @return A localized <code>String</code>.
     */
    private String getString(final String key) {
        try {
            return BUNDLE.getString(key);
        } catch (final MissingResourceException mrx) {
            return "!" + key + "!";
        }
    }

    /**
     * Get a localized string.
     * 
     * @param key
     *            A <code>String</code> key.
     * @param arguments
     *            The <code>Object[]</code> arguments.
     * @return A localized <code>String</code>.
     */
    private String getString(final String key, final Object[] arguments) {
        final String pattern = getString(key);
        try {
            return MessageFormat.format(pattern, arguments);
        } catch (final IllegalArgumentException iax) {
            return "!!" + key + "!!";
        }
    }

    /**
     * Get the version comment.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A comment <code>String</code>.
     */
    private String getVersionComment(final ContainerVersion version) {
        String comment = version.getComment();
        if (null == comment) {
            comment = "-";
        }
        return comment;
    }

    /**
     * Get the version name.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A version name <code>String</code>.
     */
    private String getVersionName(final ContainerVersion version) {
        if (version.isSetName()) {
            return version.getName();
        } else {
            return MessageFormat.format("{0}", format(version.getCreatedOn()));
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
