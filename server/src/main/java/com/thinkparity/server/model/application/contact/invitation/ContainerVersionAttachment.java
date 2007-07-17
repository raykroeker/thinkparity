/*
 * Created On:  4-Apr-07 1:49:17 PM
 */
package com.thinkparity.desdemona.model.contact.invitation;

import java.text.MessageFormat;
import java.util.UUID;

import com.thinkparity.desdemona.model.contact.invitation.Attachment;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Container Version Contact Invitation
 * Attachment<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionAttachment extends Attachment {

    /** The reference id message format pattern <code>String</code>. */
    private static final String REFERENCE_ID_PATTERN;

    /** The unique id version id separator <code>char</code>. */
    private static final char SEP;

    static {
        REFERENCE_ID_PATTERN = "{0}:{1}";
        SEP = ':';
    }

    /**
     * Parse the reference id for the unique id.
     * 
     * @param value
     *            A value <code>String</code>.
     * @return A unique id <code>String</code>.
     */
    private static String parseUniqueId(final String value) {
        if (null == value)
            return null;
        return value.substring(0, value.indexOf(SEP));
    }

    /**
     * Parse the reference id for the version id.
     * 
     * @param value
     *            A value <code>String</code>.
     * @return A version id <code>String</code>.
     */
    private static String parseVersionId(final String value) {
        if (null == value)
            return null;
        return value.substring(value.indexOf(SEP) + 1);
    }

    /**
     * Create ContainerVersionAttachment.
     *
     */
    public ContainerVersionAttachment() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.contact.invitation.Attachment#getReferenceType()
     *
     */
    @Override
    public ReferenceType getReferenceType() {
        return ReferenceType.CONTAINER_VERSION;
    }

    /**
     * Obtain the container version unique id.
     * 
     * @return A unique id <code>UUID</code>.
     */
    public UUID getUniqueId() {
        final String uniqueId = parseUniqueId(getReferenceId());
        if (null == uniqueId)
            return null;
        else
            return UUID.fromString(uniqueId);
    }

    /**
     * Obtain the container version id.
     * 
     * @return A version id <code>Long</code>.
     */
    public Long getVersionId() {
        final String versionId = parseVersionId(getReferenceId());
        if (null == versionId)
            return null;
        else
            return Long.valueOf(versionId);
    }

    /**
     * Set the unique id.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    public void setUniqueId(final UUID uniqueId) {
        setReferenceId(uniqueId, getVersionId());
    }

    /**
     * Set the version id.
     * 
     * @param versionId
     *            A version id <code>Long</code>.
     */
    public void setVersionId(final Long versionId) {
        setReferenceId(getUniqueId(), versionId);
    }

    /**
     * Set the reference id.
     * 
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     */
    private void setReferenceId(final UUID uniqueId, final Long versionId) {
        if (null == uniqueId) {
            if (null == versionId) {
                setReferenceId(null);
            } else {
                setReferenceId(MessageFormat.format(REFERENCE_ID_PATTERN, "",
                        versionId.toString()));
            }
        }
        else {
            if (null == versionId) {
                setReferenceId(MessageFormat.format(REFERENCE_ID_PATTERN,
                        uniqueId.toString(), ""));
            } else {
                setReferenceId(MessageFormat.format(REFERENCE_ID_PATTERN,
                        uniqueId, versionId));
            }
        }
    }
}
