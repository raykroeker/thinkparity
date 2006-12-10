package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate;
import com.thinkparity.codebase.model.contact.Contact;

public class ContactTabPanel extends DefaultTabPanel {

    private boolean expanded;

    private Contact contact;

    private final TabRenderer renderer;

    private final TabAnimator animator;

    public ContactTabPanel() {
        super();
        this.animator = new TabAnimator(this);
        this.renderer = new TabRenderer(this);
    }

    public Contact getContact() {
        return contact;
    }

    public void setPanelData(final Contact contact) {
        this.contact = contact;
    }

    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded.booleanValue();
    }

    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
    }

    public TabPanelPopupDelegate getPanelPopupDelegate() {
        return null;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded) {
            renderer.paintExpandedBackground(g, this);
            if (!westJList.isSelectionEmpty()) {
                final int selectionIndex = westJList.getSelectedIndex();
                renderer.paintExpandedBackgroundWest(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundCenter(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
                renderer.paintExpandedBackgroundEast(g, westJPanel.getWidth(), getHeight(), selectionIndex, this);
            }
        } else {
            renderer.paintBackground(g, getWidth(), getHeight());
        }
    }
}
