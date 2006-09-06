/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.model.user;

import org.dom4j.Element;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.model.Constants.Jabber;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.4
 */
public class User {

    /** thinkParity user. */
    public static final User THINK_PARITY;

    static final String NAME_SEP = " ";

    static {
        THINK_PARITY = new User();
        THINK_PARITY.setId(
                JabberIdBuilder.parseQualifiedJabberId(
                        Jabber.SYSTEM_QUALIFIED_JABBER_ID));
    }

    /** The user's id. */
	private JabberId id;

    /** The user's name. */
    private String name;

    /** The user's organization. */
    private String organization;

    /** The user's title. */
    private String title;

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
     * Obtain the title
     *
     * @return The String.
     */
    public String getTitle() {
        return title;
    }

    /**
	 * @return Returns the vCard.
	 */
	public Element getVCard() { return vCard; }

    /**
     * Determine whether or not the user's organization is set.
     * 
     * @return True if the organization is set; false otherwise.
     */
    public Boolean isSetOrganization() {
        return null != organization;
    }

    /**
     * Determine whether or not the user's title is set.
     * 
     * @return True if the title is set; false otherwise.
     */
    public Boolean isSetTitle() {
        return null != title;
    }

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
     * Set title.
     *
     * @param title The String.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
	 * @param cardInfo The vCard to set.
	 */
	public void setVCard(final Element vCard) { this.vCard = vCard; }
}
