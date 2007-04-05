/*
 * Created On:  Tue Oct 17, 2006 16:17 
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.ModelFactory;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.session.OfflineException;
import com.thinkparity.ophelia.model.user.UserUtils;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * A builder class attached to the scripting envrionment used
 * by the scripts.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerBuilder {

    /** A publish monitor. */
    private static final ProcessMonitor PUBLISH_MONITOR;

    static {
        PUBLISH_MONITOR = new ProcessAdapter() {
            @Override
            public void determineSteps(final Integer stages) {}
            @Override
            public void beginProcess() {}
            @Override
            public void endProcess() {}
            @Override
            public void beginStep(final Step step, final Object data) {}
            @Override
            public void endStep(final Step step) {}
        };
    }

    /** A container id. */
    private Long id;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A thinkParity <code>ModelFactory</code>. */
    private final ModelFactory modelFactory;

    /** A container name. */
    private String name;

    /** A thinkParity <code>ScriptUtil</code>. */
    private final ScriptUtil scriptUtil;

    /**
     * Create ContainerBuilder.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>
     * @param scenario
     *            A thinkParity <code>Scenario</code>.
     */
    ContainerBuilder(final Environment environment, final Workspace workspace,
            final ScriptUtil scriptUtil) {
        super();
        this.logger = new Log4JWrapper();
        this.modelFactory = ModelFactory.getInstance(environment, workspace);
        this.scriptUtil = scriptUtil;
    }

    /**
     * Add a document to a container.
     * 
     * @param name
     *            A document name <code>String</code>.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder addDocument(final String name) {
        logger.logApiId();
        logger.logVariable("name", name);
        final InputStream stream = scriptUtil.openResource(name);
        final Document document;
        try {
            document = getDocumentModel().create(name, stream);
        } finally {
            try {
                stream.close();
            } catch (final Throwable t) {
                throw panic(t, "Cannot add document {0}.", name);
            }
        }
        getContainerModel().addDocument(id, document.getId());
        return this;
    }

    /**
     * Create a container.
     * 
     * @param name
     *            The container name.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder create(final String name) {
        logger.logApiId();
        logger.logVariable("name", name);
        this.id = getContainerModel().create(name).getId();
        this.name = name;
        return this;
    }

    /**
     * Create a draft.
     *
     */
    public ContainerBuilder createDraft() {
        logger.logApiId();
        getContainerModel().createDraft(id);
        return this;
    }

    /**
     * Locate a container.
     * 
     * @param name
     *            A container name.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder find(final String name) {
        logger.logApiId();
        logger.logVariable("name", name);
        final List<Long> containers = getContainerModel().search(name);
        this.id = 0 == containers.size() ? null : containers.get(0);
        this.name = name;
        return this;
    }

    /**
     * Add a document to a container.
     * 
     * @param name
     *            A document name <code>String</code>.
     * @param modifiedName
     *            A document modified name <code>String</code>.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder modifyDocument(final String name,
            final String modifiedName) {
        logger.logApiId();
        logger.logVariable("name", name);
        logger.logVariable("modifiedName", modifiedName);
        final Document document = findDocument(name);
        final InputStream stream = scriptUtil.openResource(modifiedName);
        try {
            getDocumentModel().updateDraft(document.getId(), stream);
        } catch (final CannotLockException clx) {
            throw panic(clx, "Cannot lock document {0}", name);
        } finally {
            try {
                stream.close();
            } catch (final Throwable t) {
                throw panic(t, "Cannot modify document {0}.", name);
            }
        }
        return this;
    }

    /**
     * Publish a container to the exsiting team.
     * 
     * @param comment
     *            A publish comment <code>String</code>.
     */
    public ContainerBuilder publish(final String comment) {
        logger.logApiId();
        logger.logVariable("comment", comment);
        final ContainerModel containerModel = getContainerModel();
        final List<EMail> emails = Collections.emptyList();
        final List<Contact> contacts = Collections.emptyList();
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        try {
            containerModel.publish(PUBLISH_MONITOR, id, comment, emails,
                    contacts, teamMembers);
        } catch (final OfflineException ox) {
            throw panic(ox, "User is not online.");
        } catch (final CannotLockException clx) {
            throw panic(clx, "Cannot lock container {0}.", id);
        }
        return this;
    }

    /**
     * Publish a container.
     * 
     * @param comment
     *            A publish comment <code>String</code>.
     * @param names
     *            A list of user's names to publsh to.
     */
    public ContainerBuilder publish(final String comment, final String... names) {
        logger.logApiId();
        logger.logVariable("comment", comment);
        logger.logVariable("names", names);
        final ContactModel contactModel = getContactModel();
        final ContainerModel containerModel = getContainerModel();
        final List<EMail> emails = Collections.emptyList();
        final List<Contact> contacts = contactModel.read();
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        filter(teamMembers, names);
        filter(contacts, names);
        try {
            containerModel.publish(PUBLISH_MONITOR, id, comment, emails,
                    contacts, teamMembers);
        } catch (final OfflineException ox) {
            throw panic(ox, "User is not online.");
        } catch (final CannotLockException clx) {
            throw panic(clx, "Cannot lock container {0}.", id);
        }
        return this;
    }

    /**
     * Remove a document from a container.
     * 
     * @param names
     *            A document resource name <code>String</code>.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder removeDocument(final String name) {
        logger.logApiId();
        logger.logVariable("name", name);
        final Document document = findDocument(name);
        try {
            getContainerModel().removeDocument(id, document.getId());
        } catch (final CannotLockException clx) {
            throw panic(clx, "Cannot lock document {0}", name);
        }
        return this;
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append("id=").append(id)
            .append(",name=").append(name)
            .toString();
    }

    /**
     * Filter a list of users by name; as well as for the profile user.
     * 
     * @param <U>
     *            A user type.
     * @param users
     *            A list of users.
     * @param names
     *            A list of user's names.
     * @return A filtered list of users.
     */
    private <U extends User> void filter(final List<U> users,
            final String... names) {
        UserUtils.getInstance().filter(users, names);
        final Profile profile = modelFactory.getProfileModel().read();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(profile.getId())) {
                users.remove(i);
                break;
            }
        }
    }

    /**
     * Find a document.
     * 
     * @param name
     *            A document name.
     * @return A document.
     */
    private Document findDocument(final String name) {
        final ContainerDraft draft = getContainerModel().readDraft(id);
        for (final Document document : draft.getDocuments()) {
            if (document.getName().equals(name)) {
                return document;
            }
        }
        return null;
    }

    /**
     * Obtain the contact model interface.
     * 
     * @return A <code>ContactModel</code>.
     */
    private final ContactModel getContactModel() {
        return modelFactory.getContactModel();
    }

    /**
     * Obtain the container model interface.
     * 
     * @return A <code>ContainerModel</code>.
     */
    private final ContainerModel getContainerModel() {
        return modelFactory.getContainerModel();
    }

    /**
     * Obtain the document model interface.
     * 
     * @return A <code>DocumentModel</code>.
     */
    private final DocumentModel getDocumentModel() {
        return modelFactory.getDocumentModel();
    }

    /**
     * Panic.  No option other than to create an error and throw it.
     * 
     * @param t
     *            An error <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    private RuntimeException panic(final Throwable t,
            final String errorPattern, final Object... errorArguments) {
        return new RuntimeException(
                MessageFormat.format(errorPattern, errorArguments), t);
    }
}
