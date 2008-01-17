/**
 * Created On: 10-Jul-07 10:10:43 PM
 * $Id$
 */
package com.thinkparity.codebase.swing.text;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.constraint.StringConstraint;

/**
 * <b>Title:</b>thinkParity CommonCodebase Swing JTextField Length Filter<br>
 * <b>Description:</b>A document filter used to limit text length within a
 * swing text field. Pasted strings are clipped beyond the first ENTER.<br>
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public final class JTextFieldLengthFilter extends DocumentFilter {

    /** The maximum length the text field can support. */
    private final int maxLength;

    /**
     * Create JTextFieldLengthFilter.
     * 
     * @param maxLength
     *            The maximum length <code>Integer</code> to allow.
     */
    public JTextFieldLengthFilter(final Integer maxLength) {
        super();
        this.maxLength = maxLength.intValue();
    }

    /**
     * Create JTextFieldLengthFilter.
     * 
     * @param constraint
     *            A <code>StringConstraint</code> indicating the maximum
     *            length to allow.
     */
    public JTextFieldLengthFilter(final StringConstraint constraint) {
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
        final String insert = removeAfterEnter(string);
        if (null == insert || (fb.getDocument().getLength() + insert.length()) <= maxLength) {
            super.insertString(fb, offset, insert, attr);
        } else {
            final int length = maxLength - fb.getDocument().getLength();
            super.insertString(fb, offset, insert.substring(0, length), attr);
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
        final String replace = removeAfterEnter(text);
        if (null == replace
                || (fb.getDocument().getLength() + replace.length() - length) <= maxLength) {
            super.replace(fb, offset, length, replace, attrs);
        }
    }

    /**
     * Trim the string beyond the first ENTER encountered.
     */
    private String removeAfterEnter(final String inputString) {
        if (null == inputString)
            return null;
        final Integer findIndex = inputString.indexOf(Separator.NixNewLine.toString());
        if (0 == findIndex) {
            return null;
        } else if (0 < findIndex) {
            return inputString.substring(0, findIndex);
        }
        return inputString;
    }
}
