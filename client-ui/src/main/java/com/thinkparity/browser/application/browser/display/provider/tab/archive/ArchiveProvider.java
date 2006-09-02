/*
 * Created On: Sep 1, 2006 8:17:27 AM
 */
package com.thinkparity.browser.application.browser.display.provider.tab.archive;

import com.thinkparity.browser.application.browser.display.provider.FlatContentProvider;

import com.thinkparity.model.parity.model.archive.ArchiveModel;
import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.profile.Profile;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveProvider extends FlatContentProvider {

    /** An artifact archive provider. */
    private final FlatContentProvider artifactProvider;

    /**
     * Create ArchiveProvider.
     * 
     * @param profile
     *            The local user profile.
     * @param archiveModel
     *            A thinkParity archive interface.
     */
    public ArchiveProvider(final Profile profile,
            final ArchiveModel archiveModel) {
        super(profile);
        this.artifactProvider = new FlatContentProvider(profile) {
            @Override
            public Object[] getElements(final Object input) {
                return archiveModel.read().toArray(new Artifact[] {});
            }
        };
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.provider.FlatContentProvider#getElements(java.lang.Object)
     */
    @Override
    public Object[] getElements(Object input) {
        return artifactProvider.getElements(input);
    }
}
