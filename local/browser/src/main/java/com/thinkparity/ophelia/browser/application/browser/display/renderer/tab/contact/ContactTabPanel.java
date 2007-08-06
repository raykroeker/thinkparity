/*
 * Created On:  December 19, 2006, 9:01 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.Colors;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.LabelFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.KeyboardPopupHelper;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainPanelImageCache.TabPanelIcon;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContactTabPanel extends DefaultTabPanel {

    /** The fraction of width allowed for additional text. */
    private static final float FRACTION_WIDTH_CONTACT_ADDITIONAL;

    /** The fraction of width allowed for the contact name. */
    private static final float FRACTION_WIDTH_CONTACT_NAME;

    /** The fraction of width allowed for additional text. */
    private static final float FRACTION_WIDTH_INCOMING_INVITATION_ADDITIONAL;

    /** The fraction of width allowed for the contact name. */
    private static final float FRACTION_WIDTH_INCOMING_INVITATION_NAME;

    static {
        FRACTION_WIDTH_CONTACT_ADDITIONAL = 0.4f;
        FRACTION_WIDTH_CONTACT_NAME = 0.4f;
        FRACTION_WIDTH_INCOMING_INVITATION_ADDITIONAL = 0.25f;
        FRACTION_WIDTH_INCOMING_INVITATION_NAME = 0.25f;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private final javax.swing.JLabel collapseIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel collapsedAdditionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedContactJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel collapsedIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel collapsedIncomingInvitationJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel collapsedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel collapsedTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactAdditionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactAddressValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactEMailValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactFillterValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactLocationValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactMobilePhoneValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactPhoneValueJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel contactTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel expandIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JPanel expandedContactJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel expandedDataValuesJPanel = new javax.swing.JPanel();
    private final javax.swing.JPanel expandedJPanel = new javax.swing.JPanel();
    private final javax.swing.JLabel incomingInvitationAcceptJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel incomingInvitationAdditionalTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel incomingInvitationDeclineJLabel = LabelFactory.createLink("",Fonts.DefaultFont);
    private final javax.swing.JLabel incomingInvitationIconJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel incomingInvitationTertiaryTextJLabel = new javax.swing.JLabel();
    private final javax.swing.JLabel incomingInvitationTextJLabel = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables

    /** The contact tab's <code>DefaultActionDelegate</code>. */
    private ActionDelegate actionDelegate;

    /** A <code>Contact</code>. */
    private Contact contact;

    /** A contact <code>IncomingEMailInvitation</code>. */
    private IncomingEMailInvitation incomingEMail;

    /** A contact <code>IncomingUserInvitation</code>. */
    private IncomingUserInvitation incomingUser;

    /** The inner <code>JPanel</code> <code>GridBagConstraints</code>. */
    private final GridBagConstraints innerJPanelConstraints;
    
    /** The panel localization. */
    private final Localization localization;
    
    /** A contact <code>OutgoingEMailInvitation</code>. */
    private OutgoingEMailInvitation outgoingEMail;
    
    /** A contact <code>OutgoingUserInvitation</code>. */
    private OutgoingUserInvitation outgoingUser;
    
    /** A contact tab <code>PopupDelegate</code>. */
    private PopupDelegate popupDelegate;

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
        this.localization = new BrowserLocalization("ContactTabPanel");
        initComponents();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#adjustComponentWidth()
     */
    @Override
    public void adjustComponentWidth() {
        reloadName();
        reloadAdditionalText();
    }

    /**
     * Collapse the panel.
     *
     */
    public void collapse(final boolean animate) {
        setBorder(BORDER_COLLAPSED);
        doCollapse(animate);
    }

    /**
     * Expand the panel.
     *
     */
    public void expand(final boolean animate) {
        setBorder(BORDER_EXPANDED);
        doExpand(animate);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#expandIconMousePressed(java.awt.event.MouseEvent)
     */
    @Override
    public void expandIconMousePressed(final MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && isExpandable()) {
            tabDelegate.toggleExpansion(this);
        }
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
        else if (isSetIncomingEMail())
            id.append(incomingEMail.getId());
        else if (isSetIncomingUser())
            id.append(incomingUser.getId());
        else if (isSetOutgoingEMail())
            id.append(outgoingEMail.getId());
        else if (isSetOutgoingUser())
            id.append(outgoingUser.getId());
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
        return id.toString();
    }

    /**
     * Obtain the incoming e-mail invitation.
     * 
     * @return An <code>IncomingEMailInvitation</code>.
     */
    public IncomingEMailInvitation getIncomingEMail() {
        return incomingEMail;
    }

    /**
     * Obtain the incoming user invitation.
     * 
     * @return An <code>IncomingUserInvitation</code>.
     */
    public IncomingUserInvitation getIncomingUser() {
        return incomingUser;
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
     * Determine if the contact is set.
     * 
     * @return True if the contact is set.
     */
    public Boolean isSetContact() {
        return null != contact;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#isSetExpandedData()
     */
    public Boolean isSetExpandedData() {
        // panels on the contact tab are always popuplated with all data required when expanded.
        return Boolean.TRUE;
    }

    /**
     * Determine if the incoming e-mail invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetIncomingEMail() {
        return null != incomingEMail;
    }

    /**
     * Determine if the incoming user invitation is set.
     * 
     * @return True if the invitation is set.
     */
    public Boolean isSetIncomingUser() {
        return null != incomingUser;
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel#refresh()
     */
    public void refresh() {}

    /**
     * Apply the current connection. If the connection is offline; the accept
     * decline links for the invitations will be made invisible.
     * 
     * @param online
     *            The online <code>Boolean</code>.
     */
    public void reloadConnection(final Boolean online) {
        if (isSetIncomingEMail() || isSetIncomingUser()) {
            incomingInvitationAcceptJLabel.setVisible(online);
            incomingInvitationDeclineJLabel.setVisible(online);
        }
    }

    /**
     * Set actionDelegate.
     *
     * @param actionDelegate
     *      An ActionDelegate.
     */
    public void setActionDelegate(final ActionDelegate actionDelegate) {
        this.actionDelegate = actionDelegate;
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
        reloadName();
        reloadAdditionalText();
        
        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER));
        expandIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.EXPAND));

        reload(contactAddressValueJLabel, contact.getAddress());
        reload(contactLocationValueJLabel,
                getLocationText(contact.getCity(), contact.getProvince(),
                        contact.getCountry(), contact.getPostalCode(),
                        locale, availableLocales));
        final EMail email;
        if (0 < contact.getEmailsSize())
            email = contact.getEmails().get(0);
        else
            email = null;
        reload(contactEMailValueJLabel, email);
        reload(contactMobilePhoneValueJLabel, contact.getMobilePhone());
        reload(contactPhoneValueJLabel, contact.getPhone());
    }

    /**
     * Set the panel data.
     * 
     * @param incomingEMail
     *            An <code>IncomingEMailInvitation</code>.
     */
    public void setPanelData(final IncomingEMailInvitation incomingEMail) {
        this.incomingEMail = incomingEMail;
        initCollapsedPanel();
        reloadName();
        reloadAdditionalText();

        incomingInvitationIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        reload(incomingInvitationTertiaryTextJLabel, localization.getString("IncomingInvitationTertiaryText"));
        reload(incomingInvitationAcceptJLabel, localization.getString("IncomingInvitationAccept"));
        reload(incomingInvitationDeclineJLabel, localization.getString("IncomingInvitationDecline"));
    }

    /**
     * Set the panel data.
     * 
     * @param incomingUser
     *            An <code>IncomingUserInvitation</code>.
     */
    public void setPanelData(final IncomingUserInvitation incomingUser) {
        this.incomingUser = incomingUser;
        initCollapsedPanel();
        reloadName();
        reloadAdditionalText();

        incomingInvitationIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        reload(incomingInvitationTertiaryTextJLabel, localization.getString("IncomingInvitationTertiaryText"));
        reload(incomingInvitationAcceptJLabel, localization.getString("IncomingInvitationAccept"));
        reload(incomingInvitationDeclineJLabel, localization.getString("IncomingInvitationDecline"));
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
        reloadName();
        reloadAdditionalText();

        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        expandIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.INVISIBLE));
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
        reloadName();
        reloadAdditionalText();

        collapsedIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.USER_NOT_RECEIVED));
        expandIconJLabel.setIcon(IMAGE_CACHE.read(TabPanelIcon.INVISIBLE));
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#addPopupListeners()
     */
    @Override
    protected void addPopupListeners() {
        final KeyboardPopupHelper keyboardPopupHelper = new KeyboardPopupHelper();
        keyboardPopupHelper.addPopupListener(ContactTabPanel.this, new Runnable() {
            public void run() {
                selectPanel();
                popupDelegate.initialize(ContactTabPanel.this, ContactTabPanel.this.getWidth()/5, KEYBOARD_POPUP_Y);
                showPopup();
            }
        });
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabPanel#isExpandable()
     */
    @Override
    protected Boolean isExpandable() {
        return (isSetContact());
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     *
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int height = getHeight() - getBorder().getBorderInsets(this).top
                - getBorder().getBorderInsets(this).bottom;
        adjustBorderColor(isExpanded());
        if (isExpanded() || isAnimating()) {
            renderer.paintExpandedBackground(g, this);
            final Point location = SwingUtilities.convertPoint(expandedDataValuesJPanel, new Point(0,0), this);
            renderer.paintExpandedBackgroundFields(g, location.x,
                    expandedDataValuesJPanel.getWidth(), this);
        } else {
            renderer.paintBackground(g, getWidth(), height, selected);
        }
    }

    private void collapsedContactJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedContactJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedContactJPanelMousePressed

    private void collapsedContactJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedContactJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedContactJPanelMouseReleased

    private void collapsedIncomingInvitationJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedIncomingInvitationJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedIncomingInvitationJPanelMousePressed

    private void collapsedIncomingInvitationJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapsedIncomingInvitationJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_collapsedIncomingInvitationJPanelMouseReleased

    private void collapseIconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMouseEntered
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
    }//GEN-LAST:event_collapseIconJLabelMouseEntered

    private void collapseIconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMouseExited
        SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), null);
    }//GEN-LAST:event_collapseIconJLabelMouseExited

    private void collapseIconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_collapseIconJLabelMousePressed
        selectPanel();
        expandIconMousePressed(e);
    }//GEN-LAST:event_collapseIconJLabelMousePressed

    /**
     * Collapse the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doCollapse(final boolean animate) {
        collapsedJPanel.removeAll();
        if (isSetIncomingEMail() || isSetIncomingUser()) {
            collapsedJPanel.add(collapsedIncomingInvitationJPanel, innerJPanelConstraints.clone());
        } else {
            collapsedJPanel.add(collapsedContactJPanel, innerJPanelConstraints.clone());
        }
        doCollapse(animate, collapsedJPanel, expandedJPanel);
    }

    /**
     * Expand the panel.
     * 
     * @param animate
     *            Whether or not to animate.
     */
    private void doExpand(final boolean animate) {
        expandedJPanel.removeAll();
        if (isSetContact())
            expandedJPanel.add(expandedContactJPanel, innerJPanelConstraints.clone());
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
        doExpand(animate, collapsedJPanel, expandedJPanel);
    }

    private void expandedContactJPanelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedContactJPanelMousePressed
        jPanelMousePressed((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedContactJPanelMousePressed

    private void expandedContactJPanelMouseReleased(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandedContactJPanelMouseReleased
        jPanelMouseReleased((javax.swing.JPanel) e.getSource(), e);
    }//GEN-LAST:event_expandedContactJPanelMouseReleased

    private void expandIconJLabelMouseEntered(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseEntered
        if (isExpandable()) {
            SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), java.awt.Cursor.HAND_CURSOR);
        }
    }//GEN-LAST:event_expandIconJLabelMouseEntered

    private void expandIconJLabelMouseExited(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMouseExited
        if (isExpandable()) {
            SwingUtil.setCursor((javax.swing.JLabel) e.getSource(), null);
        }
    }//GEN-LAST:event_expandIconJLabelMouseExited

    private void expandIconJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_expandIconJLabelMousePressed
        selectPanel();
        expandIconMousePressed(e);
    }//GEN-LAST:event_expandIconJLabelMousePressed

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
        if (locations.size() > 0) {
            final StringBuilder builder = new StringBuilder();
            for (final String location : locations) {
                if (builder.length() > 0) {
                    builder.append(Separator.CommaSpace);
                }
                builder.append(location);
            }
            return builder.toString();
        } else {
            return null;
        }
    }

    private void incomingInvitationAcceptJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_incomingInvitationAcceptJLabelMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
            if (isSetIncomingEMail())
                actionDelegate.invokeForInvitation(getIncomingEMail(), ActionDelegate.Action.ACCEPT);
            else if (isSetIncomingUser())
                actionDelegate.invokeForInvitation(getIncomingUser(), ActionDelegate.Action.ACCEPT);
            else
                Assert.assertUnreachable("Inconsistent contact tab panel state.");
        }
    }//GEN-LAST:event_incomingInvitationAcceptJLabelMousePressed

    private void incomingInvitationDeclineJLabelMousePressed(final java.awt.event.MouseEvent e) {//GEN-FIRST:event_incomingInvitationDeclineJLabelMousePressed
        if (e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
            if (isSetIncomingEMail())
                actionDelegate.invokeForInvitation(getIncomingEMail(), ActionDelegate.Action.DECLINE);
            else if (isSetIncomingUser())
                actionDelegate.invokeForInvitation(getIncomingUser(), ActionDelegate.Action.DECLINE);
            else
                Assert.assertUnreachable("Inconsistent contact tab panel state.");
        }
    }//GEN-LAST:event_incomingInvitationDeclineJLabelMousePressed

    /**
     * Initialize the collapsed panel.
     * 
     */
    private void initCollapsedPanel() {
        collapsedJPanel.removeAll();
        if (isSetIncomingEMail() || isSetIncomingUser()) {
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
        final javax.swing.JLabel expandedDataFillerJLabel = new javax.swing.JLabel();

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

        expandIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconExpand.png")));
        expandIconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                expandIconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                expandIconJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        collapsedContactJPanel.add(expandIconJLabel, gridBagConstraints);

        collapsedIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconUser.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        collapsedContactJPanel.add(collapsedIconJLabel, gridBagConstraints);

        collapsedTextJLabel.setText("!Contact Text!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        collapseIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconCollapse.png")));
        collapseIconJLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                collapseIconJLabelMousePressed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        row1JPanel.add(collapseIconJLabel, gridBagConstraints);

        contactIconJLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/IconUser.png")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.insets = new java.awt.Insets(4, 1, 0, 5);
        row1JPanel.add(contactIconJLabel, gridBagConstraints);

        contactTextJLabel.setText("!Name!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
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
        contactEMailJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContactTabPanel.emailJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(8, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactEMailJLabel, gridBagConstraints);

        contactAddressJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContactTabPanel.addressJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactAddressJLabel, gridBagConstraints);

        contactPhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContactTabPanel.phoneJLabel"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(34, 51, 0, 0);
        expandedDataLabelsJPanel.add(contactPhoneJLabel, gridBagConstraints);

        contactMobilePhoneJLabel.setText(java.util.ResourceBundle.getBundle("localization/Browser_Messages").getString("ContactTabPanel.mobilePhoneJLabel"));
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 152);
        expandedDataValuesJPanel.add(expandedDataFillerJLabel, gridBagConstraints);

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

        setBorder(BORDER_COLLAPSED);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Handle the mouse pressed event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be displayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMousePressed(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            showPopup();
        } else if (e.getClickCount() == 1 && e.getButton() == MouseEvent.BUTTON1) {
            selectPanel();
        } else if ((e.getClickCount() % 2) == 0 && e.getButton() == MouseEvent.BUTTON1) {
            if (isExpandable()) {
                tabDelegate.toggleExpansion(this);
            }
        }
    }

    /**
     * Handle the mouse released event for any one of the jpanels in this tab.
     * Depending on whether or not the event is a popup trigger the popup
     * appropriate to the state of the panel will be displayed.
     * 
     * @param jPanel
     *            A <code>JPanel</code>.
     * @param e
     *            A <code>MouseEvent</code>.
     */
    private void jPanelMouseReleased(final javax.swing.JPanel jPanel,
            final java.awt.event.MouseEvent e) {
        if (e.getClickCount() == 1 && e.isPopupTrigger()) {
            selectPanel();
            popupDelegate.initialize(jPanel, e.getX(), e.getY());
            showPopup();
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
            final EMail value) {
        jLabel.setText(null == value ? Separator.Space.toString() : value.toString());
    }

    /**
     * Reload the additional text label.
     * The text may be clipped.
     */
    private void reloadAdditionalText() {
        if (isSetContact()) {
            if (isExpanded() || isAnimating()) {
                reload(contactAdditionalTextJLabel, getAdditionalText(contact), FRACTION_WIDTH_CONTACT_ADDITIONAL);
                reload(collapsedAdditionalTextJLabel, contactAdditionalTextJLabel.getText());
            } else {
                reload(collapsedAdditionalTextJLabel, getAdditionalText(contact), FRACTION_WIDTH_CONTACT_ADDITIONAL);
                reload(contactAdditionalTextJLabel, collapsedAdditionalTextJLabel.getText());
            }
        } else if (isSetIncomingEMail())
            reload(incomingInvitationAdditionalTextJLabel,
                    getAdditionalText(incomingEMail.getExtendedBy()), FRACTION_WIDTH_INCOMING_INVITATION_ADDITIONAL);
        else if (isSetIncomingUser())
            reload(incomingInvitationAdditionalTextJLabel,
                    getAdditionalText(incomingUser.getExtendedBy()), FRACTION_WIDTH_INCOMING_INVITATION_ADDITIONAL);
        else if (isSetOutgoingEMail() || isSetOutgoingUser())
            reload(collapsedAdditionalTextJLabel, localization.getString("OutgoingInvitationAdditionalText"));
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
    }

    /**
     * Reload the name label.
     * The name label may be clipped.
     */
    private void reloadName() {
        if (isSetContact()) {
            if (isExpanded() || isAnimating()) {
                reload(contactTextJLabel, contact.getName(), FRACTION_WIDTH_CONTACT_NAME);
                reload(collapsedTextJLabel, contactTextJLabel.getText());
            } else {
                reload(collapsedTextJLabel, contact.getName(), FRACTION_WIDTH_CONTACT_NAME);
                reload(contactTextJLabel, collapsedTextJLabel.getText());
            }
        } else if (isSetIncomingEMail())
            reload(incomingInvitationTextJLabel, incomingEMail.getExtendedBy()
                    .getName(), FRACTION_WIDTH_INCOMING_INVITATION_NAME);
        else if (isSetIncomingUser())
            reload(incomingInvitationTextJLabel, incomingUser.getExtendedBy()
                    .getName(), FRACTION_WIDTH_INCOMING_INVITATION_NAME);
        else if (isSetOutgoingEMail())
            reload(collapsedTextJLabel, outgoingEMail.getInvitationEMail()
                    .toString(), FRACTION_WIDTH_CONTACT_NAME);
        else if (isSetOutgoingUser())
            reload(collapsedTextJLabel, outgoingUser.getInvitationUser()
                    .getName(), FRACTION_WIDTH_CONTACT_NAME);
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
    }

    /**
     * Select this panel.
     */
    private void selectPanel() {
        tabDelegate.selectPanel(this);
    }

    /**
     * Show a popup.
     */
    private void showPopup() {
        if (isSetContact())
            popupDelegate.showForContact(contact, isExpanded());
        else if (isSetIncomingEMail())
            popupDelegate.showForInvitation(incomingEMail);
        else if (isSetIncomingUser())
            popupDelegate.showForInvitation(incomingUser);
        else if (isSetOutgoingEMail())
            popupDelegate.showForInvitation(outgoingEMail);
        else if (isSetOutgoingUser())
            popupDelegate.showForInvitation(outgoingUser);
        else
            Assert.assertUnreachable("Inconsistent contact tab panel state.");
    }
}
