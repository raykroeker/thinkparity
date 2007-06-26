/*
 * Created On: 2007-01-31 10:27:00 PST
 */
package com.thinkparity.desdemona.model.build

import com.thinkparity.codebase.DateUtil
import com.thinkparity.codebase.OS
import com.thinkparity.codebase.OSUtil
import com.thinkparity.codebase.Platform
import com.thinkparity.codebase.jabber.JabberId
import com.thinkparity.codebase.jabber.JabberIdBuilder

import com.thinkparity.codebase.model.session.Environment

/**
 * <b>Title:</b>thinkParity DesdemonaModel Build Task Configuration Helper<br>
 * <b>Description:</b>A groovy configuration build task configuration helper
 * parses a properties map and pulls out build <i>meaningful</i> build
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

        configuration["thinkparity.charset-name"] = extractCharsetName()
        configuration["thinkparity.environment"] = extractEnvironment()
        configuration["thinkparity.now"] = extractNow()
        configuration["thinkparity.os"] = extractOs()
        configuration["thinkparity.os-platform"] = extractOsPlatform()
        configuration["thinkparity.plugin-name"] = extractPluginName()
        configuration["thinkparity.product-name"] = extractProductName()
        configuration["thinkparity.release-name"] = extractReleaseName()
        configuration["thinkparity.target.classes-dir"] = extractTargetClassesDir()
        configuration["thinkparity.target.package-dir"] = extractTargetPackageDir()
        configuration["thinkparity.war-name"] = extractWarName()
        configuration["thinkparity.webapp-dir"] = extractWebappDir()
        configuration["thinkparity.zip-name"] = extractZipName()

        return configuration
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
     * Extract the ant base directory.
     *
     * @return The base directory <code>File</code>.
     */
    File extractAntBasedir() {
        return new File(properties["basedir"])
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
     * Extract the web-app directory.
     *
     * @return A web-app directory <code>File</code>.
     */
    File extractWebappDir() {
        return new File(properties["thinkparity.webapp-dir"])
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
     * Extract the product name.  Look at the
     * <code>"thinkparity.product-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractProductName() {
        return properties["thinkparity.product-name"]
    }

    /**
     * Extract the plugin name.  Look at the
     * <code>"thinkparity.plugin-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractPluginName() {
        return properties["thinkparity.plugin-name"]
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
     * Extract the plugin name.  Look at the
     * <code>"thinkparity.plugin-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractWarName() {
        return properties["thinkparity.war-name"]
    }

    /**
     * Extract the plugin name.  Look at the
     * <code>"thinkparity.plugin-name"</code> property.
     *
     * @return A product name <code>String</code>.
     */
    String extractZipName() {
        return properties["thinkparity.zip-name"]
    }
}
