/*
 * Created On:  20-Feb-07 2:28:04 PM
 */
package com.thinkparity.codebase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.thinkparity.codebase.sort.StringComparator;

/**
 * <b>Title:</b>thinkParity Locale Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class LocaleUtil {

    /** A singleton instance of <code>LocaleUtil</code>. */
    private static LocaleUtil INSTANCE;

    static {
        INSTANCE = new LocaleUtil();
    }

    /**
     * Obtain an instance of locale util.
     * 
     * @return An instance of <code>LocaleUtil</code>.
     */
    public static LocaleUtil getInstance() {
        return INSTANCE;
    }

    /**
     * Create LocaleUtil.
     *
     */
    private LocaleUtil() {
        super();
    }

    /**
     * Obtain a list of available locales. The list is filtered to those that
     * have both a language and a country specified; with no duplicates. The
     * list is sorted by the display country.
     * 
     * @return A <code>Locale[]</code>.
     */
    public Locale[] getAvailableLocales() {
        final Locale[] locales = Locale.getAvailableLocales();
        final List<Locale> availableLocales = new ArrayList<Locale>(locales.length);
        for (final Locale locale : locales) {
            if (isValid(locale))
                if (!contains(availableLocales, locale))
                    availableLocales.add(locale);
        }
        Collections.sort(availableLocales, new Comparator<Locale>() {
            final StringComparator stringComparator =
                new StringComparator(Boolean.TRUE);
            public int compare(final Locale o1, final Locale o2) {
                return stringComparator.compare(o1.getDisplayCountry(), o2.getDisplayCountry());
            }
        });
        return availableLocales.toArray(new Locale[] {});
    }

    /**
     * Determine if the list of locales already contains the locale. A
     * determination is made based solely upon the language and country. The
     * variant is ignored.
     * 
     * @param locales
     *            A <code>List</code> of <code>Locale</code>s.
     * @param locale
     *            A <code>Locale</code>.
     * @return True if the local exists in the list.
     */
    private boolean contains(final List<Locale> locales, final Locale locale) {
        for (final Locale l : locales) {
            if (l.getISO3Country().equals(locale.getISO3Country())
                    && l.getISO3Language().equals(locale.getISO3Language()))
                return true;
        }
        return false;
    }

    /**
     * Determine if a locale is valid. A locale is valid if the country and
     * language are specified and the variant is not.
     * 
     * @param locale
     *            A <code>Locale</code>.
     * @return True if the locale is valid.
     */
    private boolean isValid(final Locale locale) {
        return "".equals(locale.getVariant())
                && !"".equals(locale.getISO3Country())
                && !"".equals(locale.getISO3Language());
    }
}
