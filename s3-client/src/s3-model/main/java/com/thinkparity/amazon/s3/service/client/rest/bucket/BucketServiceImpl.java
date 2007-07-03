/*
 * Created On:  19-Jun-07 8:05:51 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.bucket;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.amazon.s3.service.*;
import com.thinkparity.amazon.s3.service.bucket.BucketService;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.bucket.S3BucketConstraints;
import com.thinkparity.amazon.s3.service.bucket.S3BucketList;
import com.thinkparity.amazon.s3.service.client.rest.RestRequest;
import com.thinkparity.amazon.s3.service.client.rest.RestResponse;
import com.thinkparity.amazon.s3.service.client.rest.RestServiceImpl;
import com.thinkparity.amazon.s3.service.client.rest.RestUtils;
import com.thinkparity.amazon.s3.service.client.rest.XmlSerializer;
import com.thinkparity.amazon.s3.service.client.rest.xml.GrantListPrinter;
import com.thinkparity.amazon.s3.service.client.rest.xml.Parser;
import com.thinkparity.amazon.s3.service.client.rest.xml.Printer;
import com.thinkparity.amazon.s3.service.client.rest.xml.S3BucketListParser;
import com.thinkparity.amazon.s3.service.client.rest.xml.S3GrantListParser;
import com.thinkparity.amazon.s3.service.client.rest.xml.S3ObjectListParser;
import com.thinkparity.amazon.s3.service.object.S3ObjectList;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BucketServiceImpl extends RestServiceImpl implements
        BucketService {

    /**
     * Create a grant list printer.
     * 
     * @return A <code>Printer</code>.
     */
    private static Printer newGrantListPrinter(final S3Owner owner,
            final List<S3Grant> grants) {
        final GrantListPrinter printer = new GrantListPrinter();
        printer.setGrants(grants);
        printer.setOwner(owner);
        return printer;
    }

    /**
     * Create a bucket list.
     * 
     * @return A <code>S3BucketList</code>.
     */
    private static S3BucketList newS3BucketList() {
        return new S3BucketList();
    }

    /**
     * Create a bucket list parser.
     * 
     * @return A <code>BucketListParser</code>.
     */
    private static Parser<S3BucketList> newS3BucketListParser() {
        return new S3BucketListParser();
    }

    /**
     * Create a grant list.
     * 
     * @return A <code>S3GrantList</code>.
     */
    private static S3GrantList newS3GrantList() {
        return new S3GrantList();
    }

    /**
     * Create a grant list parser.
     * 
     * @return A <code>S3GrantListParser</code>.
     */
    private static Parser<S3GrantList> newS3GrantListParser() {
        return new S3GrantListParser();
    }

    /**
     * Create an object list.
     * 
     * @return An instance of <code>S3ObjectList</code>.
     */
    private static S3ObjectList newS3ObjectList() {
        return new S3ObjectList();
    }

    /**
     * Create an object list parser.
     * 
     * @return An instance of <code>Parser<S3ObjectList></code>.
     */
    private static Parser<S3ObjectList> newS3ObjectListParser() {
        return new S3ObjectListParser();
    }

    /** The <code>S3AuthenticationConstraints</code>. */
    private final S3AuthenticationConstraints authConstraints;

    /** The <code>S3BucketConstraints</code>. */
    private final S3BucketConstraints bucketConstraints;

    /** The <code>S3GrantConstraints</code>. */
    private final S3GrantConstraints grantConstraints;

    /** The <code>S3OwnerConstraints</code>. */
    private final S3OwnerConstraints ownerConstraints;

    /** A set of rest utilities. */
    private final RestUtils utils;

    /**
     * Create BucketServiceImpl.
     *
     */
    public BucketServiceImpl() {
        super();
        this.authConstraints = S3AuthenticationConstraints.getInstance();
        this.grantConstraints = S3GrantConstraints.getInstance();
        this.bucketConstraints = S3BucketConstraints.getInstance();
        this.ownerConstraints = S3OwnerConstraints.getInstance();
        this.utils = new RestUtils();
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#create(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket)
     *
     */
    public void create(final S3Authentication auth, final S3Bucket bucket) {
        try {
            validate(auth);
            validate(bucket);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.PUT);
            request.setBucket(bucket);

            utils.service(request);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#delete(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket)
     *
     */
    public void delete(final S3Authentication auth, final S3Bucket bucket) {
        try {
            validate(auth);
            validate(bucket);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.DELETE);
            request.setBucket(bucket);

            utils.service(request);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#read(com.thinkparity.amazon.s3.service.S3Authentication)
     * 
     */
    public S3BucketList read(final S3Authentication auth) {
        try {
            validate(auth);

            final RestRequest request = new RestRequest();
            request.setContext(context);
            request.setAuthentication(auth);
            request.setType(RestRequest.Type.GET);

            final RestResponse<S3BucketList> response = utils.service(
                    request, newS3BucketListParser(), newS3BucketList());
            return response.getResult();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#readACL(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket)
     *
     */
    public S3GrantList readACL(final S3Authentication auth,
            final S3Bucket bucket) {
        try {
            validate(auth);
            validate(bucket);

            final RestRequest request = new RestRequest();
            request.setACLRequest();
            request.setAuthentication(auth);
            request.setBucket(bucket);
            request.setContext(context);
            request.setType(RestRequest.Type.GET);

            final RestResponse<S3GrantList> response = utils.service(
                    request, newS3GrantListParser(), newS3GrantList());

            return response.getResult();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#read(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket)
     *
     */
    public S3ObjectList readObjects(final S3Authentication auth, final S3Bucket bucket) {
        try {
            validate(auth);
            validate(bucket);

            final RestRequest request = new RestRequest();
            request.setAuthentication(auth);
            request.setBucket(bucket);
            request.setContext(context);
            request.setType(RestRequest.Type.GET);

            final RestResponse<S3ObjectList> response = utils.service(request,
                    newS3ObjectListParser(), newS3ObjectList());

            return response.getResult();
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.amazon.s3.service.bucket.BucketService#updateACL(com.thinkparity.amazon.s3.service.S3Authentication, com.thinkparity.amazon.s3.service.bucket.S3Bucket, java.util.List)
     *
     */
    public void updateACL(final S3Authentication auth, final S3Bucket bucket,
            final S3Owner owner, final List<S3Grant> grants) {
        try {
            validate(auth);
            validate(bucket);
            validate(owner);
            validate(grants);

            final RestRequest request = new RestRequest();
            request.setACLRequest();
            request.setAuthentication(auth);
            request.setBucket(bucket);
            request.setContext(context);
            request.setType(RestRequest.Type.PUT, new XmlSerializer() {
                @Override
                public long getContentLength() {
                    return -1;  // unkown
                }
                @Override
                public void write(final OutputStream stream) throws IOException {
                    newGrantListPrinter(owner, grants).write(newStreamWriter(context, stream));
                }
            });

            utils.service(request);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * Validate the grants.
     * 
     * @param grants
     *            A <code>List<S3Grant></code>.
     */
    private void validate(final List<S3Grant> grants) {
        grantConstraints.getGrants().validate(grants);
        for (final S3Grant grant : grants) {
            validate(grant);
        }
    }

    /**
     * Validate the authentication.
     * 
     * @param auth
     *            A <code>S3Authentication</code>.
     */
    private void validate(final S3Authentication auth) {
        authConstraints.getAccessKeyId().validate(auth.getAccessKeyId());
        authConstraints.getSecretAccessKey().validate(auth.getSecretAccessKey());
    }

    /**
     * Validate the bucket.
     * 
     * @param bucket
     *            A <code>S3Bucket</code>.
     */
    private void validate(final S3Bucket bucket) {
        bucketConstraints.getName().validate(bucket.getName());
    }

    /**
     * Validate the grant.
     * 
     * @param grant
     *            A <code>S3Grant</code>.
     */
    private void validate(final S3Grant grant) {
        grantConstraints.getGrant().validate(grant);
        grantConstraints.getGrantee().validate(grant.getGrantee());
        final S3Grantee grantee = grant.getGrantee();
        switch (grantee.getType()) {
        case EMAIL:
            grantConstraints.getGranteeEMail().validate(((S3EMailGrantee) grantee).getEMail().toString());
            break;
        case USER:
            grantConstraints.getGranteeDisplayName().validate(((S3UserGrantee) grantee).getDisplayName());
            grantConstraints.getGranteeId().validate(((S3UserGrantee) grantee).getId());
            break;
        default:
            Assert.assertUnreachable("Unkown grantee type.");
        }
        grantConstraints.getPermission().validate(grant.getPermission());
    }

    /**
     * Validate the owner.
     * 
     * @param owner
     *            A <code>S3Owner</code>.
     */
    private void validate(final S3Owner owner) {
        ownerConstraints.getDisplayName().validate(owner.getDisplayName());
        ownerConstraints.getId().validate(owner.getId());
    }
}
