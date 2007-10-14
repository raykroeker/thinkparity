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

class PackageWarTask {
    AntBuilder ant
    Map properties

    /**
     * Execute package task.
     *
     */
    void execute() {
        def configuration = new ConfigurationHelper(properties:properties).extract()

        def baseDir = configuration["ant.base-dir"]
        def classesDir = configuration["thinkparity.target.classes-dir"]
        def packageDir = configuration["thinkparity.target.package-dir"]
        def webappDir = configuration["thinkparity.webapp-dir"]

        ant.sequential {
            def warClassesDir = new File(packageDir,"classes")
            def warLibDir = new File(packageDir,"lib")
            mkdir(dir:warClassesDir)
            mkdir(dir:warLibDir)

            // copy classes
            copy(todir:warClassesDir) {
                fileset(dir:classesDir)
            }
            // copy libs
            copy(todir:warLibDir) {
                fileset(refid:"run.dependencies-java")
                mapper(type:"flatten")
            }
            // delete container libs
            delete {
                fileset(dir:warLibDir) {
                    include(name:"servlet.zip")
                    include(name:"commons-logging.jar")
                    include(name:"log4j.jar")
                    include(name:"mail.jar")
                }
            }
            // jar
            jar(jarfile:new File(warLibDir,"tps-codebase.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/codebase/")
                    include(name:"carol.properties")
                }
            }
            jar(jarfile:new File(warLibDir,"tps-model.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/desdemona/")
                    include(name:"com/thinkparity/ophelia/")
                    include(name:"database/")
                    include(name:"localization/")
                    include(name:"security/")
                    include(name:"xfire/")
                    include(name:"xml/")
                    include(name:"version.properties")
                }
            }
            // war
            def warFile = new File(packageDir,configuration["thinkparity.war-name"])
            def webxmlFile = new File(webappDir,"WEB-INF/web.xml")
            delete(file:warFile)
            war(destfile:warFile,duplicate:"fail",update:"true",whenempty:"fail",webxml:webxmlFile) {
                classes(dir:warClassesDir)
                lib(dir:warLibDir)
            }
            // clean-up
            delete(includeemptydirs:"true") {
                fileset(dir:packageDir) {
                    exclude(name:warFile.getName())
                }
            }
            // checksum
            checksum(file:warFile,algorithm:"MD5")
            checksum(file:warFile,algorithm:"SHA")
        }
    }
}

// execute package
new PackageWarTask(ant:new AntBuilder(project),properties:properties).execute()
