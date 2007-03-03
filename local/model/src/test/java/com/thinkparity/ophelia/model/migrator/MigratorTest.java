/*
 * Created On:  24-Jan-07 5:32:07 PM
 */
package com.thinkparity.ophelia.model.migrator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.ZipUtil;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Migrator Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorTest extends MigratorTestCase {

    /** A default deploy <code>ProcessMonitor</code>. */
    private static final ProcessMonitor DEPLOY_MONITOR;

    private static final String NAME = "Migrator test";

    static {
        DEPLOY_MONITOR = new ProcessAdapter() {
            @Override
            public void beginProcess() {}
            @Override
            public void beginStep(final Step step, final Object data) {}
            @Override
            public void determineSteps(Integer steps) {}
            @Override
            public void endProcess() {}
            @Override
            public void endStep(final Step step) {}
        };
    }

    private Fixture datum;

    private File seedFile;

    private Product seedProduct;

    private Release seedRelease;

    private List<Resource> seedResources;

    /**
     * Create MigratorTest.
     *
     */
    public MigratorTest() {
        super(NAME);
    }

    /**
     * Test deploy.  The product/release should already exist.
     * 
     */
    public void testDeployExistingRelease() {
        getMigratorModel(datum.thinkparity).deploy(DEPLOY_MONITOR, seedProduct,
                seedRelease, seedResources, seedFile);
    }

    /**
     * Test deploy.  The product/release should already exist.
     * 
     */
    public void testDeployNewRelease() {
        final Release release = new Release();
        release.setChecksum(seedRelease.getChecksum());
        release.setName(seedRelease.getName() + System.currentTimeMillis());
        getMigratorModel(datum.thinkparity).deploy(DEPLOY_MONITOR, seedProduct,
                release, seedResources, seedFile);
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        copyInputFiles(getTestCaseDirectory());
        login(OpheliaTestUser.THINKPARITY);
        try {
            seedProduct = new Product();
            seedProduct.setCreatedBy(OpheliaTestUser.THINKPARITY.getId());
            seedProduct.setCreatedOn(getSessionModel(OpheliaTestUser.THINKPARITY).readDateTime());
            seedProduct.setName("JUnit Test Product");
            seedProduct.setUniqueId(UUIDGenerator.nextUUID());
            seedProduct.setUpdatedBy(seedProduct.getCreatedBy());
            seedProduct.setUpdatedOn(seedProduct.getCreatedOn());

            ZipUtil.createZipFile(seedFile, getTestCaseDirectory(), getDefaultBufferSize());

            seedRelease = new Release();
            seedRelease.setChecksum(checksum(seedFile, getDefaultBufferSize()));
            seedRelease.setName("Version 1.0.0");

            seedResources = new ArrayList<Resource>();

            final MigratorModel migratorModel = getMigratorModel(OpheliaTestUser.THINKPARITY);
            migratorModel.deploy(DEPLOY_MONITOR, seedProduct, seedRelease,
                    seedResources, seedFile);
        } finally {
            logout(OpheliaTestUser.THINKPARITY);
        }

        datum = new Fixture(OpheliaTestUser.THINKPARITY);
        login(datum.thinkparity);
    }

    /**
     * @see com.thinkparity.ophelia.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.thinkparity);
        datum = null;
        super.tearDown();
    }

    private class Fixture extends MigratorTestCase.Fixture {
        private final OpheliaTestUser thinkparity;
        private Fixture(final OpheliaTestUser thinkparity) {
            super();
            this.thinkparity = thinkparity;
            addQueueHelper(thinkparity);
        }
    }
}
