/*
 * Created On:  19-Jun-07 8:29:26 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.amazon.s3.service.S3Owner;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3BucketList;

/**
 * <b>Title:</b>thinkParity Amanzon S3 Client Rest Bucket List Xml Parser<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3BucketListParser implements Parser<S3BucketList> {

    /** An amazon s3 bucket list. */
    private S3BucketList result;

    /** A set of parser utils. */
    private final ParserUtils utils;

    /** An xml pull parser. */
    private XmlPullParser xmlPullParser;

    /**
     * Create BucketListReader.
     *
     */
    public S3BucketListParser() {
        super();
        this.utils = new ParserUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.xml.Parser#parse(java.io.Reader, java.lang.Object)
     *
     */
    public void parse(final Reader reader, final S3BucketList result)
            throws ParseException {
        this.xmlPullParser = utils.newXmlPullParser(reader);
        this.result = result;
        try {
            parse();
        } catch (final XmlPullParserException xppx) {
            throw new ParseException(xppx);
        } catch (final IOException iox) {
            throw new ParseException(iox);
        }
    }

    /**
     * Parse the owner and bucket list.
     * 
     */
    private void parse() throws XmlPullParserException, IOException,
            ParseException {
        xmlPullParser.nextTag();    // <ListAllMyBucketsResult>
        parseOwner();
        parseBuckets();
        xmlPullParser.nextTag();    // </ListAllMyBucketsResult>
    }

    /**
     * Parse the bucket list.
     * 
     */
    private void parseBuckets() throws XmlPullParserException, IOException,
            ParseException {
        xmlPullParser.nextTag();    //   <Buckets>
        if (xmlPullParser.isEmptyElementTag()) {
            return;
        } else {
            S3Bucket bucket;
            xmlPullParser.nextTag();        //     <Bucket>
            while ("Bucket".equals(xmlPullParser.getName())) {
                bucket = new S3Bucket();
                xmlPullParser.nextTag();    //       <Name>
                xmlPullParser.next();
                bucket.setName(xmlPullParser.getText());
                xmlPullParser.nextTag();    //       </Name>
                xmlPullParser.nextTag();    //       <CreationDate>
                xmlPullParser.next();
                bucket.setCreationDate(utils.parseDate(xmlPullParser));
                xmlPullParser.nextTag();    //       </CreationDate>
                xmlPullParser.nextTag();    //     </Bucket>
                result.addBucket(bucket);
                // might be end buckets or start bucket
                xmlPullParser.nextTag();
            }
        }
    }

    /**
     * Parse the owner.
     * 
     */
    private void parseOwner() throws XmlPullParserException, IOException {
        final S3Owner owner = new S3Owner();
        xmlPullParser.nextTag();    //   <Owner>
        xmlPullParser.nextTag();    //     <ID>
        xmlPullParser.next();
        owner.setId(xmlPullParser.getText());
        xmlPullParser.nextTag();    //     </ID>
        xmlPullParser.nextTag();    //     <DisplayName>
        xmlPullParser.next();
        owner.setDisplayName(xmlPullParser.getText());
        xmlPullParser.nextTag();    //     </DisplayName>
        xmlPullParser.nextTag() ;   //   </Owner>
        result.setOwner(owner);
    }
}
