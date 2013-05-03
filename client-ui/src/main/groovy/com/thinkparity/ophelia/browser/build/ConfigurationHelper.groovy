/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.ophelia.browser.build

import java.nio.ByteBuffer

import com.thinkparity.codebase.DateUtil
import com.thinkparity.codebase.OS
import com.thinkparity.codebase.OSUtil
import com.thinkparity.codebase.Platform
import com.thinkparity.codebase.jabber.JabberId
import com.thinkparity.codebase.jabber.JabberIdBuilder

import com.thinkparity.codebase.model.session.Credentials
import com.thinkparity.codebase.model.session.Environment

import com.thinkparity.service.MigratorService
import com.thinkparity.service.SessionService
import com.thinkparity.service.StreamService
import com.thinkparity.service.client.ClientServiceFactory

/**
 * <b>Title:</b>thinkParity OpheliaUI Build Task Configuration Helper<br>
 * <b>Description:</b>A groovy configuration build task configuration helper
 * parses a properties map and pulls out build <i>meaninful</i> build
 * configuration into a map to be consumed by the various helpers and tasks.<br>
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ConfigurationHelper {

    /** The ant build property <code>Map</code>. */
    Map properties

    /** An initialization <code>Boolean</code> flag. */
    Boolean initialized

    /**
     * Initialize the configuration helper.  Set the mode system property as
     * well as the current thread's context class loader.
     *
     */
    void init() {
        if (!initialized) {
            System.setProperty("thinkparity.environment", properties["thinkparity.environment"])
            System.setProperty("thinkparity.mode", properties["thinkparity.mode"])
            System.setProperty("thinkparity.product-name", extractProductName())
            System.setProperty("thinkparity.release-name", extractReleaseName())
            Thread.currentThread().setContextClassLoader(getClass().getClassLoader())
            initialized = Boolean.TRUE
        }
    }

    /**
     * Extract the configuration.
     *
     * @return A configuration <code>Map</code>.
     */
    Map extract() {
        init()

        def configuration = [:]

        configuration["ant.base-dir"] = extractAntBasedir()

        configuration["thinkparity.buffer"] = extractBuffer()
        configuration["thinkparity.buffer-array"] = extractBufferArray()
        configuration["thinkparity.charset-name"] = extractCharsetName()
        configuration["thinkparity.credentials"] = extractCredentials()
        configuration["thinkparity.environment"] = extractEnvironment()
        configuration["thinkparity.image-dirname"] = extractImageDirname()
        configuration["thinkparity.image.core-dirname"] = extractImageCoreDirname()
        configuration["thinkparity.image.lib-dirname"] = extractImageLibDirname()
        configuration["thinkparity.image.lib.native-dirname"] = extractImageLibNativeDirname()
        configuration["thinkparity.jre-dir"] = extractJreDir()
        configuration["thinkparity.userid"] = extractUserId()
        configuration["thinkparity.now"] = extractNow()
        configuration["thinkparity.os"] = extractOs()
        configuration["thinkparity.os-platform"] = extractOsPlatform()
        configuration["thinkparity.product-name"] = extractProductName()
        configuration["thinkparity.release-name"] = extractReleaseName()
        configuration["thinkparity.service-migrator"] = extractMigratorService()
        configuration["thinkparity.service-session"] = extractSessionService()
        configuration["thinkparity.service-stream"] = extractStreamService()
        configuration["thinkparity.target.classes-dir"] = extractTargetClassesDir()
        configuration["thinkparity.target.native-dir"] = extractTargetNativeDir()
        configuration["thinkparity.target.package-dir"] = extractTargetPackageDir()
        configuration["thinkparity.target.package.image-dir"] = extractTargetPackageImageDir()
        configuration["thinkparity.target.package.image.archive-file"] = extractTargetPackageImageArchiveFile()
        configuration["thinkparity.target.package.image.bin-dir"] = extractTargetPackageImageBinDir()
        configuration["thinkparity.target.package.image.core-dir"] = extractTargetPackageImageCoreDir()
        configuration["thinkparity.target.package.image.lib-dir"] = extractTargetPackageImageLibDir()
        configuration["thinkparity.target.package.image.lib.native-dir"] = extractTargetPackageImageLibNativeDir()
        configuration["thinkparity.target.package.jre-dir"] = extractTargetPackageImageJreDir()
        configuration["thinkparity.target.workspace-dir"] = extractTargetWorkspaceDir()

        return configuration
    }

    MigratorService extractMigratorService() {
        return ClientServiceFactory.getInstance().getMigratorService()
    }

    StreamService extractStreamService() {
        return ClientServiceFactory.getInstance().getStreamService()
    }

    SessionService extractSessionService() {
        return ClientServiceFactory.getInstance().getSessionService()
    }

    /**
     * Extract the ant base directory.
     *
     * @return The base directory <code>File</code>.
     */
    File extractAntBasedir() {
        return new File(properties["basedir"])
    }

    /**
     * Extract the buffer.
     *
     * @return A <code>ByteBuffer</code>.
     */
    ByteBuffer extractBuffer() {
        // BUFFER - 2MB - ConfigurationHelper#extractBuffer()
        return ByteBuffer.allocate(1024 * 1024 * 2)
    }

    /**
     * Extract the buffer.
     *
     * @return A <code>ByteBuffer</code>.
     */
    byte[] extractBufferArray() {
        // BUFFER - 2MB - ConfigurationHelper#extractBuffer()
        return new byte[1024 * 1024 * 2]
    }

    /**
     * Extract the character set name.
     *
     * @return A characterset name <code>String</code>.
     */
    String extractCharsetName() {
        return "ISO-8859-1"
    }

    /**
     * Extract the credentials.
     *
     * @return A set of <code>Credentials</code>.
     */
    Credentials extractCredentials() {
        def credentials = new Credentials()
        credentials.setUsername("thinkparity")
        credentials.setPassword("pr3t0r1a")
        return credentials
    }

    /**
     * Extract the environment.  Look at the
     * <code>"thinkparity.environment</code> property.
     *
     * @return An instance of <code>Environment</code>.
     */
    Environment extractEnvironment() {
        def environment = Environment.valueOf(properties["thinkparity.environment"])
        return environment
    }

    /**
     * Extract the image core directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageBinDirname() {
        return "bin"
    }

    /**
     * Extract the image core directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageCoreDirname() {
        return "core"
    }

    /**
     * Extract the image directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageDirname() {
        return extractReleaseName()
    }

    /**
     * Extract the image lib directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageLibDirname() {
        return "lib"
    }

    /**
     * Extract the jre directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageJreDirname() {
        return "jre1.6.0_01"
    }

    /**
     * Extract the image lib native directory name.
     *
     * @return The directory name <code>String</code>.
     */
    String extractImageLibNativeDirname() {
        return extractOsPlatform().name().toLowerCase()
    }

    /**
     * The date/time of the task.
     *
     * @return A <code>Calendar</code>.
     */
    Calendar extractNow() {
        return DateUtil.getInstance()
    }

    /**
     * Extract the operating system.  Look at the
     * <code>"thinkparity.os"</code> property.
     *
     * @return The <code>OS</code>.
     */
    OS extractOs() {
        return OSUtil.getOs(properties["thinkparity.os"])
    }

    /**
     * Extract the platform.  Look at the OS property.
     *
     * @return A platform <code>String</code>.
     */
    Platform extractOsPlatform() {
        return extractOs().getPlatform()
    }

    /**
     * Extract the classes directory.
     *
     * @return A classes directory <code>File</code>.
     */
    File extractTargetClassesDir() {
        return new File(properties["thinkparity.target.classes-dir"])
    }

    /**
     * Extract the classes directory.
     *
     * @return A classes directory <code>File</code>.
     */
    File extractTargetNativeDir() {
        return new File(properties["thinkparity.target.native-dir"])
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageDir() {
        return new File(properties["thinkparity.target.package-dir"])
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageImageDir() {
        return new File(extractTargetPackageDir(), extractImageDirname())
    }

    /**
     * Extract the image archive file.
     *
     * @return An image archive <code>File</code>.
     */
    File extractTargetPackageImageArchiveFile() {
        return new File(extractTargetPackageDir(), "${extractReleaseName()}.zip")
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageImageBinDir() {
        return new File(extractTargetPackageImageDir(), extractImageBinDirname())
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageImageCoreDir() {
        return new File(extractTargetPackageImageDir(), extractImageCoreDirname())
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageImageLibDir() {
        return new File(extractTargetPackageImageDir(), extractImageLibDirname())
    }

    /**
     * Extract the package directory.
     *
     * @return A package directory <code>File</code>.
     */
    File extractTargetPackageImageLibNativeDir() {
        return new File(extractTargetPackageImageLibDir(), extractImageLibNativeDirname())
    }

    /**
     * Extract the jre directory.
     *
     * @return A package jre directory <code>File</code>.
     */
    File extractTargetPackageImageJreDir() {
        return new File(extractTargetPackageDir(), extractImageJreDirname())
    }

    /**
     * Extract the product name.  Look at the
     * <code>"thinkparity.product-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractProductName() {
        return properties["thinkparity.product-name"]
    }

    /**
     * Extract the release name.  Look at the
     * <code>"thinkparity.release-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractReleaseName() {
        return properties["thinkparity.release-name"]
    }

    /**
     * Build the user id from the credentials and environment.
     *
     * @return A user id <code>JabberId</code>.
     */
    JabberId extractUserId() {
        def credentials = extractCredentials()
        def environment = extractEnvironment()
        return JabberIdBuilder.build(credentials.getUsername(), "thinkparity.net")
    }

    /**
     * Extract the workspace directory.  Look at the
     * <code>"thinkparity.target.workspace-dir"</code> property.
     *
     * @return A workspace directory <code>File</code>.
     */
    File extractTargetWorkspaceDir() {
        return new File(properties["thinkparity.target.workspace-dir"])
    }
 
    /**
     * Extract the jre directory.  Look at the
     * <code>"thinkparity.jre-dir"</code> property.
     *
     * @return A jre directory <code>File</code>.
     */
    File extractJreDir() {
        // HACK
        return new File("${properties["antx.vendor-dir"]}/i686/${properties["thinkparity.os-platform"].toLowerCase()}/sun.com/jre1.6.0_01")
    }
}