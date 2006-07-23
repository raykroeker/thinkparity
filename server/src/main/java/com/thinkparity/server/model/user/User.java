/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.server.model.user;

import org.dom4j.Element;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public class User {

    /** The user's id. */
	private JabberId id;

    /** The user's name. */
    private String name;

    /** The user's organization. */
    private String organization;

    /** The user's vCard. */
	private Element vCard;

	/** Create User. */
	public User() { super(); }

	/**
	 * @return Returns the id.
	 */
	public JabberId getId() { return id; }

	/**
     * Obtain the name
     *
     * @return The String.
     */
    public String getName() { return name; }

	/**
     * Obtain the organization
     *
     * @return The String.
     */
    public String getOrganization() { return organization; }

	/**
	 * @return Returns the vCard.
	 */
	public Element getVCard() { return vCard; }

    /**
	 * @param id The id to set.
	 */
	public void setId(final JabberId id) { this.id = id; }

    /**
     * Set name.
     *
     * @param name The String.
     */
    public void setName(final String name) { this.name = name; }

    /**
     * Set organization.
     *
     * @param organization The String.
     */
    public void setOrganization(final String organization) {
        this.organization = organization;
    }

    /**
	 * @param cardInfo The vCard to set.
	 */
	public void setVCard(final Element vCard) { this.vCard = vCard; }
}
