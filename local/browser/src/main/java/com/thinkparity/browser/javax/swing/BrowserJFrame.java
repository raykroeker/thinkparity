/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JRootPane;

import org.apache.log4j.Logger;

import com.thinkparity.browser.Browser;
import com.thinkparity.browser.RandomData;
import com.thinkparity.browser.java.awt.StackLayout;
import com.thinkparity.browser.java.awt.StackLayout.Orientation;
import com.thinkparity.browser.javax.swing.document.DocumentProvider;
import com.thinkparity.browser.javax.swing.document.DocumentShuffler;
import com.thinkparity.browser.javax.swing.misc.GradientJPanel;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;
import com.thinkparity.browser.model.CollectionListProxy;
import com.thinkparity.browser.model.ModelProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserJFrame extends JFrame {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Open the main browser jFrame.
	 * 
	 * @param browser
	 *            A handle to the browser main class.
	 */
	public static void open(final Browser browser) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { doOpen(browser); }
        });
    }

	/**
	 * Set the parity look and feel; create an instance of the browser jFrame
	 * then set the frame to be visible.
	 * 
	 * @param browser
	 *            A handle to the browser main class.
	 */
	private static void doOpen(final Browser browser) {
		BrowserUI.setParityLookAndFeel();
		final BrowserJFrame browserJFrame = new BrowserJFrame(browser);
		browserJFrame.setAlwaysOnTop(true);
		browserJFrame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		browserJFrame.setTitle("Parity Browser");

		browserJFrame.setVisible(true);
		((Graphics2D) browserJFrame.getGraphics())
			.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	/**
	 * The display component for documents.
	 * 
	 */
	private final DocumentShuffler documentShuffler;

	/**
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	/**
	 * Document model api.
	 * 
	 */
	private final DocumentModel documentModel;

	/**
	 * Create a BrowserJFrame.
	 * 
	 * @throws HeadlessException
	 */
	public BrowserJFrame(final Browser browser) throws HeadlessException {
		super();
		// initialize the state
		new BrowserJFrameState(this);
		// initialize the model
		this.documentModel = ModelProvider.getDocumentModel(getClass());
		// initialize the components
		setLayout(new StackLayout());
		add(new GradientJPanel(Color.BLACK, Color.GRAY), Orientation.BOTTOM);
		// the document display
		this.documentShuffler = new DocumentShuffler();
		this.documentShuffler.setDocumentProvider(new DocumentProvider() {
			public List<Document> getDocuments() {
				try {
					final RandomData randomData = new RandomData();
					return CollectionListProxy.translate(
							documentModel.list(randomData.getProjectId()));
				}
				catch(ParityException px) {
					registerError(px);
					// NOTE ??? Do i re-throw something?  Like BrowserRuntimeError?
					return null;
				}
			}
		});
		add(documentShuffler, Orientation.TOP);
		addListeners(browser);
	}

	/**
	 * Register an un-expected error. At runtime this api will decide on how the
	 * error should be handled.
	 * 
	 * @param t
	 *            The error that has occured.
	 */
	private void registerError(final Throwable t) {}

	/**
	 * Add the required listeners to the jFrame.
	 * 
	 * @param browser
	 *            The main browser.
	 */
	private void addListeners(final Browser browser) {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) { browser.exit(0); }
		});
	}
}
