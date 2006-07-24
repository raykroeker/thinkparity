/*
 * Created On: Jul 21, 2006 2:08:58 PM
 */
package com.thinkparity.codebase.http;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * <b>Title:</b>thinkParity Link<br>
 * <b>Description:</b>A thinkParity Link is an http get href writer. It is used
 * to create encrypted\encoded links.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 * @see LinkFactory
 */
public class Link {

    /** A stack of contexts. */
    private final Queue<String> context;

    /** A map of parameter name value pairs. */
    private final Map<String, String> parameters;

    /** The link root. */
    private final String root;

    /** Create Link. */
    Link(final String root) {
        super();
        this.context = new LinkedList<String>();
        this.parameters = new HashMap<String, String>();
        this.root = root;
    }

    /**
     * Add context to the link.
     * 
     * @param context
     *            A context.
     * @return True if the context is added; false otherwise.
     */
    public boolean addContext(final String context) {
        return this.context.offer(context);
    }

    /**
     * Set the link parameter.
     * 
     * @param name
     *            The parameter name.
     * @param value
     *            The parameter value.
     * @return The previous value of the parameter.
     */
    public String addParameter(final String name, final String value) {
        return parameters.put(name, value);
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if(null != obj && obj instanceof Link) {
            return obj.toString().equals(toString());
        }
        return false;
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() { return toString().hashCode(); }

    /** @see java.lang.Object#toString() */
    @Override
    public String toString() {
        final StringBuffer toString = new StringBuffer(root);
        for(final String context : this.context) {
            toString.append("/").append(context);
        }
        Boolean firstKey = Boolean.TRUE;
        for(final String key : parameters.keySet()) {
            if(firstKey) {
                toString.append("?");
                firstKey = Boolean.FALSE;
            }
            else { toString.append("&"); }
            toString.append(key).append("=").append(parameters.get(key));
        }
        return toString.toString();
    }
}
