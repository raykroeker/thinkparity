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

	private JabberId updatedBy;

	private Calendar updatedOn;

	/**
	 * Create a ArtifactRemoteInfo.
	 */
	public ArtifactRemoteInfo() { super(); }

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

}
