/*
 * Created On: Fri May 26 2006 16:27 PDT
 */
package com.thinkparity.ophelia.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.Icon;

import com.thinkparity.codebase.FuzzyDateFormat;
import com.thinkparity.codebase.OSUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * <b>Title:</b>thinkParity OpheliaUI Constants<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.73
 */
public final class Constants {

    /** Colour constants. */
    public static final class Colors {
        public static final class Browser {
            // Standard orange and blue from the original logo:
            // Orange:  new Color(252, 146, 62, 255);
            // Blue:    new Color(11, 49, 122, 255);
            public static final class Border {
                // Browser border color
                public static final Color BORDER_COLOR = new Color(130, 130, 130, 255);
            }
            public static final class Link {
                public static final Color LINK_FOREGROUND = new Color(49, 102, 148, 255);
            }
            public static final class List {
                // TODO Can remove these if the tab cell code is stripped out
                public static final Color LIST_EVEN_BG = new Color(245, 246, 247, 255);         // light gray (bluish gray on my monitor)
                public static final Color LIST_FG = Color.BLACK;
                public static final Color LIST_NOT_ENABLED_TEXT_FG = new Color(49, 102, 148, 255);
                public static final Color LIST_ODD_BG = Color.WHITE;
                public static final Color LIST_SELECTION_BG = new Color(207, 221, 241, 255);    // blue
                public static final Color LIST_SELECTION_FG = Color.BLACK;           
            }
            public static final class MainStatus {
                public static final Color BG_GRAD_FINISH = new Color(183, 190, 196, 255);
                public static final Color BG_GRAD_START = Color.WHITE;
                public static final Color CONNECTION_FOREGROUND_OFFLINE = new Color(255, 96, 6, 255);
                public static final Color CONNECTION_FOREGROUND_ONLINE = Color.BLACK;
                public static final Color CUSTOM_MESSAGE_FOREGROUND = Color.BLACK;
                public static final Color TOP_BORDER = new Color(130, 130, 130, 255);
                public static final Color USER_NAME_FOREGROUND = Color.BLACK;
            }
            public static final class MainTitle {
                public static final Color BG_GRAD_FINISH = new Color(192, 197, 205, 255);
                public static final Color BG_GRAD_START = new Color(239, 241, 242, 255);
                public static final Color SEARCH_OUTLINE = new Color(204, 215, 226, 255);
            }
            public static final class MainTitleTop {
                public static final Color BG_GRAD_FINISH = new Color(239, 241, 242, 255);  // about 25% of the gradient
                public static final Color BG_GRAD_START = new Color(251, 252, 252, 255);                
            }
            public static final class Panel {
                public static final Color PANEL_ADDITIONAL_TEXT_FG = new Color(49, 102, 148, 255);
                public static final Color PANEL_BORDER = new Color(212, 212, 212, 255);
                public static final Color PANEL_COLLAPSED_BACKGROUND = new Color(234, 234, 234, 255);
                public static final Color PANEL_COLLAPSED_SELECTION_LINE = new Color(100, 100, 100, 255);
                public static final Color PANEL_CONTAINER_TEXT_FG = Color.BLACK;
                public static final Color PANEL_DISABLED_TEXT_FG = new Color(100, 100, 100, 255);
            }
            public static final class ProgressBar {
                public static final Color BORDER = new Color(115, 106, 96, 255);
            }
            public static final class Publish {
                public static final Color FIRST_CONTACT_BORDER = new Color(212, 212, 212, 255);
            }
            public static final class SemiTransparentLayer {
                public static final float LAYER_ALPHA = 0.30F;
                public static final Color LAYER_COLOR = new Color(239, 241, 242, 255);
            }
            public static final class TabCell {
                // TODO Can remove these if the tab cell code is stripped out
                public static final Color TEXT = Color.BLACK;
                public static final Color TEXT_MOUSEOVER = new Color(0, 0, 120, 255);
            }
            public static final class Window {
                public static final Color BORDER_BOTTOM = new Color(206, 215, 226, 255);
                public static final Color BORDER_BOTTOM_LEFT = new Color(206, 215, 226, 255); 
                public static final Color BORDER_BOTTOM_RIGHT= new Color(212, 220, 229, 255);
                public static final Color BORDER_TOP = new Color(242, 245, 248, 255);
                public static final Color BORDER_TOP_LEFT = new Color(239, 242, 247, 255);
                public static final Color BORDER_TOP_RIGHT = new Color(242, 245, 248, 255);
                public static final Color TITLE_GRADIENT_BOTTOM = new Color(247, 249, 250, 255);
                public static final Color TITLE_GRADIENT_TOP = new Color(250, 251, 253, 255);
            }
        }
        public static final class Swing {
            public static final Color DEFAULT_LIST_SELECTION_BG = new Color(201, 208, 218, 255);
            public static final Color DEFAULT_LIST_SELECTION_FG = Color.BLACK;
            public static final Color MENU_BETWEEN_ITEMS_BG = Color.white;
            public static final Color MENU_BG = new Color(230, 235, 241, 255);
            public static final Color MENU_FG = Color.BLACK;
            public static final Color MENU_ITEM_BG = new Color(230, 236, 241, 255);
            public static final Color MENU_ITEM_FG = Color.BLACK;
            public static final Color MENU_ITEM_SELECTION_BG = new Color(201, 208, 218, 255);
            public static final Color MENU_ITEM_SELECTION_FG = Color.BLACK;
            public static final Color MENU_SELECTION_BG = new Color(201, 208, 218, 255);
            public static final Color MENU_SELECTION_FG = Color.BLACK;
        }
    }

    /** Date format objects. */
    public static final class DateFormats {
        public static final FuzzyDateFormat FUZZY = new FuzzyDateFormat();
    }

    /** Dimension constants. */
    public static final class Dimensions {
        public static final class BrowserWindow {
            public static final Point DEFAULT_LOCATION = new Point(100, 100);
            public static final Dimension DEFAULT_SIZE = new Dimension(700, 400);
            public static final Dimension MIN_SIZE = new Dimension(500, 300);
            public static final class Display {
                public static final Integer STATUS_HEIGHT = 34;
                public static final Integer TITLE_HEIGHT = 48;
            }
        }
        public static final class PopupMenu {
            public static final Integer MINIMUM_WIDTH = 135;
        }
    }

    /** thinkParity directories. */
    public static final class Directories {
        /** A user's data directory <code>File</code>. */
        public static final File USER_DATA;
        /** A user's home directory <code>File</code>. */
        public static final File USER_HOME;
        static {
            switch (OSUtil.getOS()) {
            case WINDOWS_XP:
                USER_HOME = new File(System.getenv("USERPROFILE"));
                USER_DATA = new File(USER_HOME, "My Documents\\thinkParity");
                break;
            case LINUX:
            case MAC_OSX:
                USER_HOME = new File(System.getenv("HOME"));
                USER_DATA = new File(USER_HOME, "Documents/thinkParity");
                break;
            default:
                throw Assert.createUnreachable("Unknown os.");
            }
            if (!USER_DATA.exists()) {
                Assert.assertTrue(USER_DATA.mkdirs(),
                        "Cannot create directory {0}.",
                        USER_DATA.getAbsolutePath());
            } else {
                Assert.assertTrue(USER_DATA.isDirectory(),
                        "{0} is not a directory.",
                        USER_DATA.getAbsolutePath());
            }
        }
        public static final class ThinkParity {
            public static final File DIRECTORY = new File(System.getProperty("thinkparity-directory"));
            public static final File PLUGIN_ROOT = new File(DIRECTORY, DirectoryNames.ThinkParity.PLUGIN_ROOT);
        }
    }

    /** thinkParity directory names. */
    public static final class DirectoryNames {
        public static final String DEFAULT_PROFILE = "Default";
        public static final String PLUGIN_LIB = "lib";
        public static final class ThinkParity {
            public static final String PLUGIN_ROOT = "plugins";
        }
    }

    /** thinkParity file extensions. */
    public static final class FileExtensions {
        public static final String JAR = ".jar";
        public static final String PAR = ".par";
    }
    public static final class Files {
        public static final File EXECUTABLE = new File(
                System.getProperty("thinkparity-executable"));
        public static final File IMAGE_EXECUTABLE = new File(
                System.getProperty("thinkparity.image-executable"));
        public static final File JAR = new File(
                Directories.ThinkParity.DIRECTORY, "thinkParity.jar");
    }
    /** thinkParity icons. */
    public static final class Icons {
        public static final class BrowserTitle {
            public static final Icon LEFTMOST_TAB_SELECTED =
                ImageIOUtil.readIcon("BrowserTitle_LeftmostTabSelected.png");
            public static final Icon TAB =
                ImageIOUtil.readIcon("BrowserTitle_Tab.png");
            public static final Icon TAB_ROLLOVER =
                ImageIOUtil.readIcon("BrowserTitle_TabRollover.png");
            public static final Icon TAB_SELECTED =
                ImageIOUtil.readIcon("BrowserTitle_TabSelected.png");
        }
        public static final class Tray {
            public static final Icon TRAY_ICON_OFFLINE =
                ImageIOUtil.readIcon("ThinkParityGray16x16.png");
            public static final Icon TRAY_ICON_ONLINE =
                ImageIOUtil.readIcon("ThinkParity16x16.png");
        }
    }
    public static final class Images {
        public static final BufferedImage WINDOW_ICON_IMAGE = ImageIOUtil.read("ThinkParity32x32.png");
        public static final class BrowserTitle {
            public static final BufferedImage BROWSER_BOTTOM_LEFT_INNER =
                ImageIOUtil.read("BrowserBottomLeftInner.png");
            public static final BufferedImage BROWSER_BOTTOM_LEFT_OUTER =
                ImageIOUtil.read("BrowserBottomLeftOuter.png");            
            public static final BufferedImage BROWSER_BOTTOM_RIGHT_INNER =
                ImageIOUtil.read("BrowserBottomRightInner.png");
            public static final BufferedImage BROWSER_BOTTOM_RIGHT_OUTER =
                ImageIOUtil.read("BrowserBottomRightOuter.png");
            public static final BufferedImage BROWSER_TOP_LEFT_INNER =
                ImageIOUtil.read("BrowserTopLeftInner.png");
            public static final BufferedImage BROWSER_TOP_LEFT_OUTER =
                ImageIOUtil.read("BrowserTopLeftOuter.png");
            public static final BufferedImage BROWSER_TOP_RIGHT_INNER =
                ImageIOUtil.read("BrowserTopRightInner.png");
            public static final BufferedImage BROWSER_TOP_RIGHT_OUTER =
                ImageIOUtil.read("BrowserTopRightOuter.png");
            public static final BufferedImage DIALOG_BACKGROUND =
                ImageIOUtil.read("DialogBackground.png");
            public static final BufferedImage DIALOG_BOTTOM_LEFT =
                ImageIOUtil.read("DialogBottomLeft.png");
            public static final BufferedImage DIALOG_BOTTOM_RIGHT =
                ImageIOUtil.read("DialogBottomRight.png");
            public static final BufferedImage DIALOG_TOP_LEFT =
                ImageIOUtil.read("DialogTopLeft.png");
            public static final BufferedImage DIALOG_TOP_RIGHT =
                ImageIOUtil.read("DialogTopRight.png");   
            public static final BufferedImage HALO =
                ImageIOUtil.read("BrowserTitle_SearchHalo.png");
            public static final BufferedImage LOGO =
                ImageIOUtil.read("thinkParityLogo.png");
            public static final BufferedImage SEARCH_BACKGROUND =
                ImageIOUtil.read("BrowserTitle_SearchBackground.png");
        }
    }
    public static final class InsetFactors {
        public static final Float LEVEL_0 = 1.0F;
        public static final Float LEVEL_1 = 2.0F;
        public static final Float LEVEL_2 = 4.0F;
    }
    /** Lookup keys. */
    public static final class Keys {
        public static final class Persistence {
            public static final String CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY =
                "com.thinkparity.ophelia.browser.platform.action.container.AddDocument#CurrentDirectory";
        }
    }
    /** Menu constants. */
    public static final class Menus {
        public static final class Swing {
            /** The submenu offset in X direction. */
            public static final int SUBMENU_POPUP_OFFSET_X = -1;
            /** The submenu offset in Y direction. */
            public static final int SUBMENU_POPUP_OFFSET_Y = 0;
        }
    }
    public static final class PopupMenuInfo {
        public static final Integer ACTIVATION_DELAY = 200; // Milliseconds
    }

    /** Window resize constants. */
    public static final class Resize {
        public static final Integer EDGE_PIXEL_BUFFER = 6;
    }

    /** Scrollbar constants. */
    public static final class ScrollBar {
        public static final Integer UNIT_INCREMENT = 25;
    }

    public static final class Search {
        public static final Integer ACTIVATION_DELAY = 500; // Milliseconds
    }

    public static final class Session {
        public static final Long CONNECT_TIMER_PERIOD = 1 * 60 * 1000L;
        public static final Long RECONNECT_DELAY = 1 * 60 * 1000L;
    }
    
    public static final class ShutdownHooks {
        public static final class Name {
            public static final String REMOVE_FIREWALL_RULE = "TPS-OpheliaUI-RemoveFirewallRule";
        }
        public static final class Priority {
            public static final Integer REMOVE_FIREWALL_RULE = Integer.valueOf(9);
        }
    }

    public static final class Sundry {
        public static final String FIREWALL_RULE_NAME = "thinkParity(TM) Platform";
    }

    public static final class WindowUtil {
        public static final Integer BROWSER_WINDOW_SIZE = 9;
        public static final Integer DEFAULT_SIZE = 5;
        public static final Integer DIALOG_WINDOW_SIZE = 9;
        public static final Integer NOTIFICATION_WINDOW_SIZE = 9;
    }
}
