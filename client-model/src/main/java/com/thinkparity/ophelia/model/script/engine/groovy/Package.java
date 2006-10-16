/*
 * Created On: Oct 15, 2006 11:50:22 AM
 */
package com.thinkparity.ophelia.model.script.engine.groovy;

import com.thinkparity.codebase.model.container.Container;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Package extends ThinkParityGroovyObject {

    private Long id;

    private String name;

    /** Create Package. */
    public Package() {
        this(null);
    }

    public Package(final String name) {
        super();
        setName(name);
    }

    public void addDocuments(final String... names) {
        utils.addDocumentsToContainer(getId(), names);
    }

    public Package create() {
        final Container container = utils.createContainer(getName());
        setId(null == container ? null : container.getId());
        return this;
    }

    public Package create(final String name) {
        setName(name);
        return create();
    }

    public void createDraft() {
        utils.createContainerDraft(getId());
    }

    public Package find() {
        setId(utils.findContainer(name));
        setName(utils.readContainerName(getId()));
        return this;
    }

    public Package find(final String name) {
        setName(name);
        return find();
    }

    public void publish() {
        utils.publishContainer(getId(), null);
    }

    public void publish(final String... names) {
        utils.publishContainer(getId(), null, names);
    }

    /**
     * Obtain the id
     *
     * @return The Long.
     */
    private Long getId() {
        return id;
    }

    /**
     * Obtain the name
     *
     * @return The String.
     */
    private String getName() {
        return name;
    }

    /**
     * Set id.
     *
     * @param id The Long.
     */
    private void setId(Long id) {
        this.id = id;
    }

    /**
     * Set name.
     *
     * @param name The String.
     */
    private void setName(String name) {
        this.name = name;
    }
}
