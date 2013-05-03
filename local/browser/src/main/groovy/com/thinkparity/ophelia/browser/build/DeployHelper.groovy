/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import com.thinkparity.codebase.BytesFormat
import com.thinkparity.codebase.TimeFormat
import com.thinkparity.codebase.codec.MD5Util
import com.thinkparity.codebase.jabber.JabberId
import com.thinkparity.codebase.nio.ChannelUtil

import com.thinkparity.codebase.model.migrator.Product
import com.thinkparity.codebase.model.migrator.Release
import com.thinkparity.codebase.model.migrator.Resource
import com.thinkparity.codebase.model.stream.StreamInfo
import com.thinkparity.codebase.model.stream.StreamSession
import com.thinkparity.stream.StreamMonitor
import com.thinkparity.stream.StreamWriter

import com.thinkparity.service.AuthToken
import com.thinkparity.service.MigratorService
import com.thinkparity.service.StreamService

import java.nio.channels.ReadableByteChannel

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Deploy Helper<br>
 * <b>Description:</b>The deploy helper uses the migrator interface within the
 * model to deploy an image of the ui's updatable code.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DeployHelper {

    /** An <code>AntBuilder</code>. */
    AntBuilder ant

    /** A build task configuration <code>Map</code>. */
    Map configuration

    /** The image directory <code>File</code>. */
    File imageDir

    /** A migrator web-service. */
    MigratorService migratorService

    /** A stream web-service. */
    StreamService streamService

    /** A web-service authentication token. */
    AuthToken authToken

    /**
     * Initialize the deploy helper.
     *
     */
    void init() {
        if (null == imageDir)
            imageDir = configuration["thinkparity.target.package.image-dir"]
        if (null == migratorService)
            migratorService = configuration["thinkparity.service-migrator"]
        if (null == streamService)
            streamService = configuration["thinkparity.service-stream"]
        if (null == authToken)
            authToken = configuration["thinkparity.auth-token"]
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
            imageArchiveFile.delete()
        println "Archiving release ${imageArchiveFile.getName()}"
        ant.zip(destfile:imageArchiveFile,basedir:imageDir,level:9)
        // deploy
        println "Uploading release ${imageArchiveFile.getName()}"
		def preUpload = System.currentTimeMillis()
        upload(product, release, imageArchiveFile)
        def timeFormat = new TimeFormat()
        println "Upload:  ${timeFormat.format(System.currentTimeMillis() - preUpload)}"
		println "Deploying release ${release.getName()}"
		def preDeploy = System.currentTimeMillis()
        migratorService.deploy(authToken, product, release, resources)
		println "Deploy:  ${timeFormat.format(System.currentTimeMillis() - preDeploy)}"
    }

    /**
     * Calculate a checksum for a file.
     *
     * @param file
     *      A <code>File</code>.
     * @return An MD5 checksum <code>String</code>.
     */
    String checksum(File file) {
        final ReadableByteChannel channel = ChannelUtil.openReadChannel(file);
        try {
            return MD5Util.md5Base64(channel, configuration["thinkparity.buffer-array"])
        } finally {
            channel.close();
        }
    }

    /**
     * Upload a release.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param release
     *            A <code>Release</code>.
     * @param file
     *            A <code>File</code>.
     * @throws FileNotFoundException
     * @throws IOException
     */
    void upload(Product product, Release release, File file) {
        final StreamInfo streamInfo = new StreamInfo()
        streamInfo.setMD5(checksum(file))
        streamInfo.setSize(Long.valueOf(file.length()))
        final StreamSession session = streamService.newUpstreamSession(
                authToken, streamInfo, product, release)
        final BytesFormat bytesFormat = new BytesFormat()
        final StreamWriter writer = new StreamWriter(new DeployStreamMonitor(),
            session);
        final InputStream stream = new FileInputStream(file);
        try {
            writer.write(stream, file.length());
        } finally {
            stream.close();
        }
    }
}

private class DeployStreamMonitor implements StreamMonitor {
    public void chunkReceived(final int chunkSize) {}
    public void chunkSent(final int chunkSize) {}
    public String getName() {
        return "DeployHelper#upload";
    }
    public void reset() {}
}
