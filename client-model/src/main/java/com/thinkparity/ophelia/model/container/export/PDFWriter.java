/*
 * Created On: 10-Oct-06 5:00:49 PM
 */
package com.thinkparity.ophelia.model.container.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;

/**
 * <b>Title:</b>thinkParity OpheliaModel PDF Writer<br>
 * <b>Description:</b>A pdf writer that serializes the data model to xml then
 * uses an xsl style sheet to generate a pdf.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PDFWriter  {

    /** The export <code>FileSystem</code>. */
    private final FileSystem exportFileSystem;

    /** The export <code>PDFXMLWriter</code>. */
    private final PDFXMLWriter xmlWriter;

    /**
     * Create PDFWriter.
     * 
     * @param impl
     *            A <code>ContainerModelImpl</code>.
     * @param exportFileSystem
     *            An export <code>FileSystem</code>.
     */
    public PDFWriter(final FileSystem exportFileSystem) {
        super();
        this.exportFileSystem = exportFileSystem;
        this.xmlWriter = new PDFXMLWriter(exportFileSystem);
    }

    /**
     * Write a pdf file. Generate an xml serialization of the data, combine it
     * with an xsl sheet via the apache fop library and create a pdf.
     * 
     * @param pdfPath
     *            The pdf file path within the export file system.
     * @param resources
     *            A <code>Map</code> of named resource <code>File</code>s.
     * @param container
     *            A <code>Container</code>.
     * @param createdBy
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
     * @throws TransformerException
     * @throws IOException
     */
    public void write(final String pdfPath, final Map<String, File> resources,
            final Container container, final User createdBy,
            final ContainerVersion latestVersion,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, User> versionsPublishedBy,
            final Map<ContainerVersion, List<DocumentVersion>> documents,
            final Map<DocumentVersion, Long> documentsSize,
            final Map<ContainerVersion, List<ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, Map<DocumentVersion, Delta>> deltas,
            final List<TeamMember> teamMembers)
            throws TransformerException, IOException {
        final String xmlPath = "pdfWriter.xml";
        xmlWriter.write(xmlPath, resources, container, createdBy,
                latestVersion, versions, versionsPublishedBy, documents,
                documentsSize, publishedTo, deltas, teamMembers);

        final Driver driver = new Driver();
        final Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG);
        driver.setLogger(logger);
        driver.setRenderer(Driver.RENDER_PDF);

        final OutputStream outStream = new FileOutputStream(resolvePDFFile(pdfPath));
        final InputStream xslStream = ResourceUtil.getInputStream("xml/xslt/pdfWriter.xsl");
        try {
            driver.setOutputStream(outStream);
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer transformer =
                factory.newTransformer(new StreamSource(xslStream));

            final Result result = new SAXResult(driver.getContentHandler());
            transformer.transform(new StreamSource(
                    exportFileSystem.findFile(xmlPath)), result);
        } finally {
            try {
                xslStream.close();
            } finally {
                try {
                    outStream.close();
                } finally {
                    Assert.assertTrue(
                            exportFileSystem.findFile(xmlPath).delete(),
                            "Could not delete xml file {0}.", xmlPath);
                }
            } 
        }
    }

    /**
     * Resolve the pdf export file. The file will be created if it does not
     * exist.
     * 
     * @param path
     *            The pdf file path within the export file system.
     * @return The pdf <code>File</code>.
     * @throws IOException
     */
    private File resolvePDFFile(final String path) throws IOException {
        if (null == exportFileSystem.find(path)) {
            return exportFileSystem.createFile(path);
        } else {
            return exportFileSystem.findFile(path);
        }
    }
}
