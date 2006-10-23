/*
 * Created On: Sun Oct 22 2006 10:58 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;

import com.thinkparity.codebase.model.session.Environment;

import com.thinkparity.desdemona.DesdemonaTestUser;
import com.thinkparity.desdemona.model.ModelTestCase;

abstract class StreamTestCase extends ModelTestCase {
    protected StreamTestCase(final String name) {
        super(name);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    protected StreamServer createStreamServer(final DesdemonaTestUser testUser, final File workingDirectory) {
        final Environment environment = testUser.getEnvironment();
        return new StreamServer(workingDirectory, environment.getStreamHost(), environment.getStreamPort());
    }
    protected abstract class Fixture {
        protected Fixture() {
            super();
        }
    }
}
