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

import com.thinkparity.ophelia.model.migrator.monitor.DeployMonitor;
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

    private static final String NAME = "Migrator test";

    private Product seedProduct;

    private Release seedRelease;

    private List<Resource> seedResources;

    private File seedFile;

    private Fixture datum;

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
        getMigratorModel(datum.thinkparity).deploy(new DeployMonitor() {
        }, seedProduct, seedRelease, seedResources, seedFile);
    }

    /**
     * Test deploy.  The product/release should already exist.
     * 
     */
    public void testDeployNewRelease() {
        final Release release = new Release();
        release.setChecksum(seedRelease.getChecksum());
        release.setName(seedRelease.getName() + System.currentTimeMillis());
        getMigratorModel(datum.thinkparity).deploy(new DeployMonitor() {
        }, seedProduct, release, seedResources, seedFile);
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

            ZipUtil.createZipFile(seedFile, getTestCaseDirectory());

            seedRelease = new Release();
            seedRelease.setChecksum(checksum(seedFile, 1024));
            seedRelease.setName("Version 1.0.0");

            seedResources = new ArrayList<Resource>();

            final MigratorModel migratorModel = getMigratorModel(OpheliaTestUser.THINKPARITY);
            migratorModel.deploy(new DeployMonitor() {
            }, seedProduct, seedRelease, seedResources, seedFile);
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
