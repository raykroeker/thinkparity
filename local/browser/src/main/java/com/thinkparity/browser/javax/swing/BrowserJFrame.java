/*
 * Dec 30, 2005
 */
package com.thinkparity.browser.javax.swing;

import java.awt.Color;
import java.awt.Dimension;
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
import com.thinkparity.browser.java.awt.StackLayout;
import com.thinkparity.browser.java.awt.StackLayout.Orientation;
import com.thinkparity.browser.javax.swing.document.DocumentProvider;
import com.thinkparity.browser.javax.swing.document.DocumentShuffler;
import com.thinkparity.browser.javax.swing.misc.ColorPanel;
import com.thinkparity.browser.log4j.BrowserLoggerFactory;
import com.thinkparity.browser.model.CollectionListProxy;
import com.thinkparity.browser.model.ModelProvider;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserJFrame extends JFrame {

	/**
	 * The default size of the browser.
	 * 
	 */
	private static final Dimension defaultSize;

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	static {
		defaultSize = new Dimension(267, 406);
	}

	/**
	 * Obtain the default size of the browser.
	 *
	 */
	public static Dimension getDefaultSize() { return defaultSize; }

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
	 * Handle to an apache logger.
	 * 
	 */
	protected final Logger logger = BrowserLoggerFactory.getLogger(getClass());

	private final Color backgroundColor =
		BrowserColorUtil.getRGBColor(247, 248, 250, 255);

	/**
	 * Document model api.
	 * 
	 */
	protected final DocumentModel documentModel =
		ModelProvider.getDocumentModel(getClass());

	/**
	 * Project model api.
	 * 
	 */
	protected final ProjectModel projectModel =
		ModelProvider.getProjectModel(getClass());

	/**
	 * The display component for documents.
	 * 
	 */
	private final DocumentShuffler documentShuffler;

	/**
	 * Create a BrowserJFrame.
	 * 
	 * @throws HeadlessException
	 */
	public BrowserJFrame(final Browser browser) throws HeadlessException {
		super();
		// initialize the state
		new BrowserJFrameState(this);
		// initialize the components
		setLayout(new StackLayout());
		add(new ColorPanel(backgroundColor), Orientation.BOTTOM);
		// the document display
		this.documentShuffler = new DocumentShuffler();
		this.documentShuffler.setDocumentProvider(new DocumentProvider() {
			public List<Document> getDocuments() {
				try {
					final Project myProjects = projectModel.getMyProjects();
					return CollectionListProxy.translate(
							documentModel.list(myProjects.getId()));
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

	/**
	 * Register an un-expected error. At runtime this api will decide on how the
	 * error should be handled.
	 * 
	 * @param t
	 *            The error that has occured.
	 */
	private void registerError(final Throwable t) {}
}
