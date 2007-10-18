/*
 * Created On:  9-Oct-07 12:31:52 PM
 */
package com.thinkparity.desdemona.model.admin.derby;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.Key;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.sql.DataSource;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.TimeFormat;
import com.thinkparity.codebase.crypto.EncryptFile;
import com.thinkparity.codebase.tar.ArchiveDirectory;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.stream.StreamInfo;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.upload.UploadFile;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.desdemona.model.Constants.Product.Desdemona;
import com.thinkparity.desdemona.model.admin.AdminModel;
import com.thinkparity.desdemona.model.io.IOService;
import com.thinkparity.desdemona.model.io.sql.DerbySql;
import com.thinkparity.desdemona.model.migrator.Archive;
import com.thinkparity.desdemona.model.migrator.InternalMigratorModel;

import com.thinkparity.desdemona.service.persistence.PersistenceService;

import com.thinkparity.desdemona.util.DateTimeProvider;
import com.thinkparity.desdemona.util.DefaultRetryHandler;
import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b>thinkParity Desdemona Model Derby Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DerbyModelImpl extends AdminModel implements DerbyModel,
        InternalDerbyModel {

    /** A time format. */
    private static final TimeFormat TIME_FORMAT;

    static {
        TIME_FORMAT = new TimeFormat();
    }

    /** A derby io interface. */
    private DerbySql derbyIO;

    /** A set of properties. */
    private Properties properties;

    /**
     * Create DerbyModelImpl.
     *
     */
    public DerbyModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.derby.DerbyModel#archive()
     *
     */
    @Override
    public void archive() {
        final File backupRoot = newBackupRoot();
        try {
            logger.logVariable("backupRoot", backupRoot);

            logger.logInfo("Backing up derby.");
            final Long backupDuration;
            final Long backupBegin = System.currentTimeMillis();
            derbyIO.backup(backupRoot);
            backupDuration = System.currentTimeMillis() - backupBegin;

            /* tar the directory */
            logger.logInfo("Archiving backup.");
            final File archiveFile = newArchiveFile(backupRoot);
            final ArchiveDirectory archiveDelegate = new ArchiveDirectory();
            final Long archiveDuration;
            synchronized (getBufferLock()) {
                final Long archiveBegin = System.currentTimeMillis();
                archiveDelegate.archive(backupRoot, archiveFile, getBufferArray());
                archiveDuration = System.currentTimeMillis() - archiveBegin;
            }

            /* create the archive */
            final Archive archive = new Archive();
            archive.setUniqueId(UUIDGenerator.nextUUID());

            /* encrypt the archive file */
            logger.logInfo("Encrypting archive.");
            final Key key = getSecretKeySpec();
            final File encryptFile = newEncryptFile(archiveFile);
            final EncryptFile encryptDelegate = new EncryptFile(key.getAlgorithm());
            final Long encryptDuration;
            synchronized (getBufferLock()) {
                final Long encryptBegin = System.currentTimeMillis();
                    encryptDelegate.encrypt(key, archiveFile, encryptFile,
                        getBufferArray());
                    encryptDuration = System.currentTimeMillis() - encryptBegin;
            }

            /* set the archive attributes */
            archive.setMD5(checksum(encryptFile));
            archive.setSize(encryptFile.length());

            /* upload */
            logger.logInfo("Uploading archive.");
            final StreamSession session = newUpstreamSession(archive);
            final UploadFile uploadDelegate = newUploadFile(session);
            final Long uploadDuration;
            final Long uploadBegin = System.currentTimeMillis();
            uploadDelegate.upload(encryptFile);
            uploadDuration = System.currentTimeMillis() - uploadBegin;

            /* cleanup */
            final Long cleanUpDuration;
            final Long cleanUpBegin = System.currentTimeMillis();
            try {
                FileUtil.deleteTree(backupRoot);
            } catch (final Exception x2) {
                logger.logWarning(x2, "Could not delete backup root {0}.",
                        backupRoot);
            }
            encryptFile.delete();
            archiveFile.delete();
            cleanUpDuration = System.currentTimeMillis() - cleanUpBegin;

            logger.logVariable("backupDuration", formatDuration(backupDuration));
            logger.logVariable("archiveDuration", formatDuration(archiveDuration));
            logger.logVariable("encryptDuration", formatDuration(encryptDuration));
            logger.logVariable("uploadDuration", formatDuration(uploadDuration));
            logger.logVariable("cleanUpDuration", formatDuration(cleanUpDuration));
        } catch (final Exception x) {
            try {
                FileUtil.deleteTree(backupRoot);
            } catch (final Exception x2) {
                logger.logWarning(x2, "Could not delete backup root {0}.",
                        backupRoot);
            }
            throw panic(x);
        }
    }

    /**
     * Format a duration as elapsed time.
     * 
     * @param duration
     *            A <code>Long</code>.
     * @return A <code>String</code>.
     */
    private String formatDuration(final Long duration) {
        return TIME_FORMAT.format(duration);
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.AdminModel#initialize()
     *
     */
    @Override
    protected void initialize() {
        final DataSource dataSource = PersistenceService.getInstance().getDataSource();
        derbyIO = IOService.getInstance().getFactory().newDerbyIO(dataSource);
        properties = DesdemonaProperties.getInstance();
    }

    /**
     * Instantiate an archive file. The file will be created. It will be named
     * with the file system root as its parent and the backup root's name as its
     * name and a tar file extension.
     * 
     * @param backupRoot
     *            A <code>File</code>.
     * @return A <code>File</code>.
     */
    private File newArchiveFile(final File backupRoot) throws IOException {
        final String archiveFilePath = MessageFormat.format(
                "{0}.tar", backupRoot.getName());
        final File archiveFile = new File(backupRoot.getParentFile(), archiveFilePath);
        if (archiveFile.createNewFile()) {
            return archiveFile;
        } else {
            throw new IOException("Could not create archive file:  " + archiveFile);
        }
    }

    /**
     * Instantiate a backup root. The directory will not be created. It will be
     * named with the file system root as its parent and the current date/time
     * as its name suffix to the closest quarter hour. The product name will
     * prefix the root name.
     * 
     * @param backupRoot
     *            A <code>File</code>.
     * @return A <code>File</code>.
     */
    private File newBackupRoot() {
        final Calendar now = DateTimeProvider.getCurrentDateTime();
        DateUtil.setClosestQuarterHour(now);
        final File backupRoot = new File(properties.getProperty("thinkparity.backup.root"));
        final String backupRootChild = MessageFormat.format(
                "Backup_{0}_{1,date,yyyyMMdd_HHmm}", Desdemona.PRODUCT_NAME,
                now.getTime());
        return new File(backupRoot, backupRootChild);
    }

    /**
     * Instantiate an encrypt file. The file will be created. It will be named
     * with the file system root as its parent and the compress file's name as
     * its name and a secret file extension.
     * 
     * @param archiveFile
     *            A <code>File</code>.
     * @return A <code>File</code>.
     */
    private File newEncryptFile(final File compressFile) throws IOException {
        final String encryptFilePath = MessageFormat.format(
                "{0}.secret", compressFile.getName());
        final File encryptFile = new File(compressFile.getParentFile(), encryptFilePath);
        if (encryptFile.createNewFile()) {
            return encryptFile;
        } else {
            throw new IOException("Could not create encrypt file " + encryptFile);
        }
    }

    /**
     * Instantiate an upload file delegate.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @return An <code>UploadFile</code>.
     */
    private UploadFile newUploadFile(final StreamSession streamSession)
            throws URISyntaxException {
        return new com.thinkparity.desdemona.util.stream.UploadFile(
                new DefaultRetryHandler(streamSession), streamSession);
    }

    /**
     * Instantiate an upstream session for the archive.
     * 
     * @param archive
     *            An <code>Archive</code>.
     * @return A <code>StreamSession</code>.
     */
    private StreamSession newUpstreamSession(final Archive archive) {
        final StreamInfo streamInfo = new StreamInfo();
        streamInfo.setMD5(archive.getMD5());
        streamInfo.setSize(archive.getSize());

        final InternalMigratorModel migratorModel = getMigratorModel();
        final Product product = migratorModel.readProduct(Desdemona.PRODUCT_NAME);
        final Release release = migratorModel.readLatestRelease(product.getName(), OSUtil.getOs());
        return getStreamModel().newUpstreamSession(streamInfo, product, release, archive);
    }
}
