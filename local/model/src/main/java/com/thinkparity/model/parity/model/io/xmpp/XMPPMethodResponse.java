/*
 * Created On: May 14, 2006 9:12:54 AM
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.xmpp.JabberId;

import com.thinkparity.migrator.Library;
import com.thinkparity.migrator.Release;

/**
 * The parity bootstrap's xmpp method response.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPMethodResponse extends IQ {

    /** A method's response result. */
    private final Map<String, Result> result;

    /** Create XMPPMethodResponse. */
    XMPPMethodResponse() {
        super();
        this.result = new HashMap<String, Result>();
    }

    /**
     * Determine whether or not the response contains a result.
     * 
     * @return True if the response contains a result; false otherwise.
     */
    public Boolean containsResult() { return 0 < result.size(); }

    /** @see org.jivesoftware.smack.packet.IQ#getChildElementXML() */
    public String getChildElementXML() { return "NO SUCH CHILD XML"; }

    public byte[] readBytes(final String name) {
        return (byte[]) result.get(name).javaValue;
    }

    public Calendar readResultCalendar(final String name) {
        return (Calendar) result.get(name).javaValue;
    }

    /**
     * Read a jabber idresult value.
     * 
     * @param name
     *            The result name.
     * @return A jabber id value.
     */
    public JabberId readResultJabberId(final String name) {
        return (JabberId) result.get(name).javaValue;
    }

    /**
     * Read a result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public List<Library> readResultLibraries(final String name) {
        final List<Object> genericLibraries = (List<Object>) result.get(name).javaValue;
        final List<Library> libraries = new LinkedList<Library>();
        for(final Object genericLibrary : genericLibraries)
            libraries.add((Library) genericLibrary);
        return libraries;
    }

    public Library.Type readResultLibraryType(final String name) {
        return (Library.Type) result.get(name).javaValue;
    }

    /**
     * Read a result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public Long readResultLong(final String name) {
        return (Long) result.get(name).javaValue;
    }

    public List<Release> readResultReleases(final String name) {
        final List<Object> genericReleases = (List<Object>) result.get(name).javaValue;
        final List<Release> releases = new LinkedList<Release>();
        for(final Object genericRelease : genericReleases) {
            releases.add((Release) genericRelease);
        }
        return releases;
    }

    /**
     * Read a result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public String readResultString(final String name) {
        return (String) result.get(name).javaValue;
    }

    /**
     * Read a result list of strings.
     * 
     * @param name
     *            The result name.
     * @return A list of strings.
     */
    public List<String> readResultStrings(final String name) {
        return (List<String>) result.get(name).javaValue;
    }

    public List<EMail> readResultEMails(final String name) {
        return (List<EMail>) result.get(name).javaValue;
    }

    public List<ProfileEMail> readResultProfileEMails(final String name) {
        return (List<ProfileEMail>) result.get(name).javaValue;
    }

    public byte[] readSmallBytes(final String name) {
        return (byte[]) result.get(name).javaValue;
    }

    /**
     * Write a result value.
     * 
     * @param name
     *            The result name.
     * @param javaType
     *            The result data type.
     * @param javaValue
     *            The result value.
     */
    void writeResult(final String name, final Class javaType,
            final Object javaValue) {
        result.put(name, new Result(javaType, javaValue));
    }

    /** Result container including the result data type as well as its value. */
    private class Result {

        /** The java result data type. */
        private final Class javaType;

        /** The java result data value. */
        private final Object javaValue;

        /**
         * Create Result.
         * 
         * @param javaType
         *            The java result data type.
         * @param javaValue
         *            The java result data value.
         */
        private Result(final Class javaType, final Object javaValue) {
            super();
            this.javaType = javaType;
            this.javaValue = javaValue;
        }
    }
}
