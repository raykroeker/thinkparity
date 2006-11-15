/*
 * Created On: Fri May 26 2006 16:27 PDT
 */
package com.thinkparity.ophelia.browser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.MessageFormat;

import javax.swing.Icon;

import com.thinkparity.codebase.Application;
import com.thinkparity.codebase.FuzzyDateFormat;

import com.thinkparity.ophelia.browser.util.ImageIOUtil;

/**
 * Browser constants.
 *
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public final class Constants {
    
    /** Colour constants. */
    public static final class Colors {
        public static final class Browser {
            public static final class Border {
                public static final Color BORDER_COLOR = new Color(130, 130, 130, 255);
            }
            public static final class MainStatus {
                public static final Color BG_GRAD_FINISH = new Color(183, 190, 196, 255);
                public static final Color BG_GRAD_START = Color.WHITE;
                public static final Color CONNECTION_FOREGROUND = new Color(255, 96, 6, 255);
                public static final Color TOP_BORDER = new Color(130, 130, 130, 255);
            }
            public static final class MainTitle {
                public static final Color BG_GRAD_FINISH = new Color(192, 197, 205, 255);
                public static final Color BG_GRAD_START = new Color(239, 241, 242, 255);
                public static final Color SEARCH_BACKGROUND = new Color(237, 241, 244, 255);
                public static final Color SEARCH_OUTLINE = new Color(204, 215, 226, 255);
                public static final Color SIGN_UP_BACKGROUND = new Color(255, 199, 60, 70);
                public static final Color BOTTOM_BORDER = new Color(167, 167, 167, 255);
            }
            public static final class MainTitleTop {
                public static final Color BG_GRAD_FINISH = new Color(239, 241, 242, 255);  // about 25% of the gradient
                public static final Color BG_GRAD_START = new Color(251, 252, 252, 255);                
            }
            public static final class Window {
                public static final Color BORDER_COLOR_OUTER = new Color(110, 110, 130, 255);
                public static final Color BORDER_COLOR_MID_AT_TOP = new Color(176, 181, 189, 255);      // Continues gradient exactly
                public static final Color BORDER_COLOR_MID_AT_BOTTOM = new Color(134, 138, 144, 255);   // MainTitle.BG_GRAD_FINISH * 0.7
                public static final Color BORDER_COLOR_INNER_AT_TOP = new Color(154, 158, 164, 255);    // MainTitle.BG_GRAD_FINISH * 0.8
                public static final Color BORDER_COLOR_INNER_AT_BOTTOM = new Color(134, 138, 144, 255); // MainTitle.BG_GRAD_FINISH * 0.7
                public static final Color BG_GRAD_FINISH = new Color(173, 177, 185, 255);      // MainTitle.BG_GRAD_FINISH * 0.9
                public static final Color BG_GRAD_START = new Color(221, 227, 236, 255);       // MainTitle.BG_GRAD_FINISH * 1.15
                public static final Color TITLE_BOTTOM_BORDER = new Color(154, 158, 164, 255);
            }
            public static final class TabCell {
                public static final Color TEXT = Color.BLACK;
                public static final Color TEXT_CLOSED = Color.BLACK;
                public static final Color TEXT_MOUSEOVER = new Color(0, 0, 120, 255);
            }
            public static final class UpdateProfile {
                public static final Color EMAIL_CELL_BG = new Color(255, 255, 255, 255);
                public static final Color EMAIL_CELL_BG_SELECTED = new Color(255, 0, 0, 255);
            }
            public static final class Table {
                public static final Color HEADER_BG = new Color(245, 248, 252, 255);
                public static final Color HEADER_FG = Color.BLACK;
                public static final Color HEADER_ROLLOVER_BG = new Color(87, 136, 206, 255);
                public static final Color HEADER_ROLLOVER_FG = Color.WHITE;
                public static final Color ROW_EVEN_BG = new Color(245, 246, 247, 255);
                public static final Color ROW_ODD_BG = Color.WHITE;
            }
            public static final class SemiTransparentLayer {
                public static final Color LAYER_COLOR = new Color(239, 241, 242, 255);
                public static final float LAYER_ALPHA = 0.30F;
            }
            public static final class List {
                public static final Color LIST_SELECTION_BG = new Color(207, 221, 241, 255);    // blue
                public static final Color LIST_SELECTION_FG = Color.BLACK;
                public static final Color LIST_FG = Color.BLACK;
                public static final Color LIST_EVEN_BG = new Color(245, 246, 247, 255);         // light gray (bluish gray on my monitor)
                public static final Color LIST_ODD_BG = Color.WHITE;
                public static final Color LIST_EXPANDED_BG = Color.WHITE;
                public static final Color LIST_GRADIENT_DARK = new Color(204, 208, 214, 255);   // darker color taken from browser title area
                public static final Color LIST_GRADIENT_LIGHT = new Color(245, 246, 247, 255);  // lighter color taken from browser title area
                public static final Color LIST_SELECTION_BORDER = new Color(130, 130, 130, 255); // new Color(100, 100, 100, 255);   // dark gray
                public static final Color INNER_LIST_SELECTION_BORDER = new Color(100, 100, 100, 255);   // dark gray
                public static final Color LIST_LACK_MOST_RECENT_VERSION_FG = new Color(100, 100, 100, 255);  // dark gray                
            }
            public static final class Menu {
                public static final Color MENU_BORDER = new Color(157, 157, 161, 255);
            }
        }
        public static final class Swing {
            public static final Color DEFAULT_LIST_SELECTION_BG = new Color(207, 221, 241, 255);
            public static final Color DEFAULT_LIST_SELECTION_FG = Color.BLACK;
            public static final Color MENU_ITEM_SELECTION_BG = new Color(187, 183, 199, 255);
            public static final Color MENU_ITEM_SELECTION_FG = Color.BLACK;
            public static final Color MENU_ITEM_BG = new Color(245, 246, 247, 255);
            public static final Color MENU_ITEM_FG = Color.BLACK;
            public static final Color MENU_SELECTION_BG = new Color(187, 183, 199, 255);
            public static final Color MENU_SELECTION_FG = Color.BLACK;
            public static final Color MENU_BG = new Color(245, 246, 247, 255);
            public static final Color MENU_FG = Color.BLACK;
        }
    }

    /** Date format objects. */
    public static final class DateFormats {
        public static final FuzzyDateFormat FUZZY = new FuzzyDateFormat();
    }

    /** Dimension constants. */
    public static final class Dimensions {
        public static final class BrowserWindow {
            public static final Dimension DEFAULT_SIZE = new Dimension(450, 587);
            public static final Point DEFAULT_LOCATION = new Point(100, 100);
            public static final Dimension MIN_SIZE = new Dimension(400, 200);
            public static final class Display {
                public static final Integer STATUS_HEIGHT = 34;
                public static final Integer TITLE_HEIGHT = 48;
            }
        }
    }

    /** Parity directories. */
    public static final class Directories {

        /** System property parity.install. */
        public static final File PARITY_INSTALL =
            new File(System.getProperty("parity.install"));

        public static final File PARITY_PLUGIN_ROOT =
            new File(PARITY_INSTALL, "plugins");
    }
    
    public static final class DirectoryNames {
        public static final String DEFAULT_PROFILE = "Default";
        public static final String PLUGIN_LIB = "lib";
    }

    public static final class FileExtensions {
        public static final String JAR = ".jar";
        public static final String PAR = ".par";
    }

    public static final class Icons {
        public static final class BrowserTitle {
            public static final Icon TAB =
                ImageIOUtil.readIcon("BrowserTitle_Tab.png");
            public static final Icon TAB_ROLLOVER =
                ImageIOUtil.readIcon("BrowserTitle_TabRollover.png");
            public static final Icon TAB_SELECTED =
                ImageIOUtil.readIcon("BrowserTitle_TabSelected.png");
            public static final Icon LEFTMOST_TAB =
                ImageIOUtil.readIcon("BrowserTitle_LeftmostTab.png");
            public static final Icon LEFTMOST_TAB_ROLLOVER =
                ImageIOUtil.readIcon("BrowserTitle_LeftmostTabRollover.png");
            public static final Icon LEFTMOST_TAB_SELECTED =
                ImageIOUtil.readIcon("BrowserTitle_LeftmostTabSelected.png");
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
            public static final BufferedImage DIALOG_BOTTOM_LEFT_INNER =
                ImageIOUtil.read("DialogBottomLeftInner.png");
            public static final BufferedImage DIALOG_BOTTOM_LEFT_OUTER =
                ImageIOUtil.read("DialogBottomLeftOuter.png");
            
            public static final BufferedImage DIALOG_BOTTOM_RIGHT_INNER =
                ImageIOUtil.read("DialogBottomRightInner.png");
            public static final BufferedImage DIALOG_BOTTOM_RIGHT_OUTER =
                ImageIOUtil.read("DialogBottomRightOuter.png");
            public static final BufferedImage DIALOG_TOP_LEFT_INNER =
                ImageIOUtil.read("DialogTopLeftInner.png");
            public static final BufferedImage DIALOG_TOP_LEFT_OUTER =
                ImageIOUtil.read("DialogTopLeftOuter.png");
            public static final BufferedImage DIALOG_TOP_RIGHT_INNER =
                ImageIOUtil.read("DialogTopRightInner.png");
            public static final BufferedImage DIALOG_TOP_RIGHT_OUTER =
                ImageIOUtil.read("DialogTopRightOuter.png");
            public static final BufferedImage HALO =
                ImageIOUtil.read("BrowserTitle_SearchHalo.png");
            public static final BufferedImage LOGO =
                ImageIOUtil.read("BrowserTitle_Logo.png");
        }
    }

    public static final class InsetFactors {
        public static final Float LEVEL_0 = 1.0F;
        public static final Float LEVEL_1 = 2.0F;
        public static final Float LEVEL_2 = 4.0F;
    }

    /** Java constants. */
    public static final class Java {

        /** Executable file. */
        public static final String EXECUTABLE =
                new StringBuffer(System.getProperty("java.home"))
                .append(File.separator).append("bin")
                .append(File.separator).append("javaw")
                .toString();

        public static final String MAIN_CLASS = "com.thinkparity.ThinkParity";

        /** Class path option. */
        public static final String OPTION_CLASS_PATH = "-cp";

        public static final String OPTION_CLASS_PATH_VALUE =
                new StringBuffer(".").append(File.separator)
                .append("thinkParity.jar")
                .toString();
        public static final MessageFormat OPTION_PARITY_IMAGE = new MessageFormat("-Dparity.image.name={0}");
        public static final MessageFormat OPTION_PARITY_INSTALL = new MessageFormat("-Dparity.install={0}");
    }

    /** Lookup keys. */
    public static final class Keys {
        public static final class Persistence {
            public static final String CONTAINER_ADD_DOCUMENT_CURRENT_DIRECTORY =
                "com.thinkparity.ophelia.browser.platform.action.container.AddDocument#CurrentDirectory";
            public static final String CONTAINER_EXPORT_DRAFT_SELECTED_DIRECTORY =
                "com.thinkparity.ophelia.browser.platform.action.container.ExportDraft#SelectedDirectory";
            public static final String CONTAINER_EXPORT_VERSION_SELECTED_DIRECTORY =
                "com.thinkparity.ophelia.browser.platform.action.container.ExportVersion#SelectedDirectory";
        }
    }

    /** Logging constants. */
    public static final class Logging {
        public static final String APPLICATION_LOG_ID = Application.OPHELIA + "] [APPLICATION";
        public static final String PLATFORM_LOG_ID = Application.OPHELIA + "] [PLATFORM";
    }

    public static final class Network {
        public static final Integer TIMEOUT = 750;
        public static final Integer TTL = 0;
    }

    public static final class PopupMenuInfo {
        public static final Integer ACTIVATION_DELAY = 200; // Milliseconds
    }

    public static final class Release {
        public static final String ARTIFACT_ID = "lBrowser";
        public static final String GROUP_ID = "com.thinkparity.parity";
        public static final String VERSION = Version.getVersion();
    }

    /** Window resize constants. */
    public static final class Resize {
        public static final Integer EDGE_PIXEL_BUFFER = 6;
    }
    
    public static final class Search {
        public static final Integer ACTIVATION_DELAY = 500; // Milliseconds
    }

    public static final class Session {
        public static final Long CONNECT_TIMER_PERIOD = 1 * 60 * 1000L;
        public static final Long RECONNECT_DELAY = 1 * 60 * 1000L;
    }
}
