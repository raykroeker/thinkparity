/*
 * Created On:  21-Jun-07 9:34:53 PM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.amazon.s3.service.S3EMailGrantee;
import com.thinkparity.amazon.s3.service.S3Grant;
import com.thinkparity.amazon.s3.service.S3Owner;
import com.thinkparity.amazon.s3.service.S3UserGrantee;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class GrantListPrinter implements Printer {

    /** A <code>List<S3Grant></code>. */
    private final List<S3Grant> grants;

    /** The <code>S3Owner</code>. */
    private S3Owner owner;

    /**
     * Create GrantListPrinter.
     * 
     * @param grants
     *            A <code>List<S3Grant></code>.
     */
    public GrantListPrinter() {
        super();
        this.grants = new ArrayList<S3Grant>();
    }

    /**
     * Obtain grants.
     *
     * @return A List<S3Grant>.
     */
    public List<S3Grant> getGrants() {
        return grants;
    }

    /**
     * Obtain owner.
     *
     * @return A S3Owner.
     */
    public S3Owner getOwner() {
        return owner;
    }

    /**
     * Set grants.
     *
     * @param grants
     *		A List<S3Grant>.
     */
    public void setGrants(final List<S3Grant> grants) {
        this.grants.clear();
        this.grants.addAll(grants);
    }

    /**
     * Set owner.
     *
     * @param owner
     *		A S3Owner.
     */
    public void setOwner(final S3Owner owner) {
        this.owner = owner;
    }

    /**
     * @see com.thinkparity.amazon.s3.service.client.rest.xml.Printer#write(java.io.Writer)
     *
     */
    public void write(final Writer writer) throws IOException {
        writer.write("<AccessControlPolicy>");
        writer.write("<Owner>");
        writer.write("<ID>");
        writer.write(owner.getId());
        writer.write("</ID>");
        writer.write("<DisplayName>");
        writer.write(owner.getDisplayName());
        writer.write("</DisplayName>");
        writer.write("</Owner>");
        writer.write("<AccessControlList>");
        for (final S3Grant grant : grants) {
            writer.write("<Grant>");
            writer.write("<Grantee xsi:type=\"");
            switch (grant.getGrantee().getType()) {
            case USER:
                writer.write("CannonicalUser\">");
                writer.write("<ID>");
                writer.write(((S3UserGrantee) grant.getGrantee()).getId());
                writer.write("</ID>");
                writer.write("<DisplayName>");
                writer.write(((S3UserGrantee) grant.getGrantee()).getId());
                writer.write("</DisplayName>");
                break;
            case EMAIL:
                writer.write("AmazonCustomerByEmail\">");
                writer.write("<EmailAddress>");
                writer.write(((S3EMailGrantee) grant.getGrantee()).getEMail().toString());
                writer.write("</EmailAddress>");
                break;
            default:
                Assert.assertUnreachable("Unknown grantee type.");
            }
            writer.write("</Grantee>");
            writer.write("<Permission>");
            writer.write(grant.getPermission().name());
            writer.write("</Permission>");
            writer.write("</Grant>");
        }
        writer.write("</AccessControlList>");
        writer.write("</AccessControlPolicy>");
    }
}
