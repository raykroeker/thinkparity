/*
 * Created On: Oct 15, 2006 11:55:19 AM
 */
package com.thinkparity.ophelia.model.script.engine.groovy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.thinkparity.codebase.ResourceUtil;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.user.TeamMember;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ThinkParityGroovyUtils {

    private final ContactModel contactModel;

    private final ContainerModel containerModel;

    private final DocumentModel documentModel;

    /** Create ThinkParityGroovyUtils. */
    ThinkParityGroovyUtils() {
        super();
        this.contactModel = ContactModel.getModel(null, null);
        this.containerModel = ContainerModel.getModel(null, null);
        this.documentModel = DocumentModel.getModel(null, null);
    }

    void addDocumentsToContainer(final Long id, final String... names) {
        Document document;
        InputStream stream;
        for (final String name : names) {
            stream = openStream(name);
            try {
                document = documentModel.create(name, stream);
            } finally {
                try {
                    stream.close();
                } catch (final Throwable t) {
                    throw translateError(t);
                }
            }
            containerModel.addDocument(id, document.getId());
        }
    }

    Container createContainer(final String name) {
        return ContainerModel.getModel(null, null).create(name);
    }

    void createContainerDraft(final Long id) {
        ContainerModel.getModel(null, null).createDraft(id);
    }

    Long findContainer(final String name) {
        final List<Long> containers = ContainerModel.getModel(null, null).search(name);
        return 0 == containers.size() ? null : containers.get(0);
    }

    void publishContainer(final Long id, final String comment) {
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        final List<Contact> contacts = Collections.emptyList();
        containerModel.publish(id, comment, contacts, teamMembers);
    }

    void publishContainer(final Long id, final String comment,
            final String... names) {
        final List<TeamMember> teamMembers = containerModel.readTeam(id);
        final List<Contact> contacts = contactModel.read();
        final List<TeamMember> filteredTeamMembers = filter(teamMembers, names);
        final List<Contact> filteredContacts = filter(contacts, names);
        containerModel.publish(id, comment, filteredContacts, filteredTeamMembers);
    }

    String readContainerName(final Long id) {
        return ContainerModel.getModel(null, null).read(id).getName();
    }

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

    private InputStream openStream(final String name) {
        return ResourceUtil.getInputStream(name);
    }

    private RuntimeException translateError(final Throwable t) {
        return new RuntimeException(t);
    }
}
