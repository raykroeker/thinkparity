/*
 * Created On:  23-Jan-07 5:35:19 PM
 */
package com.thinkparity.desdemona.model.migrator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.ZipUtil;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.email.EMailBuilder;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.xmpp.event.ProductReleaseDeployedEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants;
import com.thinkparity.desdemona.model.DownloadHelper;
import com.thinkparity.desdemona.model.io.sql.MigratorSql;
import com.thinkparity.desdemona.util.smtp.SMTPService;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Migrator Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorModelImpl extends AbstractModelImpl implements
        MigratorModel, InternalMigratorModel {

    /** A <code>MigratorSql</code> persistence. */
    private MigratorSql migratorSql;

    /** An instance of <code>SMTPService</code>. */
    private final SMTPService smtpService;

    /**
     * Create MigratorModelImpl.
     *
     */
    public MigratorModelImpl() {
        super();
        this.smtpService = SMTPService.getInstance();
    }
   
    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#deploy(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.util.List)
     * 
     */
    public void deploy(final Product product, final Release release,
            final List<Resource> resources) {
        try {
            // find/create the product
            final Product localProduct = readProduct(product.getName());

            // ensure release is new
            Assert.assertNotTrue(doesExistRelease(localProduct.getId(),
                    release.getName(), release.getOs()),
                    "Release {0} for product {1} on {0} already exists.",
                    release.getName(), product.getName(), release.getOs());

            // download the release
            final StreamSession session = getStreamModel().newDownstreamSession(product, release);
            final File tempFile = createTempFile();
            try {
                final DownloadHelper downloadHelper = newDownloadHelper(session);
                downloadHelper.download(tempFile);
                final FileSystem tempFileSystem = new FileSystem(createTempDirectory());
                try {
                    // extract the release
                    synchronized (getBufferLock()) {
                        ZipUtil.extractZipFile(tempFile,
                                tempFileSystem.getRoot(), getBufferArray());
                    }
                    // create
                    createRelease(localProduct, release, readPreviousRelease(
                            localProduct, release), resources, tempFileSystem);
                    // notify
                    notifyProductReleaseDeployed(localProduct, release, resources);
                } finally {
                    tempFileSystem.deleteTree();
                }
            } finally {
                tempFile.delete();
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#logError(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Error, java.util.Calendar)
     * 
     */
    public void logError(final Product product, final Release release,
            final Error error) {
        try {
            final Release localRelease = migratorSql.readRelease(
                    product.getName(), release.getName(), release.getOs());

            final File tempErrorFile = createTempFile();
            try {
                final FileWriter fileWriter = new FileWriter(tempErrorFile);
                try {
                    XStreamUtil.getInstance().toXML(error, fileWriter);
                } finally {
                    fileWriter.close();
                }
                final InputStream inputStream = new FileInputStream(tempErrorFile);
                try {
                    migratorSql.createError(user, localRelease, inputStream,
                            tempErrorFile.length(), getBufferSize(), error);
                } finally {
                    inputStream.close();
                }
                // send e-mail
                final Boolean notify =
                    Boolean.valueOf(getConfiguration("logerror.notify"));
                if (notify.booleanValue()) {
                    final MimeMessage mimeMessage = smtpService.createMessage();
                    inject(mimeMessage, error);
                    final EMail toRecipient =
                        EMailBuilder.parse(getConfiguration("logerror.notify.to"));
                    final List<String> ccRecipients = StringUtil.tokenize(
                                getConfiguration("logerror.notify.cc"),
                                Separator.Comma, new ArrayList<String>(2));
                    addRecipient(mimeMessage, toRecipient);
                    for (final String ccRecipient : ccRecipients)
                        addRecipient(mimeMessage, MimeMessage.RecipientType.CC,
                                EMailBuilder.parse(ccRecipient));

                    final InternetAddress fromInternetAddress = new InternetAddress();
                    fromInternetAddress.setAddress(Constants.Internet.Mail.FROM_ADDRESS);
                    fromInternetAddress.setPersonal(Constants.Internet.Mail.FROM_PERSONAL);
                    mimeMessage.setFrom(fromInternetAddress);
                    smtpService.deliver(mimeMessage);
                }
            } finally {
                tempErrorFile.delete();
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#readLatestRelease(java.lang.String, com.thinkparity.codebase.OS)
     *
     */
    public Release readLatestRelease(final String productName, final OS os) {
        try {
            final String latestReleaseName = migratorSql.readLatestReleaseName(
                    productName, os);
            return migratorSql.readRelease(productName, latestReleaseName, os);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#readProduct(java.lang.String)
     * 
     */
    public Product readProduct(final String name) {
        try {
            return migratorSql.readProduct(name);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.InternalMigratorModel#readProductFeature(com.thinkparity.codebase.model.migrator.Product, java.lang.String)
     *
     */
    public Feature readProductFeature(final Product product, final String name) {
        try {
            return migratorSql.readProductFeature(product, name);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#readProductFeatures(java.lang.String)
     * 
     */
    public List<Feature> readProductFeatures(final String name) {
        try {
            return migratorSql.readProductFeatures(name);   
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#readRelease(java.lang.String,
     *      java.lang.String, com.thinkparity.codebase.OS)
     * 
     */
    public Release readRelease(final String productName, final String name,
            final OS os) {
        try {
            return migratorSql.readRelease(productName, name, os);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.migrator.MigratorModel#readResources(java.lang.String,
     *      java.lang.String, com.thinkparity.codebase.OS)
     * 
     */
    public List<Resource> readResources(final String productName,
            final String releaseName, final OS os) {
        try {
            final Release release = migratorSql.readRelease(productName,
                    releaseName, os);
            return migratorSql.readResources(release);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initialize()
     *
     */
    @Override
    protected void initialize() {
        migratorSql = new MigratorSql();
    }

    /**
     * Create a release. This will create the release then examine the resources
     * by name release name and path to see if they already exist. If no such
     * resource exists it will be created otherwise simply a reference between
     * the release and the resource is created.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param previousRelease
     *            The previous <code>Release</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param releaseFileSystem
     *            A <code>FileSystem</code>.
     * @throws IOException
     */
    private void createRelease(final Product product, final Release release,
            final Release previousRelease, final List<Resource> resources,
            final FileSystem releaseFileSystem) throws IOException {
        migratorSql.createRelease(product, release);
        InputStream stream;
        Resource previousReleaseResource;
        for (final Resource resource : resources) {
            /* if the resource exists in the previous release and it has not
             * changed re-use it */
            if (null != previousRelease && migratorSql.doesExistResource(
                    previousRelease, resource.getPath()).booleanValue()) {
                previousReleaseResource = migratorSql.readResource(previousRelease, resource.getPath());
                if (resource.getChecksumAlgorithm().equals(previousReleaseResource.getChecksumAlgorithm())
                        && resource.getChecksum().equals(previousReleaseResource.getChecksum())) {
                    migratorSql.addResource(release, previousReleaseResource);
                    continue;
                }
            }
            // create the resource
            stream = new FileInputStream(releaseFileSystem.findFile(resource.getPath()));
            try {
                migratorSql.createResource(resource, stream, getBufferSize());
            } finally {
                stream.close();
            }
            // add to the release
            migratorSql.addResource(release, resource);
        }
    }

    /**
     * Determine if a release exists.
     * 
     * @param productId
     *            A product id <code>Long</code>.
     * @param name
     *            A release name <code>String</code>.
     * @return True if the release exists.
     */
    private boolean doesExistRelease(final Long productId, final String name,
            final OS os) {
        return migratorSql.doesExistRelease(productId, name, os); 
    }

    /**
     * Inject the error into the mime message. We create an html message
     * displaying an error notification.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param error
     *            An <code>Error</code>.
     */
    private void inject(final MimeMessage mimeMessage, final Error error)
            throws MessagingException {
        final ErrorText errorText = new ErrorText(getEnvironment(), Locale.getDefault(), error);
        mimeMessage.setSubject(errorText.getSubject());

        final MimeBodyPart errorBody = new MimeBodyPart();
        errorBody.setContent(errorText.getBody(), errorText.getBodyType());

        final Multipart errorContent = new MimeMultipart();
        errorContent.addBodyPart(errorBody);
        mimeMessage.setContent(errorContent);
    }

    /**
     * Notify all users of a product that a new release is available.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     */
    private void notifyProductReleaseDeployed(final Product product,
            final Release release, final List<Resource> resources) {
        // TODO limit the event to users of the product
        final ProductReleaseDeployedEvent event = new ProductReleaseDeployedEvent();
        event.setProduct(product);
        event.setRelease(release);
        event.setResources(resources);
        enqueuePriorityEvents(getIds(getUserModel().read(),
                new ArrayList<JabberId>()), event);
    }

    /**
     * Read the previous release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>Release</code>.
     */
    private Release readPreviousRelease(final Product product,
            final Release release) {
        return migratorSql.readPreviousRelease(product.getName(),
                release.getDate(), release.getOs());
    }
}
