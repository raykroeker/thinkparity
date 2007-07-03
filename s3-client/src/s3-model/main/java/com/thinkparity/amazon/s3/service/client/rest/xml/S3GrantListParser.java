/*
 * Created On:  19-Jun-07 8:29:26 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.thinkparity.amazon.s3.service.S3Grant;
import com.thinkparity.amazon.s3.service.S3GrantList;
import com.thinkparity.amazon.s3.service.S3Owner;
import com.thinkparity.amazon.s3.service.S3UserGrantee;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class S3GrantListParser implements Parser<S3GrantList> {

    /** A <code>S3GrantList</code> */
    private S3GrantList result;

    /** A set of parser utils. */
    private final ParserUtils utils;

    /** An xml pull parser. */
    private XmlPullParser xmlPullParser;

    /**
     * Create BucketListReader.
     *
     */
    public S3GrantListParser() {
        super();
        this.utils = new ParserUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.xml.Parser#parse(java.io.Reader,
     *      java.lang.Object)
     * 
     */
    public void parse(final Reader reader, final S3GrantList result)
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
    private void parse() throws XmlPullParserException, IOException {
        xmlPullParser.nextTag();    // <AccessControlPolicy>
        parseOwner();
        parseGrants();
        xmlPullParser.nextTag();    // </AccessControlPolicy>
    }

    /**
     * Parse the grant list.
     * 
     */
    private void parseGrants() throws XmlPullParserException, IOException {
        xmlPullParser.nextTag();    //   <AccessControlList>
        if (xmlPullParser.isEmptyElementTag()) {
            return;
        } else {
            S3Grant grant;
            S3UserGrantee grantee;
            xmlPullParser.nextTag();        //     <Grant>
            while ("Grant".equals(xmlPullParser.getName())) {
                grant = new S3Grant();
                grantee = new S3UserGrantee();
                xmlPullParser.nextTag();    //       <Grantee>
                xmlPullParser.nextTag();    //         <ID>
                xmlPullParser.next();
                grantee.setId(xmlPullParser.getText());
                xmlPullParser.nextTag();    //         </ID>
                xmlPullParser.nextTag();    //         <DisplayName>
                xmlPullParser.next();
                grantee.setDisplayName(xmlPullParser.getText());
                xmlPullParser.nextTag();    //         </DisplayName>
                xmlPullParser.nextTag();    //       </Grantee>
                grant.setGrantee(grantee);
                xmlPullParser.nextTag();    //       <Permission>
                xmlPullParser.next();
                grant.setPermission(S3Grant.Permission.valueOf(xmlPullParser.getText()));
                xmlPullParser.nextTag();    //       </Permission>
                xmlPullParser.nextTag();    //     </Grant>
                result.addGrant(grant);
                // might be end acl list or start grant
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
