/*
 * Created On: 10-Oct-06 9:43:35 AM
 */
package com.thinkparity.ophelia.model.artifact;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ArtifactNameGenerator {

    protected static final String ARTIFACT_FILE_PATTERN;

    protected static final String ARTIFACT_VERSION_FILE_PATTERN;

    protected static final String ARTIFACT_VERSION_DIRECTORY_PATTERN;

    protected static final String ARTIFACT_DIRECTORY_PATTERN;

    static {
        ARTIFACT_FILE_PATTERN = "{0}";
        ARTIFACT_DIRECTORY_PATTERN = "";
        ARTIFACT_VERSION_FILE_PATTERN = "";
        ARTIFACT_VERSION_DIRECTORY_PATTERN = "";
    }

    /**
     * Create ArtifactNameGenerator.
     *
     */
    protected ArtifactNameGenerator() {
        super();
    }
}
