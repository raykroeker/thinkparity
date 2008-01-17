/*
 * Created On:  26-Sep-07 7:49:07 PM
 */
package com.thinkparity.desdemona.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.logging.or.UserRenderer;

import com.thinkparity.desdemona.model.profile.payment.Currency;
import com.thinkparity.desdemona.model.profile.payment.PaymentPlan;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity Desdemona Model Payment Plan Log4J Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PaymentPlanRenderer implements ObjectRenderer {

    /** A currency renderer. */
    private final ObjectRenderer currencyRenderer;

    /** A user renderer. */
    private final ObjectRenderer userRenderer;

    /**
     * Create PaymentPlanRenderer.
     *
     */
    public PaymentPlanRenderer() {
        super();
        this.currencyRenderer = new CurrencyRenderer();
        this.userRenderer = new UserRenderer();
    }

    /**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
     */
    @Override
    public String doRender(final Object o) {
        if (null == o) {
            return Separator.Null.toString();
        } else {
            final PaymentPlan o2 = (PaymentPlan) o;
            return StringUtil.toString(PaymentPlan.class,
                    "getCurrency()", doRender(o2.getCurrency()),
                    "getId()", o2.getId(),
                    "getName()", o2.getName(),
                    "getOwner()", doRender(o2.getOwner()),
                    "getUniqueId()", o2.getUniqueId());
        }
    }

    /**
     * Render a currency.
     * 
     * @param currency
     *            A <code>Currency</code>.
     * @return A <code>String</code>.
     */
    private String doRender(final Currency currency) {
        return currencyRenderer.doRender(currency);
    }

    /**
     * Render a user.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>String</code>.
     */
    private String  doRender(final User user) {
        return userRenderer.doRender(user);
    }
}
