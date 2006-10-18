/*
 * Created On: Wed Oct 18, 2006 09:22
 */
package com.thinkparity.ophelia.browser.mode.demo;

import com.thinkparity.codebase.FileSystem;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
interface DemoProvider {
    public Demo readDemo(final FileSystem fileSystem);
    public Demo readDemo();
}
