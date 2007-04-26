/*
 * Created On: 2007-01-31 10:27:00 PST
 */
import com.thinkparity.antx.Dependency
import com.thinkparity.antx.DependencyTracker
import com.thinkparity.codebase.FileUtil
import com.thinkparity.ophelia.browser.build.ConfigurationHelper

/**
 * <b>Title:</b>thinkParity Browser Package Task<br>
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
 * The package task creates a package for the browser including a native launcher.
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
        def packageJreDir = configuration["thinkparity.target.package.jre-dir"]
        def jreDir = configuration["thinkparity.jre-dir"]

        ant.sequential {
            mkdir(dir:packageDir)
            mkdir(dir:packageJreDir)

            // $/thinkParity.jar
            jar(destfile:new File(packageDir,"thinkParity.jar"),duplicate:"fail",update:"true",whenempty:"fail") {
                manifest {
                    attribute(name:"Main-Class",value:"com.thinkparity.ThinkParity")
                }
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/")
                    exclude(name:"com/thinkparity/codebase/")
                    exclude(name:"com/thinkparity/ophelia/")
                }
                fileset(dir:classesDir) {
                    include(name:"com/thinkparity/codebase/assertion/")
                    include(name:"com/thinkparity/codebase/Enum*")
                    include(name:"com/thinkparity/codebase/JVMUtil*")
                    include(name:"com/thinkparity/codebase/OS*")
                    include(name:"com/thinkparity/codebase/OSUtil*")
                    include(name:"com/thinkparity/codebase/Platform*")
                    include(name:"com/thinkparity/codebase/StringUtil*")
                    include(name:"com/thinkparity/codebase/SystemUtil*")
                }
            }
            // $/LICENSE.txt
            // $/README.txt
            copy(todir:packageDir) {
                fileset(dir:baseDir) {
                    include(name:"LICENSE.txt")
                    include(name:"README.txt")
                }
            }
            // /jre/*
            copy(todir:packageJreDir) {
                fileset(dir:jreDir)
            }
        }

        // $/thinkParity.properties
        def dependencies = new DependencyTracker().getDependencies(Dependency.Scope.RUNTIME)
        def properties = new File(packageDir,"thinkParity.properties")
        ant.delete(file:properties)
        properties.withWriterAppend(configuration["thinkparity.charset-name"], { writer ->
            writer.write("# thinkParity")
            // thinkparity.image
            newLine(writer)
            writer.write("thinkparity.image:${configuration["thinkparity.image-dirname"]}")
            newLine(writer)
        })
    }

    void newLine(Writer writer) {
        writer.write(properties["line.separator"])
    }
}

// execute package
new PackageTask(ant:new AntBuilder(project),properties:properties).execute()
