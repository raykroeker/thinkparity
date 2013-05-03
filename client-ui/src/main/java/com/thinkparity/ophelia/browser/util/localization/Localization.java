/**
 * Created On: 17-Apr-07 4:07:49 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.util.localization;

import java.io.InputStream;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public interface Localization {

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @return The localised text.
     */
    public String getString(final Enum<?> type);

    /**
     * Obtain the localised string for an enumerated type.
     * 
     * @param type
     *            An enumerated type.
     * @param arguments
     *            The arguments for the pattern.
     * @return The localised text.
     */
    public String getString(final Enum<?> type, final Object[] arguments);

    /**
     * Obtain a localised string.
     * 
     * @param localKey
     *            The key within the given context.
     * @return The localised text.
     */
    public String getString(final String localKey);

    /**
     * Obtain a localised string.
     * 
     * @param localKey
     *            The key within the given context.
     * @param arguments
     *            The arguments for the pattern.
     * @return The localised text.
     */
    public String getString(final String localKey, final Object[] arguments);

    /**
     * Open a localized resource.
     * 
     * @param name
     *            The resource name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    public InputStream openResource(final String name);
}
