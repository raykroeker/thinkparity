/**
 * Created On: 23-Jun-2006 2:57:44 PM
 * $Id$
 */
package com.thinkparity.model.parity.model.filter.user;

import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.model.parity.model.filter.AbstractFilter;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class SearchUser extends AbstractFilter<User> {

    /**
     * Result of an index search.
     * 
     */
    private final List<IndexHit> searchResults;

    /**
     * Create a Search.
     */
    public SearchUser(final List<IndexHit> searchResults) {
        this.searchResults = searchResults;
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#debug(org.apache.log4j.Logger)
     * 
     */
    public void debug(final Logger logger) {
        logger.debug("[LMODEL] [FILTER] [USER] [INDEX HIT MATCH]");
        for(final IndexHit indexHit : searchResults) { logger.debug(indexHit); }
    }

    /**
     * @see com.thinkparity.model.parity.model.filter.Filter#doFilter(T)
     * 
     */
    public Boolean doFilter(final User o) {
        for(final IndexHit indexHit : searchResults) {
            if(indexHit.getId().equals(o.getId())) { return Boolean.FALSE; }
        }
        return Boolean.TRUE;
    }

    public void setResults(final List<IndexHit> searchResults) {
        this.searchResults.clear();
        this.searchResults.addAll(searchResults);
    }
}
