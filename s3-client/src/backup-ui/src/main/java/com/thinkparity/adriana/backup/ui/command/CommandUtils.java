/*
 * Created On:  29-Sep-07 4:22:05 PM
 */
package com.thinkparity.adriana.backup.ui.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class CommandUtils {

    /**
     * Create CommandUtil.
     *
     */
    CommandUtils() {
        super();
    }

    /**
     * Extract a list of ordered postcommands from the properties.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>List<String></code>.
     */
    List<String> extractPostcommands(final Properties properties) {
        return extractFilteredOrderedProperties(PropertyNames.POSTCOMMAND,
                properties);
    }

    /**
     * Extract a list of ordered precommands from the properties.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>List<String></code>.
     */
    List<String> extractPrecommands(final Properties properties) {
        return extractFilteredOrderedProperties(PropertyNames.PRECOMMAND,
                properties);
    }

    /**
     * Extract a list of ordered resources.
     * 
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>List<String></code>.
     */
    List<String> extractResources(final Properties properties) {
        return extractFilteredOrderedProperties(PropertyNames.RESOURCE,
                properties);
    }

    /**
     * Extract a list of filtered ordered properties. The name will filter the
     * list of properties returned; and the tail end of the property name will
     * order it. The name must mach the pattern:
     * 
     * <pre>
     * x.y
     * </pre>
     * 
     * where y is an integer.
     * 
     * @param name
     *            A <code>String</code>.
     * @param properties
     *            A set of <code>Properties</code>.
     * @return A <code>List<String></code>.
     */
    private List<String> extractFilteredOrderedProperties(final String name,
            final Properties properties) {
        final Set<String> nameSet = properties.stringPropertyNames();
        final List<String> filteredNameList = new ArrayList<String>(nameSet.size());
        String substring;
        for (final String n : nameSet) {
            if (n.startsWith(name)) {
                substring = n.substring(name.length() - 1);
                if (1 < substring.length()) {
                    if ('.' == substring.charAt(0)) {
                        try {
                            Integer.valueOf(substring.substring(1));
                        } catch (NumberFormatException nfx) {
                            continue;
                        }
                        filteredNameList.add(n);
                    }
                }
            }
        }
        Collections.sort(filteredNameList);
        final List<String> filteredOrderedValues = new ArrayList<String>(filteredNameList.size());
        for (final String n : filteredNameList) {
            filteredOrderedValues.add(properties.getProperty(n));
        }
        return filteredOrderedValues;
    }

    /** <b>Title:</b>Property Names:  POSTCOMMAND,PRECOMMAND,RESOURCE<br> */
    static final class PropertyNames {
        static final String POSTCOMMAND = "thinkparity.adriana.backup.postcommand";
        static final String PRECOMMAND = "thinkparity.adriana.backup.precommand";
        static final String RESOURCE = "thinkparity.adriana.backup.resource";
    }
}
