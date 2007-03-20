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
 * The package task creates a wildfire plugin for the model.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */

class PackageTask {
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

        ant.sequential {
            mkdir(dir:packageDir)

            copy(todir:packageDir) {
                fileset(dir:baseDir) {
                    include(name:"README.txt")
                    include(name:"LICENSE.txt")
                }
                fileset(dir:classesDir) {
                    include(name:"changelog.html")
                    include(name:"icon_large.gif")
                    include(name:"icon_small.gif")
                    include(name:"plugin.xml")
                    include(name:"readme.html")
                }
                fileset(dir:classesDir) {
                    include(name:"wildfire-handlers.xml")
                }
            }

            def pluginClassesDir = new File(packageDir,"classes")
            mkdir(dir:pluginClassesDir)
            copy(todir:pluginClassesDir) {
                fileset(dir:classesDir) {
                    include(name:"log4j.properties")
                    include(name:"localization/")
                    include(name:"carol.properties")
                }
            }

            def pluginLibDir = new File(packageDir,"lib")
            mkdir(dir:pluginLibDir)
            copy(todir:pluginLibDir) {
                fileset(refid:"runtime.dependencies-java")
                mapper(type:"flatten")
            }

            jar(jarfile:new File(pluginLibDir,"codebase.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/codebase/")
                    include(name:"carol.properties")
                }
            }

            jar(jarfile:new File(pluginLibDir,"model.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/desdemona/")
                    include(name:"com/thinkparity/ophelia/")
                    include(name:"carol.properties")
                }
            }

            def pluginFilename = configuration["thinkparity.plugin-name"]
            def pluginFile = new File(packageDir,pluginFilename)
            ant.delete(file:pluginFile)
            jar(jarfile:pluginFile,duplicate:"fail",update:"true",whenempty:"fail") {
                fileset(dir:packageDir)
            }
        }
    }
}

// execute package
new PackageTask(ant:new AntBuilder(project),properties:properties).execute()
