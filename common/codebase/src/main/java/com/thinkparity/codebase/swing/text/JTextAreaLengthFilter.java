/*
 * Created On:  10-May-07 8:37:46 AM
 */
package com.thinkparity.codebase.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity CommonCodebase Swing JTextArea Length Filter<br>
 * <b>Description:</b>A document filter used to limit text length within a
 * swing text area.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class JTextAreaLengthFilter extends DocumentFilter {

    /** The maximum length the text area can support. */
    private final int maxLength;

    /**
     * Create JTextAreaLengthFilter.
     * 
     * @param maxLength
     *            The maximum length <code>Integer</code> to allow.
     */
    public JTextAreaLengthFilter(final Integer maxLength) {
        super();
        this.maxLength = maxLength.intValue();
    }

    /**
     * Create JTextAreaLengthFilter.
     * 
     * @param constraint
     *            A <code>StringConstraint</code> indicating the maximum
     *            length to allow.
     */
    public JTextAreaLengthFilter(final StringConstraint constraint) {
        super();
        this.maxLength = constraint.getMaxLength().intValue();
    }

    /**
     * @see javax.swing.text.DocumentFilter#insertString(javax.swing.text.DocumentFilter.FilterBypass,
     *      int, java.lang.String, javax.swing.text.AttributeSet)
     * 
     */
    @Override
    public void insertString(final FilterBypass fb, final int offset,
            final String string, final AttributeSet attr)
            throws BadLocationException {
        if (null == string || (fb.getDocument().getLength() + string.length()) <= maxLength) {
            super.insertString(fb, offset, string, attr);
        } else {
            final int length = maxLength - fb.getDocument().getLength();
            super.insertString(fb, offset, string.substring(0, length), attr);
        }
    }

    /**
     * @see javax.swing.text.DocumentFilter#replace(javax.swing.text.DocumentFilter.FilterBypass,
     *      int, int, java.lang.String, javax.swing.text.AttributeSet)
     * 
     */
    @Override
    public void replace(final FilterBypass fb, final int offset,
            final int length, final String text, final AttributeSet attrs)
            throws BadLocationException {
        if (null == text
                || (fb.getDocument().getLength() + text.length() - length) <= maxLength) {
            super.replace(fb, offset, length, text, attrs);
        }
    }
}
