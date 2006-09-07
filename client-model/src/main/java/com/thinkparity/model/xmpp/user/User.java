/*
 * Created On: May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

/**
 * <b>Title:</b>thinkParity User<br>
 * <b>Description:</b>A thinkParity user. The user represents each and and
 * every user in the system. It consists of a login, name; organization and
 * title which are shared with everyone (the login only by the software).<br>
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.14
 */
public class User {

    /** The thinkParity user. */
    public static final User THINK_PARITY;

	static final String NAME_SEP = " ";

	static {
        THINK_PARITY = new User();
        THINK_PARITY.setId(JabberIdBuilder.parseUsername("thinkparity"));
    }

	/** The user's jabber id. */
	private JabberId id;

    /** The local user pk. */
    private Long localId;

	/** The user's name. */
	private String name;

	/** The user's organization. */
	private String organization;

	/** The user's title. */
    private String title;

    /** Create User. */
	public User() { super(); }

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof User) {
			return ((User) obj).id.equals(id);
		}
		return false;
	}

    /**
     * Obtain the user's id.
     * 
     * @return The user's jabber id.
     */
    public JabberId getId() {
        return id;
    }

    /**
     * Obtain the user's local id.
     * 
     * @return The local id.
     */
    public Long getLocalId() {
        return localId;
    }

    /**
	 * Obtain the user's name.
	 * 
	 * @return The user's name.
	 */
	public String getName() {
        return name;
	}

	/**
	 * Obtain the user's organization.
	 * 
	 * @return Returns the organization.
	 */
	public String getOrganization() {
        return organization;
	}

	/**
	 * Obtain the simple username of the user.
	 * 
	 * @return The simple username; without the domain\resource suffix.
	 */
	public String getSimpleUsername() {
        return id.getUsername();
	}

	/**
     * Obtain the title
     *
     * @return The String.
     */
    public String getTitle() {
        return title;
    }

	/**
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() {
        return id.getQualifiedUsername();
	}

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
	public int hashCode() { return id.hashCode(); }

    /**
     * Determine if the organiation is set.
     * 
     * @return True if the organization is set.
     */
    public Boolean isSetOrganization() {
        return null != organization;
    }

    /**
     * Determine if the user's title is set.
     * 
     * @return True if the title is set; false otherwise.
     */
    public Boolean isSetTitle() {
        return null != title;
    }

    /**
	 * Set the user's id.
	 * 
	 * @param id
	 *            The user's jabber id.
	 */
	public void setId(final JabberId id) {
        this.id = id;
	}

    /**
     * Set the user's local id.
     * 
     * @param localId
     *            A local id.
     */
    public void setLocalId(final Long localId) {
        this.localId = localId;
    }

    /**
	 * Set the user's name.
	 * 
	 * @param name
	 *            The user's name.
	 */
	public void setName(final String name) {
        this.name = name;
	}

    /**
     * Set the user's name.
     * 
     * @param first
     *            The user's first name.
     * @param last
     *            The user's last name.
     */
    public void setName(final String first, final String last) {
        this.name = new UserNameBuilder(first, last).getName();
    }

    /**
     * Set the user's name.
     * 
     * @param first
     *            The user's first name.
     * @param middle
     *            The user's middle name.
     * @param last
     *            The user's last name.
     */
    public void setName(final String first, final String middle,
            final String last) {
        this.name = new UserNameBuilder(first, middle, last).getName();
    }

    /**
	 * Set the user's organization.
	 * 
	 * @param organization
	 *            The user's organization.
	 */
	public void setOrganization(final String organization) {
		this.organization = organization;
	}

    /**
     * Set title.
     *
     * @param title The String.
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .append("/").append(localId)
                .append("/").append(name)
                .append("/").append(organization)
                .toString();
    }
}
