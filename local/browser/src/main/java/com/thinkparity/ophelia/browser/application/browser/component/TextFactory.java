/*
 * Jan 13, 2006
 */
package com.thinkparity.ophelia.browser.application.browser.component;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import javax.swing.JMenuItem;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * <b>Title:</b>thinkParity OpheliaUI Text Factory<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class TextFactory extends ComponentFactory {

    /** A <code>JPopupMenu</code> for all common text component items. */
    private static final JPopupMenu JPOPUP_MENU;

    /** A localization bundle for the popup menu. */
    private static final ResourceBundle RESOURCE_BUNDLE;

	/** A singleton instance of <code>TextFactory</code>. */
	private static final TextFactory SINGLETON;

	static {
	    SINGLETON = new TextFactory();
        RESOURCE_BUNDLE = ResourceBundle.getBundle("localization/Misc_Messages");
        JPOPUP_MENU = MenuFactory.createPopup();

        final JMenuItem copy = new JMenuItem(RESOURCE_BUNDLE.getString("TextFactory.Copy.Text"));
        copy.setMnemonic(RESOURCE_BUNDLE.getString("TextFactory.Copy.Mnemonic").charAt(0));
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ((JTextComponent) JPOPUP_MENU.getInvoker()).copy();
            }
        });
        JPOPUP_MENU.add(copy);

        final JMenuItem cut = new JMenuItem(RESOURCE_BUNDLE.getString("TextFactory.Cut.Text"));
        cut.setMnemonic(RESOURCE_BUNDLE.getString("TextFactory.Cut.Mnemonic").charAt(0));
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ((JTextComponent) JPOPUP_MENU.getInvoker()).cut();
            }
        });
        JPOPUP_MENU.add(cut);

        final JMenuItem paste = new JMenuItem(RESOURCE_BUNDLE.getString("TextFactory.Paste.Text"));
        paste.setMnemonic(RESOURCE_BUNDLE.getString("TextFactory.Paste.Mnemonic").charAt(0));
        paste.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ((JTextComponent) JPOPUP_MENU.getInvoker()).paste();
            }
        });
        JPOPUP_MENU.add(paste);

        final JMenuItem delete = new JMenuItem(RESOURCE_BUNDLE.getString("TextFactory.Delete.Text"));
        delete.setMnemonic(RESOURCE_BUNDLE.getString("TextFactory.Delete.Mnemonic").charAt(0));
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                final JTextComponent jTextComponent = ((JTextComponent) JPOPUP_MENU.getInvoker());
                final int start = jTextComponent.getSelectionStart();
                final int end = jTextComponent.getSelectionEnd();
                try {
                    jTextComponent.getDocument().remove(start, end);
                } catch (final BadLocationException blx) {
                    new Log4JWrapper().logError(blx,
                            "Could not remove text from {0} at location [{0},{1}].",
                            jTextComponent.getName(), start, end);
                }
            }
        });
        JPOPUP_MENU.add(delete);

        JPOPUP_MENU.addSeparator();
        final JMenuItem selectAll = new JMenuItem(RESOURCE_BUNDLE.getString("TextFactory.SelectAll.Text"));
        selectAll.setMnemonic(RESOURCE_BUNDLE.getString("TextFactory.SelectAll.Mnemonic").charAt(0));
        selectAll.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                ((JTextComponent) JPOPUP_MENU.getInvoker()).selectAll();
            }
        });
        JPOPUP_MENU.add(selectAll);
	}

    public static JTextField create() {
        return SINGLETON.doCreate();
    }

    public static JTextField create(final Font font) {
        return SINGLETON.doCreate(font);
    }

    public static JTextArea createArea() {
        return SINGLETON.doCreateArea();
    }

    public static JTextArea createArea(final Font font) {
        return SINGLETON.doCreateArea(font);
    }

    public static JPasswordField createPassword() {
        return SINGLETON.doCreatePassword();
    }

    public static JPasswordField createPassword(final Font font) {
        return SINGLETON.doCreatePassword(font);
    }

	/**
     * Create TextFactory.
     *
	 */
	private TextFactory() {
        super();
	}

	private JTextField doCreate() {
		final JTextField jTextField = new JTextField();
		applyDefaultFont(jTextField);
        applyJPopupMenu(jTextField, JPOPUP_MENU);
		return jTextField;		
	}

	private JTextField doCreate(final Font font) {
        final JTextField jTextField = doCreate();
        applyFont(jTextField, font);
        return jTextField;      
    }

	private JTextArea doCreateArea() {
		final JTextArea jTextArea = new JTextArea();
		applyDefaultFont(jTextArea);
        applyJPopupMenu(jTextArea, JPOPUP_MENU);
		return jTextArea;
	}

    private JTextArea doCreateArea(final Font font) {
        final JTextArea jTextArea = doCreateArea();
        applyFont(jTextArea, font);
        return jTextArea;
    }

    private JPasswordField doCreatePassword() {
        final JPasswordField jPasswordField = new JPasswordField();
        applyDefaultFont(jPasswordField);
        return jPasswordField;
    }

    private JPasswordField doCreatePassword(final Font font) {
        final JPasswordField jPasswordField = doCreatePassword();
        applyFont(jPasswordField, font);
        return jPasswordField;
    }
}
