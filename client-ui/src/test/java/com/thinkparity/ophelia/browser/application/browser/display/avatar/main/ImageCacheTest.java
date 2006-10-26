/**
 * 
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.main;

import com.thinkparity.ophelia.browser.BrowserTestCase;

/**
 * @author raymond
 *
 */
public final class ImageCacheTest extends BrowserTestCase {
    private static final String NAME = "Image Cache Test";
    public ImageCacheTest() {
        super(NAME);
    }
    public void testCellCache() {
        new MainCellImageCache();
    }
    public void testPanelCache() {
        new MainPanelImageCache();
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
