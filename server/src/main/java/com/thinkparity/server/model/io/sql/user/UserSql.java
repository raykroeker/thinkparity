/*
 * Created On: Jul 20, 2006 2:43:22 PM
 */
package com.thinkparity.server.model.io.sql.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.io.sql.AbstractSql;

/**
 * @author raymond@thinkparity.com
 * @version
 */
public class UserSql extends AbstractSql {

    /** Sql to read e-mail addresses. */
    private static final String SQL_READ_EMAIL =
            new StringBuffer("select PUE.USERNAME,PUE.EMAIL ")
            .append("from parityUserEmail PUE ")
            .append("inner join jiveUser JU on PUE.USERNAME = JU.USERNAME ")
            .append("where JU.USERNAME=?")
            .toString();

    /** Create UserSql. */
    public UserSql() { super(); }

    public List<String> readEmail(final JabberId jabberId) throws SQLException {
        Connection cx = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cx = getCx();
            logStatement(SQL_READ_EMAIL);
            ps = cx.prepareStatement(SQL_READ_EMAIL);
            set(ps, 1, jabberId.getUsername());
            rs = ps.executeQuery();
            final List<String> emails = new ArrayList<String>();
            while(rs.next()) { emails.add(rs.getString("EMAIL")); }
            return emails;
        }
        finally { close(cx, ps, rs); }

    }
}
