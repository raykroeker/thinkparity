/*
 * Created On: October 7, 2006, 10:25 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container.ContainerModel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.container.ContainerDraft.ArtifactState;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContainerVersionsPanel extends DefaultTabPanel {

    /** The <code>Container</code>. */
    private Container container;

    /** The version's <code>Document</code>s. */
    private final Map<ContainerVersion, List<Document>> documents;

    /** The <code>ContainerModel</code>. */
    private final ContainerModel model;

    /** The version's published by <code>User</code>. */
    private final Map<ContainerVersion, User> publishedBy;

    /** The version's <code>User</code> and <code>ArtifactReceipt</code>. */
    private final Map<ContainerVersion, Map<User, ArtifactReceipt>> users;

    /** The container's <code>ContainerVersion</code>s. */
    private final List<ContainerVersion> versions;

    /** The version's content list model. */
    private final DefaultListModel versionsContentModel;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList versionsJList;
    // End of variables declaration//GEN-END:variables

    /** The version's list model. */
    private final DefaultListModel versionsModel;

    /**
     * Create ContainerVersionsPanel
     *
     * @param model
     *      The <code>ContainerModel</code>.
     */
    public ContainerVersionsPanel(final ContainerModel model) {
        super();
        this.documents = new HashMap<ContainerVersion, List<Document>>();
        this.model = model;
        this.publishedBy = new HashMap<ContainerVersion, User>();
        this.users = new HashMap<ContainerVersion, Map<User, ArtifactReceipt>>();
        this.versions = new ArrayList<ContainerVersion>();
        this.versionsModel = new DefaultListModel();
        this.versionsContentModel = new DefaultListModel();
        initComponents();
    }

    /**
     * Add a container version. The container version includes a list of
     * documents; a list of users and their respective receipts; and the user
     * who published the container.
     * 
     * @param version
     *            A <code>ContainerVersion</code>.
     * @param documents
     *            A <code>List&lt;Document&gt;</code>.
     * @param users
     *            A <code>Map&lt;User, ArtifactReceipt&gt;</code>.
     * @param publishedBy
     *            A <code>User</code>.
     */
    public void add(final ContainerVersion version,
            final List<Document> documents,
            final Map<User, ArtifactReceipt> users, final User publishedBy) {
        this.versions.add(version);
        this.documents.put(version, documents);
        this.users.put(version, users);
        this.publishedBy.put(version, publishedBy);

        final List<VersionContentCell> contents =
            new ArrayList<VersionContentCell>(documents.size() + users.size());
        for (final Document document : documents) {
            contents.add(new VersionContentCell(version, document));
        }
        for (final Entry<User, ArtifactReceipt> entry : users.entrySet()) {
            contents.add(new VersionContentCell(entry.getKey(), entry.getValue()));
        }
        versionsModel.addElement(new VersionCell(contents, version, publishedBy));
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#getId()
     */
    @Override
    public Object getId() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(container.getId()).toString();
    }

    /**
     * Select the first version cell.
     *
     */
    public void selectFirstVersion() {
        if (0 < versionsModel.size()) {
            versionsJList.setSelectedIndex(0);
        }
    }

    /**
     * Set the container draft.
     * 
     * @param container
     *            The <code>Container</code>.
     * @param draft
     *            The <code>ContainerDraft</code>.
     */
    public void setDraft(final Container container, final ContainerDraft draft) {
        this.container = container;

        if (null != draft) {
            final List<VersionContentCell> contents =
                new ArrayList<VersionContentCell>(documents.size() + users.size());
            for (final Document document : draft.getDocuments()) {
                contents.add(new VersionContentCell(document, draft.getState(document)));
            }

            versionsModel.addElement(new VersionCell(contents, draft));
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#triggerPopup(java.awt.Component,
     *      java.awt.event.MouseEvent)
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        new ContainerPopup(model, container).show(invoker, e);
    }

    private void formMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_formMouseClicked
    }//GEN-LAST:event_formMouseClicked

    private void formMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_formMouseReleased
    }//GEN-LAST:event_formMouseReleased

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        javax.swing.JList versionsContentJList;
        javax.swing.JScrollPane versionsContentJScrollPane;
        javax.swing.JScrollPane versionsJScrollPane;

        versionsJScrollPane = new javax.swing.JScrollPane();
        versionsJList = new javax.swing.JList();
        versionsContentJScrollPane = new javax.swing.JScrollPane();
        versionsContentJList = new javax.swing.JList();

        setOpaque(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        versionsJScrollPane.setBorder(null);
        versionsJList.setModel(versionsModel);
        versionsJList.setCellRenderer(new VersionCellRenderer());
        versionsJList.setVisibleRowCount(5);
        versionsJList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                versionsJListValueChanged(evt);
            }
        });
        versionsJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsJListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsJListMouseReleased(evt);
            }
        });
        versionsJList.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                versionsJListMouseWheelMoved(evt);
            }
        });

        versionsJScrollPane.setViewportView(versionsJList);

        versionsContentJScrollPane.setBorder(null);
        versionsContentJList.setModel(versionsContentModel);
        versionsContentJList.setCellRenderer(new VersionContentCellRenderer());
        versionsContentJList.setVisibleRowCount(5);
        versionsContentJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseClicked(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                versionsContentJListMouseReleased(evt);
            }
        });
        versionsContentJList.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                versionsContentJListMouseWheelMoved(evt);
            }
        });

        versionsContentJScrollPane.setViewportView(versionsContentJList);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(versionsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 326, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(versionsContentJScrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(versionsContentJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(versionsJScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void versionsContentJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsContentJListMouseClicked
    }//GEN-LAST:event_versionsContentJListMouseClicked

    private void versionsContentJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsContentJListMouseReleased
    }//GEN-LAST:event_versionsContentJListMouseReleased

    private void versionsContentJListMouseWheelMoved(java.awt.event.MouseWheelEvent e) {//GEN-FIRST:event_versionsContentJListMouseWheelMoved
    }//GEN-LAST:event_versionsContentJListMouseWheelMoved

    private void versionsJListMouseClicked(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseClicked
    }//GEN-LAST:event_versionsJListMouseClicked

    private void versionsJListMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_versionsJListMouseReleased
    }//GEN-LAST:event_versionsJListMouseReleased
    
    private void versionsJListMouseWheelMoved(java.awt.event.MouseWheelEvent e) {//GEN-FIRST:event_versionsJListMouseWheelMoved
    }//GEN-LAST:event_versionsJListMouseWheelMoved

    private void versionsJListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_versionsJListValueChanged
        if (!evt.getValueIsAdjusting()) {
            logger.logVariable("evt", evt);
            versionsContentModel.clear();
            for (final Object selectedValue : versionsJList.getSelectedValues()) {
                for (final VersionContentCell contentCell : ((VersionCell) selectedValue).contents) {
                    versionsContentModel.addElement(contentCell);
                }
            }
        }
    }//GEN-LAST:event_versionsJListValueChanged

    /** The version list cell wrapper. */
    class VersionCell {

        /** The cell contents. */
        private final List<VersionContentCell> contents;

        /** The cell text <code>String</code>. */
        private final String text;

        /**
         * Create VersionCell.
         * 
         * @param draft
         *            The cell <code>ContainerDraft</code>.
         */
        private VersionCell(final List<VersionContentCell> contents,
                final ContainerDraft draft) {
            this.contents = contents;
            final Integer documentCount = draft.getDocumentCount();
            this.text = 1 == documentCount ?
                    "Draft - 1 Document" :
                    MessageFormat.format("Draft - {0} Documents",
                            documentCount);
        }

        /**
         * Create VersionCell.
         * 
         * @param version
         *            The cell <code>ContainerVersion</code>.
         */
        private VersionCell(final List<VersionContentCell> contents,
                final ContainerVersion version, final User publishedBy) {
            this.contents = contents;
            this.text = MessageFormat.format(
                    "Version - {0,date,MMM d, yyyy h:mm a} - {1}",
                    version.getCreatedOn().getTime(),
                    publishedBy.getName(), publishedBy.getTitle(),
                    publishedBy.getOrganization());
        }

        /**
         * Obtain the cell text.
         *
         * @return The cell text <code>String</code>.
         */
        String getText() {
            return text;
        }
    }

    /** The content list cell wrapper. */
    class VersionContentCell {

        /** The cell text <code>String</code>. */
        private final String text;

        /**
         * Create VersionContentCell.
         * 
         * @param document
         *            A <code>Document</code>.
         */
        private VersionContentCell(final ContainerVersion version,
                final Document document) {
            this.text = MessageFormat.format("{0}", document.getName());
        }

        /**
         * Create VersionContentCell.
         * 
         * @param document
         *            A <code>Document</code>.
         */
        private VersionContentCell(final Document document,
                final ArtifactState state) {
            final String formatPattern;
            switch (state) {
            case ADDED:
            case MODIFIED:
            case REMOVED:
                formatPattern = "{0} - {1}";
                break;
            case NONE:
                formatPattern = "{0}";
                break;
            default:
                throw Assert.createUnreachable("UNKNOWN DOCUMENT STATE");
            }
            this.text = MessageFormat.format(formatPattern,
                    document.getName(), state);
        }


        private VersionContentCell(final User user, final ArtifactReceipt receipt) {
            if (receipt.isSetReceivedOn()) {
                this.text = MessageFormat.format("{0} - {3,date,MMM dd, yyyy h:mm a}",
                        user.getName(), user.getOrganization(), user.getTitle(),
                        receipt.getReceivedOn().getTime());
            } else {
                this.text = MessageFormat.format("{0}",
                        user.getName(), user.getOrganization(), user.getTitle());
            }
        }

        /**
         * Obtain the cell text.
         *
         * @return The cell text <code>String</code>.
         */
        String getText() {
            return text;
        }
    }
}
