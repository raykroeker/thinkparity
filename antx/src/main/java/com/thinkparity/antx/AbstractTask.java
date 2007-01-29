/*
 * Created On:  26-Jan-07 6:41:38 PM
 */
package com.thinkparity.antx;

import java.io.File;
import java.text.MessageFormat;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractTask extends Task {

    /**
     * Obtain a file property from the project.
     * 
     * @param project
     *            A <code>Project</code>.
     * @param propertyName
     *            A property name <code>String</code>.
     * @return A property value <code>File</code>.
     */
    protected static final File getFileProperty(final Project project,
            final String propertyName) {
        validateFileProperty(project, propertyName);
        return new File(getProperty(project, propertyName));
    }

    /**
     * Obtain a property from the project.
     * 
     * @param project
     *            A <code>Project</code>.
     * @param propertyName
     *            A property name <code>String</code>.
     * @return A property value <code>String</code>.
     */
    protected static final String getProperty(final Project project,
            final String propertyName) {
        validateProperty(project, propertyName);
        return project.getProperty(propertyName);
    }

    protected static final BuildException panic(final String message,
            final Object... messageArguments) {
        final String panicMessage = MessageFormat.format(message, messageArguments);
        return new BuildException(panicMessage);
    }

    protected static final BuildException panic(final Throwable cause,
            final String message, final Object... messageArguments) {
        final String panicMessage = MessageFormat.format(message, messageArguments);
        return new BuildException(panicMessage, cause);
    }

    /**
     * Validate that the property is set as a file.
     * 
     * @param project
     *            A <code>Project</code>.
     * @param propertyName
     *            A property name <code>String</code>.
     */
    protected static final void validateFileProperty(final Project project,
            final String propertyName) {
        validateProperty(project, propertyName);
    }

    /**
     * Validate that the property is set for the project.
     * 
     * @param project
     *            A <code>Project</code>.
     * @param propertyName
     *            A property name <code>String</code>.
     */
    protected static final void validateProperty(final Project project,
            final String propertyName) {
        final String value = project.getProperty(propertyName);
        if (null == value)
            throw panic("Property {0} does not exist.", propertyName);
    }

    /**
     * Create AbstractTask.
     *
     */
    public AbstractTask() {
        super();
    }

    /**
     * @see org.apache.tools.ant.Task#execute()
     *
     */
    @Override
    public void execute() throws BuildException {
        validate();
        doExecute();
    }

    /**
     * Debug the task state.
     *
     */
    protected abstract void debug();

    /**
     * Execute the task.
     *
     * @throws BuildException
     */
    protected abstract void doExecute() throws BuildException;

    /**
     * Validate the task.
     * 
     * @throws BuildException
     */
    protected abstract void validate() throws BuildException;

    /**
     * <b>Title:</b>Build Adapter<br>
     * <b>Description:</b>A simple class that implements the buidl listener so that various
     * build listeners need not implement all methods.<br>
     */
    protected static class BuildAdapter implements BuildListener {
        /**
         * @see org.apache.tools.ant.BuildListener#buildFinished(org.apache.tools.ant.BuildEvent)
         *
         */
        public void buildFinished(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#buildStarted(org.apache.tools.ant.BuildEvent)
         *
         */
        public void buildStarted(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#messageLogged(org.apache.tools.ant.BuildEvent)
         *
         */
        public void messageLogged(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#targetFinished(org.apache.tools.ant.BuildEvent)
         *
         */
        public void targetFinished(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#targetStarted(org.apache.tools.ant.BuildEvent)
         *
         */
        public void targetStarted(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#taskFinished(org.apache.tools.ant.BuildEvent)
         *
         */
        public void taskFinished(final BuildEvent event) {}
        /**
         * @see org.apache.tools.ant.BuildListener#taskStarted(org.apache.tools.ant.BuildEvent)
         *
         */
        public void taskStarted(final BuildEvent event) {}
    }

}
