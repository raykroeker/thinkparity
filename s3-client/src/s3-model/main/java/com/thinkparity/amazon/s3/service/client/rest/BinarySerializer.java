/*
 * Created On:  22-Jun-07 1:05:44 PM
 */
package com.thinkparity.amazon.s3.service.client.rest;

/**
 * <b>Title:</b>thinkParity Amazon S3 Service Rest Binary Request Content
 * Serializer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class BinarySerializer implements RestRequestContentSerializer {

    /**
     * Create BinarySerializer.
     *
     */
    public BinarySerializer() {
        super();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.RestRequestContentSerializer#getContentType()
     *
     */
    public String getContentType() {
        return "binary/octet-stream";
    }
}
