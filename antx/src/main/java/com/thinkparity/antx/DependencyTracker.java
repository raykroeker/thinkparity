/*
 * Created On:  29-Jan-07 1:20:31 PM
 */
package com.thinkparity.antx;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class DependencyTracker {

    /** A list of all dependencies. */
    private static final List<Dependency> ALL_DEPENDENCIES;

    /** A list of dependencies currently being tracked by scope. */
    private static final Map<Dependency.Scope, List<Dependency>> SCOPED_DEPENDENCIES;

    static {
        ALL_DEPENDENCIES = new Vector<Dependency>(35);
        SCOPED_DEPENDENCIES = new Hashtable<Dependency.Scope, List<Dependency>>(
                Dependency.Scope.values().length, 1.0F);
        for (final Dependency.Scope scope : Dependency.Scope.values()) {
            SCOPED_DEPENDENCIES.put(scope, new Vector<Dependency>(35));
        }
    }

    /**
     * Create DependencyTracker.
     * 
     * @param task
     *            The <code>AntXTask</code> consumer of the tracker.
     */
    public DependencyTracker() {
        super();
    }

    /**
     * Obtain the tracked dependencies.
     * 
     * @return A <code>List</code> of <code>Dependency</code>s.
     */
    public List<Dependency> getAllDependencies() {
        return Collections.unmodifiableList(ALL_DEPENDENCIES);
    }

    /**
     * Obtain the tracked dependencies by scope.
     * 
     * @param scope
     *            A <code>Dependency.Scope</code>.
     * @return A <code>List</code> of <code>Dependency</code>s.
     */
    public List<Dependency> getDependencies(final Dependency.Scope scope) {
        return Collections.unmodifiableList(SCOPED_DEPENDENCIES.get(scope));
    }

    /**
     * Determine whether or not a dependency is being tracked.
     * 
     * @param dependency
     *            A <code>Dependency</code>.
     * @return True if the dependeny is being tracked.
     */
    public Boolean isTracked(final Dependency dependency) {
        return Boolean.valueOf(ALL_DEPENDENCIES.contains(dependency));
    }

    /**
     * Determine whether or not a dependency is being tracked.
     * 
     * @param scope
     *            A <code>Dependency.Scope</code>.
     * @param dependency
     *            A <code>Dependency</code>.
     * @return True if the dependeny is being tracked.
     */
    public Boolean isTracked(final Dependency.Scope scope,
            final Dependency dependency) {
        return Boolean.valueOf(SCOPED_DEPENDENCIES.get(scope).contains(dependency));
    }

    /**
     * Track the dependency.
     * 
     * @param scope
     *            A <code>Dependency.Scope</code>.
     * @param dependency
     *            A <code>Dependency</code>.
     */
    void track(final Dependency.Scope scope, final Dependency dependency) {
        if (isTracked(scope, dependency))
            AntXTask.panic("Dependency {0} is already being tracked.", dependency);
        ALL_DEPENDENCIES.add(dependency);
        final List<Dependency> scopedList = SCOPED_DEPENDENCIES.get(scope);
        scopedList.add(dependency);
        SCOPED_DEPENDENCIES.put(scope, scopedList);
    }
}
