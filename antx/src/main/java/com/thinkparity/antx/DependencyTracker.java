/*
 * Created On:  29-Jan-07 1:20:31 PM
 */
package com.thinkparity.antx;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DependencyTracker {

    /** A list of all dependencies currently being tracked. */
    private static final List<Dependency> DEPENDENCIES;

    static {
        DEPENDENCIES = new Vector<Dependency>();
    }

    /**
     * Create DependencyTracker.
     * 
     * @param task
     *            The <code>AntXTask</code> consumer of the tracker.
     */
    DependencyTracker(final AntXTask task) {
        super();
    }

    /**
     * Obtain the tracked dependencies.
     * 
     * @return A <code>List</code> of <code>Dependency</code>s.
     */
    public List<Dependency> getDependencies() {
        return Collections.unmodifiableList(DEPENDENCIES);
    }

    /**
     * Determine whether or not a dependency is being tracked.
     * 
     * @param dependency
     *            A <code>Dependency</code>.
     * @return True if the dependeny is being tracked.
     */
    public Boolean isTracked(final Dependency dependency) {
        return Boolean.valueOf(DEPENDENCIES.contains(dependency));
    }

    /**
     * Track the dependency.
     * 
     * @param dependency
     *            A <code>Dependency</code>.
     */
    void track(final Dependency dependency) {
        if (isTracked(dependency))
            AntXTask.panic("Dependency {0} is already being tracked.", dependency);
        DEPENDENCIES.add(dependency);
    }
}
