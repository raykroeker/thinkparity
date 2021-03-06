/*
 * Jan 20, 2006
 */
package com.thinkparity.ophelia.browser.platform.application.display.avatar;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.swing.AbstractJPanel;

import com.thinkparity.ophelia.browser.Constants.Images;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.Resizer;
import com.thinkparity.ophelia.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.ophelia.browser.platform.Platform;
import com.thinkparity.ophelia.browser.platform.application.Application;
import com.thinkparity.ophelia.browser.platform.application.ApplicationId;
import com.thinkparity.ophelia.browser.platform.application.ApplicationListener;
import com.thinkparity.ophelia.browser.platform.application.ApplicationRegistry;
import com.thinkparity.ophelia.browser.platform.application.window.WindowTitleButtons;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.util.State;
import com.thinkparity.ophelia.browser.util.localization.BrowserLocalization;
import com.thinkparity.ophelia.browser.util.localization.Localization;

/**
 * <b>Title:</b>thinkParity Browser Avatar<br>
 * <b>Description:</b>An abstraction of the view component of the MVC design
 * pattern for the thinkParity browser.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class Avatar extends AbstractJPanel {

    /** The thinkparity application registry. */
    protected final ApplicationRegistry applicationRegistry;

    /** The avatar's content provider. */
    protected ContentProvider contentProvider;

    /** The avatar input. */
    protected Object input;

    /** Localization helper utility. */
    protected final Localization localization;

    /** The thinkParity <code>PluginRegistry</code>. */
    protected final PluginRegistry pluginRegistry;

	/** The Resizer */
    protected Resizer resizer;

	/** A set of avatar object utilities. */
    protected final AvatarUtils utils;

    /** The application listener. */
    private ApplicationListener applicationListener;

    /** The clipped background image. */
    private BufferedImage clippedBackgroundImage;

    /** Close button enabled flag. */
    private Boolean closeButtonEnabled;

	/** An avatar's <code>EventDispatcher</code>. */
    private EventDispatcher<Avatar> eventDispatcher;

    /** A list of the avatar's input error messages. */
    private final List<String> inputErrors;
    
    /** The scaled background image. */
    private BufferedImage scaledBackgroundImage;

    /** The avatar's scrolling policy. */
    private final ScrollPolicy scrollPolicy;

    /** The window title buttons. */
    private WindowTitleButtons windowTitleButtons;

    /**
     * Create Avatar.
     * 
     * @param id
     *            The <code>AvatarId</code>.
     */
    protected Avatar(final AvatarId id) {
        this(id.toString());
    }

	/**
	 * Create a Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 */
	protected Avatar(final String l18nContext) {
		this(l18nContext, ScrollPolicy.NONE);
	}

	/**
     * Create an Avatar.
     * 
     * @param l18nContext
     *            The localiztaion context.
     * @param background
     *            The avatar background colour.
     */
	protected Avatar(final String l18nContext, final Color background) {
		this(l18nContext, ScrollPolicy.NONE, background);
	}

	/**
	 * Create an Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param scrollPolicy
	 *            The scrolling policy.
	 */
	protected Avatar(final String l18nContext, final ScrollPolicy scrollPolicy) {
		super();
        this.applicationRegistry = new ApplicationRegistry();
        this.closeButtonEnabled = Boolean.TRUE;
		this.inputErrors = new LinkedList<String>();
        this.localization = new BrowserLocalization(l18nContext);
        this.pluginRegistry = new PluginRegistry();
		this.scrollPolicy = scrollPolicy;
		this.utils = new AvatarUtils(this);
	}

    /**
	 * Create an Avatar.
	 * 
	 * @param l18nContext
	 *            The localization context.
	 * @param scrollPolicy
	 *            The scrolling policy.
	 * @param background
	 *            The background color.
	 */
	protected Avatar(final String l18nContext, final ScrollPolicy scrollPolicy,
			final Color background) {
		super(background);
        this.applicationRegistry = new ApplicationRegistry();
		this.inputErrors = new LinkedList<String>();
        this.localization = new BrowserLocalization(l18nContext);
        this.pluginRegistry = new PluginRegistry();
		this.scrollPolicy = scrollPolicy;
        this.closeButtonEnabled = Boolean.TRUE;
        this.utils = new AvatarUtils(this);
	}

    /**
     * Get the avatar title, used for dialogs.
     * 
     * @return the avatar title
     */
    public String getAvatarTitle() {
        return getString("Title");
    }

    /**
	 * Obtain the content provider.
	 * 
	 * @return The content provider.
	 */
	public ContentProvider getContentProvider() {
        return contentProvider;
	}

    /**
     * Obtain the browser application.
     * 
     * @return The browser application.
     */
	public Browser getController() {
	    return (Browser) applicationRegistry.get(ApplicationId.BROWSER);
	}

    /**
     * Obtain the avatar's event dispatcher.
     * 
     * @return An <code>EventDispatcher</code>.
     */
    public EventDispatcher<?> getEventDispatcher() {
        return eventDispatcher;
    }

	/**
	 * Obtain the avatar id.
	 * 
	 * @return The avatar id.
	 */
	public abstract AvatarId getId();

    /**
	 * Obtain the avatar's input.
	 * 
	 * @return The input.
	 */
	public Object getInput() {
        return input;
	}

	/**
	 * Obtain the scroll policy for the avatar.
	 * 
	 * @return The scroll policy for the avatar.
	 */
	public ScrollPolicy getScrollPolicy() {
        return scrollPolicy;
	}

	/**
	 * Obtain the avatar's state information.
	 * 
	 * @return The avatar's state information.
	 */
	public abstract State getState();

	/**
     * Initialize the avatar. This happens after creation but prior to any other
     * method being called. This allows an opportunity for the avatar to
     * initialize itself.
     * 
     * @param platform
     *            The thinkParity <code>Platform</code>.
     */
    public void initialize(final Platform platform) {
    }

    /**
     * Install the move listener so the mouse can be used to
     * move the avatar.
     */
	public void installMoveListener() {
        addMoveListener(this);
    }

    /**
     * Install the request focus listener so the avatar gets focus when
     * the user clicks on it.
     */
    public void installRequestFocusListener() {
        addRequestFocusListener(this);
    }

    /**
     * Install the resizer so the mouse can be used to resize
     * the avatar.
     */
    public void installResizer() {
        if (null != resizer) {
            resizer.removeAllListeners();
        }
        resizer = new Resizer(getController(), this, Boolean.FALSE, getResizeEdges());
    }

    /**
     * Determine if there is an avatar background image, used for dialogs.
     * With few exceptions (eg. title, main, and status areas of browser)
     * this should be true.
     */
    public Boolean isAvatarBackgroundImage() {
        return Boolean.TRUE;
        
    }

    /**
     * Determine if there is an avatar title, used for dialogs.
     * With few exceptions (eg. help-about) this should be true.
     */
    public Boolean isAvatarTitle() {
        return Boolean.TRUE;
    }

	/**
	 * Reload the avatar. This event is called when either the content provider
	 * or the input has changed; or as a manual reload of the avatar.
	 * 
	 */
	public void reload() {}

	/**
     * Set the avatar's content provider. The avatar is reloaded when the
     * content provider is changed. The content provider is a bound property.
     * 
     * @param contentProvider
     *            A <code>ContentProvider</code>.
     */
	public void setContentProvider(final ContentProvider contentProvider) {
		Assert.assertNotNull(contentProvider,
                "Cannot set a null content provider for avatar:  {0}", getId());
		if (this.contentProvider == contentProvider
				|| contentProvider.equals(this.contentProvider))
            return;
        final ContentProvider oldContentProvider = this.contentProvider;
		this.contentProvider = contentProvider;
		reload();
        firePropertyChange("contentProvider", oldContentProvider, contentProvider);
	}

	/**
     * Set the avatar's event dispatcher. The event dispatcher is a bound
     * property.
     * 
     * @param eventDispatcher
     *            An <code>EventDispatcher</code>.
     */
    public void setEventDispatcher(final EventDispatcher<Avatar> eventDispatcher) {
        Assert.assertNotNull(eventDispatcher,
                "Cannot set a null event dispatcher for avatar:  {0}", getId());
        if (this.eventDispatcher == eventDispatcher
                || eventDispatcher.equals(this.eventDispatcher))
            return;
        final EventDispatcher<?> oldEventDispatcher = this.eventDispatcher;
        this.eventDispatcher = eventDispatcher;
        firePropertyChange("eventDispatcher", oldEventDispatcher, eventDispatcher);

        removeApplicationListener(this.applicationListener);
        this.applicationListener = new ApplicationListener() {
            public void notifyEnd(final Application application) {
                Avatar.this.eventDispatcher.removeListeners(Avatar.this);
            }
            public void notifyHibernate(Application application) {}
            public void notifyRestore(Application application) {}
            public void notifyStart(Application application) {}   
        };
        addApplicationListener(this.applicationListener);
    }

    /**
	 * Set the avatar's input.
	 * 
	 * @param input
	 *            The avatar input.
	 */
	public void setInput(final Object input) {
		Assert.assertNotNull("Cannot set null input:  " + getId(), input);
		if (this.input == input || input.equals(this.input))
            return;
		
        final Object oldInput = this.input;
		this.input = input;
		reload();
        firePropertyChange("input", oldInput, input);
	}

    /**
	 * Set the avatar state.
	 * 
	 * @param state
	 *            The avatar's state information.
	 */
	public abstract void setState(final State state);

    /**
     * Set the avatar's window title buttons.
     * 
     * @param windowTitleButtons
     *            A <code>WindowTitleButtons</code>.
     */
    public void setWindowTitleButtons(final WindowTitleButtons windowTitleButtons) {
        this.windowTitleButtons = windowTitleButtons;
        this.windowTitleButtons.setCloseButtonEnabled(closeButtonEnabled);
    }

    /**
     * Set an error for display.
     *
     * @param error
     *            The error.
     */
    protected final void addInputError(final String inputError) {
        inputErrors.add(inputError);
    }

    /**
     * Set an error for display.
     *
     * @param error
     *            The error.
     */
    protected final void addInputError(final String inputError,
            final Object... arguments) {
        inputErrors.add(MessageFormat.format(inputError, arguments));
    }

    /**
     * Add an avatar validation listener to the text component.
     *
     * @param jTextComponent
     *            A swing <code>JTextComponent</code>.
     */
    protected final void addValidationListener(final JTextComponent jTextComponent) {
        final String listenerId = getId().name() + "#documentListener";
        DocumentListener listener = (DocumentListener) jTextComponent.getClientProperty(listenerId);
        if (null == listener) {
            listener = new DocumentListener() {
                public void changedUpdate(final DocumentEvent e) {
                    validateInput();
                }
                public void insertUpdate(final DocumentEvent e) {
                    validateInput();
                }
                public void removeUpdate(final DocumentEvent e) {
                    validateInput();
                }
            };
        }
        jTextComponent.putClientProperty(listenerId, listener);
        jTextComponent.getDocument().removeDocumentListener(listener);
        jTextComponent.getDocument().addDocumentListener(listener);
    }

    /**
	 * Clear all display errors.
	 *
	 */
	protected final void clearInputErrors() {
        inputErrors.clear();
	}

    /**
	 * Determine whether or not the error has been set.
	 * 
	 * @return True if error has been set; false otherwise.
	 */
	protected final Boolean containsInputErrors() {
        return 0 < inputErrors.size();
	}

    /**
	 * Obtain the error.
	 * 
	 * @return The error.
	 */
	protected final List<String> getInputErrors() {
        return Collections.unmodifiableList(inputErrors);
	}

	/**
     * Obtain the avatar's localization.
     * 
     * @return A <code>Localization</code>.
     */
    protected final Localization getLocalization() {
        return localization;
    }

    /**
     * Obtain the pluginRegistry
     *
     * @return The PluginRegistry.
     */
    protected PluginRegistry getPluginRegistry() {
        return pluginRegistry;
    }

	/**
     * Get the resize edges. Override this method if you don't want
     * resize support for the avatar, or if only some of the avatar
     * edges support resize. Default is BOTTOM which is suited to
     * avatars on dialogs, because the top is the window title.
     */
    protected Resizer.ResizeEdges getResizeEdges() {
        return Resizer.ResizeEdges.BOTTOM;
    }

	/**
     * Obtain a browser session.
     * 
     * @return A <code>BrowserSession</code>.
     */
    protected BrowserSession getSession() {
        return getController().getSession(getId());
    }

	/**
     * Obtain a browser session.
     * 
     * @return A <code>BrowserSession</code>.
     */
    protected BrowserSession getSession(final Boolean create) {
        return getController().getSession(getId(), create);
    }

	/**
     * @see Localization#getString(String)
     * 
     */
    protected String getString(final String localKey) {
    	return localization.getString(localKey);
    }

	/**
     * @see Localization#getString(String, Object[])
     * 
     */
    protected String getString(final String localKey, final Object[] arguments) {
    	return localization.getString(localKey, arguments);
    }

    /**
     * Causes <i>doRun.run()</i> to be executed asynchronously on the AWT event
     * dispatching thread.
     * 
     * @param doRun
     *            The runnable to execute.
     * @see SwingUtilities#invokeLater(java.lang.Runnable)
     */
	protected void invokeLater(final Runnable doRun) {
        SwingUtilities.invokeLater(doRun);
    }

    /**
	 * Determine whether or not the platform is running in test mode.
	 * 
	 * @return True if the platform is in test mode; false otherwise.
	 */
	protected final Boolean isDebugMode() {
		return getController().getPlatform().isDevelopmentMode();
	}

    /**
     * Determine whether the user input for the frame is valid.
     * 
     * @return True if the input is valid; false otherwise.
     */
    protected Boolean isInputValid() {
        validateInput();
        return !containsInputErrors();
    }

    /**
     * These get and set methods are used by classes that intend to do their own
     * mouse dragging. (For example, the bottom right resize control.)
     * 
     */
    protected Boolean isResizeDragging() {
        if (null == resizer) {
            return Boolean.FALSE;
        } else {
            return resizer.isResizeDragging();
        }
    }

	/**
	 * Determine whether or not the platform is running in test mode.
	 * 
	 * @return True if the platform is in test mode; false otherwise.
	 */
	protected final Boolean isTestMode() {
		return getController().getPlatform().isTestingMode();
	}

	/**
     * Open a localized resource.
     * 
     * @param name
     *            The resource name.
     * @return An <code>InputStream</code>.
     */
    protected final InputStream openResource(final String name) {
        return localization.openResource(name);
    }

    /**
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (isAvatarBackgroundImage()) {
            final Graphics g2 = g.create();
            try {
                // NOTE This constant affects dialog resize performance. It can be made larger (eg. 5)
                // if dialog resize performance becomes an issue.
                final int extra = 1;
                
                // Scale the background image to the correct size or slightly larger.
                // For performance reasons, don't use scaling to resize with every pixel change.
                if ((null == scaledBackgroundImage)
                        || (scaledBackgroundImage.getWidth() < getWidth())
                        || (scaledBackgroundImage.getWidth() > (getWidth() + extra))
                        || (scaledBackgroundImage.getHeight() < getHeight())
                        || (scaledBackgroundImage.getHeight() > (getHeight() + extra))) {
                    final Image image = Images.BrowserTitle.DIALOG_BACKGROUND.getScaledInstance(
                            getWidth() + extra, getHeight() + extra, Image.SCALE_FAST);
                    scaledBackgroundImage = new BufferedImage(getWidth() + extra, getHeight() + extra, BufferedImage.TYPE_INT_RGB);
                    final Graphics gImage = scaledBackgroundImage.createGraphics();
                    try {
                        gImage.drawImage(image, 0, 0, null);
                    }
                    finally { gImage.dispose(); }
                }
                
                // Clip the image to the exact size.
                if (null != scaledBackgroundImage) {
                    if ((null == clippedBackgroundImage)
                            || (clippedBackgroundImage.getWidth() != getWidth())
                            || (clippedBackgroundImage.getHeight() != getHeight())) {
                        clippedBackgroundImage = scaledBackgroundImage.getSubimage(
                                0, 0, getWidth(), getHeight());
                    }
                }
                
                // Draw the background image.
                if (null != clippedBackgroundImage) {                
                    g2.drawImage(clippedBackgroundImage, 0, 0, getWidth(), getHeight(), null);
                }
            }
            finally { g2.dispose(); }
        }
    }

    /**
     * Enable or disable the close button on the window title.
     * 
     * @param closeButtonEnabled
     *            A <code>Boolean</code>, true to enable the close button.
     */
    protected void setCloseButtonEnabled(final Boolean closeButtonEnabled) {
        this.closeButtonEnabled = closeButtonEnabled;
        if (null != windowTitleButtons) {
            windowTitleButtons.setCloseButtonEnabled(closeButtonEnabled);
        }
    }

    /**
     * Enable or disable text entry for a text component.
     * 
     * @param jTextComponent
     *            A swing <code>JTextComponent</code>.
     * @param enable
     *            The enable <code>Boolean</code>.
     */
    protected void setEditable(final JTextComponent jTextComponent, final Boolean enable) {
        jTextComponent.setEditable(enable);
        jTextComponent.setFocusable(enable);
        jTextComponent.setOpaque(enable);
    }

    /**
     * These get and set methods are used by classes that intend to do their
     * own mouse dragging. (For example, the bottom right resize control.)
     */
    protected void setResizeDragging(final Boolean resizeDragging) {
        if (null != resizer) {
            resizer.setResizeDragging(resizeDragging);
        }
    }

    /**
	 * Provide a visual cue to the user that work is being done.
	 *
	 */
	protected void toggleVisualFeedback(final Boolean isWorking) {
		if (isWorking.booleanValue()) {
            setIsWorking();
		} else {
            setIsNotWorking();
		}
	}

    /**
     * Validate the avatar input. The default implementation of this method will
     * clear the error list.  Each avatar should implment this method and add
     * localized text to the errors list in order to create a user-meaningful
     * list.
     * 
     */
    protected void validateInput() {
        clearInputErrors();
    }

    /**
     * Add an application listener.
     * 
     * @param applicationListener
     *            An <code>ApplicationListener</code>.  
     */
    private void addApplicationListener(final ApplicationListener applicationListener) {
        getController().addListener(applicationListener);
    }

    /**
     * Remove an application listener.
     * 
     * @param applicationListener
     *            An <code>ApplicationListener</code>.  
     */
    private void removeApplicationListener(final ApplicationListener applicationListener) {
        if (null != applicationListener) {
            getController().removeListener(applicationListener);
        }
    }

    private void setIsNotWorking() {
		final Component[] components = getComponents();
		for(final Component c : components) {
			if (c.getClass().isAssignableFrom(JButton.class)) {
				c.setEnabled(true);
			}
		}

	}

    private void setIsWorking() {
		final Component[] components = getComponents();
		for(final Component c : components) {
			if (c.getClass().isAssignableFrom(JButton.class)) {
				c.setEnabled(false);
			}
		}
	}

    /**
	 * The scrolling policy for the avatar.
	 * 
	 */
	public enum ScrollPolicy { BOTH, HORIZONTAL, NONE, VERTICAL }
}
