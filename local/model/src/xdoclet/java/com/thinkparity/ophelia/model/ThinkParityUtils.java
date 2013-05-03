/*
 * Created On:  11-Jan-07 11:28:03 AM
 */
package com.thinkparity.ophelia.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceMethod;
import com.thinkparity.ophelia.model.qtags.ThinkParityInterfaceMethodImpl;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ThinkParityUtils {

    static String format(final Calendar dateTime) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                dateTime.getTime());
    }

    static Calendar getCurrentDateTime() {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault());
    }

    /**
     * Determine whether or not the metadata is tagged.
     * 
     * @param javaClass
     *            A metadata <code>JavaClass</code>.
     * @param docletTagName
     *            A qdox doclet tag's name <code>String</code>.
     * @return True if the docletTag is applied.
     */
    static Boolean isTagged(final JavaClass javaClass,
            final String docletTagName) {
        final DocletTag[] docletTags = javaClass.getTags();
        for (final DocletTag docletTag : docletTags) {
            if (docletTagName.equals(docletTag.getName()))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Determine the java method's visibility.
     * 
     * @param javaMethod
     *            A metadata <code>JavaMethod</code>.
     * @param docletTagName
     *            A qdox doclet tag's name <code>String</code>.
     * @return True if the docletTag is applied.
     */
    static Boolean isTagged(final JavaMethod javaMethod,
            final Visibility visibility) {
        final ThinkParityInterfaceMethod tpim =
            (ThinkParityInterfaceMethod) javaMethod.getTagByName(
                    ThinkParityInterfaceMethodImpl.NAME);
        if (null == tpim) {
            return Boolean.FALSE;
        } else {
            return tpim.getVisibility().equals(visibility.toString().toLowerCase());
        }
    }


    /**
     * Create ThinkParityUtils.
     *
     */
    private ThinkParityUtils() {
        super();
    }

    enum Visibility { EXTERNAL, INTERNAL }
}
