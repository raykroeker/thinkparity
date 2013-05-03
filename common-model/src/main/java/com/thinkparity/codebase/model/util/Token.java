/*
 * Created On: Tue Oct 17, 2006
 */
package com.thinkparity.codebase.model.util;

/**
 * <b>Title:</b>thinkParity CommonModel Token<br>
 * <b>Description:</b>A unique token.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Token {

    /** The token value <code>String</code>. */
    private String value;
    
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
        if (this == obj)
            return true;
        if (null == obj)
            return false;
        if (getClass() != obj.getClass())
            return false;
        return ((Token) obj).value.equals(value);
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
