/*
 * Mar 6, 2006
 */
package com.thinkparity.model.parity.model.index;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactIndex {

	private final List<JabberId> contacts;

	private JabberId createdBy;

	private Calendar createdOn;

	private Long id;

	private JabberId keyHolder;

	private String name;

	/**
	 * Create a ArtifactIndex.
	 * 
	 */
	public ArtifactIndex() {
		super();
		this.contacts = new LinkedList<JabberId>();
	}

	public Boolean addAllContacts(final List<JabberId> artifactContacts) {
		return this.contacts.addAll(artifactContacts);
	}

	public Boolean addContact(final JabberId artifactContact) {
		return contacts.add(artifactContact);
	}

	public void clearContacts() {
		this.contacts.clear();
	}

	/**
	 * @return Returns the contacts.
	 */
	public List<JabberId> getContacts() {
		return contacts;
	}

	/**
	 * @return Returns the createdBy.
	 */
	public JabberId getCreatedBy() {
		return createdBy;
	}

	/**
	 * @return Returns the createdOn.
	 */
	public Calendar getCreatedOn() {
		return createdOn;
	}



	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}



	/**
	 * @return Returns the keyHolder.
	 */
	public JabberId getKeyHolder() {
		return keyHolder;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	public Boolean removeAllContacts(final List<JabberId> artifactContacts) {
		return this.contacts.removeAll(artifactContacts);
	}

	public Boolean removeContact(final JabberId artifactContact) {
		return contacts.remove(artifactContact);
	}

	/**
	 * @param createdBy The createdBy to set.
	 */
	public void setCreatedBy(JabberId artifactCreatedBy) {
		this.createdBy = artifactCreatedBy;
	}

	/**
	 * @param createdOn The createdOn to set.
	 */
	public void setCreatedOn(Calendar artifactCreatedOn) {
		this.createdOn = artifactCreatedOn;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param keyHolder The keyHolder to set.
	 */
	public void setKeyHolder(JabberId artifactKeyHolder) {
		this.keyHolder = artifactKeyHolder;
	}

	/**
	 * @param artifactName The name to set.
	 */
	public void setName(String artifactName) {
		this.name = artifactName;
	}

}
