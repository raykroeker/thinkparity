/*
 * Mar 25, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactRemoteInfo {

    /** The updator. */
	private JabberId updatedBy;

    /** The update date. */
	private Calendar updatedOn;

	/**
	 * Create a ArtifactRemoteInfo.
	 */
	public ArtifactRemoteInfo() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof ArtifactRemoteInfo) {
            return ((ArtifactRemoteInfo) obj).updatedBy.equals(updatedBy) &&
                    ((ArtifactRemoteInfo) obj).updatedOn.equals(updatedOn);
        }
        return false;
    }

    /**
	 * @return Returns the updatedBy.
	 */
	public JabberId getUpdatedBy() {
		return updatedBy;
	}

    /**
	 * @return Returns the updatedOn.
	 */
	public Calendar getUpdatedOn() {
		return updatedOn;
	}

	/** @see java.lang.Object#hashCode() */
    public int hashCode() { return toString().hashCode(); }

	/**
	 * @param updatedBy The updatedBy to set.
	 */
	public void setUpdatedBy(JabberId updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @param updatedOn The updatedOn to set.
	 */
	public void setUpdatedOn(Calendar updatedOn) {
		this.updatedOn = updatedOn;
	}

	/** @see java.lang.Object#toString() */
    public String toString() {
        return new StringBuffer(getClass().getName())
                .append(":updatedBy=").append(updatedBy)
                .append(",updatedOn=").append(updatedOn)
                .toString();
    }

}
