/*
 * Created On: May 19, 2006 4:21:00 PM
 * $Id$
 */
package com.thinkparity.codebase.model.migrator;

/**
 * The core interface is implented by all thinkparity core modules. It is used
 * by the migrator to upgrade the module as well as deprecate it as time goes
 * on.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public interface Migrator {

    /**
     * Deprecate a core component.
     *
     */
    public void deprecate() throws MigrationError;

    /**
     * Upgrade a core component from one release to another. The releases need
     * not be sequential; and it is up to the component to handle incremental
     * upgrades.
     * 
     * @param from
     *            The starting release.
     * @param to
     *            The ending release.
     * @throws MigrationError
     */
    public void upgrade(final Release from, final Release to)
            throws MigrationError;
}
