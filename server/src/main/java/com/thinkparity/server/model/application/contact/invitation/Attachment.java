/*
 * Created On:  4-Apr-07 11:54:05 AM
 */
package com.thinkparity.desdemona.model.contact.invitation;

import com.thinkparity.codebase.HashCodeUtil;
import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Contact Invitation Attachment<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Attachment {

    /** An invitation id <code>Long</code>. */
    private Long invitationId;

    /** An attachment reference id <code>String</code>. */
    private String referenceId;

    /** The <code>ReferenceType</code>. */
    private ReferenceType referenceType;


    /**
     * Create InvitationAttachment.
     *
     */
    public Attachment() {
        super();
    }


    /**
     * @see java.lang.Object#equals(java.lang.Object)
     *
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Attachment attachment = (Attachment) obj;
        return attachment.invitationId.equals(invitationId)
                && attachment.referenceId.equals(referenceId)
                && attachment.referenceType.equals(referenceType);
    }


    /**
     * Obtain invitationId.
     *
     * @return A Long.
     */
    public Long getInvitationId() {
        return invitationId;
    }

    /**
     * Obtain referenceId.
     *
     * @return A String.
     */
    public String getReferenceId() {
        return referenceId;
    }

    /**
     * Obtain the reference type.
     * 
     * @return A <code>ReferenceType</code>.
     */
    public ReferenceType getReferenceType() {
        return referenceType;
    }

    /**
     * @see java.lang.Object#hashCode()
     *
     */
    @Override
    public int hashCode() {
        return HashCodeUtil.hashCode(invitationId, referenceId, referenceType);
    }

    /**
     * Set invitationId.
     *
     * @param invitationId
     *		A Long.
     */
    public void setInvitationId(final Long invitationId) {
        this.invitationId = invitationId;
    }

    /**
     * Set referenceId.
     *
     * @param referenceId
     *		A String.
     */
    public void setReferenceId(final String referenceId) {
        this.referenceId = referenceId;
    }

    /**
     * Set the reference type.
     * 
     * @param referenceType
     *            A <code>ReferenceType</code>.
     */
    public void setReferenceType(final ReferenceType referenceType) {
        this.referenceType = referenceType;
    }

    /**
     * <b>Title:</b>Attachment Reference Type<br>
     * <b>Description:</b><br>
     */
    public enum ReferenceType {

        /** A container version <code>ReferenceType</code>. */
        CONTAINER_VERSION(0);

        public static ReferenceType fromId(final Integer id) {
            switch (id.intValue()) {
            case 0:
                return CONTAINER_VERSION;
            default:
                throw Assert.createUnreachable("Unknown attachment type.");
            }
        }

        /** An id <code>Integer</code>. */
        private final Integer id;

        /**
         * Create ReferenceType.
         * 
         * @param id
         *            An id <code>Integer</code>.
         */
        private ReferenceType(final Integer id) {
            this.id = id;
        }

        /**
         * Obtain the reference type id.
         * 
         * @return An <code>integer</code> id.
         */
        public Integer getId() {
            return id;
        }
    }
}
