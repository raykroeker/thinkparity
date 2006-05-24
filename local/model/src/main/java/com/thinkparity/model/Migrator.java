/*
 * Created On: May 20, 2006 12:33:52 PM
 * $Id$
 */
package com.thinkparity.model;

import com.thinkparity.migrator.MigrationError;
import com.thinkparity.migrator.Release;

/**
 * A parity migrator for the local model.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class Migrator implements com.thinkparity.migrator.Migrator {

    /** Create Migrator. */
    public Migrator() { super(); }

    /** @see com.thinkparity.migrator.Migrator#deprecate() */
    public void deprecate() throws MigrationError {}

    /** @see com.thinkparity.migrator.Migrator#upgrade(com.thinkparity.migrator.Release, com.thinkparity.migrator.Release) */
    public void upgrade(final Release from, final Release to)
            throws MigrationError {}
}
