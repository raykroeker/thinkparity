/*
 * Created On:  28-Feb-07 8:24:03 PM
 */
package com.thinkparity.desdemona.model.rules;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Internal Rules Interface<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalRuleModel extends RuleModel {

    /**
     * Determine if the model user is publish restricted.
     * 
     * @return True if the model user is publish restricted.
     */
    public Boolean isPublishRestricted();
}
