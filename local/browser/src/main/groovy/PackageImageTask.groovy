/*
 * Created On: 2007-01-31 10:27:00 PST
 */
import com.thinkparity.antx.Dependency
import com.thinkparity.antx.DependencyTracker
import com.thinkparity.codebase.FileUtil
import com.thinkparity.ophelia.browser.build.ConfigurationHelper

/**
 * <b>Title:</b>thinkParity Browser Package Image Task<br>
 * <b>Description:</b>A deployment task to be used from the build process.  The
 * expected usage is from within a groovy taskdef.  Using the taskdef
 * provides:<ul>
 * <li>An instance of AntBuilder that knows about the current project.
 * <li>An instance of the current ant project.
 * <li>An instance of the ant properties.
 * <li>The owning target that invoked the script.
 * <li>The wrapping task.
 * {@link http://groovy.codehaus.org/Groovy+Ant+Task Groovy Ant Task}
 *
 * The package image task creates an execution image for the browser.  An image
 * represents a release of a product.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PackageImageTask {
    AntBuilder ant
    Map properties

    /**
     * Execute package image.
     *
     */
    void execute() {
        def configuration = new ConfigurationHelper(properties:properties).extract()
        
        def baseDir = configuration["ant.base-dir"]

        def classesDir = configuration["thinkparity.target.classes-dir"]
        def nativeDir = configuration["thinkparity.target.native-dir"]
        def imageDir = configuration["thinkparity.target.package.image-dir"]
        def imageCoreDir = configuration["thinkparity.target.package.image.core-dir"]
        def imageLibDir = configuration["thinkparity.target.package.image.lib-dir"]
        def imageLibNativeDir = configuration["thinkparity.target.package.image.lib.native-dir"]

        ant.sequential {
            mkdir(dir:imageDir)
            mkdir(dir:imageCoreDir)
            mkdir(dir:imageLibDir)
            mkdir(dir:imageLibNativeDir)

            // /LICENSE.txt
            // /README.txt
            copy(todir:imageDir) {
                fileset(dir:baseDir) {
                    include(name:"LICENSE.txt")
                    include(name:"README.txt")
                }
                // $/thinkParityImage.properties
                fileset(dir:classesDir) {
                    include(name:"thinkParityImage.properties")
                }
            }
            // /core/browser.jar
            jar(destfile:new File(imageCoreDir,"browser.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/ophelia/browser/")
                    // NOTE demo resources
                    include(name:"demo/")
                    include(name:"fonts/")
                    include(name:"images/")
                    include(name:"localization/")
                    include(name:"localization/Model_Messages*")
                    include(name:"log4j.properties")
                    include(name:"version.properties")
                }
            }
            // /core/codebase.jar
            jar(destfile:new File(imageCoreDir,"codebase.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/codebase/")
                    include(name:"carol.properties")
                }
            }
            // /core/model.jar
            jar(destfile:new File(imageCoreDir,"model.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/ophelia/model/")
                    include(name:"org/jivesoftware/smack/")
                    include(name:"database/")
                    include(name:"localization/Model_Messages*")
                    include(name:"security/*_client_*")
                    include(name:"xml/")
                }
            }
            // /lib/*.jar
            copy(todir:imageLibDir) {
                fileset(refid:"runtime.dependencies-java")
                mapper(type:"flatten")
            }
            // /lib/${native}/*
            copy(todir:imageLibNativeDir) {
                fileset(refid:"runtime.dependencies-native")
                mapper(type:"flatten")
            }
            // /lib/${native}/*
            copy(todir:imageLibNativeDir) {
                fileset(dir:nativeDir) {
                    include(name:"Win32WindowUtil.dll")
                    include(name:"Win32FirewallUtil.dll")
                }
                mapper(type:"flatten")
            }
        }
        // /thinkParityImage.properties
        def dependencies = new DependencyTracker().getDependencies(Dependency.Scope.RUNTIME)
        def imageProperties = new File(imageDir,"thinkParityImage.properties")
        ant.delete(file:imageProperties)
        imageProperties.withWriterAppend(configuration["thinkparity.charset-name"], { writer ->
            writer.write("# thinkParity Image")
            // thinkparity.image-classpath
            newLine(writer)
            writer.write("thinkparity.image-classpath:core/codebase.jar,core/model.jar,core/browser.jar")
            for (dependency in dependencies) {
                if (dependency.getType().equals(Dependency.Type.JAVA)) {
	                writer.write(",")
	                writer.write(imageLibDir.getName())
	                writer.write("/")
	                writer.write(dependency.getLocation().getName())
	            }
            }
            // thinkparity.image-jvmargs
            newLine(writer)
            writer.write("thinkparity.image-jvmargs:")
            writer.write("-Xms36m -Djava.net.useSystemProxies=true")
            // thinkparity.image-librarypath
            newLine(writer)
            writer.write("thinkparity.image-librarypath:")
            writer.write(imageLibDir.getName())
            writer.write("/")
            writer.write(imageLibNativeDir.getName())
            // thinkparity.image-main
            newLine(writer)
            writer.write("thinkparity.image-main:com.thinkparity.ophelia.browser.Browser")
            // thinkparity.product-name
            newLine(writer)
            writer.write("thinkparity.product-name:")
            writer.write(configuration["thinkparity.product-name"])
            // thinkparity.release-name
            newLine(writer)
            writer.write("thinkparity.release-name:")
            writer.write(configuration["thinkparity.release-name"])
            // thinkparity.release-os
            newLine(writer)
            writer.write("thinkparity.release-os:")
            writer.write(configuration["thinkparity.os"].name())
            newLine(writer)
        })
    }

    void newLine(Writer writer) {
        writer.write(properties["line.separator"])
    }
}

// execute package image
new PackageImageTask(ant:new AntBuilder(project),properties:properties).execute()