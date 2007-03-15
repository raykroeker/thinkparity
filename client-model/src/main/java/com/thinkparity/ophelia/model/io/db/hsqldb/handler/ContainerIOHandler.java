/*
 * Created On: Jun 28, 2006 8:49:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerDraftDocument;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionDelta;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class ContainerIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.ContainerIOHandler {

    /** Sql to insert a version relationship. */
    private static final String SQL_ADD_VERSION_REL =
            new StringBuffer("insert into CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("(CONTAINER_ID,CONTAINER_VERSION_ID,ARTIFACT_ID,")
            .append("ARTIFACT_VERSION_ID,ARTIFACT_TYPE_ID) ")
            .append("values (?,?,?,?,?)")
            .toString();

    /** Sql to create a container. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into CONTAINER ")
            .append("(CONTAINER_ID) ")
            .append("values (?)")
            .toString();

    /** Sql to create a container version delta. */
    private static final String SQL_CREATE_ARTIFACT_DELTA =
        new StringBuffer("insert into CONTAINER_VERSION_ARTIFACT_VERSION_DELTA ")
        .append("(CONTAINER_VERSION_DELTA_ID,DELTA,DELTA_ARTIFACT_ID,DELTA_ARTIFACT_VERSION_ID) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a container version delta. */
    private static final String SQL_CREATE_DELTA =
        new StringBuffer("insert into CONTAINER_VERSION_DELTA ")
        .append("(CONTAINER_ID,COMPARE_CONTAINER_VERSION_ID,")
        .append("COMPARE_TO_CONTAINER_VERSION_ID) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to create a draft. */
    private static final String SQL_CREATE_DRAFT =
            new StringBuffer("insert into CONTAINER_DRAFT ")
            .append("(CONTAINER_DRAFT_ID,CONTAINER_DRAFT_USER_ID) ")
            .append("values (?,?)")
            .toString();

    /** Sql to create a draft artifact relationship. */
    private static final String SQL_CREATE_DRAFT_ARTIFACT_REL =
            new StringBuffer("insert into CONTAINER_DRAFT_ARTIFACT_REL ")
            .append("(CONTAINER_DRAFT_ID,ARTIFACT_ID,ARTIFACT_STATE) ")
            .append("values (?,?,?)")
            .toString();

    /** Sql to create a draft document. */
    private static final String SQL_CREATE_DRAFT_DOCUMENT =
        new StringBuffer("insert into CONTAINER_DRAFT_DOCUMENT ")
        .append("(CONTAINER_DRAFT_ID,DOCUMENT_ID,CONTENT,CONTENT_SIZE,")
        .append("CONTENT_CHECKSUM,CHECKSUM_ALGORITHM) ")
        .append("values(?,?,?,?,?,?)")
        .toString();

    /** Sql to read the container published to list. */
    private static final String SQL_CREATE_PUBLISHED_TO =
            new StringBuffer("insert into CONTAINER_VERSION_PUBLISHED_TO ")
            .append("(CONTAINER_ID,CONTAINER_VERSION_ID,USER_ID,PUBLISHED_ON) ")
            .append("values (?,?,?,?)")
            .toString();

    /** Sql to create a container version. */
    private static final String SQL_CREATE_VERSION =
            new StringBuffer("insert into CONTAINER_VERSION ")
            .append("(CONTAINER_ID,CONTAINER_VERSION_ID) ")
            .append("values (?,?)")
            .toString();
    
    /** Sql to delete a container. */
    private static final String SQL_DELETE =
            new StringBuffer("delete from CONTAINER ")
            .append("where CONTAINER_ID=?")
            .toString();

    /** Sql to delete a container version delta. */
    private static final String SQL_DELETE_ARTIFACT_DELTA =
        new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_DELTA ")
        .append("where CONTAINER_VERSION_DELTA_ID=(")
        .append("select CONTAINER_VERSION_DELTA_ID ")
        .append("from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("COMPARE_CONTAINER_VERSION_ID=? and ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?)")
        .toString();

    /** Sql to delete all container version deltas. */
    private static final String SQL_DELETE_ARTIFACT_DELTAS =
        new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_DELTA ")
        .append("where CONTAINER_VERSION_DELTA_ID=(")
        .append("select CONTAINER_VERSION_DELTA_ID ")
        .append("from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("(COMPARE_CONTAINER_VERSION_ID=? or ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?))")
        .toString();

    /** Sql to delete a container version delta. */
    private static final String SQL_DELETE_DELTA =
        new StringBuffer("delete from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("COMPARE_CONTAINER_VERSION_ID=? and ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?")
        .toString();

    /** Sql to delete all container version delta. */
    private static final String SQL_DELETE_DELTAS =
        new StringBuffer("delete from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("(COMPARE_CONTAINER_VERSION_ID=? or ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?)")
        .toString();

    /** Sql to delete a draft. */
    private static final String SQL_DELETE_DRAFT =
            new StringBuffer("delete from CONTAINER_DRAFT ")
            .append("where CONTAINER_DRAFT_ID=?")
            .toString();

    /** Sql to delete a draft artifact relationship. */
    private static final String SQL_DELETE_DRAFT_ARTIFACT_REL =
            new StringBuffer("delete from CONTAINER_DRAFT_ARTIFACT_REL ")
            .append("where CONTAINER_DRAFT_ID=? ")
            .append("and ARTIFACT_ID=?")
            .toString();

    /** Sql to delete the draft document. */
    private static final String SQL_DELETE_DRAFT_DOCUMENT =
        new StringBuffer("delete from CONTAINER_DRAFT_DOCUMENT ")
        .append("where CONTAINER_DRAFT_ID=? and DOCUMENT_ID=?")
        .toString();

    /** Sql to delete the draft document. */
    private static final String SQL_DELETE_DRAFT_DOCUMENTS =
        new StringBuffer("delete from CONTAINER_DRAFT_DOCUMENT ")
        .append("where CONTAINER_DRAFT_ID=?")
        .toString();

    /** Sql to delete the published to user list. */
    private static final String SQL_DELETE_PUBLISHED_TO =
            new StringBuffer("delete from CONTAINER_VERSION_PUBLISHED_TO ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to delete a container version. */
    private static final String SQL_DELETE_VERSION =
            new StringBuffer("delete from CONTAINER_VERSION ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to determine if the container version to artifact version exists. */
    private static final String SQL_DOES_EXIST_VERSION =
        new StringBuffer("select COUNT(ARTIFACT_ID) \"COUNT\" ")
        .append("from CONTAINER_VERSION_ARTIFACT_VERSION_REL CVAVR ")
        .append("where CVAVR.CONTAINER_ID=? ")
        .append("and CVAVR.CONTAINER_VERSION_ID=? ")
        .append("and CVAVR.ARTIFACT_ID=? ")
        .append("and CVAVR.ARTIFACT_VERSION_ID=?")
        .toString();

    /** Sql to extract the artifact version delta. */
    private static final String SQL_EXTRACT_DELTA =
        new StringBuffer("select CONTAINER_VERSION_DELTA_ID,DELTA,DELTA_ARTIFACT_ID,DELTA_ARTIFACT_VERSION_ID ")
        .append("from CONTAINER_VERSION_ARTIFACT_VERSION_DELTA CVAVD ")
        .append("where CVAVD.CONTAINER_VERSION_DELTA_ID=?")
        .toString();

    /** Sql to open a draft document input stream. */
    private static final String SQL_OPEN_DRAFT_DOCUMENT =
        new StringBuffer("select CONTENT ")
        .append("from CONTAINER_DRAFT_DOCUMENT ")
        .append("where CONTAINER_DRAFT_ID=? and DOCUMENT_ID=?")
        .toString();

    /** Sql to read a container. */
    private static final String SQL_READ =
            new StringBuffer("select C.CONTAINER_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,ARTIFACT_UNIQUE_ID,")
            .append("UC.JABBER_ID CREATED_BY,A.CREATED_ON,")
            .append("UU.JABBER_ID UPDATED_BY,A.UPDATED_ON,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON, ")
            .append("CD.CONTAINER_DRAFT_USER_ID ")
            .append("from CONTAINER C ")
            .append("inner join ARTIFACT A on C.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
            .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
            .append("left join CONTAINER_DRAFT CD on C.CONTAINER_ID=CD.CONTAINER_DRAFT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID")
            .toString();

    /** Sql to read the artifact delta count. */
    private static final String SQL_READ_ARTIFACT_DELTA_COUNT =
        new StringBuffer("select COUNT(CVD.CONTAINER_VERSION_DELTA_ID) as \"COUNT\" ")
        .append("from CONTAINER_VERSION_DELTA CVD ")
        .append("inner join CONTAINER_VERSION_ARTIFACT_VERSION_DELTA CVAVD ")
        .append("on CVD.CONTAINER_VERSION_DELTA_ID=CVAVD.CONTAINER_VERSION_DELTA_ID ")
        .append("where CVD.CONTAINER_ID=? and ")
        .append("CVD.COMPARE_CONTAINER_VERSION_ID=? and ")
        .append("CVD.COMPARE_TO_CONTAINER_VERSION_ID=?")
        .toString();

    /** Sql to read the artifact delta count. */
    private static final String SQL_READ_ARTIFACT_DELTA_COUNT_2 =
        new StringBuffer("select COUNT(CVD.CONTAINER_VERSION_DELTA_ID) as \"COUNT\" ")
        .append("from CONTAINER_VERSION_DELTA CVD ")
        .append("inner join CONTAINER_VERSION_ARTIFACT_VERSION_DELTA CVAVD ")
        .append("on CVD.CONTAINER_VERSION_DELTA_ID=CVAVD.CONTAINER_VERSION_DELTA_ID ")
        .append("where CVD.CONTAINER_ID=? and ")
        .append("(CVD.COMPARE_CONTAINER_VERSION_ID=? or ")
        .append("CVD.COMPARE_TO_CONTAINER_VERSION_ID=?)")
        .toString();

    /** Sql to read a container. */
    private static final String SQL_READ_BY_CONTAINER_ID =
            new StringBuffer(SQL_READ).append(" ")
            .append("where C.CONTAINER_ID=?")
            .toString();

    /** Sql to read a container. */
    private static final String SQL_READ_BY_TEAM_MEMBER_ID =
            new StringBuffer(SQL_READ).append(" ")
            .append("inner join ARTIFACT_TEAM_REL ATR ")
            .append("on ATR.ARTIFACT_ID=A.ARTIFACT_ID ")
            .append("inner join PARITY_USER TU on ATR.USER_ID=TU.USER_ID ")
            .append("where TU.USER_ID=? ")
            .append("order by C.CONTAINER_ID asc")
            .toString();

    /** Sql to read a version delta. */
    private static final String SQL_READ_DELTA =
        new StringBuffer("select CONTAINER_VERSION_DELTA_ID,CONTAINER_ID,")
        .append("COMPARE_CONTAINER_VERSION_ID,COMPARE_TO_CONTAINER_VERSION_ID ")
        .append("from CONTAINER_VERSION_DELTA CVD ")
        .append("where CVD.CONTAINER_ID=? and ")
        .append("CVD.COMPARE_CONTAINER_VERSION_ID=? and ")
        .append("CVD.COMPARE_TO_CONTAINER_VERSION_ID=?")
        .toString();

    /** Sql to read the delta count. */
    private static final String SQL_READ_DELTA_COUNT =
        new StringBuffer("select COUNT(CONTAINER_VERSION_DELTA_ID) as \"COUNT\" ")
        .append("from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("COMPARE_CONTAINER_VERSION_ID=? and ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?")
        .toString();

    /** Sql to read the delta count. */
    private static final String SQL_READ_DELTA_COUNT_2 =
        new StringBuffer("select COUNT(CONTAINER_VERSION_DELTA_ID) as \"COUNT\" ")
        .append("from CONTAINER_VERSION_DELTA ")
        .append("where CONTAINER_ID=? and ")
        .append("(COMPARE_CONTAINER_VERSION_ID=? or ")
        .append("COMPARE_TO_CONTAINER_VERSION_ID=?)")
        .toString();

    /** Sql to read document versions from the artifact version attachments. */
    private static final String SQL_READ_DOCUMENT_VERSIONS =
            new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
            .append("A.ARTIFACT_UNIQUE_ID,UC.JABBER_ID CREATED_BY,AV.CREATED_ON,")
            .append("UU.JABBER_ID UPDATED_BY,AV.UPDATED_ON,D.DOCUMENT_ID,")
            .append("DV.CONTENT_CHECKSUM,DV.CHECKSUM_ALGORITHM,")
            .append("DV.CONTENT_SIZE,DV.DOCUMENT_VERSION_ID,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
            .append("from CONTAINER_VERSION_ARTIFACT_VERSION_REL CVAVR ")
            .append("inner join ARTIFACT A on CVAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
            .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
            .append("inner join DOCUMENT_VERSION DV on CVAVR.ARTIFACT_ID=DV.DOCUMENT_ID ")
            .append("and CVAVR.ARTIFACT_VERSION_ID=DV.DOCUMENT_VERSION_ID ")
            .append("inner join ARTIFACT_VERSION AV on DV.DOCUMENT_ID=AV.ARTIFACT_ID ")
            .append("and DV.DOCUMENT_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("inner join PARITY_USER UC on AV.CREATED_BY=UC.USER_ID ")
            .append("inner join PARITY_USER UU on AV.UPDATED_BY=UU.USER_ID ")
            .append("where CVAVR.CONTAINER_ID=? and CVAVR.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read documents from the artifact version attachments. */
    private static final String SQL_READ_DOCUMENTS =
            new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
            .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
            .append("A.ARTIFACT_UNIQUE_ID,UC.JABBER_ID CREATED_BY,A.CREATED_ON,")
            .append("UU.JABBER_ID UPDATED_BY,A.UPDATED_ON,")
            .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
            .append("ARI.UPDATED_ON REMOTE_UPDATED_ON ")
            .append("from CONTAINER_VERSION_ARTIFACT_VERSION_REL CVAVR ")
            .append("inner join ARTIFACT A on CVAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
            .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
            .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
            .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
            .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
            .append("where CVAVR.CONTAINER_ID=? and CVAVR.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read a draft. */
    private static final String SQL_READ_DRAFT =
            new StringBuffer("select ATR.ARTIFACT_ID,ATR.USER_ID,")
            .append("U.JABBER_ID,U.NAME,U.ORGANIZATION,U.TITLE,")
            .append("CD.CONTAINER_DRAFT_ID ")
            .append("from ARTIFACT_TEAM_REL ATR inner join PARITY_USER U on ATR.USER_ID=U.USER_ID ")
            .append("inner join CONTAINER_DRAFT CD on ATR.ARTIFACT_ID=CD.CONTAINER_DRAFT_ID ")
            .append("and CD.CONTAINER_DRAFT_USER_ID=ATR.USER_ID ")
            .append("where CD.CONTAINER_DRAFT_ID=?")
            .toString();

    /** Sql to read a container draft document. */
    private static final String SQL_READ_DRAFT_DOCUMENT =
        new StringBuffer("select CONTAINER_DRAFT_ID,DOCUMENT_ID,CONTENT_SIZE,")
        .append("CONTENT_CHECKSUM,CHECKSUM_ALGORITHM ")
        .append("from CONTAINER_DRAFT_DOCUMENT ")
        .append("where CONTAINER_DRAFT_ID=? and DOCUMENT_ID=?")
        .toString();

    /** Sql to count the draft documents. */
    private static final String SQL_READ_DRAFT_DOCUMENT_COUNT =
        new StringBuffer("select COUNT(CONTAINER_DRAFT_ID) \"DOCUMENT_COUNT\" ")
        .append("from CONTAINER_DRAFT_DOCUMENT ")
        .append("where CONTAINER_DRAFT_ID=?")
        .toString();

    /** Sql to read draft documents. */
    private static final String SQL_READ_DRAFT_DOCUMENTS =
        new StringBuffer("select A.ARTIFACT_ID,A.ARTIFACT_NAME,")
        .append("A.ARTIFACT_STATE_ID,A.ARTIFACT_TYPE_ID,")
        .append("A.ARTIFACT_UNIQUE_ID,UC.JABBER_ID CREATED_BY,A.CREATED_ON,")
        .append("UU.JABBER_ID UPDATED_BY,A.UPDATED_ON,")
        .append("ARI.UPDATED_BY REMOTE_UPDATED_BY,")
        .append("ARI.UPDATED_ON REMOTE_UPDATED_ON, ")
        .append("CDAVR.ARTIFACT_STATE DRAFT_ARTIFACT_STATE ")
        .append("from CONTAINER_DRAFT_ARTIFACT_REL CDAVR ")
        .append("inner join ARTIFACT A on CDAVR.ARTIFACT_ID=A.ARTIFACT_ID ")
        .append("inner join PARITY_USER UC on A.CREATED_BY=UC.USER_ID ")
        .append("inner join PARITY_USER UU on A.UPDATED_BY=UU.USER_ID ")
        .append("inner join DOCUMENT D on A.ARTIFACT_ID=D.DOCUMENT_ID ")
        .append("left join ARTIFACT_REMOTE_INFO ARI on A.ARTIFACT_ID=ARI.ARTIFACT_ID ")
        .append("where CDAVR.CONTAINER_DRAFT_ID=?")
        .toString();

    /** Sql to read the container published to list. */
    private static final String SQL_READ_PUBLISHED_TO =
        new StringBuilder("select U.JABBER_ID,U.USER_ID,U.NAME,")
        .append("U.ORGANIZATION,U.TITLE ")
        .append("from CONTAINER C ")
        .append("inner join ARTIFACT A on C.CONTAINER_ID=A.ARTIFACT_ID ")
        .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
        .append("inner join CONTAINER_VERSION CV on AV.ARTIFACT_ID=CV.CONTAINER_ID ")
        .append("and AV.ARTIFACT_VERSION_ID=CV.CONTAINER_VERSION_ID ")
        .append("inner join CONTAINER_VERSION_PUBLISHED_TO CVPT on CV.CONTAINER_ID=CVPT.CONTAINER_ID ")
        .append("and CV.CONTAINER_VERSION_ID=CVPT.CONTAINER_VERSION_ID ")
        .append("inner join PARITY_USER U on CVPT.USER_ID=U.USER_ID ")
        .append("where CV.CONTAINER_ID=? and CV.CONTAINER_VERSION_ID=?")
        .toString();

    /** Sql to read the container published to list. */
    private static final String SQL_READ_PUBLISHED_TO_BY_USER =
            new StringBuffer("select U.JABBER_ID,U.USER_ID,U.NAME,")
            .append("U.ORGANIZATION,U.TITLE,CVPT.CONTAINER_ID,")
            .append("CVPT.PUBLISHED_ON,CVPT.RECEIVED_ON ")
            .append("from CONTAINER C ")
            .append("inner join ARTIFACT A on C.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("inner join CONTAINER_VERSION CV on AV.ARTIFACT_ID=CV.CONTAINER_ID ")
            .append("and AV.ARTIFACT_VERSION_ID=CV.CONTAINER_VERSION_ID ")
            .append("inner join CONTAINER_VERSION_PUBLISHED_TO CVPT on CV.CONTAINER_ID=CVPT.CONTAINER_ID ")
            .append("and CV.CONTAINER_VERSION_ID=CVPT.CONTAINER_VERSION_ID ")
            .append("inner join PARITY_USER U on CVPT.USER_ID=U.USER_ID ")
            .append("where CV.CONTAINER_ID=? and CV.CONTAINER_VERSION_ID=? ")
            .append("and CVPT.PUBLISHED_ON=? and CVPT.USER_ID=?")
            .toString();

    /** Sql to read the published to count. */
    private static final String SQL_READ_PUBLISHED_TO_COUNT =
            new StringBuffer("select COUNT(*) PUBLISHED_TO_COUNT ")
            .append("from CONTAINER_VERSION_PUBLISHED_TO CVPT ")
            .append("where CVPT.CONTAINER_ID=? and CVPT.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read the container published to list. */
    private static final String SQL_READ_PUBLISHED_TO_RECEIPTS =
            new StringBuffer("select U.JABBER_ID,U.USER_ID,U.NAME,")
            .append("U.ORGANIZATION,U.TITLE,CVPT.CONTAINER_ID,")
            .append("CVPT.PUBLISHED_ON,CVPT.RECEIVED_ON ")
            .append("from CONTAINER C ")
            .append("inner join ARTIFACT A on C.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("inner join ARTIFACT_VERSION AV on A.ARTIFACT_ID=AV.ARTIFACT_ID ")
            .append("inner join CONTAINER_VERSION CV on AV.ARTIFACT_ID=CV.CONTAINER_ID ")
            .append("and AV.ARTIFACT_VERSION_ID=CV.CONTAINER_VERSION_ID ")
            .append("inner join CONTAINER_VERSION_PUBLISHED_TO CVPT on CV.CONTAINER_ID=CVPT.CONTAINER_ID ")
            .append("and CV.CONTAINER_VERSION_ID=CVPT.CONTAINER_VERSION_ID ")
            .append("inner join PARITY_USER U on CVPT.USER_ID=U.USER_ID ")
            .append("where CV.CONTAINER_ID=? and CV.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read a container version. */
    private static final String SQL_READ_VERSION =
            new StringBuffer("select CV.CONTAINER_ID,CV.CONTAINER_VERSION_ID,")
            .append("A.ARTIFACT_TYPE_ID,AV.ARTIFACT_UNIQUE_ID,UC.JABBER_ID CREATED_BY,")
            .append("AV.CREATED_ON,AV.ARTIFACT_NAME,AV.COMMENT,")
            .append("UU.JABBER_ID UPDATED_BY,AV.UPDATED_ON ")
            .append("from CONTAINER_VERSION CV ")
            .append("inner join ARTIFACT_VERSION AV on CV.CONTAINER_ID=AV.ARTIFACT_ID ")
            .append("inner join PARITY_USER UC on AV.CREATED_BY=UC.USER_ID ")
            .append("inner join PARITY_USER UU on AV.UPDATED_BY=UU.USER_ID ")
            .append("and CV.CONTAINER_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("inner join ARTIFACT A on CV.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("where CV.CONTAINER_ID=? and CV.CONTAINER_VERSION_ID=?")
            .toString();

    /** Sql to read container versions. */
    private static final String SQL_READ_VERSIONS =
            new StringBuffer("select CV.CONTAINER_ID,CV.CONTAINER_VERSION_ID,")
            .append("A.ARTIFACT_TYPE_ID,AV.ARTIFACT_UNIQUE_ID,UC.JABBER_ID CREATED_BY,")
            .append("AV.CREATED_ON,AV.ARTIFACT_NAME,AV.COMMENT,")
            .append("UU.JABBER_ID UPDATED_BY,AV.UPDATED_ON ")
            .append("from CONTAINER_VERSION CV ")
            .append("inner join ARTIFACT_VERSION AV on CV.CONTAINER_ID=AV.ARTIFACT_ID ")
            .append("inner join PARITY_USER UC on AV.CREATED_BY=UC.USER_ID ")
            .append("inner join PARITY_USER UU on AV.UPDATED_BY=UU.USER_ID ")
            .append("and CV.CONTAINER_VERSION_ID=AV.ARTIFACT_VERSION_ID ")
            .append("inner join ARTIFACT A on CV.CONTAINER_ID=A.ARTIFACT_ID ")
            .append("where CV.CONTAINER_ID=?")
            .toString();

    /** Sql to remove a version relationship. */
    private static final String SQL_REMOVE_VERSION_REL =
            new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=? ")
            .append("and ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
            .toString();

    /** Sql to remove all version relationships. */
    private static final String SQL_REMOVE_VERSIONS_REL =
            new StringBuffer("delete from CONTAINER_VERSION_ARTIFACT_VERSION_REL ")
            .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=? ")
            .toString();

    /** Sql to restore a container. */
    private static final String SQL_RESTORE =
            new StringBuffer("insert into CONTAINER ")
            .append("(CONTAINER_ID) ")
            .append("values (?)")
            .toString();

    /** Sql to update a container version comment. */
    private static final String SQL_UPDATE_COMMENT =
        new StringBuffer("update ARTIFACT_VERSION ")
        .append("set COMMENT=? ")
        .append("where ARTIFACT_ID=? and ARTIFACT_VERSION_ID=?")
        .toString();

    /** Sql to update a draft document. */
    private static final String SQL_UPDATE_DRAFT_DOCUMENT =
        new StringBuffer("update CONTAINER_DRAFT_DOCUMENT ")
        .append("set CONTENT=?,CONTENT_SIZE=?,CONTENT_CHECKSUM=? ")
        .append("where CONTAINER_DRAFT_ID=? and DOCUMENT_ID=?")
        .toString();

    /** Sql to update the container name. */
    private static final String SQL_UPDATE_NAME =
            new StringBuffer("update ARTIFACT ")
            .append("set ARTIFACT_NAME=? ")
            .append("where ARTIFACT_ID=?")
            .toString();

    private static final String SQL_UPDATE_PUBLISHED_TO =
        new StringBuffer("update CONTAINER_VERSION_PUBLISHED_TO ")
        .append("set RECEIVED_ON=? ")
        .append("where CONTAINER_ID=? and CONTAINER_VERSION_ID=? and USER_ID=? ")
        .append("and PUBLISHED_ON=?")
        .toString();

    /** Generic artifact io. */
    private final ArtifactIOHandler artifactIO;
    
    /** Document io. */
    private final DocumentIOHandler documentIO;
    
    /** User io. */
    private final UserIOHandler userIO;

    /**
     * Create ContainerIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public ContainerIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.artifactIO = new ArtifactIOHandler(dataSource);
        this.documentIO = new DocumentIOHandler(dataSource);
        this.userIO = new UserIOHandler(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#addVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long, java.lang.Long,
     *      com.thinkparity.codebase.model.artifact.ArtifactType)
     * 
     */
    public void addVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId, final ArtifactType artifactType) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_ADD_VERSION_REL);
            session.setLong(1, containerId);
            session.setLong(2, containerVersionId);
            session.setLong(3, artifactId);
            session.setLong(4, artifactVersionId);
            session.setTypeAsInteger(5, artifactType);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not add version.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#create()
     * 
     */
    public void create(final Container container) {
        final Session session = openSession();
        try {
            artifactIO.create(session, container);

            session.prepareStatement(SQL_CREATE);
            session.setLong(1, container.getId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create container.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createDelta(com.thinkparity.codebase.model.container.ContainerVersionDelta)
     *
     */
    public void createDelta(final ContainerVersionDelta versionDelta) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DELTA);
            session.setLong(1, versionDelta.getContainerId());
            session.setLong(2, versionDelta.getCompareVersionId());
            session.setLong(3, versionDelta.getCompareToVersionId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create delta.");
            versionDelta.setId(session.getIdentity("CONTAINER_VERSION_DELTA"));

            session.prepareStatement(SQL_CREATE_ARTIFACT_DELTA);
            session.setLong(1, versionDelta.getId());
            for (final ContainerVersionArtifactVersionDelta delta : versionDelta.getDeltas()) {
                session.setTypeAsString(2, delta.getDelta());
                session.setLong(3, delta.getArtifactId());
                session.setLong(4, delta.getArtifactVersionId());
                if (1 != session.executeUpdate())
                    throw new HypersonicException("Could not create artifact delta.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createDraft(com.thinkparity.ophelia.model.container.ContainerDraft)
     */
    public void createDraft(final ContainerDraft draft) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DRAFT);
            session.setLong(1, draft.getContainerId());
            session.setLong(2, draft.getOwner().getLocalId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create draft.");
            
            session.prepareStatement(SQL_CREATE_DRAFT_ARTIFACT_REL);
            session.setLong(1, draft.getContainerId());
            for(final Artifact artifact : draft.getArtifacts()) {
                session.setLong(2, artifact.getId());
                session.setStateAsString(3, draft.getState(artifact));
                if(1 != session.executeUpdate())
                    throw new HypersonicException("Could not create draft artifact relationship.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createDraftArtifactRel(java.lang.Long, java.lang.Long, com.thinkparity.model.parity.model.container.ContainerDraftArtifactState)
     */
    public void createDraftArtifactRel(final Long containerId,
            final Long artifactId, final ContainerDraft.ArtifactState state) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DRAFT_ARTIFACT_REL);
            session.setLong(1, containerId);
            session.setLong(2, artifactId);
            session.setEnumTypeAsString(3, state);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create draft artifact relationship.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createDraftDocument(java.lang.Long, java.lang.Long, java.lang.Integer, java.io.InputStream, java.lang.Long, java.lang.String, java.lang.String, java.lang.String)
     *
     */
    public void createDraftDocument(final ContainerDraftDocument draftDocument,
            final InputStream stream, final Integer bufferSize) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_DRAFT_DOCUMENT);
            session.setLong(1, draftDocument.getContainerDraftId());
            session.setLong(2, draftDocument.getDocumentId());
            session.setBinaryStream(3, stream, draftDocument.getSize(), bufferSize);
            session.setLong(4, draftDocument.getSize());
            session.setString(5, draftDocument.getChecksum());
            session.setString(6, draftDocument.getChecksumAlgorithm());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create draft document.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createPublishedTo(java.lang.Long,
     *      java.lang.Long, java.util.List)
     * 
     */
    public void createPublishedTo(final Long containerId, final Long versionId,
            final List<User> publishedTo, final Calendar publishedOn) {
        final Session session = openSession();
        try {
            for (final User publishedToUser : publishedTo) {
                createPublishedTo(session, containerId, versionId,
                        publishedToUser, publishedOn);
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createPublishedTo(java.lang.Long, java.lang.Long, com.thinkparity.codebase.model.user.User)
     *
     */
    public void createPublishedTo(final Long containerId, final Long versionId,
            final User publishedTo, final Calendar publishedOn) {
        final Session session = openSession();
        try {
            createPublishedTo(session, containerId, versionId, publishedTo,
                    publishedOn);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#createVersion(com.thinkparity.codebase.model.container.ContainerVersion)
     * 
     */
    public void createVersion(final ContainerVersion version) {
        final Session session = openSession();
        try {
            artifactIO.createVersion(session, version);
            session.prepareStatement(SQL_CREATE_VERSION);
            session.setLong(1, version.getArtifactId());
            session.setLong(2, version.getVersionId());
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create version.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#delete(java.lang.Long)
     * 
     */
    public void delete(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE);
            session.setLong(1, containerId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete container.");
            artifactIO.delete(session, containerId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDeltas(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void deleteDelta(final Long containerId,
            final Long compareVersionId, final Long compareToVersionId) {
        final Session session = openSession();
        try {
            final int artifactDeltaCount = readArtifactDeltaCount(session, containerId, compareVersionId, compareToVersionId);
            session.prepareStatement(SQL_DELETE_ARTIFACT_DELTA);
            session.setLong(1, containerId);
            session.setLong(2, compareVersionId);
            session.setLong(3, compareToVersionId);
            if (artifactDeltaCount != session.executeUpdate())
                throw new HypersonicException("Could not delete artifact deltas.");

            final int deltaCount = readDeltaCount(session, containerId,
                    compareVersionId, compareToVersionId);
            session.prepareStatement(SQL_DELETE_DELTA);
            session.setLong(1, containerId);
            session.setLong(2, compareVersionId);
            session.setLong(3, compareToVersionId);
            if (deltaCount != session.executeUpdate())
                throw new HypersonicException("Could not delete delta.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDeltas(java.lang.Long, java.lang.Long)
     *
     */
    public void deleteDeltas(final Long containerId, final Long versionId) {
        final Session session = openSession();
        try {
            final int artifactDeltaCount = readArtifactDeltaCount(session, containerId, versionId);
            session.prepareStatement(SQL_DELETE_ARTIFACT_DELTAS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.setLong(3, versionId);
            if (artifactDeltaCount != session.executeUpdate())
                throw new HypersonicException("Could not delete artifact deltas.");
            
            final int deltaCount = readDeltaCount(session, containerId, versionId);
            session.prepareStatement(SQL_DELETE_DELTAS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.setLong(3, versionId);
            if (deltaCount != session.executeUpdate())
                throw new HypersonicException("Could not delete deltas.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDraft(java.lang.Long)
     */
    public void deleteDraft(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_DRAFT);
            session.setLong(1, containerId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete draft.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDraftArtifactRel(java.lang.Long, java.lang.Long)
     */
    public void deleteDraftArtifactRel(final Long containerId, final Long artifactId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_DRAFT_ARTIFACT_REL);
            session.setLong(1, containerId);
            session.setLong(2, artifactId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete draft artifact relationship.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDraftDocument(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void deleteDraftDocument(final Long containerDraftId,
            final Long documentId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_DRAFT_DOCUMENT);
            session.setLong(1, containerDraftId);
            session.setLong(2, documentId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete draft document.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteDraftDocument(java.lang.Long, java.lang.Long)
     *
     */
    public void deleteDraftDocuments(final Long containerDraftId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_DRAFT_DOCUMENTS);
            session.setLong(1, containerDraftId);
            session.executeUpdate();
            if (0 != readDraftDocumentCount(session, containerDraftId).intValue())
                throw new HypersonicException("Could not delete draft documents.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#deleteVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public void deleteVersion(Long containerId, Long versionId) {
        final Session session = openSession();
        try {
            final int publishedToCount =
                readPublishedToCount(session, containerId, versionId);
            session.prepareStatement(SQL_DELETE_PUBLISHED_TO);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            final int publishedToDeleted = session.executeUpdate();
            if (publishedToCount != publishedToDeleted)
                throw translateError(
                        "Could only delete {0} of {1} published to rows.",
                        publishedToDeleted, publishedToCount);

            session.prepareStatement(SQL_DELETE_VERSION);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not delete version.");
            artifactIO.deleteVersion(session, containerId, versionId);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#doesExistVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long, java.lang.Long)
     * 
     */
    public Boolean doesExistVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_VERSION);
            session.setLong(1, containerId);
            session.setLong(2, containerVersionId);
            session.setLong(3, artifactId);
            session.setLong(4, artifactVersionId);
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine artifact existance.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#openDraftDocument(java.lang.Long, java.lang.Long)
     *
     */
    public InputStream openDraftDocument(final Long containerDraftId,
            final Long documentId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_OPEN_DRAFT_DOCUMENT);
            session.setLong(1, containerDraftId);
            session.setLong(2, documentId);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getBlob("CONTENT");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#read(java.lang.Long)
     * 
     */
    public Container read(final Long containerId, final User localUser) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_CONTAINER_ID);
            session.setLong(1, containerId);
            session.executeQuery();

            if (session.nextResult()) {
                return extractContainer(session, localUser);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#read()
     * 
     */
    public List<Container> read(final User localUser) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.executeQuery();

            final List<Container> containers = new ArrayList<Container>();
            while (session.nextResult()) {
                containers.add(extractContainer(session, localUser));
            }
            return containers;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readDelta(java.lang.Long, java.lang.Long, java.lang.Long)
     *
     */
    public ContainerVersionDelta readDelta(final Long containerId, final Long compareVersionId, final Long compareToVersionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DELTA);
            session.setLong(1, containerId);
            session.setLong(2, compareVersionId);
            session.setLong(3, compareToVersionId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractDelta(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readDocuments(java.lang.Long, java.lang.Long)
     */
    public List<Document> readDocuments(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOCUMENTS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<Document> documents = new ArrayList<Document>();
            while(session.nextResult()) {
                documents.add(documentIO.extractDocument(session));
            }
            return documents;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readDocumentVersions(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public List<DocumentVersion> readDocumentVersions(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOCUMENT_VERSIONS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<DocumentVersion> versions = new ArrayList<DocumentVersion>();
            while (session.nextResult()) {
                versions.add(extractDocumentVersion(session));
            }
            return versions;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readDraft(java.lang.Long)
     */
    public ContainerDraft readDraft(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT);
            session.setLong(1, containerId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractDraft(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readDraftDocument(java.lang.Long, java.lang.Long)
     *
     */
    public ContainerDraftDocument readDraftDocument(
            final Long containerDraftId, final Long documentId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_DOCUMENT);
            session.setLong(1, containerDraftId);
            session.setLong(2, documentId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractDraftDocument(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readForTeamMember(com.thinkparity.codebase.model.user.User)
     * 
     */
    public List<Container> readForTeamMember(final Long teamMemberId, final User localUser) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_BY_TEAM_MEMBER_ID);
            session.setLong(1, teamMemberId);
            session.executeQuery();

            final List<Container> containers = new ArrayList<Container>();
            while (session.nextResult()) {
                containers.add(extractContainer(session, localUser));
            }
            return containers;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readVersion(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public ContainerVersion readLatestVersion(final Long containerId) {
        final Session session = openSession();
        try {
            return readVersion(containerId, artifactIO.getLatestVersionId(session, containerId));
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readPublishedTo(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public List<User> readPublishedTo(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PUBLISHED_TO);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<User> publishedTo = new ArrayList<User>();
            while (session.nextResult()) {
                publishedTo.add(userIO.extractUser(session));
            }
            return publishedTo;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readPublishedToReceipt(java.lang.Long,
     *      java.lang.Long, java.util.Calendar,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    public ArtifactReceipt readPublishedToReceipt(final Long containerId,
            final Long versionId, final Calendar publishedOn, final User user) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PUBLISHED_TO_BY_USER);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.setCalendar(3, publishedOn);
            session.setLong(4, user.getLocalId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractReceipt(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readPublishedTo(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public List<ArtifactReceipt> readPublishedToReceipts(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PUBLISHED_TO_RECEIPTS);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            final List<ArtifactReceipt> publishedTo = new ArrayList<ArtifactReceipt>();
            while (session.nextResult()) {
                publishedTo.add(extractReceipt(session));
            }
            return publishedTo;
        } finally {
            session.close();
        }
    }

    /**
     * Read a container version.
     * 
     * @param containerId
     *            The container id.
     * @param versionId
     *            The version id.
     * @return The container version.
     */
    public ContainerVersion readVersion(final Long containerId,
            final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSION);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeQuery();
            if (session.nextResult()) {
                return extractVersion(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#readVersions(java.lang.Long)
     * 
     */
    public List<ContainerVersion> readVersions(final Long containerId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_VERSIONS);
            session.setLong(1, containerId);
            session.executeQuery();
            final List<ContainerVersion> versions = new ArrayList<ContainerVersion>();
            while(session.nextResult()) {
                versions.add(extractVersion(session));
            }
            return versions;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#removeVersion(java.lang.Long,
     *      java.lang.Long, java.lang.Long, java.lang.Long)
     * 
     */
    public void removeVersion(final Long containerId,
            final Long containerVersionId, final Long artifactId,
            final Long artifactVersionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_REMOVE_VERSION_REL);
            session.setLong(1, containerId);
            session.setLong(2, containerVersionId);
            session.setLong(3, artifactId);
            session.setLong(4, artifactVersionId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not remove version.");
        } finally {
            session.close();
        }
    }
    
    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#removeVersions(java.lang.Long, java.lang.Long)
     */
    public void removeVersions(final Long containerId, final Long versionId) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_REMOVE_VERSIONS_REL);
            session.setLong(1, containerId);
            session.setLong(2, versionId);
            session.executeUpdate();
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#restore(com.thinkparity.codebase.model.container.Container)
     */
    public void restore(final Container container) {
        final Session session = openSession();
        try {
            artifactIO.restore(session, container);
            session.prepareStatement(SQL_RESTORE);
            session.setLong(1, container.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not restore container.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#updateComment(java.lang.Long, java.lang.Long, java.lang.String)
     *
     */
    public void updateComment(final Long containerId, final Long versionId,
            final String comment) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_COMMENT);
            session.setString(1, comment);
            session.setLong(2, containerId);
            session.setLong(3, versionId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update comment.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#updateDraftDocument(java.lang.Long,
     *      java.lang.Long, java.lang.Integer, java.io.InputStream,
     *      java.lang.Long, java.lang.String)
     * 
     */
    public void updateDraftDocument(final ContainerDraftDocument draftDocument,
            final InputStream stream, final Integer bufferSize) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_DRAFT_DOCUMENT);
            session.setBinaryStream(1, stream, draftDocument.getSize(), bufferSize);
            session.setLong(2, draftDocument.getSize());
            session.setString(3, draftDocument.getChecksum());
            session.setLong(4, draftDocument.getContainerDraftId());
            session.setLong(5, draftDocument.getDocumentId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update draft document.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#updateName(java.lang.Long, java.lang.String)
     */
    public void updateName(final Long containerId, final String name) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_NAME);
            session.setString(1, name);
            session.setLong(2, containerId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update name.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ContainerIOHandler#updatePublishedTo(java.lang.Long,
     *      java.lang.Long, com.thinkparity.codebase.jabber.JabberId,
     *      java.util.Calendar, java.util.Calendar)
     * 
     */
    public void updatePublishedTo(final Long containerId, final Long versionId,
            final Calendar publishedOn, final JabberId receivedBy,
            final Calendar receivedOn) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PUBLISHED_TO);
            session.setCalendar(1, receivedOn);
            session.setLong(2, containerId);
            session.setLong(3, versionId);
            session.setLong(4, readLocalId(receivedBy));
            session.setCalendar(5, publishedOn);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update container version published to list.");
        } finally {
            session.close();
        }
    }

    /**
     * Extract a container from the session.
     * 
     * @param session
     *            A database session.
     * @return A container.
     */
    Container extractContainer(final Session session, final User localUser) {
        final Container container = new Container();
        container.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
        container.setCreatedOn(session.getCalendar("CREATED_ON"));
        container.setId(session.getLong("CONTAINER_ID"));
        final Long draftOwnerId = session.getLong("CONTAINER_DRAFT_USER_ID");
        container.setDraft(null != draftOwnerId);
        container.setLocalDraft(null != draftOwnerId && localUser.getLocalId().equals(draftOwnerId));
        container.setName(session.getString("ARTIFACT_NAME"));
        container.setRemoteInfo(artifactIO.extractRemoteInfo(session));
        container.setState(session.getStateFromInteger("ARTIFACT_STATE_ID"));
        container.setType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
        container.setUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
        container.setUpdatedBy(session.getQualifiedUsername("UPDATED_BY"));
        container.setUpdatedOn(session.getCalendar("UPDATED_ON"));

        container.setFlags(artifactIO.getFlags(container.getId()));
        return container;
    }

    /**
     * Extract the delta from the session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>Delta</code>.
     */
    ContainerVersionDelta extractDelta(final Session session) {
        final ContainerVersionDelta delta = new ContainerVersionDelta();
        delta.setCompareToVersionId(session.getLong("COMPARE_TO_CONTAINER_VERSION_ID"));
        delta.setCompareVersionId(session.getLong("COMPARE_CONTAINER_VERSION_ID"));
        delta.setContainerId(session.getLong("CONTAINER_ID"));
        delta.setId(session.getLong("CONTAINER_VERSION_DELTA_ID"));
        session.prepareStatement(SQL_EXTRACT_DELTA);
        session.setLong(1, delta.getId());
        session.executeQuery();
        while (session.nextResult()) {
            delta.addDelta(extractArtifactDelta(session));
        }
        return delta;
    }

    /**
     * Extract the draft from the session.
     * 
     * @param session
     *            A database session.
     * @return A draft.
     */
    ContainerDraft extractDraft(final Session session) {
        final ContainerDraft draft = new ContainerDraft();
        draft.setContainerId(session.getLong("CONTAINER_DRAFT_ID"));
        draft.setOwner(artifactIO.extractTeamMember(session));
        addAllDraftDocuments(draft);
        return draft;
    }

    // TODO-javadoc ContainerIOHandler#extractDraftDocument
    ContainerDraftDocument extractDraftDocument(final Session session) {
        final ContainerDraftDocument draftDocument = new ContainerDraftDocument();
        draftDocument.setChecksum(session.getString("CONTENT_CHECKSUM"));
        draftDocument.setChecksumAlgorithm(session.getString("CHECKSUM_ALGORITHM"));
        draftDocument.setDocumentId(session.getLong("DOCUMENT_ID"));
        draftDocument.setContainerDraftId(session.getLong("CONTAINER_DRAFT_ID"));
        draftDocument.setSize(session.getLong("CONTENT_SIZE"));
        return draftDocument;
    }

    /**
     * Extract an artifact receipt from the published to table.
     * @param session A <code>Session</code>.
     * @param user
     * @return An <code>ArtifactReceipt</code>.
     */
    ArtifactReceipt extractReceipt(final Session session) {
        final ArtifactReceipt receipt = new ArtifactReceipt();
        receipt.setArtifactId(session.getLong("CONTAINER_ID"));
        receipt.setPublishedOn(session.getCalendar("PUBLISHED_ON"));
        receipt.setReceivedOn(session.getCalendar("RECEIVED_ON"));
        receipt.setUser(userIO.extractUser(session));
        return receipt;
    }

    /**
     * Extract a container version from the database session.
     * 
     * @param session
     *            A databsae session.
     * @return A container version.
     */
    ContainerVersion extractVersion(final Session session) {
       final ContainerVersion version = new ContainerVersion();
       version.setArtifactId(session.getLong("CONTAINER_ID"));
       version.setArtifactType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
       version.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
       version.setComment(session.getString("COMMENT"));
       version.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
       version.setCreatedOn(session.getCalendar("CREATED_ON"));
       version.setName(session.getString("ARTIFACT_NAME"));
       version.setUpdatedBy(session.getQualifiedUsername("UPDATED_BY"));
       version.setUpdatedOn(session.getCalendar("UPDATED_ON"));
       version.setVersionId(session.getLong("CONTAINER_VERSION_ID"));
       version.setMetaData(getVersionMetaData(version.getArtifactId(), version.getVersionId()));
       return version;
    }

    /**
     * Add all draft documents to the draft.
     * 
     * @param draft
     *            A draft.
     */
    private void addAllDraftDocuments(final ContainerDraft draft) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DRAFT_DOCUMENTS);
            session.setLong(1, draft.getContainerId());
            session.executeQuery();
            Document document;
            while(session.nextResult()) {
                document = documentIO.extractDocument(session);
                draft.addDocument(document);
                draft.putState(document, session.getContainerStateFromString("DRAFT_ARTIFACT_STATE"));
            }
        } finally {
            session.close();
        }
    }

    /**
     * Create a published to entry.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param publishedTo
     *            A published to <code>User</code>.
     * @param publishedOn
     *            A published on <code>Calendar</code>.
     */
    private void createPublishedTo(final Session session,
            final Long containerId, final Long versionId, final User publishedTo, final Calendar publishedOn) {
        session.prepareStatement(SQL_CREATE_PUBLISHED_TO);
        session.setLong(1, containerId);
        session.setLong(2, versionId);
        session.setLong(3, publishedTo.getLocalId());
        session.setCalendar(4, publishedOn);
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create published to entry.");
    }

    /**
     * Extract the artifact delta from the session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>ContainerVersionArtifactVersionDelta</code>.
     */
    private ContainerVersionArtifactVersionDelta extractArtifactDelta(
            final Session session) {
        final ContainerVersionArtifactVersionDelta delta = new ContainerVersionArtifactVersionDelta();
        delta.setArtifactId(session.getLong("DELTA_ARTIFACT_ID"));
        delta.setArtifactVersionId(session.getLong("DELTA_ARTIFACT_VERSION_ID"));
        delta.setDelta(Delta.valueOf(session.getString("DELTA")));
        return delta;
    }

    private DocumentVersion extractDocumentVersion(final Session session) {
        final DocumentVersion dv = new DocumentVersion();
        dv.setArtifactId(session.getLong("DOCUMENT_ID"));
        dv.setArtifactType(session.getTypeFromInteger("ARTIFACT_TYPE_ID"));
        dv.setArtifactUniqueId(session.getUniqueId("ARTIFACT_UNIQUE_ID"));
        dv.setChecksum(session.getString("CONTENT_CHECKSUM"));
        dv.setChecksumAlgorithm(session.getString("CHECKSUM_ALGORITHM"));
        dv.setCreatedBy(session.getQualifiedUsername("CREATED_BY"));
        dv.setCreatedOn(session.getCalendar("CREATED_ON"));
        dv.setName(session.getString("ARTIFACT_NAME"));
        dv.setSize(session.getLong("CONTENT_SIZE"));
        dv.setUpdatedBy(session.getQualifiedUsername("UPDATED_BY"));
        dv.setUpdatedOn(session.getCalendar("UPDATED_ON"));
        dv.setVersionId(session.getLong("DOCUMENT_VERSION_ID"));

        dv.setMetaData(documentIO.getVersionMetaData(dv.getArtifactId(), dv.getVersionId()));
        return dv;
    }

    /**
     * Obtain the version meta data.
     * 
     * @param artifactId
     *            The artifact id.
     * @param versionId
     *            The version id.
     * @return The version meta data.
     */
    private Properties getVersionMetaData(final Long containerId, final Long versionId) {
        final Session session = openSession();
        try {
            return artifactIO.getVersionMetaData(session, containerId, versionId);
        } finally {
            session.close();
        }
    }

    /**
     * Read the artifact delta count.
     * 
     * @param session
     *            A hypersonic <code>Session</code>.
     * @param deltaId
     *            A delta id <code>Long</code>.
     * @return An artifact delta count <code>int</code>.
     */
    private int readArtifactDeltaCount(final Session session,
            final Long containerId, final Long versionId) {
        session.prepareStatement(SQL_READ_ARTIFACT_DELTA_COUNT_2);
        session.setLong(1, containerId);
        session.setLong(2, versionId);
        session.setLong(3, versionId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("COUNT");
    }

    /**
     * Read the artifact delta count.
     * 
     * @param session
     *            A hypersonic <code>Session</code>.
     * @param deltaId
     *            A delta id <code>Long</code>.
     * @return An artifact delta count <code>int</code>.
     */
    private int readArtifactDeltaCount(final Session session,
            final Long containerId, final Long compareVersionId,
            final Long compareToVersionId) {
        session.prepareStatement(SQL_READ_ARTIFACT_DELTA_COUNT);
        session.setLong(1, containerId);
        session.setLong(2, compareVersionId);
        session.setLong(3, compareToVersionId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("COUNT");
    }

    /**
     * Read the delta count.
     * 
     * @param session
     *            A hypersonic <code>Session</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return An artifact delta count <code>int</code>.
     */
    private int readDeltaCount(final Session session, final Long containerId,
            final Long versionId) {
        session.prepareStatement(SQL_READ_DELTA_COUNT_2);
        session.setLong(1, containerId);
        session.setLong(2, versionId);
        session.setLong(3, versionId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("COUNT");
    }

    /**
     * Read the delta count.
     * 
     * @param session
     *            A hypersonic <code>Session</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return An artifact delta count <code>int</code>.
     */
    private int readDeltaCount(final Session session, final Long containerId,
            final Long compareVersionId, final Long compareToVersionId) {
        session.prepareStatement(SQL_READ_DELTA_COUNT);
        session.setLong(1, containerId);
        session.setLong(2, compareVersionId);
        session.setLong(3, compareToVersionId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("COUNT");
    }

    /**
     * Obtain the draft document count.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @return The draft document count <code>Integer</code>.
     */
    private Integer readDraftDocumentCount(final Session session, final Long containerId) {
        session.prepareStatement(SQL_READ_DRAFT_DOCUMENT_COUNT);
        session.setLong(1, containerId);
        session.executeQuery();
        if (session.nextResult()) {
            return session.getInteger("DOCUMENT_COUNT");
        } else {
            return null;
        }
    }

    private Long readLocalId(final JabberId userId) {
        final Session session = openSession();
        try {
            return userIO.readLocalId(session, userId);
        } finally {
            session.close();
        }
    }

    /**
     * Read the number of published to rows.
     * 
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A row count <code>Integer</code>.
     */
    private int readPublishedToCount(final Session session,
            final Long containerId, final Long versionId) {
        session.prepareStatement(SQL_READ_PUBLISHED_TO_COUNT);
        session.setLong(1, containerId);
        session.setLong(2, versionId);
        session.executeQuery();
        session.nextResult();
        return session.getInteger("PUBLISHED_TO_COUNT");
    }
}
