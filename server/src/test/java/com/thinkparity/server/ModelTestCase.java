/*
 * Created On: Tue Jun 06 2006 10:31 PDT
 * $Id$
 */
package com.thinkparity.server;

import java.util.UUID;

import com.raykroeker.junitx.TestCase;

import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.artifact.Artifact;
import com.thinkparity.server.model.artifact.ArtifactModel;
import com.thinkparity.server.model.session.Session;

/**
 * An abstraction of the parity remote model test case.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public abstract class ModelTestCase extends TestCase {

    /**
     * Create ModelTestCase.
     *
     * @param name
     *      The test name.
     */
    protected ModelTestCase(final String name) { super(name); }

    /**
     * Create an artifact.
     *
     * @return The new artifact.
     */
    protected Artifact createArtifact() throws ParityServerModelException {
        final UUID artifactUniqueId = UUID.randomUUID();
        return getArtifactModel().create(artifactUniqueId);
    }

    /**
     * Obtain the parity artifact interface.
     *
     * @return The parity artifact interface.
     */
    protected ArtifactModel getArtifactModel() {
        //return ArtifactModel.getModel();
        return null;
    }

    /** @see com.raykroeker.junitx.TestCase#setUp() */
    protected void setUp() throws Exception { super.setUp(); }

    /** @see com.raykroeker.junitx.TestCase#tearDown() */
    protected void tearDown() throws Exception { super.tearDown(); }
/*
    private Session createSession() {
        return  new Session() {
            final JID jid = JUnitUser.JUnit.;
            final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(jid.toString());
            public JabberId getJabberId() { return jabberId; }
            public JID getJID() { return jid; }
        };
    }

    protected class JUnitUser {
        protected static JUnitUser JUnit = new JUnitUser(
				"parity", "parity", "rkutil.raykroeker.com", 5222, "junit");

        protected static JUnitUser JUnitX = new JUnitUser(
				"parity", "parity", "rkutil.raykroeker.com", 5222, "junit.x");

        protected static JUnitUser JUnitY = new JUnitUser(
                "parity", "parity", "rkutil.raykroeker.com", 5222, "junit.y");

        protected static JUnitUser JUnitZ = new JUnitUser(
                "parity", "parity", "rkutil.raykroeker.com", 5222, "junit.z");

        private final String password;
        private final String resource;
        private final String serverHost;
        private final Integer serverPort;
        private final String username;
        
        private JUnitUser(final String password, final String resource,
			    final String serverHost, final Integer serverPort,
                final String username) {
            super();
            this.password = password;
            this.resource = resource;
            this.serverHost = serverHost;
            this.serverPort = serverPort;
            this.username = username;
        }
        
        public String getPassword() { return password; }
        
        public String getResource() { return resource; }
        
        public String getServerHost() { return serverHost; }
        
        public Integer getServerPort() { return serverPort; }
        
        public User getUser() { return new User(username); }
        
        public String getUsername() { return username; }
        
        public JabberId getJabberId() {
            return JabberIdBuilder.parseQualifiedJabberId(
                    new StringBuffer(username)
                    .append('@').append(serverHost)
                    .append('/').append(resource).toString());
        }
    }*/
}