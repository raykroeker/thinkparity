/*
 * Created On: Jun 11, 2006 3:16:55 PM
 */
package com.thinkparity.codebase.model.session;

import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b>thinkParity CommonModel Session Credentials<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.6
 */
public final class Credentials {

    /** The e-mail address. */
    private EMail email;

    /** The password. */
    private String password;

    /** The username. */
    private String username;

    /**
     * Create Credentials.
     *
     */
    public Credentials() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    @Override
    public boolean equals(final Object obj) {
        if (null == obj) {
            return false;
        } else {
            if (this == obj) {
                return true;
            } else {
                if (getClass() == obj.getClass()) {
                    if (null == username && null == ((Credentials) obj).username) {
                        if (null == email && null == ((Credentials) obj).email) {
                            return ((Credentials) obj).password.equals(password);
                        } else {
                            return ((Credentials) obj).password.equals(password)
                                && ((Credentials) obj).email.equals(email);
                        }
                    } else {
                        return ((Credentials) obj).password.equals(password)
                                && ((Credentials) obj).username.equals(username)
                                && ((Credentials) obj).email.equals(email);

                    }
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * Obtain the email.
     *
     * @return A <code>EMail</code>.
     */
    public EMail getEMail() {
        return email;
    }

    /**
     * Obtain the password
     *
     * @return The String.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Obtain the username
     *
     * @return The String.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + (null == username ? 0 : username.hashCode());
        result = PRIME * result + (null == email ? 0 : email.hashCode());
        return result;
    }

    /**
     * Determine if the email is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetEMail() {
        return null != email;
    }

    /**
     * Determine if the username is set.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean isSetUsername() {
        return null != username;
    }

    /**
     * Set the email.
     *
     * @param email
     *		A <code>EMail</code>.
     */
    public void setEMail(final EMail email) {
        this.email = email;
    }

    /**
     * Set password.
     *
     * @param password The password.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Set username.
     *
     * @param username The username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * @see java.lang.Object#toString()
     * 
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(username).toString();
    }
}
