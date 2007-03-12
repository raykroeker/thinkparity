/*
 * Created On:  December 19, 2006, 9:01 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactTabPanel extends DefaultTabPanel {

    /** The contact tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** The panel's animating state. */
    private boolean animating;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel collapsedAdditionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedContactJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel collapsedIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedIncomingInvitationJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel collapsedTextJLabel = new javax.swing.JLabel();
    /** A <code>Contact</code>. */
    private Contact contact;
    private final javax.swing.JLabel contactAdditionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactAddressValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactEMailValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactFillterValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactLocationValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactMobilePhoneValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactPhoneValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactTextJLabel = new javax.swing.JLabel();
    /** The panel's expanded state. */
    private boolean expanded;
    private final javax.swing.JPanel expandedContactJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel expandedDataValuesJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    /** A contact <code>IncomingInvitation</code>. */
    private IncomingInvitation incoming;
    private final javax.swing.JLabel incomingInvitationAcceptJLabel = LabelFactory.createLink("",Fonts.DefaultFont);

    private final javax.swing.JLabel incomingInvitationAdditionalTextJLabel = new javax.swing.JLabel();

    private final javax.swing.JLabel incomingInvitationDeclineJLabel = LabelFactory.createLink("",Fonts.DefaultFont);

    private final javax.swing.JLabel incomingInvitationIconJLabel = new javax.swing.JLabel();

    private final javax.swing.JLabel incomingInvitationTertiaryTextJLabel = new javax.swing.JLabel();

    private final javax.swing.JLabel incomingInvitationTextJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** The inner <code>JPanel</code> <code>GridBagConstraints</code>. */
    private final GridBagConstraints innerJPanelConstraints;

    /** The invited by <code>User</code> for incoming invitations. */
    private User invitedBy;

    /** The panel localization. */
    private final MainCellL18n localization;

    /** A contact <code>OutgoingEMailInvitation</code>. */
    private OutgoingEMailInvitation outgoingEMail;
    
    /** A contact <code>OutgoingUserInvitation</code>. */
    private OutgoingUserInvitation outgoingUser;
    
    /** A contact tab <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

    /** A contact <code>Profile</code>. */
    private Profile profile;

    /**
     * Create ContactTabPanel.
     * 
     * @param session
     *            A <code>BrowserSession</code>.
     */
    public ContactTabPanel(final BrowserSession session) {
        super(session);
        this.innerJPanelConstraints = new GridBagConstraints();
        this.innerJPanelConstraints.fill = GridBagConstraints.BOTH;
        this.innerJPanelConstraints.weightx = this.innerJPanelConstraints.weighty = 1.0F;
        this.localization = new MainCellL18n("ContactTabPanel");
        initComponents();
    }

    /**
     * Apply connection status.
     */
    public void applyConnection(final Connection connection) {
        if (isSetIncoming()) {
            switch(connection) {
            case OFFLINE:
                incomingInvitationAcceptJLabel.setVisible(false);
                incomingInvitationDeclineJLabel.setVisible(false);
                break;
            case ONLINE:
                incomingInvitationAcceptJLabel.setVisible(true);
                incomingInvitationDeclineJLabel.setVisible(true);
                break;
            default:
                Assert.assertUnreachable("Unknown connection id.");
            }
        }
    }

    /**
     * Collapse the panel.
     *
     */
    public void collapse(final boolean animate) {
        doCollapse(animate);
    }

    /**
     * Expand the panel.
     *
     */
    public void expand(final boolean animate) {
        doExpand(animate);
    }

    /**
     * Obtain actionDelegate.
     *
     * @return An ActionDelegate.
     */
    public ActionDelegate getActionDelegate() {
        return actionDelegate;
    }

    /**
     * Obtain the contact panel data.
     * 
     * @return A <code>Contact</code>.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#getId()
     *
     */
    @Override
    public Object getId() {
        final StringBuffer id = new StringBuffer(getClass().getName())
            .append("//");
        if (isSetContact())
            id.append(contact.getId());
        else if (isSetIncoming())
            id.append(incoming.getId());
        else if (isSetOutgoingEMail())
            id.append(outgoingEMail.getId());
        else if (isSetOutgoingUser())
            id.append(outgoingUser.getId());
        else if (isSetProfile())
            id.append(profile.getId());
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
        return id.toString();
    }

    /**
     * Obtain the incoming invitation.
     * 
     * @return An <code>IncomingInvitation</code>.
     */
    public IncomingInvitation getIncoming() {
        return incoming;
    }
    
    /**
     * Obtain the invited-by user for an incoming invitation.
     * 
     * @return A <code>User</code>.
     */
    public User getInvitedBy() {
        return invitedBy;
    }

    /**
     * Obtain the outgoing e-mail invitation.
     * 
     * @return An <code>OutgoingEMailInvitation</code>.
     */
    public OutgoingEMailInvitation getOutgoingEMail() {
        return outgoingEMail;
    }

    /**
     * Obtain the outgoing user invitation.
     * 
     * @return An <code>OutgoingEMailInvitation</code>.
     */
    public OutgoingUserInvitation getOutgoingUser() {
        return outgoingUser;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#getPopupDelegate()
     *
     */
    public PopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Obtain the profile.
     * 
     * @return A <code>Profile</code>.
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * Determine if the panel is animating a collapse or expand operation.
     * 
     * @return True if the panel is in the process of expanding or collapsing.
     */
    public Boolean isAnimating() {
        return animating;
    }

    /**
     * Determine if the panel is expanded.
     * 
     * @return True if the panel is expanded.
     */
    public Boolean isExpanded() {
        return Boolean.valueOf(expanded);
    }

    /**
     * Determine if the contact is set.
     * 
     * @return True if the contact is set.
     */
    public Boolean isSetContact() {
        return null != contact;
    }

    /**
     * Determine if the incoming invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetIncoming() {
        return null != incoming;
    }

    /**
     * Determine if the outgoing e-mail invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetOutgoingEMail() {
        return null != outgoingEMail;
    }

    /**
     * Determine if the outgoing user invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetOutgoingUser() {
        return null != outgoingUser;
    }

    /**
     * Determine if the profile is set.
     * 
     * @return True if the profile is set.
     */
    public Boolean isSetProfile() {
        return null != profile;
    }

    /**
     * Set actionDelegate.
     *
     * @param actionDelegate
     *      A ContainerTabActionDelegate.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
    }

    /**
     * Makes the panel expanded or not.
     * 
     * @param expanded
     *            Whether to expand the panel.
     */
    public void setExpanded(final Boolean expanded) {
        if (expanded.booleanValue())
            doExpand(false);
        else
            doCollapse(false);
    }

    /**
     * Set the panel data.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @param locale
     *            The <code>Locale</code>.
     * @param availableLocales
     *            An array of available <code>Locale</code>s.
     */
    public void setPanelData(final Contact contact, final Locale locale,
            final Locale[] availableLocales) {
        this.contact = contact;
        initCollapsedPanel();
        
        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
        reload(collapsedTextJLabel, contact.getName());
        reload(collapsedAdditionalTextJLabel, getAdditionalText(contact));

        reload(contactTextJLabel, contact.getName());
        reload(contactAdditionalTextJLabel, getAdditionalText(contact));

        reload(contactAddressValueJLabel, contact.getAddress());
        reload(contactLocationValueJLabel,
                getLocationText(contact.getCity(), contact.getProvince(),
                        contact.getCountry(), contact.getPostalCode(),
                        locale, availableLocales));
        if (0 < contact.getEmailsSize())
            reload(contactEMailValueJLabel, contact.getEmails().get(0).toString());
        else
            reload(contactEMailValueJLabel, null);
        reload(contactMobilePhoneValueJLabel, contact.getMobilePhone());
        reload(contactPhoneValueJLabel, contact.getPhone());
    }

    /**
     * Set the panel data.
     * 
     * @param incomingInvitation
     *            An <code>IncomingInvitation</code>.
     * @param invitedBy
     *            The invited by <code>User</code>.
     */
    public void setPanelData(final IncomingInvitation incoming,
            final User invitedBy) {
        this.incoming = incoming;
        this.invitedBy = invitedBy;
        initCollapsedPanel();

        incomingInvitationIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        reload(incomingInvitationAdditionalTextJLabel, getAdditionalText(invitedBy));
        reload(incomingInvitationTertiaryTextJLabel, localization.getString("IncomingInvitationTertiaryText"));
        reload(incomingInvitationAcceptJLabel, localization.getString("IncomingInvitationAccept"));
        reload(incomingInvitationDeclineJLabel, localization.getString("IncomingInvitationDecline"));
        reload(incomingInvitationTextJLabel, invitedBy.getName());
    }

    /**
     * Set the panel data.
     * 
     * @param outgoing
     *            A <code>OutgoingInvitation</code>.
     */
    public void setPanelData(final OutgoingEMailInvitation outgoingEMail) {
        this.outgoingEMail = outgoingEMail;
        initCollapsedPanel();

        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        reload(collapsedAdditionalTextJLabel, null);
        reload(collapsedTextJLabel, outgoingEMail.getEmail().toString());
    }

    /**
     * Set the panel data.
     * 
     * @param outgoing
     *            A <code>OutgoingInvitation</code>.
     */
    public void setPanelData(final OutgoingUserInvitation outgoingUser) {
        this.outgoingUser = outgoingUser;
        initCollapsedPanel();

        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        reload(collapsedAdditionalTextJLabel, getAdditionalText(outgoingUser.getUser()));
        reload(collapsedTextJLabel, outgoingUser.getUser().getName());
    }

    /**
     * Set the panel data.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @param emails
     *            A list of <code>ProfileEMail</code>s.
     * @param locale
     *            The <code>Locale</code>.
     * @param availableLocales
     *            An array of available <code>Locale</code>s.
     */
    public void setPanelData(final Profile profile,
            final List<ProfileEMail> emails,
            final Locale locale, final Locale[] availableLocales) {
        this.profile = profile;
        initCollapsedPanel();

        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
        reload(collapsedTextJLabel, profile.getName());
        reload(collapsedAdditionalTextJLabel, getAdditionalText(profile));

        reload(contactTextJLabel, profile.getName());
        reload(contactAdditionalTextJLabel, getAdditionalText(profile));

        reload(contactAddressValueJLabel, profile.getAddress());
        reload(contactLocationValueJLabel,
                getLocationText(profile.getCity(), profile.getProvince(),
                        profile.getCountry(), profile.getPostalCode(),
                        locale, availableLocales));
        if (0 < emails.size())
            reload(contactEMailValueJLabel, emails.get(0).getEmail().toString());
        else
            reload(contactEMailValueJLabel, null);
        reload(contactMobilePhoneValueJLabel, profile.getMobilePhone());
        reload(contactPhoneValueJLabel, profile.getPhone());
    }

    /**
     * Set popupDelegate.
     *
     * @param popupDelegate
     *      A ContainerTabPopupDelegate.
     */
    public void setPopupDelegate(final PopupDelegate popupDelegate) {
        this.popupDelegate = popupDelegate;
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     *
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (expanded || animating) {
            renderer.paintExpandedBackground(g, this);
            final Point location = SwingUtilities.convertPoint(expandedDataValuesJPanel, new Point(0,0), this);
            renderer.paintExpandedBackgroundFields(g, location.x,
                    expandedDataValuesJPanel.getWidth(), this);
        } else {
            renderer.paintBackground(g, getWidth(), getHeight(), selected);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#repaintLists()
     *
     */
    @Override
    protected void repaintLists() {}
    
    private void collapsedContactJPanelMousePressed(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedContactJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedContactJPanelMousePressed

    private void collapsedContactJPanelMouseReleased(java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedContactJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedContactJPanelMouseReleased

    private void collapsedIncomingInvitationJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedIncomingInvitationJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedIncomingInvitationJPanelMousePressed

    private void collapsedIncomingInvitationJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedIncomingInvitationJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedIncomingInvitationJPanelMouseReleased

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        collapsedJPanel.removeAll();
        if (isSetIncoming())
            collapsedJPanel.add(collapsedIncomingInvitationJPanel, innerJPanelConstraints.clone());
        else 
            collapsedJPanel.add(collapsedContactJPanel, innerJPanelConstraints.clone());

        if (animate) {
            final Dimension expandedPreferredSize = getPreferredSize();
            expandedPreferredSize.height = ANIMATION_MAXIMUM_HEIGHT;
            setPreferredSize(expandedPreferredSize);
            animating = true;
            animator.collapse(ANIMATION_HEIGHT_ADJUSTMENT,
                    ANIMATION_MINIMUM_HEIGHT, new Runnable() {
                        public void run() {
                            animating = false;
                            expanded = false;

                            if (isAncestorOf(expandedJPanel))
                                remove(expandedJPanel);
                            if (isAncestorOf(collapsedJPanel))
                                remove(collapsedJPanel);
                            add(collapsedJPanel, constraints.clone());
                            
                            revalidate();
                            repaint();
                            firePropertyChange("expanded", !expanded, expanded);
                        }
            });
        } else {
            expanded = false;

            if (isAncestorOf(expandedJPanel))
                remove(expandedJPanel);
            if (isAncestorOf(collapsedJPanel))
                remove(collapsedJPanel);
            add(collapsedJPanel, constraints.clone());
            
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MINIMUM_HEIGHT;
            setPreferredSize(preferredSize);

            revalidate();
            repaint();
            firePropertyChange("expanded", !expanded, expanded);
        }
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        expandedJPanel.removeAll();
        if (isSetContact() || isSetProfile())
            expandedJPanel.add(expandedContactJPanel, innerJPanelConstraints.clone());
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");

        if (isAncestorOf(expandedJPanel))
            remove(expandedJPanel);
        if (isAncestorOf(collapsedJPanel))
            remove(collapsedJPanel);
        add(expandedJPanel, constraints.clone());

        if (animate) {
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MINIMUM_HEIGHT;
            setPreferredSize(preferredSize);
            animating = true;
            animator.expand(ANIMATION_HEIGHT_ADJUSTMENT,
                    ANIMATION_MAXIMUM_HEIGHT, new Runnable() {
                        public void run() {
                            expanded = true;
                            animating = false;

                            revalidate();
                            repaint();
                            firePropertyChange("expanded", !expanded, expanded);
                        }
            });
        } else {
            expanded = true;
            final Dimension preferredSize = getPreferredSize();
            preferredSize.height = ANIMATION_MAXIMUM_HEIGHT;
            setPreferredSize(preferredSize);

            revalidate();
            repaint();
            firePropertyChange("expanded", !expanded, expanded);
        }
    }

    private void expandedContactJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedContactJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedContactJPanelMousePressed

    private void expandedContactJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedContactJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedContactJPanelMouseReleased

    /**
     * Extract additional text from a user.
     * 
     * @param <T>
     *            A user type.
     * @param user
     *            A <code>T</code>.
     * @return Additional display text <code>String</code>.
     */
    private <T extends User> String getAdditionalText(final T user) {
        final String pattern = "({0}, {1})";
        final Object[] values = new Object[] { user.getTitle(), user.getOrganization() };
        return new MessageFormat(pattern).format(values);
    }

    /**
     * Get display text for a country, given an ISO3 country string
     * and Locale information.
     * 
     * @param country
     *            A country ISO3 <code>String</code>.
     * @param inLocale
     *            The <code>Locale</code>.
     * @param availableLocales
     *            An array of available <code>Locale</code>s.   
     * @return A country display <code>String</code>.
     */
    private String getCountryText(final String country, final Locale inLocale,
            final Locale[] availableLocales) {
        String countryText = null;
        for (final Locale locale : availableLocales) {
            if (locale.getISO3Country().equals(country)) {
                countryText = locale.getDisplayCountry(inLocale);
                break;
            }
        }

        return countryText;
    }

    /**
     * Prepare location text.
     * 
     * @param city
     *            A <code>String</code>.
     * @param province
     *            A <code>String</code>.
     * @param country
     *            A <code>String</code>.
     * @param postalCode
     *            A <code>String</code>.
     * @param locale
     *            The <code>Locale</code>.
     * @param availableLocales
     *            An array of available <code>Locale</code>s.   
     * @return The location <code>String</code>.
     */
    private String getLocationText(final String city, final String province,
            final String country, final String postalCode,
            final Locale locale, final Locale[] availableLocales) {
        final List<String> locations = new ArrayList<String>();
        if (null != city) {
            locations.add(city);
        }
        if (null != province) {
            locations.add(province);
        }
        if (null != country) {
            locations.add(getCountryText(country, locale, availableLocales));
        }
        if (null != postalCode) {
            locations.add(postalCode);
        }
        if (locations.size()>0) {
            final StringBuffer buffer = new StringBuffer();
            for (final String location : locations) {
                if (buffer.length()>0) {
                    buffer.append(", ");
                }
                buffer.append(location);
            }
            return buffer.toString();
        } else {
            return null;
        }
    }

    private void incomingInvitationAcceptJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_incomingInvitationAcceptJLabelMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            tabDelegate.selectPanel(this);
            actionDelegate.invokeForInvitation(getIncoming(), ActionDelegate.Action.ACCEPT);
        }
    }//GEN-LAST:event_incomingInvitationAcceptJLabelMousePressed

    private void incomingInvitationDeclineJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_incomingInvitationDeclineJLabelMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            tabDelegate.selectPanel(this);
            actionDelegate.invokeForInvitation(getIncoming(), ActionDelegate.Action.DECLINE);
        }
    }//GEN-LAST:event_incomingInvitationDeclineJLabelMousePressed

    /**
     * Initialize the collapsed panel.
     */
    private void initCollapsedPanel() {
        collapsedJPanel.removeAll();
        if (isSetIncoming()) {
            collapsedJPanel.add(collapsedIncomingInvitationJPanel, innerJPanelConstraints.clone());
        } else {
            collapsedJPanel.add(collapsedContactJPanel, innerJPanelConstraints.clone());
        }
        if (isAncestorOf(collapsedJPanel)) {
            remove(collapsedJPanel);
        }
        add(collapsedJPanel, constraints.clone());
        revalidate();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        final javax.swing.JLabel fillerJLabel = new javax.swing.JLabel();
        final javax.swing.JPanel row1JPanel = new javax.swing.JPanel();
        final javax.swing.JPanel row2JPanel = new javax.swing.JPanel();
        final javax.swing.JPanel expandedDataLabelsJPanel = new javax.swing.JPanel();
        final javax.swing.JLabel contactEMailJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel contactAddressJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel contactPhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel contactMobilePhoneJLabel = new javax.swing.JLabel();
        final javax.swing.JLabel contactFillerJLabel = new javax.swing.JLabel();

        collapsedJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedJPanel.setOpaque(false);
        expandedJPanel.setLayout(new java.awt.GridBagLayout());

        expandedJPanel.setOpaque(false);
        collapsedContactJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedContactJPanel.setOpaque(false);
        collapsedContactJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedContactJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collapsedContactJPanelMouseReleased(evt);
            }
        });

        collapsedIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconUser.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 27, 0, 5);
        collapsedContactJPanel.add(collapsedIconJLabel, gridBagConstraints);

        collapsedTextJLabel.setText("!Contact Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedContactJPanel.add(collapsedTextJLabel, gridBagConstraints);

        collapsedAdditionalTextJLabel.setForeground(Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG);
        collapsedAdditionalTextJLabel.setText("!Contact Additional Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        collapsedContactJPanel.add(collapsedAdditionalTextJLabel, gridBagConstraints);

        collapsedIncomingInvitationJPanel.setLayout(new java.awt.GridBagLayout());

        collapsedIncomingInvitationJPanel.setOpaque(false);
        collapsedIncomingInvitationJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapsedIncomingInvitationJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                collapsedIncomingInvitationJPanelMouseReleased(evt);
            }
        });

        incomingInvitationIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconUser.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 27, 0, 5);
        collapsedIncomingInvitationJPanel.add(incomingInvitationIconJLabel, gridBagConstraints);

        incomingInvitationTextJLabel.setText("!Contact Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedIncomingInvitationJPanel.add(incomingInvitationTextJLabel, gridBagConstraints);

        incomingInvitationAdditionalTextJLabel.setForeground(Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG);
        incomingInvitationAdditionalTextJLabel.setText("!Contact Additional Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        collapsedIncomingInvitationJPanel.add(incomingInvitationAdditionalTextJLabel, gridBagConstraints);

        incomingInvitationTertiaryTextJLabel.setText("!Contact Tertiary Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        collapsedIncomingInvitationJPanel.add(incomingInvitationTertiaryTextJLabel, gridBagConstraints);

        incomingInvitationAcceptJLabel.setText("!accept!");
        incomingInvitationAcceptJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                incomingInvitationAcceptJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 4, 0);
        collapsedIncomingInvitationJPanel.add(incomingInvitationAcceptJLabel, gridBagConstraints);

        incomingInvitationDeclineJLabel.setText("!decline!");
        incomingInvitationDeclineJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                incomingInvitationDeclineJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 4, 0);
        collapsedIncomingInvitationJPanel.add(incomingInvitationDeclineJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        collapsedIncomingInvitationJPanel.add(fillerJLabel, gridBagConstraints);

        expandedContactJPanel.setOpaque(false);
        expandedContactJPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandedContactJPanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                expandedContactJPanelMouseReleased(evt);
            }
        });

        row1JPanel.setLayout(new java.awt.GridBagLayout());

        row1JPanel.setOpaque(false);
        contactIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconUser.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 27, 0, 5);
        row1JPanel.add(contactIconJLabel, gridBagConstraints);

        contactTextJLabel.setText("!Name!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 3, 4, 0);
        row1JPanel.add(contactTextJLabel, gridBagConstraints);

        contactAdditionalTextJLabel.setForeground(Colors.Browser.Panel.PANEL_ADDITIONAL_TEXT_FG);
        contactAdditionalTextJLabel.setText("!Title, Organization!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 4, 0);
        row1JPanel.add(contactAdditionalTextJLabel, gridBagConstraints);

        row2JPanel.setLayout(new java.awt.GridBagLayout());

        row2JPanel.setOpaque(false);
        expandedDataLabelsJPanel.setLayout(new java.awt.GridBagLayout());

        expandedDataLabelsJPanel.setOpaque(false);
        contactEMailJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("TAB_CONTACT.emailJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactEMailJLabel, gridBagConstraints);

        contactAddressJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("TAB_CONTACT.addressJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactAddressJLabel, gridBagConstraints);

        contactPhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("TAB_CONTACT.phoneJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(34, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactPhoneJLabel, gridBagConstraints);

        contactMobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/JPanel_Messages").getString("TAB_CONTACT.mobilePhoneJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactMobilePhoneJLabel, gridBagConstraints);

        contactFillerJLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 129, 0, 0);
        expandedDataLabelsJPanel.add(contactFillerJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weighty = 1.0;
        row2JPanel.add(expandedDataLabelsJPanel, gridBagConstraints);

        expandedDataValuesJPanel.setLayout(new java.awt.GridBagLayout());

        expandedDataValuesJPanel.setOpaque(false);
        contactEMailValueJLabel.setText("Email Address Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(8, 10, 0, 0);
        expandedDataValuesJPanel.add(contactEMailValueJLabel, gridBagConstraints);

        contactAddressValueJLabel.setText("Address Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        expandedDataValuesJPanel.add(contactAddressValueJLabel, gridBagConstraints);

        contactLocationValueJLabel.setText("Location Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        expandedDataValuesJPanel.add(contactLocationValueJLabel, gridBagConstraints);

        contactPhoneValueJLabel.setText("Phone Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        expandedDataValuesJPanel.add(contactPhoneValueJLabel, gridBagConstraints);

        contactMobilePhoneValueJLabel.setText("Mobile Phone Value");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        expandedDataValuesJPanel.add(contactMobilePhoneValueJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        expandedDataValuesJPanel.add(contactFillterValueJLabel, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        row2JPanel.add(expandedDataValuesJPanel, gridBagConstraints);

        org.jdesktop.layout.GroupLayout expandedContactJPanelLayout = new org.jdesktop.layout.GroupLayout(expandedContactJPanel);
        expandedContactJPanel.setLayout(expandedContactJPanelLayout);
        expandedContactJPanelLayout.setHorizontalGroup(
            expandedContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(row1JPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            .add(row2JPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        expandedContactJPanelLayout.setVerticalGroup(
            expandedContactJPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(expandedContactJPanelLayout.createSequentialGroup()
                .add(row1JPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 23, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(row2JPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 141, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        setLayout(new java.awt.GridBagLayout());

        setBorder(BORDER);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handle the mouse pressed event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be dislpayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMousePressed(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            tabDelegate.selectPanel(this);
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            if (isSetContact())
                popupDelegate.showForContact(contact, isExpanded());
            else if (isSetIncoming())
                popupDelegate.showForInvitation(incoming);
            else if (isSetOutgoingEMail())
                popupDelegate.showForInvitation(outgoingEMail);
            else if (isSetOutgoingUser())
                popupDelegate.showForInvitation(outgoingUser);
            else if (isSetProfile())
                popupDelegate.showForProfile(profile, isExpanded());
            else
                Assert.assertUnreachable("Inconsistent contact tab panel state.");
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            tabDelegate.selectPanel(this);
        } else if ((e.getClickCount() % 2) == 0 && e.getButton() == MouseEvent.BUTTON1) {
            if (isSetContact() || isSetProfile()) {
                tabDelegate.toggleExpansion(this);
            }
        }
    }

    /**
     * Handle the mouse released event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be dislpayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMouseReleased(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            tabDelegate.selectPanel(this);
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            if (isSetContact())
                popupDelegate.showForContact(contact, isExpanded());
            else if (isSetIncoming())
                popupDelegate.showForInvitation(incoming);
            else if (isSetOutgoingEMail())
                popupDelegate.showForInvitation(outgoingEMail);
            else if (isSetOutgoingUser())
                popupDelegate.showForInvitation(outgoingUser);
            else if (isSetProfile())
                popupDelegate.showForProfile(profile, isExpanded());
            else
                Assert.assertUnreachable("Inconsistent contact tab panel state.");
        }
    }

    /**
     * Reload a display label.
     * 
     * @param jLabel
     *            A swing <code>JLabel</code>.
     * @param value
     *            The label value.
     */
    private void reload(final javax.swing.JLabel jLabel,
            final String value) {
        jLabel.setText(null == value ? " " : value);
    }
}
