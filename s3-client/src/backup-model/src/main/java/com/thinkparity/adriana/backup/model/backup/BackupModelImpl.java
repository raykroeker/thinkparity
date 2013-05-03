/*
 * Created On:  29-Sep-07 5:13:37 PM
 */
package com.thinkparity.adriana.backup.model.backup;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.util.List;

import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.adriana.backup.model.Model;
import com.thinkparity.adriana.backup.model.backup.delegate.ArchiveDirectory;
import com.thinkparity.adriana.backup.model.backup.delegate.ChecksumFile;
import com.thinkparity.adriana.backup.model.backup.delegate.CompressFile;
import com.thinkparity.adriana.backup.model.backup.delegate.CopyResources;
import com.thinkparity.adriana.backup.model.util.Session;
import com.thinkparity.amazon.s3.service.bucket.S3Bucket;
import com.thinkparity.amazon.s3.service.object.S3Key;
import com.thinkparity.amazon.s3.service.object.S3Object;
import com.thinkparity.amazon.s3.service.object.S3ObjectContentType;
import com.thinkparity.amazon.s3.service.object.S3ReadableObjectContent;

/**
 * <b>Title:</b>thinkParity Adriana Backup Model Backup Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupModelImpl extends Model implements BackupModel {

    /**
     * Instantiate an archive file name.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>String</code>.
     */
    private static String newArchiveFileName(final Session session) {
        return session.getId();
    }

    /**
     * Create BackupModelImpl.
     *
     */
    public BackupModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.adriana.backup.model.backup.BackupModel#archive(com.thinkparity.adriana.backup.model.util.Session, java.util.List)
     *
     */
    @Override
    public void archive(final Session session, final List<Resource> resourceList) {
        try {
            final File directory = createTempDirectory(session, "archive-working");
            try {
                /* copy resources */
                new CopyResources(getDirectBuffer()).copy(resourceList, directory);
                /* create archive file */
                final File archiveFile = createTempFile(session, "archive-file");
                new ArchiveDirectory(getDirectBuffer()).archive(directory, archiveFile);
                /* compress archive file */
                final File compressFile = createTempFile(session, newArchiveFileName(session));
                new CompressFile(getDirectBuffer()).compress(archiveFile, compressFile);
            } finally {
                FileUtil.deleteTree(directory);
            }
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * @see com.thinkparity.adriana.backup.model.backup.BackupModel#newSession()
     *
     */
    @Override
    public Session newSession() {
        return null;
    }

    private static String newS3ObjectName(final Session session) {
        return null;
    }

    private static File newArchiveFile(final Session session) {
        return new File(session.getWorkingDirectory(), newArchiveFileName(session));
    }

    /**
     * @see com.thinkparity.adriana.backup.model.backup.BackupModel#backup(com.thinkparity.adriana.backup.model.util.Session)
     *
     */
    @Override
    public void backup(final Session session) {
        try {
            /* s3 bucket */
            final S3Bucket bucket = new S3Bucket();
            bucket.setName(session.getS3BucketName());
            /* s3 object */
            final File archiveFile = newArchiveFile(session);
            final S3Object object = new S3Object();
            object.setChecksum(new ChecksumFile(getDirectBuffer()).checksum(archiveFile));
            /* s3 key */
            final S3Key key = new S3Key();
            key.setResource(objectKey);
            object.setKey(key);
            object.setSize(archiveFile.length());
            object.setType(S3ObjectContentType.BINARY_OCTET_STREAM);

            final S3ReadableObjectContent content = new S3ReadableObjectContent() {
                public ReadableByteChannel openReadChannel() throws IOException {
                    return ChannelUtil.openReadChannel(archiveFile);
                }
            };
            create(bucket, object, content);
        } catch (final Exception x) {
            throw panic(x);
        }
    }
}
