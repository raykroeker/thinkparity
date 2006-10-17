/*
 * Created On: May 14, 2006 9:12:54 AM
 */
package com.thinkparity.ophelia.model.io.xmpp;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Library;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.Token;

/**
 * The parity bootstrap's xmpp method response.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
@SuppressWarnings({ "unchecked", "unused" })
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
        return (byte[]) readResult(name);
    }

    public Calendar readResultCalendar(final String name) {
        return (Calendar) readResult(name);
    }

    public Container readResultContainer(final String name) {
        return (Container) readResult(name);
    }

    public List<Container> readResultContainers(final String name) {
        return (List<Container>) readResult(name);
    }

    public List<ContainerVersion> readResultContainerVersions(final String name) {
        return (List<ContainerVersion>) readResult(name);
    }

    public List<Document> readResultDocuments(final String name) {
        return (List<Document>) readResult(name);
    }

    public List<DocumentVersion> readResultDocumentVersions(final String name) {
        return (List<DocumentVersion>) readResult(name);
    }

    public List<EMail> readResultEMails(final String name) {
        return (List<EMail>) readResult(name);
    }

    /**
     * Read a jabber idresult value.
     * 
     * @param name
     *            The result name.
     * @return A jabber id value.
     */
    public JabberId readResultJabberId(final String name) {
        return (JabberId) readResult(name);
    }

    /**
     * Read a result list of jabber ids.
     * 
     * @param name
     *            The result name.
     * @return A list of jabber ids.
     */
    public List<JabberId> readResultJabberIds(final String name) {
        return (List<JabberId>) readResult(name);
    }

    /**
     * Read a result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public List<Library> readResultLibraries(final String name) {
        final List<Object> genericLibraries = (List<Object>) readResult(name);
        final List<Library> libraries = new LinkedList<Library>();
        for(final Object genericLibrary : genericLibraries)
            libraries.add((Library) genericLibrary);
        return libraries;
    }

    public Library.Type readResultLibraryType(final String name) {
        return (Library.Type) readResult(name);
    }

    /**
     * Read a result value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    public Long readResultLong(final String name) {
        return (Long) readResult(name);
    }

    public List<ProfileEMail> readResultProfileEMails(final String name) {
        return (List<ProfileEMail>) readResult(name);
    }

    public List<Release> readResultReleases(final String name) {
        final List<Object> genericReleases = (List<Object>) readResult(name);
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
        return (String) readResult(name);
    }

    /**
     * Read a result list of strings.
     * 
     * @param name
     *            The result name.
     * @return A list of strings.
     */
    public List<String> readResultStrings(final String name) {
        return (List<String>) readResult(name);
    }

    public Token readResultToken(final String name) {
        return (Token) readResult(name);
    }

    public byte[] readSmallBytes(final String name) {
        return (byte[]) readResult(name);
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

    /**
     * Read the result java value.
     * 
     * @param name
     *            The result name.
     * @return The result value.
     */
    private Object readResult(final String name) {
        if (result.containsKey(name)) {
            return result.get(name).javaValue;
        } else {
            return null;
        }
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
