/*
 * Created On:  8-Aug-07 5:37:19 PM
 */
package com.thinkparity.desdemona.model.profile.payment.provider;

import java.util.Properties;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentProviderFactory {

    public static PaymentProvider createInstance(final String className,
            final Properties properties) {
        try {
            final Class<?> type = Class.forName(className);
            final PaymentProvider provider = (PaymentProvider) type.newInstance();
            provider.configure(properties);
            return provider;
        } catch (final ClassNotFoundException cnfx) {
            throw new RuntimeException(cnfx);
        } catch (final InstantiationException ix) {
            throw new RuntimeException(ix);
        } catch (final IllegalAccessException iax) {
            throw new RuntimeException(iax);
        }
    }

    /**
     * Create PaymentProviderFactory.
     *
     */
    private PaymentProviderFactory() {
        super();
    }
}
