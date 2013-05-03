/*
 * Created On:  21-Jun-07 3:09:36 PM
 */
package com.thinkparity.amazon.s3.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3ClientConsole {

    private final BufferedReader bufferedReader;

    /**
     * Create S3ClientConsole.
     *
     */
    public S3ClientConsole() {
        super();
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    public Boolean confirm(final String promptPattern,
            final Object... promptArguments) {
        final String confirmation = readLineImpl(promptPattern, promptArguments);
        if (isAffirmative(confirmation)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    public void print(final String pattern, final Object... arguments) {
        System.out.print(MessageFormat.format(pattern, arguments));
    }

    public void println(final String pattern, final Object... arguments) {
        System.out.println(MessageFormat.format(pattern, arguments));
    }

    public String readLine(final String promptPattern,
            final Object... promptArguments) {
        return readLineImpl(promptPattern, promptArguments);
    }

    private boolean isAffirmative(final String input) {
        return "y".equalsIgnoreCase(input) || "yes".equalsIgnoreCase(input);
    }

    private String readLineImpl(final String promptPattern,
            final Object... promptArguments) {
        try {
            System.out.print(MessageFormat.format(promptPattern, promptArguments));
            return bufferedReader.readLine();
        } catch (final IOException iox) {
            throw new RuntimeException(iox);
        }
    }
    
}
