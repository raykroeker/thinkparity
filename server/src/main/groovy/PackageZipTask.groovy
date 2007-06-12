/*
 * Created On: 2007-01-31 10:27:00 PST
 */
import com.thinkparity.antx.Dependency
import com.thinkparity.antx.DependencyTracker
import com.thinkparity.codebase.FileUtil
import com.thinkparity.desdemona.model.build.ConfigurationHelper

/**
 * <b>Title:</b>thinkParity DesdemonaModel Package Task<br>
 * <b>Description:</b>A build task to be used from an ant build process.  The
 * expected usage is from within a groovy taskdef.  Using the taskdef
 * provides:<ul>
 * <li>An instance of AntBuilder that knows about the current project.
 * <li>An instance of the current ant project.
 * <li>An instance of the ant properties.
 * <li>The owning target that invoked the script.
 * <li>The wrapping task.
 * {@link http://groovy.codehaus.org/Groovy+Ant+Task Groovy Ant Task}
 *
 * The package war task creates a war archive.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */

class PackageZipTask {
    AntBuilder ant
    Map properties

    /**
     * Execute package task.
     *
     */
    void execute() {
        def configuration = new ConfigurationHelper(properties:properties).extract()

        def packageDir = configuration["thinkparity.target.package-dir"]

        ant.sequential {
            // zip
            def warFile = new File(packageDir,configuration["thinkparity.war-name"])
            def zipFile = new File(packageDir,configuration["thinkparity.zip-name"])
            delete(file:zipFile)
            zip(destfile:zipFile,duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:packageDir) {
                    include(name:configuration["thinkparity.war-name"])
                    include(name:"${configuration["thinkparity.war-name"]}.SHA")
                    include(name:"${configuration["thinkparity.war-name"]}.MD5")
                }
            }
            // checksum
            checksum(file:zipFile,algorithm:"MD5")
            checksum(file:zipFile,algorithm:"SHA")
        }
    }
}

// execute package
new PackageZipTask(ant:new AntBuilder(project),properties:properties).execute()
