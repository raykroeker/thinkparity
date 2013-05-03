/*
 * Created On:  2-Mar-07 9:41:23 AM
 */
package com.thinkparity.codebase.junitx;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * <b>Title:</b>thinkParity JUnit eXtensions Test Case Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class TestCaseUtil {

    /**
     * Read a file into a string. Note that this method assumes very small files
     * and uses a 64B buffer accordingly.
     * 
     * @param file
     *            A <code>File</code>.
     * @return A <code>String</code>.
     * @throws IOException
     */
    static final String read(final File file) throws IOException {
        final Reader reader = new FileReader(file);
        try {
            final StringBuffer sbuf = new StringBuffer();
            // BUFFER - 64B - TestCaseUtil#read(File)
            char[] cbuf = new char[64];
            int chars;
            while((chars = reader.read(cbuf)) > 0) {
                sbuf.append(cbuf, 0, chars);
            }
            return sbuf.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Write a string to a file.
     * 
     * @param file
     *            A <code>File</code>.
     * @param string
     *            A <code>String</code>.
     * @throws IOException
     */
    static final void write(final File file, final String string)
            throws IOException {
        final Writer writer = new FileWriter(file);
        try {
            writer.write(string);
            writer.flush();
        } finally {
            writer.close();
        }
    }


    /**
     * Create TestCaseUtil.
     *
     */
    private TestCaseUtil() {
        super();
    }
}
