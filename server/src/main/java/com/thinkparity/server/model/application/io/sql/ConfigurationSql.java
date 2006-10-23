/*
 * Created On: Oct 22, 2006 2:03:11 PM
 */
package com.thinkparity.desdemona.model.io.sql;

import java.util.Collections;
import java.util.List;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ConfigurationSql extends AbstractSql {

    /** Create ConfigurationSql. */
    public ConfigurationSql() {
        super();
    }

    public void create(final String key, final String value) {}

    public void delete(final String key) {}

    public String read(final String key) {
        return "";
    }

    public List<String> readKeys() {
        return Collections.emptyList();
    }

    public String update(final String key, final String value) {
        return "";
    }
}
