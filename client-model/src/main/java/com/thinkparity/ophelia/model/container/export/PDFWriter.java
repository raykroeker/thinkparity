/*
 * Created On: 10-Oct-06 5:00:49 PM
 */
package com.thinkparity.ophelia.model.container.export;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Driver;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

/**
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
     * Write a pdf.
     * 
     * @param pdfPath
     * @param container
     * @param containerCreatedBy
     * @param versions
     * @param versionsPublishedBy
     * @param documents
     * @param documentsSize
     * @param publishedTo
     * @param sharedWith
     * @throws TransformerException
     * @throws IOException
     */
    public void write(
            final String pdfPath,
            final Container container,
            final User containerCreatedBy,
            final List<ContainerVersion> versions,
            final Map<ContainerVersion, User> versionsPublishedBy,
            final Map<ContainerVersion, List<DocumentVersion>> documents,
            final Map<DocumentVersion, Integer> documentsSize,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> publishedTo,
            final Map<ContainerVersion, Map<User, ArtifactReceipt>> sharedWith)
            throws TransformerException, IOException {
        final String xmlPath = "pdfWriter.xml";
        xmlWriter.write(xmlPath, container, containerCreatedBy, versions,
                versionsPublishedBy, documents, documentsSize, publishedTo,
                sharedWith);

        final Driver driver = new Driver();
        final Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_DEBUG);
        driver.setLogger(logger);
        driver.setRenderer(Driver.RENDER_PDF);

        final OutputStream outStream = new FileOutputStream(newFile(pdfPath));
        try {
            driver.setOutputStream(outStream);
            final TransformerFactory factory = TransformerFactory.newInstance();
            final Transformer transformer = factory.newTransformer(
                    new StreamSource(ResourceUtil.getFile("xml/xslt/pdfWriter.xslt")));

            final Result result = new SAXResult(driver.getContentHandler());
            transformer.transform(new StreamSource(
                    exportFileSystem.findFile(xmlPath)), result);
        } finally {
            outStream.close();
            Assert.assertTrue(exportFileSystem.findFile(xmlPath).delete(),
                    "Could not delete xml file {0}.", xmlPath);
        }
    }

    private File newFile(final String path) throws IOException {
        if (null == exportFileSystem.find(path)) {
            return exportFileSystem.createFile(path);
        } else {
            return exportFileSystem.findFile(path);
        }
    }
}
