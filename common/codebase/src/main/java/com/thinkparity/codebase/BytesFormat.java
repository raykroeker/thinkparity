/*
 * Created On:  14-Mar-07 3:45:44 PM
 */
package com.thinkparity.codebase;

import java.text.MessageFormat;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Size Format<br>
 * <b>Description:</b>A size format has the ability to format a given number of
 * bytes into a string for display.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class BytesFormat {

    /** The format for the number of bytes. */
    private static MessageFormat FORMAT;

    static {
        FORMAT = new MessageFormat("{0,number,0.0}{1}");
    }

    /**
     * Create SizeFormat.
     *
     */
    public BytesFormat() {
        super();
    }

    /**
     * Format a number of bytes into a string.
     * 
     * @param bytes
     *            A number of bytes.
     * @return A display string.
     */
    public String format(final Long bytes) {
        return format(Unit.AUTO, bytes);
    }

    /**
     * Format a number of bytes into a string at a given unit.
     * 
     * @param unit
     *            A byte <code>Unit</code>.
     * @param bytes
     *            A number of bytes.
     * @return A display string.
     */
    public String format(final Unit unit, final Long bytes) {
        final Object[] arguments = new Object[2];
        switch (unit) {
        case AUTO:
            if (bytes >= 1099511627776L) {
                return format(Unit.TB, bytes);
            } else if (bytes >= 1073741824L) {
                return format(Unit.GB, bytes);
            } else if (bytes >= 1048576L) {
                return format(Unit.MB, bytes);
            } else if (bytes >= 1024L) {
                return format(Unit.KB, bytes);
            } else if (bytes > 0L && bytes < 1024L) {
                return format(Unit.B, bytes);
            } else {
                return format(Unit.B, 0L);
            }
        case B:
            arguments[0] = bytes;
            break;
        case GB:
            arguments[0] = bytes / 1024F / 1024F / 1024F;
            break;
        case KB:
            arguments[0] = bytes / 1024F;
            break;
        case MB:
            arguments[0] = bytes / 1024F / 1024F;
            break;
        case TB:
            arguments[0] = bytes / 1024F / 1024F / 1024F / 1024F;
            break;
        default:
            throw Assert.createUnreachable("Unknown bytes format unit.");
        }
        arguments[1] = unit.name();
        return FORMAT.format(arguments);
    }

    /**
     * <b>Title:</b>Bytes Format Unit<br>
     * <b>Description:</b><br>
     */
    public enum Unit { AUTO, B, GB, KB, MB, TB }
}
