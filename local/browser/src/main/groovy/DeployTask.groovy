/*
 * Created On: 2007-01-31 10:27:00 PST
 */
import com.thinkparity.ophelia.browser.build.ConfigurationHelper
import com.thinkparity.ophelia.browser.build.DeployHelper
import com.thinkparity.ophelia.browser.build.ProductBuilder
import com.thinkparity.ophelia.browser.build.ReleaseBuilder
import com.thinkparity.ophelia.browser.build.ResourceBuilder
import com.thinkparity.ophelia.browser.build.SessionHelper
import com.thinkparity.ophelia.browser.build.WorkspaceHelper

/**
 * <b>Title:</b>thinkParity Browser Deploy Task<br>
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
 * The deploy task uses the migrator interface within the model to deploy the
 * image package.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DeployTask {
    AntBuilder ant
    Map properties

    /**
     * Execute deploy.
     *
     */
    void execute() {
        def configuration = new ConfigurationHelper(properties:properties).extract()

        def sessionHelper = new SessionHelper(configuration:configuration)
        if (!sessionHelper.isLoggedIn())
            sessionHelper.login()
        try {
            def product = new ProductBuilder(configuration:configuration).create()
            def release = new ReleaseBuilder(configuration:configuration).create()
            def resources = new ResourceBuilder(configuration:configuration,product:product,release:release).create()
            new DeployHelper(configuration:configuration).deploy(product, release, resources)
        } catch (final Throwable t) {
            sessionHelper.logout()
            ant.fail(t.getMessage())
        }
    }
}

// execute deploy
new DeployTask(ant:new AntBuilder(project),properties:properties).execute()
