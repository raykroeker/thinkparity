/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utils for Compress.
 */
public class CompressUtils {

    /*
     * This class should not be constructed
     */
    private CompressUtils() {
        // unused
    }
    
    /**
     * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>.
     */
    public static void copy( final InputStream input, final OutputStream output )
        throws IOException {
        final byte[] buffer = new byte[ 8024 ];
        int n = 0;
        while( -1 != ( n = input.read( buffer ) ) ) {
            output.write( buffer, 0, n );
        }
    }
    
    /**
     * Compares one byte array to another
     * @param source- the array to compare to 
     * @param headerBytes - the bytearray match
     */
    public static boolean compareByteArrays(byte[] source, byte[] match) {
        int i = 0;
        while(source.length < i || i < match.length ) {
            if(source[i] != match[i]) {
                return false;
            }
            i++;
        }
        return true;
    }
}
