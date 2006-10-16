/**
 * 
 */
package com.thinkparity.ophelia.model.script;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * A builder class attached to the scripting envrionment used
 * by the scripts.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerBuilder {

    /** A list of document ids. */
    private final List<Long> documentIds;

    /** A thinkParity <code>Environment</code>. */
    private final Environment environment;

    /** A container id. */
    private Long id;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A container name. */
    private String name;

    /** A thinkparity <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create ContainerBuilder.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     * @param workspace
     *            A thinkParity <code>Workspace</code>
     */
    ContainerBuilder(final Environment environment, final Workspace workspace) {
        super();
        this.documentIds = new ArrayList<Long>();
        this.environment = environment;
        this.logger = new Log4JWrapper();
        this.workspace = workspace;
    }

    /**
     * Add a document to a container.
     * 
     * @param names
     *            A document resource name <code>String</code>.
     * @return A reference to the <code>ContainerBuilder</code>.
     */
    public ContainerBuilder addDocument(final String name, final String resource) {
        logger.logApiId();
        logger.logVariable("name", name);
        final InputStream stream = openStream(resource);
        final Document document;
        try {
            document = getDocumentModel().create(name, stream);
        } finally {
            try {
                stream.close();
            } catch (final Throwable t) {
                throw translateError(t);
            }
        }
        getContainerModel().addDocument(id, document.getId());
        documentIds.add(document.getId());
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
    public void createDraft() {
        logger.logApiId();
        getContainerModel().createDraft(id);
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
        this.documentIds.clear();
        this.name = name;
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
    public void publish(final String comment, final String... names) {
        logger.logApiId();
        logger.logVariable("comment", comment);
        logger.logVariable("names", names);
        final ContactModel contactModel = getContactModel();
        final ContainerModel containerModel = getContainerModel();
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        final List<Contact> contacts = contactModel.read();
        final List<TeamMember> filteredTeamMembers = filter(teamMembers, names);
        final List<Contact> filteredContacts = filter(contacts, names);
        containerModel.publish(id, comment, filteredContacts, filteredTeamMembers);
    }

    /**
     * Publish a container to the exsiting team.
     * 
     * @param comment
     *            A publish comment <code>String</code>.
     */
    public void publish(final String comment) {
        logger.logApiId();
        logger.logVariable("comment", comment);
        final ContainerModel containerModel = getContainerModel();
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        final List<Contact> contacts = Collections.emptyList();
        containerModel.publish(id, comment, contacts, teamMembers);
    }

    /**
     * Filter a list of users by name.
     * 
     * @param <U>
     *            A user type.
     * @param users
     *            A list of users.
     * @param names
     *            A list of user's names.
     * @return A filtered list of users.
     */
    private <U extends User> List<U> filter(final List<U> users,
            final String... names) {
        final List<U> filteredUsers = new ArrayList<U>(users.size());
        for (final String name : names) {
            for (final U user : users) {
                if (user.getName().equals(name))
                    filteredUsers.add(user);
            }
        }
        return filteredUsers;
    }

    /**
     * Obtain the contact model interface.
     * 
     * @return A <code>ContactModel</code>.
     */
    private final ContactModel getContactModel() {
        return ContactModel.getModel(environment, workspace);
    }

    /**
     * Obtain the container model interface.
     * 
     * @return A <code>ContainerModel</code>.
     */
    private final ContainerModel getContainerModel() {
        return ContainerModel.getModel(environment, workspace);
    }

    /**
     * Obtain the document model interface.
     * 
     * @return A <code>DocumentModel</code>.
     */
    private final DocumentModel getDocumentModel() {
        return DocumentModel.getModel(environment, workspace);
    }

    /**
     * Open an input stream for the resource.
     * 
     * @param name
     *            A resource name <code>String</code>.
     * @return An <code>InputStream</code>.
     */
    private InputStream openStream(final String name) {
        return ResourceUtil.getInputStream(name);
    }

    /**
     * Translate an error.
     * 
     * @param t
     *            An error <code>Throwable</code>.
     * @return A <code>RuntimeException</code>.
     */
    private RuntimeException translateError(final Throwable t) {
        return new RuntimeException(t);
    }

    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append("id=").append(id)
            .append(",name=").append(name)
            .toString();
    }
}
