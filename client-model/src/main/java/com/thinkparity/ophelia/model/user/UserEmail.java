/*
 * Created On: Jul 8, 2006 9:44:40 AM
 */
package com.thinkparity.ophelia.model.user;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class UserEmail {

    /** The e-mail address. */
    private String email;

    /** The e-mail id. */
    private Long id;

    /** Create UserEmail. */
    public UserEmail() { super(); }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof UserEmail) {
            return ((UserEmail) obj).id.equals(id) && 
                    ((UserEmail) obj).email.equals(email);
        }
        return false;
    }

    /**
     * Obtain the email
     *
     * @return The String.
     */
    public String getEmail() { return email; }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    public Long getId() { return id; }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() { return id.hashCode() & email.hashCode(); }

    /**
     * Set email.
     *
     * @param email The String.
     */
    public void setEmail(final String email) { this.email = email; }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .append("/").append(email)
                .toString();
    }
}
