/*
 * Created On: Jul 4, 2006 6:21:59 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.OpheliaTestUser;

/**
 * <b>Title:</b>thinkParity Search Containers Test<br>
 * <b>Description:</b>Test of the search containers index api.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class SearchContainersTest extends IndexTestCase {

    /** Test name. */
    private static final String NAME = "Test search containers";

	/** Test datum. */
	private Fixture datum;

	/** Create SearchContainersTest. */
	public SearchContainersTest() { super(NAME); }

    /**
     * Test search using a partial match at the beginning of the word.
     *
     */
    public void testSearchPartialBegin() {
        final List<Container> c_list = new ArrayList<Container>();
        final Map<String, Integer> ehit_map = new HashMap<String, Integer>(3, 1.0F);
        Container c;
        for (final String containerName : datum.containerNames) {
            c = createContainer(datum.junit, containerName);
            addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
            publish(datum.junit, c.getId(), "JUnit.X thinkParity");
            datum.waitForEvents();
            c_list.add(c);
        }
        final List<Long> cid_list = new ArrayList<Long>(3);
        final List<Long> cid_list_x = new ArrayList<Long>(3);
        ehit_map.put(datum.containerNames.get(0), 1);
        ehit_map.put(datum.containerNames.get(1), 1);
        String searchExpression;
        for (final String containerName : datum.containerNames) {
            searchExpression = containerName.substring(0, 6);
            cid_list.clear();
            cid_list.addAll(searchContainers(datum.junit, searchExpression));
            cid_list_x.clear();
            cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));

            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", ehit_map.get(containerName).intValue(), cid_list.size());
            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
        }
        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getName().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", 4, cid_list.size());
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getName().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 4, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getOrganization().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getOrganization().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getTitle().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getTitle().substring(0, 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
    }

    /**
     * Test search using a partial match at the end of the word.
     *
     */
    public void testSearchPartialEnd() {
        final List<Container> c_list = new ArrayList<Container>();
        final Map<String, Integer> ehit_map = new HashMap<String, Integer>(3, 1.0F);
        Container c;
        for (final String containerName : datum.containerNames) {
            c = createContainer(datum.junit, containerName);
            addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
            publish(datum.junit, c.getId(), "JUnit.X thinkParity");
            datum.waitForEvents();
            c_list.add(c);
        }
        final List<Long> cid_list = new ArrayList<Long>(3);
        final List<Long> cid_list_x = new ArrayList<Long>(3);
        ehit_map.put(datum.containerNames.get(0), 2);
        ehit_map.put(datum.containerNames.get(1), 2);
        String searchExpression;
        for (final String containerName : datum.containerNames) {
            searchExpression = containerName.substring(containerName.length() - 4);
            cid_list.clear();
            cid_list.addAll(searchContainers(datum.junit, searchExpression));
            cid_list_x.clear();
            cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));

            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", ehit_map.get(containerName).intValue(), cid_list.size());
            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
        }
        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getName().substring(datum.junit.getName().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getName().substring(datum.junit_x.getName().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getOrganization().substring(datum.junit.getOrganization().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getOrganization().substring(datum.junit_x.getOrganization().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getTitle().substring(datum.junit.getTitle().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getTitle().substring(datum.junit_x.getTitle().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
    }

    /**
     * Test search using a partial match at the midpoint of the word.
     *
     */
    public void testSearchPartialMid() {
        final List<Container> c_list = new ArrayList<Container>();
        final Map<String, Integer> ehit_map = new HashMap<String, Integer>(3, 1.0F);
        Container c;
        for (final String containerName : datum.containerNames) {
            c = createContainer(datum.junit, containerName);
            addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
            publish(datum.junit, c.getId(), "JUnit.X thinkParity");
            datum.waitForEvents();
            c_list.add(c);
        }
        final List<Long> cid_list = new ArrayList<Long>(3);
        final List<Long> cid_list_x = new ArrayList<Long>(3);
        ehit_map.put(datum.containerNames.get(0), 1);
        ehit_map.put(datum.containerNames.get(1), 1);
        String searchExpression;
        for (final String containerName : datum.containerNames) {
            searchExpression = containerName.substring(6, containerName.length() - 4);
            cid_list.clear();
            cid_list.addAll(searchContainers(datum.junit, searchExpression));
            cid_list_x.clear();
            cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));

            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", ehit_map.get(containerName).intValue(), cid_list.size());
            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
        }
        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getName().substring(4, datum.junit.getName().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + datum.junit.getName() + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getName().substring(4, datum.junit_x.getName().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getOrganization().substring(4, datum.junit.getOrganization().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getOrganization().substring(4, datum.junit_x.getOrganization().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getTitle().substring(4, datum.junit.getTitle().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getTitle().substring(4, datum.junit_x.getTitle().length() - 4);
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
    }

    /**
     * Test search using whole word criteria.
     *
     */
    public void testSearchWholeWord() {
        final List<Container> c_list = new ArrayList<Container>();
        final Map<String, Integer> ehit_map = new HashMap<String, Integer>(3, 1.0F);
        Container c;
        for (final String containerName : datum.containerNames) {
            c = createContainer(datum.junit, containerName);
            addDocument(datum.junit, c.getId(), "JUnitTestFramework.doc");
            publish(datum.junit, c.getId(), "JUnit.X thinkParity");
            datum.waitForEvents();
            c_list.add(c);
        }
        final List<Long> cid_list = new ArrayList<Long>(3);
        final List<Long> cid_list_x = new ArrayList<Long>(3);
        ehit_map.put(datum.containerNames.get(0), 1);
        ehit_map.put(datum.containerNames.get(1), 2);
        String searchExpression;
        for (final String containerName : datum.containerNames) {
            searchExpression = containerName;
            cid_list.clear();
            cid_list.addAll(searchContainers(datum.junit, searchExpression));

            cid_list_x.clear();
            cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));

            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", ehit_map.get(containerName).intValue(), cid_list.size());
            assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
        }
        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getName();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 4, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getName();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getOrganization();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getOrganization();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit.getTitle();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());

        cid_list.clear();
        cid_list_x.clear();
        searchExpression = datum.junit_x.getTitle();
        cid_list.addAll(searchContainers(datum.junit, searchExpression));
        cid_list_x.addAll(searchContainers(datum.junit_x, searchExpression));
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", 2, cid_list.size());
        assertEquals("Number of containers returned by search " + searchExpression + " does not match expectation.", cid_list.size(), cid_list_x.size());
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#setUp()
     *
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        datum = new Fixture(OpheliaTestUser.JUNIT, OpheliaTestUser.JUNIT_X);
        login(datum.junit);
        login(datum.junit_x);
        // delete all existing containers
        final List<Container> c_list = readContainers(datum.junit);
        for (final Container c : c_list) {
            deleteContainer(datum.junit, c.getId());
        }
        c_list.clear();
        c_list.addAll(readContainers(datum.junit_x));
        for (final Container c : c_list) {
            deleteContainer(datum.junit_x, c.getId());
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.test.ticket.TicketTestCase#tearDown()
     *
     */
    @Override
    protected void tearDown() throws Exception {
        logout(datum.junit);
        logout(datum.junit_x);
        datum = null;
        super.tearDown();
    }

    /**
     * <b>Title:</b>Test Fixture<br>
     * <b>Description:</b>Contains a the test datum. The list of container
     * names to search for are simply a single word name, a multi-word name and
     * a special character name. The expected hits are 1 because no other
     * container name will match, 3 because the word "name" will match the text
     * name within the user's name for all three packages and 1 because no other
     * container name will match.<br>
     */
    private class Fixture extends IndexTestCase.Fixture {
        private final List<String> containerNames;
        private final OpheliaTestUser junit;
        private final OpheliaTestUser junit_x;
        private Fixture(final OpheliaTestUser junit,
                final OpheliaTestUser junit_x) {
            this.containerNames = new ArrayList<String>(3);
            this.containerNames.add("SingleWordContainerName");
            this.containerNames.add("Multiple Word Container Name");
            this.junit = junit;
            this.junit_x = junit_x;
            addQueueHelper(junit);
            addQueueHelper(junit_x);
        }
    }
}
