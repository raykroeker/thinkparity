/*
 * Created On: Jul 20, 2006 12:36:53 PM
 */
package com.thinkparity.codebase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class VCard extends org.jivesoftware.smackx.packet.VCard {

    /** Create VCard. */
    public VCard() { super(); }

    /**
     * Create VCard.
     * 
     * @param jiveVCard
     *            A jive vCard.
     */
    VCard(final org.jivesoftware.smackx.packet.VCard jiveVCard) {
        this();
        final Field[] fields = org.jivesoftware.smackx.packet.VCard.class.getDeclaredFields();
        for(int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            if (field.getDeclaringClass() == VCard.class &&
                    !Modifier.isFinal(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    field.set(this, field.get(jiveVCard));
                }
                catch(IllegalAccessException iax) {
                    throw new RuntimeException(iax);
                }
            }
        }
    }
}
