/*
 * Created On: Sep 1, 2006 8:17:27 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.provider.tab.archive;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider;
import com.thinkparity.ophelia.model.archive.ArchiveModel;

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
                return archiveModel.readContainers().toArray(new Container[] {});
            }
        };
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.provider.FlatContentProvider#getElements(java.lang.Object)
     */
    @Override
    public Object[] getElements(Object input) {
        return artifactProvider.getElements(input);
    }
}
