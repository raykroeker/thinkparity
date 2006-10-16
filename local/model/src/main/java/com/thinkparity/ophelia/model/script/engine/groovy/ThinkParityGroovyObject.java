/*
 * Created On: Oct 15, 2006 11:44:15 AM
 */
package com.thinkparity.ophelia.model.script.engine.groovy;

import groovy.lang.GroovyObjectSupport;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class ThinkParityGroovyObject extends GroovyObjectSupport {

    protected final ThinkParityGroovyUtils utils;

    /** Create ThinkParityGroovyObject. */
    protected ThinkParityGroovyObject() {
        super();
        this.utils = new ThinkParityGroovyUtils();
    }
}
