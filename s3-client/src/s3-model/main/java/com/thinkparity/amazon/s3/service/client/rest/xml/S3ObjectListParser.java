/*
 * Created On:  19-Jun-07 8:29:26 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.amazon.s3.service.S3Owner;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3ObjectListParser implements Parser<S3ObjectList> {

    /** A <code>S3ObjectList</code> */
    private S3ObjectList result;

    /** A set of parser utils. */
    private final ParserUtils utils;

    /** An xml pull parser. */
    private XmlPullParser xmlPullParser;

    /**
     * Create BucketListReader.
     *
     */
    public S3ObjectListParser() {
        super();
        this.utils = new ParserUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.xml.Parser#parse(java.io.Reader,
     *      java.lang.Object)
     * 
     */
    public void parse(final Reader reader, final S3ObjectList result)
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
        xmlPullParser.nextTag();    // <ListBucketResult>
        parseParameters();
        parseObjects();
        parseCommonPrefixes();
    }

    /**
     * Parse the common prefixes.
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseCommonPrefixes() throws XmlPullParserException, IOException {
        if ("CommonPrefixes".equals(xmlPullParser.getName())
                && XmlPullParser.START_TAG == xmlPullParser.getEventType()) {
            if (!xmlPullParser.isEmptyElementTag()) {
                // end common prefixes or or start prefix
                xmlPullParser.nextTag();
                while ("Prefix".equals(xmlPullParser.getName())) {
                    xmlPullParser.next();
                    result.addCommonPrefix(xmlPullParser.getText());
                    xmlPullParser.nextTag();    // </Prefix>
                    // end common prefixes or or start prefix
                    xmlPullParser.nextTag();
                }
            }
        }
    }

    /**
     * Parse the grant list.
     * 
     */
    private void parseObjects() throws XmlPullParserException, IOException,
            ParseException {
        S3Object object;
        S3Key key;
        while ("Contents".equals(xmlPullParser.getName())) {
            object = new S3Object();
            xmlPullParser.nextTag();    //  <Key>
            xmlPullParser.next();
            key = new S3Key();
            key.setResource(xmlPullParser.getText());
            object.setKey(key);
            xmlPullParser.nextTag();    //  </Key>

            xmlPullParser.nextTag();    //  <LastModified>
            xmlPullParser.next();
            object.setLastModified(utils.parseDate(xmlPullParser));
            xmlPullParser.nextTag();    //  </LastModified>

            xmlPullParser.nextTag();    //  <ETag>
            xmlPullParser.next();
            object.setETag(xmlPullParser.getText());
            xmlPullParser.nextTag();    //  </ETag>

            xmlPullParser.nextTag();    //  <Size>
            xmlPullParser.next();
            object.setSize(Long.valueOf(xmlPullParser.getText()));
            xmlPullParser.nextTag();    //  </Size>

            object.setOwner(parseOwner());

            xmlPullParser.nextTag();    //  <StorageClass>
            xmlPullParser.next();
            object.setStorageClass(S3Object.StorageClass.valueOf(
                    xmlPullParser.getText()));
            xmlPullParser.nextTag();    //  <StorageClass>
            xmlPullParser.nextTag();    //  </Contents>
            result.addObject(object);

            // end common prefixes or start contents
            xmlPullParser.nextTag();
        }
    }

    /**
     * Parse the owner.
     * 
     */
    private S3Owner parseOwner() throws XmlPullParserException, IOException {
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
        return owner;
    }

    /**
     * Parse the parameters used to generate the object list.
     * 
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void parseParameters() throws XmlPullParserException, IOException {
        xmlPullParser.nextTag();    //    <Name>
        xmlPullParser.next();
        result.setBucketName(xmlPullParser.getText());
        xmlPullParser.nextTag();    //    </Name>

        xmlPullParser.nextTag();        //    <Prefix>
        if (!xmlPullParser.isEmptyElementTag()) {
            xmlPullParser.next();
            if (XmlPullParser.TEXT == xmlPullParser.getEventType()) {
                result.setPrefix(xmlPullParser.getText());
                xmlPullParser.nextTag();    //    </Prefix>
            }
        }
        
        xmlPullParser.nextTag();        //    <Marker>
        if (!xmlPullParser.isEmptyElementTag()) {
            xmlPullParser.next();
            if (XmlPullParser.TEXT == xmlPullParser.getEventType()) {
                result.setMarker(xmlPullParser.getText());
                xmlPullParser.nextTag();    //    </Marker>
            }
        }

        xmlPullParser.nextTag();        //    <MaxKeys>
        xmlPullParser.next();
        result.setMaxKeys(Integer.valueOf(xmlPullParser.getText()));
        xmlPullParser.nextTag();    //    </MaxKeys>

        xmlPullParser.nextTag();    //    <IsTruncated>
        xmlPullParser.next();
        result.setTruncated(Boolean.valueOf(xmlPullParser.getText()));
        xmlPullParser.nextTag();    //    </IsTruncated>

        // end list bucket result or begin delimiter
        xmlPullParser.nextTag();
        if ("Delimiter".equals(xmlPullParser.getName())) {
            if (!xmlPullParser.isEmptyElementTag()) {
                xmlPullParser.next();
                if (XmlPullParser.TEXT == xmlPullParser.getEventType()) {
                    result.setDelimiter(xmlPullParser.getText());
                    xmlPullParser.nextTag();    //    </Delimiter>
                }
            }
        }
    }
}
