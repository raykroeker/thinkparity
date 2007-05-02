/*
 * Created On: 10-Oct-06 2:22:27 PM
 */
package com.thinkparity.ophelia.model.container;

import java.text.MessageFormat;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Name Generator<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public final class ContainerNameGenerator {

    /**
     * Create ContainerNameGenerator.
     *
     */
    ContainerNameGenerator() {
        super();
    }

    /**
     * Generate a directory name for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A directory name <code>String</code>.
     */
    public String directoryName(final Container container) {
        return MessageFormat.format("{0}", container.getName());
    }

    /**
     * Generate a directory name for a container version.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A directory name <code>String</code>.
     */
    public String directoryName(final ContainerVersion version) {
        return MessageFormat.format("{0,date,MMM dd, yyyy h mm ss a}",
                version.getUpdatedOn().getTime());
    }

    /**
     * Generate an export directory name.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return An export directory name <code>String</code>.
     */
    public String exportDirectoryName(final Container container) {
        return MessageFormat.format("{0}", container.getId());
    }

    /**
     * Generate a directory name for export.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @return A directory name <code>String</code>.
     */
    public String exportDirectoryName(final ContainerVersion version) {
        return directoryName(version);
    }

    /**
     * Generate a pdf name for a container.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return A pdf file name <code>String</code>.
     */
    public String pdfFileName(final Container container) {
        return MessageFormat.format("{0}.pdf", container.getName()); 
    }

    /**
     * Generate an xml file name.
     * 
     * @param container
     *            A <code>Container</code>.
     * @return An xml file name <code>String</code>.
     */
    public String xmlFileName(final Container container) {
        return MessageFormat.format("{0}.xml", container.getName());
    }
}
