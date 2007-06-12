/*
 * Created On:  3-Jun-07 2:57:15 PM
 */
package com.thinkparity.desdemona.web.service;

import javax.jws.WebService;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.model.rules.RuleModel;

import com.thinkparity.service.AuthToken;
import com.thinkparity.service.RuleService;

/**
 * <b>Title:</b>thinkParity Desdemona Rule <br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
@WebService(endpointInterface = "com.thinkparity.service.RuleService")
public class RuleSEI extends ServiceSEI implements RuleService {

    /**
     * Create RuleSEI.
     *
     */
    public RuleSEI() {
        super();
    }

    /**
     * @see com.thinkparity.services.RulesService#isPublishRestricted(com.thinkparity.service.AuthToken, com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Boolean isPublishRestricted(final AuthToken authToken,
            final JabberId publishTo) {
        return getModel(authToken).isPublishRestrictedTo(publishTo);
    }

    /**
     * Obtain a rule model for an authenticated session.
     * 
     * @param authToken
     *            A session <code>AuthToken</code>.
     * @return An instance of <code>RuleModel</code>.
     */
    private RuleModel getModel(final AuthToken authToken) {
        return getModelFactory(authToken).getRuleModel();
    }
}
