/*
 * Created On:  25-Jul-07 8:45:37 PM
 */
package com.thinkparity.codebase.bzip2;

import com.thinkparity.common.StringUtil.Charset;

/**
 * <b>Title:</b>thinkParity Codebase BZip2 Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class Constants {

    /** The bzip2 header. */
    static final byte[] HEADER;

    static {
        HEADER = "BZ".getBytes(Charset.UTF_8.getCharset());
    }

    /**
     * Create Constants.
     *
     */
    private Constants() {
        super();

    }

}
