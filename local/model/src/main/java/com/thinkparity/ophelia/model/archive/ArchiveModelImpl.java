/*
 * Generated On: Sep 01 06 10:06:21 AM
 */
package com.thinkparity.ophelia.model.archive;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.thinkparity.codebase.model.artifact.Artifact;


import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.util.filter.Filter;
import com.thinkparity.ophelia.model.util.filter.artifact.DefaultFilter;
import com.thinkparity.ophelia.model.util.sort.ComparatorBuilder;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Archive Model Implementation</br>
 * <b>Description:</b><br>
 *
 * @author CreateModel.groovy
 * @version 1.1.2.1
 */
class ArchiveModelImpl extends AbstractModelImpl {

    /** A default comparator. */
    private final Comparator<Artifact> defaultComparator;

    /** A default filter. */
    private final Filter<? super Artifact> defaultFilter;

    /**
     * Create ArchiveModelImpl.
     *
     * @param workspace
     *		The thinkParity workspace.
     */
    ArchiveModelImpl(final Workspace workspace) {
        super(workspace);
        this.defaultComparator = new ComparatorBuilder().createByName();
        this.defaultFilter = new DefaultFilter();
    }

    List<Artifact> read() {
        logApiId();
        return read(defaultComparator);
    }

    List<Artifact> read(final Comparator<Artifact> comparator) {
        logApiId();
        logVariable("comparator", comparator);
        return read(comparator, defaultFilter);
    }

    List<Artifact> read(final Comparator<Artifact> comparator,
            final Filter<? super Artifact> filter) {
        logApiId();
        logVariable("comparator", comparator);
        logVariable("filter", filter);
        final List<Artifact> containers = new ArrayList<Artifact>();
        containers.addAll(getInternalContainerModel().read());
        return containers;
    }

    List<Artifact> read(final Filter<? super Artifact> filter) {
        logApiId();
        logVariable("filter", filter);
        return read(defaultComparator, filter);
    }
}
