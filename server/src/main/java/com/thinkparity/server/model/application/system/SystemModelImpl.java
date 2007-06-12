/*
 * Created On:  3-Jun-07 3:06:22 PM
 */
package com.thinkparity.desdemona.model.system;

import java.text.MessageFormat;
import java.util.Calendar;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Version;
import com.thinkparity.desdemona.util.DateTimeProvider;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SystemModelImpl extends AbstractModelImpl implements
        SystemModel, InternalSystemModel {

    /**
     * Create SystemModelImpl.
     *
     */
    public SystemModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.system.SystemModel#readDateTime()
     *
     */
    public Calendar readDateTime() {
        try {
            return DateTimeProvider.getCurrentDateTime();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.system.SystemModel#readVersion()
     *
     */
    public String readVersion() {
        try {
            return MessageFormat.format("{0} - {1} - {2}",
                    Version.getProductName(), Version.getReleaseName(),
                    Version.getBuildId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
}
