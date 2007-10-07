/*
 * Created On:  20-Sep-07 9:25:57 AM
 */
package com.thinkparity.desdemona.model.profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <b>Title:</b>thinkParity Desdemona Model Profile Create Test<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateLoadTest extends ProfileTestCase {

    /** Test iterations. */
    private static final int ITERATIONS = 100000;

    /** Test name. */
    private static final String NAME = "Load test create";

    /** A failure time format. */
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");

    /**
     * Create CreateLoadTest.
     *
     */
    public CreateLoadTest() {
        super(NAME);
    }

    /**
     * Load test creating a profiles.
     * 
     */
    public void testLoad() {
        final List<Long> failures = new ArrayList<Long>(ITERATIONS);
        for (int i = 0; i < ITERATIONS; i++) {
            TEST_LOGGER.logTraceId();
            TEST_LOGGER.logVariable("i", i);
            try {
                final CreateTest createTest = new CreateTest();
                createTest.setUp();
                try {
                    createTest.testCreateAllFeaturesInfo();
                } finally {
                    createTest.tearDown();
                }
            } catch (final Exception x) {
                failures.add(System.currentTimeMillis());
                TEST_LOGGER.logError(x,
                        "Create load test failed at iteration {0}.", i);
            }
        }
        TEST_LOGGER.logInfo("{0}/{1} failure(s).", failures.size(), ITERATIONS);
        for (int i = 0; i < failures.size(); i++) {
            final Calendar date = Calendar.getInstance();
            date.setTimeInMillis(failures.get(i));
            TEST_LOGGER.logInfo("Failure {0} at {1}.", i, SDF.format(date));
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * @see com.thinkparity.desdemona.model.ModelTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
