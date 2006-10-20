/**
 * 
 */
package com.thinkparity.ophelia.model.container;

import java.util.List;

import com.thinkparity.codebase.filter.Filter;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * @author raymond
 *
 */
public class BookmarkTest extends ContainerTestCase {

    private static final String NAME = "Bookmark Test";

    private Fixture datum;

    /**
     * @param name
     */
    public BookmarkTest() {
        super(NAME);
    }

    public void testBookmark() {
        datum.containerModel.addBookmark(datum.container.getId());
        List<Container> containers = datum.containerModel.read(datum.withBookmarkFilter);
        assertContains(containers, datum.container);
        
        containers = datum.containerModel.read(datum.withoutBookmarkFilter);
        assertDoesNotContain(containers, datum.container);

        datum.containerModel.removeBookmark(datum.container.getId());
        containers = datum.containerModel.read(datum.withBookmarkFilter);
        assertDoesNotContain(containers, datum.container);

        containers = datum.containerModel.read(datum.withoutBookmarkFilter);
        assertContains(containers, datum.container);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final ContainerModel containerModel = getContainerModel(OpheliaTestUser.JUNIT);
        final Container container = createContainer(OpheliaTestUser.JUNIT, NAME);
        datum = new Fixture(container, containerModel);
    }

    /**
     * @see com.thinkparity.ophelia.model.container.ContainerTestCase#tearDown()
     * 
     */
    @Override
    protected void tearDown() throws Exception {
        datum = null;
        super.tearDown();
    }

    private class Fixture extends ContainerTestCase.Fixture {
        private final Container container;
        private final ContainerModel containerModel;
        private final Filter<? super Artifact> withBookmarkFilter;
        private final Filter<? super Artifact> withoutBookmarkFilter;
        private Fixture(final Container container,
                final ContainerModel containerModel) {
            this.container = container;
            this.containerModel = containerModel;
            this.withBookmarkFilter = new Filter<Artifact>() {
                public Boolean doFilter(final Artifact o) {
                    return !o.isBookmarked();
                }
            };
            this.withoutBookmarkFilter = new Filter<Artifact>() {
                public Boolean doFilter(final Artifact o) {
                    return o.isBookmarked();
                }
            };
        }
    }
}
