/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.ZipUtil
import com.thinkparity.codebase.jabber.JabberId

import com.thinkparity.codebase.model.migrator.Product
import com.thinkparity.codebase.model.migrator.Release
import com.thinkparity.codebase.model.migrator.Resource
import com.thinkparity.codebase.model.stream.StreamSession
import com.thinkparity.codebase.model.stream.StreamMonitor
import com.thinkparity.codebase.model.stream.StreamWriter
import com.thinkparity.codebase.model.stream.StreamException
import com.thinkparity.codebase.model.util.codec.MD5Util

import com.thinkparity.ophelia.model.util.xmpp.XMPPSession

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Deploy Helper<br>
 * <b>Description:</b>The deploy helper uses the migrator interface within the
 * model to deploy an image of the ui's updatable code.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DeployHelper {

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** The image directory <code>File</code>. */
    File imageDir

    /** An <code>XMPPSession</code>. */
    XMPPSession xmppSession

    JabberId userId

    /**
     * Initialize the deploy helper.
     *
     */
    void init() {
        if (null == imageDir)
            imageDir = configuration["thinkparity.target.package.image-dir"]
        if (null == xmppSession)
            xmppSession = configuration["thinkparity.xmpp-session"]
        if (null == userId)
            userId = configuration["thinkparity.userid"]
    }

    /**
     * Deploy a product release.
     *
     * @param product
     *      A <code>Product</code>.
     * @param release
     *      A <code>Release</code>.
     * @param resources
     *      A <code>List</code> of <code>Resource</code>s.
     */
    void deploy(Product product, Release release, List resources) {
        init()
        // zip the package image
        final File imageArchiveFile = configuration["thinkparity.target.package.image.archive-file"]
        if (imageArchiveFile.exists())
            imageArchiveFile.delete();
        ZipUtil.createZipFile(imageArchiveFile, imageDir);
        release.setChecksum(checksum(imageArchiveFile, 2048))
        // deploy
        deploy(configuration["thinkparity.userid"], product, release, resources, upload(imageArchiveFile))
    }

    /**
     * Deploy the product release.
     *
     * @param userId
     * @param product
     * @param release
     * @param resources
     * @param streamId
     */
    void deploy(JabberId userId, Product product, Release release,
        List resources, String streamId) {
        xmppSession.deployMigrator(configuration["thinkparity.userid"],
            product, release, resources, streamId)
    }

    /**
     * Calculate a checksum for a file.
     *
     * @param file
     *      A <code>File</code>.
     * @param buffer
     *      A buffer size <code>Integer</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    String checksum(File file, Integer buffer) {
        InputStream stream = new BufferedInputStream(new FileInputStream(file),
            buffer);
        try {
            return MD5Util.md5Hex(stream);
        } finally {
            stream.close();
        }
    }

    /**
     * Upload a file.
     *
     * @param file
     *      A <code>File</code>.
     * @return A stream id <code>String</code>.
     */
    String upload(File file) {
        final StreamSession session = xmppSession.createStreamSession(userId);
        final InputStream stream = new BufferedInputStream(new FileInputStream(file), 2048);
        final Long streamSize = file.length();
        try {
            return upload(session, stream, streamSize);
        } finally {
            stream.close();
        }
    }

    /**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    String upload(final StreamSession session, final InputStream stream,
            final Long size) throws IOException {
        stream.mark(stream.available())
        return upload(new DeployStreamMonitor(helper:this, session:session,
            stream:stream, size:size), session, stream, size, 0L)
    }

    /**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    String upload(final StreamMonitor monitor, final StreamSession session,
            final InputStream stream, final Long size, final Long offset)
            throws IOException {
        stream.reset()
        long skipped = stream.skip(offset)
        while (skipped < offset && 0 < skipped) {
            skipped += stream.skip(offset.longValue() - skipped)
        }
        final Long actualOffset
        if (skipped == offset.longValue()) {
            println "Resuming upload for ${session} at ${offset}."
            actualOffset = offset
        } else {
            println "Could not resume download for ${session} at ${offset}.  Starting over."
            actualOffset = 0L
        }
        final StreamWriter writer = new StreamWriter(monitor, session)
        writer.open()
        try {
            final String streamId = xmppSession.createStream(userId, session)
            writer.write(streamId, stream, size, actualOffset)
            return streamId
        } finally {
            writer.close()
        }
    }
}

/**
 * <b>Title:</b>Deploy Helper Monitor<br>
 * <b>Description:</b>A deployment progress monitor.<br>
 */
class DeployStreamMonitor implements StreamMonitor {
    DeployHelper helper

    StreamSession session

    InputStream stream

    Long size

    Long recoverChunkOffset = 0;

    Long totalChunks = 0;

    public void chunkReceived(final int chunkSize) {}

    public void chunkSent(final int chunkSize) {
        totalChunks += chunkSize;
        println "Uploaded ${totalChunks}/${size}."
    }

    public void headerReceived(final String header) {}

    public void headerSent(final String header) {}

    public void streamError(final StreamException error) {
        if (error.isRecoverable()) {
            if (recoverChunkOffset <= totalChunks) {
                // attempt to resume the upload
                recoverChunkOffset = totalChunks;
                try {
                    helper.upload(this, session, stream, size, Long.valueOf(recoverChunkOffset));
                } catch (final IOException iox) {
                    throw new RuntimeException(iox);
                }
            } else {
                throw error;
            }
        } else {
            throw error;
        }
    }
}
