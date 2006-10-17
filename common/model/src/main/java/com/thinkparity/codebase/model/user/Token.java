/*
 * Created On: Tue Oct 17, 2006
 */
package com.thinkparity.codebase.model.user;

/**
 * <b>Title:</b>thinkParity Session Token<br>
 * <b>Description:</b>A token uniquely identifying the user's installation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Token {

    /** The token value <code>String</code>. */
    private transient String value;
    
    /**
     * Create Token.
     */
    public Token() {
        super();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (null != obj && obj instanceof Token) {
            return ((Token) obj).value.equals(value);
        }
        return false;
    }

    /**
     * Obtain the token value.
     * 
     * @return A value <code>String</code>.
     */
    public String getValue() {
        return value;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    /**
     * Set the token value.
     * 
     * @param value
     *            A value <code>String</code>.
     */
    public void setValue(final String value) {
        this.value = value;
    }
}
