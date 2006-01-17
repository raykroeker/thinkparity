/*
 * Jan 16, 2006
 */
package com.thinkparity.browser.javax.swing.document.history;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.thinkparity.browser.javax.swing.BrowserJFrame;
import com.thinkparity.browser.provider.ContentProvider;
import com.thinkparity.browser.provider.FlatContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentVersion;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HistoryShuffler extends JPanel {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The history content provider.
	 * 
	 */
	private FlatContentProvider contentProvider;

	/**
	 * The history input object.
	 * 
	 */
	private Document input;

	/**
	 * Handle to the JPanel for use with annonymouse inner classes.
	 * 
	 */
	private final JPanel jPanel;

	/**
	 * Create a HistoryShuffler.
	 * 
	 */
	public HistoryShuffler() {
		super();
		this.jPanel = this;
		addMouseListener(new MouseInputAdapter() {
			public void mouseClicked(MouseEvent e) {
				final Component c = getComponentAt(e.getPoint());
				if(null != c) {
					final MouseEvent ce =
						SwingUtilities.convertMouseEvent(jPanel, e, c);
					c.dispatchEvent(ce);
				}
			}
		});
		setBackground(BrowserJFrame.getBackgroundColor());
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setOpaque(true);
		setLayout(new GridBagLayout());
	}
	
	/**
	 * Obtain the current content provider.
	 * 
	 * @return The content provider.
	 */
	public ContentProvider getContentProvider() { return contentProvider; }

	/**
	 * Obtain the history shuffler's input.
	 * 
	 * @return The input.
	 */
	public Object getInput() { return input; }

	/**
	 * Refresh the content on the avatar.
	 *
	 */
	public void refresh() {
		for(Component component : getComponents()) { remove(component); }
		if(null != input) {
			final Object[] versions = contentProvider.getElements(input);
			HistoryAvatar avatar;
			for(final Object version : versions) {
				avatar = translate((DocumentVersion) version);
				avatar.transferToDisplay();
				add(avatar, createAvatarConstraints());
			}
			add(createFillerComponent(), createFillerConstraints());
		}
		invalidate();
	}

	/**
	 * Set the current content provider.
	 * 
	 * @param contentProvider
	 *            The new content provider.
	 */
	public void setContentProvider(final ContentProvider contentProvider) {
		Assert.assertNotNull(
				"The HistoryShuffler content provider must not be null.",
				contentProvider);
		Assert.assertOfType(
				"The HistoryShuffler content provider must be of type FlatContentProvider.",
				FlatContentProvider.class, contentProvider);
		// they're the same; do nothing
		if(this.contentProvider == contentProvider) { return; }

		this.contentProvider = (FlatContentProvider) contentProvider;
		refresh();
	}

	/**
	 * The the history shuffler's input.
	 * 
	 * @param input
	 *            The input.
	 */
	public void setInput(final Object input) {
		Assert.assertNotNull(
				"The HistoryShuffler input must not be null.", input);
		Assert.assertOfType(
				"The HistoryShuffler input must be of type Document.",
				Document.class, input);
		// if they're the same do nothing
		if(this.input == input || input.equals(this.input)) { return; }

		// set the input and refresh
		this.input = (Document) input;
		refresh();
	}

	/**
	 * Create the avatar layout constraints.
	 * 
	 * @return The avatar layout constraints.
	 */
	private Object createAvatarConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Create a swing component to use as padding.
	 * 
	 * @return The swing component.
	 */
	private Component createFillerComponent() { return new JLabel(""); }

	/**
	 * Create the layout constraints for the padding component.
	 * 
	 * @return The layout constraints.
	 */
	private Object createFillerConstraints() {
		return new GridBagConstraints(0, GridBagConstraints.RELATIVE,
				1, 1,
				1.0, 1.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0),
				0, 0);
	}

	/**
	 * Translate a document version into a history avatar for display.
	 * 
	 * @param version
	 *            The document version.
	 * @return The history avatar.
	 */
	private HistoryAvatar translate(final DocumentVersion version) {
		final HistoryAvatar avatar = new HistoryAvatar();
		avatar.setInput(version);
		return avatar;
	}
}
