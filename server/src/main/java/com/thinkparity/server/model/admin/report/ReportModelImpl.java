/*
 * Created On:  23-Oct-07 6:47:22 PM
 */
package com.thinkparity.desdemona.model.admin.report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import au.com.bytecode.opencsv.CSVWriter;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.crypto.EncryptFile;
import com.thinkparity.codebase.tar.ArchiveDirectory;

import com.thinkparity.codebase.model.user.UserVCard;
import com.thinkparity.codebase.model.util.VCardReader;

import com.thinkparity.desdemona.model.admin.AdminModel;
import com.thinkparity.desdemona.model.io.IOService;
import com.thinkparity.desdemona.model.io.sql.ReportSql;

import com.thinkparity.desdemona.service.persistence.PersistenceService;

/**
 * <b>Title:</b>thinkParity Report Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReportModelImpl extends AdminModel implements ReportModel,
        InternalReportModel {

    /** A report sql interface. */
    private ReportSql reportIO;

    /** A vcard reader. */
    private VCardReader<UserVCard> vcardReader;

    /**
     * Create ReportModelImpl.
     *
     */
    public ReportModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.report.ReportModel#generate()
     *
     */
    @Override
    public void generate() {
        try {
            final List<ReportUser> userList = reportIO.readUserList(vcardReader);
            final List<Invitation> firstAcceptedInvitationList = 
                reportIO.readFirstAcceptedInvitationList(vcardReader);
            final List<Invitation> invitationList = reportIO.readInvitationList(
                    vcardReader);
            final Report report = new Report();
            report.setFirstAcceptedInvitationList(firstAcceptedInvitationList);
            report.setUserList(userList);
            report.setInvitationList(invitationList);
            final FileSystem reportRoot = new FileSystem(createTempDirectory("admin-report"));
            final File archiveFile = newArchiveFile(reportRoot.getRoot());
            try {
                serialize(reportRoot.getRoot(), report);

                /* tar the report directory */
                final ArchiveDirectory archiveDelegate = new ArchiveDirectory();
                synchronized (getBufferLock()) {
                    archiveDelegate.archive(reportRoot.getRoot(), archiveFile,
                            getBufferArray());
                }

                /* encrypt the report archive */
                final Key key = getSecretKeySpec();
                final File encryptFile = newEncryptFile(archiveFile);
                final EncryptFile encryptDelegate = new EncryptFile(key.getAlgorithm());
                synchronized (getBufferLock()) {
                    encryptDelegate.encrypt(key, archiveFile, encryptFile,
                            getBufferArray());
                }
            } finally {
                try {
                    reportRoot.deleteTree();
                } finally {
                    Assert.assertTrue("Could not delete report archive file.",
                            archiveFile.delete());
                }
            }
        } catch (final Exception x) {
            throw panic(x);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.admin.AdminModel#initialize()
     *
     */
    @Override
    protected void initialize() {
        final DataSource dataSource = PersistenceService.getInstance().getDataSource();
        reportIO = IOService.getInstance().getFactory().newReportIO(dataSource);

        vcardReader = new VCardReader<UserVCard>() {
            /**
             * @see com.thinkparity.codebase.model.util.VCardReader#read(com.thinkparity.codebase.model.user.UserVCard, java.io.Reader)
             *
             */
            @Override
            public void read(final UserVCard vcard, final Reader reader)
                    throws IOException {
                /* TODO - ReportModelImpl#initialize()$VCardReader - extract */
                try {
                    final StringBuilder encrypted = new StringBuilder();
                    final char[] cbuf = new char[1024];
                    int read;
                    while (true) {
                        read = reader.read(cbuf);
                        if (-1 == read) {
                            break;
                        }
                        encrypted.append(cbuf, 0, read);
                    }
                    XSTREAM_UTIL.fromXML(new StringReader(decrypt(
                            encrypted.toString())), vcard);
                } catch (final GeneralSecurityException gsx) {
                    throw new IOException(gsx);
                }
            }
        };
    }

    /**
     * Format the calendar.
     * 
     * @param calendar
     *            A <code>Calendar</code>.
     * @return A <code>String</code>.
     */
    private String format(final Calendar calendar) {
        if (null == calendar) {
            return null;
        } else {
            return MessageFormat.format("{0,date,yyyy-MM-dd HH:mm:ss.SSS Z}",
                    calendar.getTime());
        }
    }

    /**
     * Instantiate an archive file. The file will be created. It will be named
     * with the report root's parent as its parent and the report root's name as
     * its name and a tar file extension.
     * 
     * @param reportRoot
     *            A <code>File</code>.
     * @return A <code>File</code>.
     */
    private File newArchiveFile(final File reportRoot) throws IOException {
        final String archiveFilePath = MessageFormat.format(
                "{0}.tar", reportRoot.getName());
        final File archiveFile = new File(reportRoot.getParentFile(), archiveFilePath);
        if (archiveFile.createNewFile()) {
            return archiveFile;
        } else {
            throw new IOException("Could not create archive file:  " + archiveFile);
        }
    }

    /**
     * Instantiate a csv writer.
     * 
     * @param directory
     *            A <code>File</code>
     * @param name
     *            A <code>String</code>.
     * @return A <code>CSVWriter</code>.
     * @throws IOException
     */
    private CSVWriter newCSVWriter(final File directory, final String name)
            throws IOException {
        final File file = new File(directory, MessageFormat.format("{0}.csv", name));
        final Writer writer = new FileWriter(file, false);
        return new CSVWriter(writer, ',', '"', '\\', Separator.NixNewLine.toString());
    }

    /**
     * Instantiate an encrypt file. The file will be created. It will be named
     * with the file system root as its parent and the compress file's name as
     * its name and a secret file extension.
     * 
     * @param archiveFile
     *            A <code>File</code>.
     * @return A <code>File</code>.
     */
    private File newEncryptFile(final File archiveFile) throws IOException {
        final String encryptFilePath = MessageFormat.format(
                "{0}.secret", archiveFile.getName());
        final File encryptFile = new File(archiveFile.getParentFile(), encryptFilePath);
        if (encryptFile.createNewFile()) {
            return encryptFile;
        } else {
            throw new IOException("Could not create encrypt file " + encryptFile);
        }
    }

    /**
     * Serialize a report.
     * 
     * @param directory
     *            A <code>File</code>.
     * @param report
     *            A <code>Report</code>.
     */
    private void serialize(final File directory, final Report report)
            throws IOException {
        final List<String[]> valueArrayList = new ArrayList<String[]>();
        CSVWriter csvWriter;

        /* user.csv */
        csvWriter = newCSVWriter(directory, "user");
        try {
            valueArrayList.clear();
            csvWriter.writeNext(new String[] { "user.username",
                    "user.created_on", "user.country", "user.language",
                    "user.name", "user.organization", "user.time_zone",
                    "user.title" });
            final List<ReportUser> reportUserList = report.getUserList();
            for (final ReportUser reportUser : reportUserList) {
                valueArrayList.add(new String[] {
                        reportUser.getSimpleUsername(),
                        format(reportUser.getCreatedOn()),
                        reportUser.getCountry(), reportUser.getLanguage(),
                        reportUser.getName(), reportUser.getOrganization(),
                        reportUser.getTimeZone(), reportUser.getTitle() });
            }
            csvWriter.writeAll(valueArrayList);
        } finally {
            try {
                csvWriter.flush();
            } finally {
                csvWriter.close();
            }
        }

        /* network.csv */
        csvWriter= newCSVWriter(directory, "network");
        try {
            valueArrayList.clear();
            csvWriter.writeNext(new String[] { "invitation.source.name",
                    "invitation.source.organization", "invitation.source.title",
                    "invitation.target.name", "invitation.target.organization",
                    "invitation.target.title", "invitation.extended_on",
                    "invitation.accepted_on" });
            final List<Invitation> invitationList = report.getInvitationList();
            for (final Invitation invitation : invitationList) {
                valueArrayList.add(new String[] {
                        invitation.getInvitedBy().getName(),
                        invitation.getInvitedBy().getOrganization(),
                        invitation.getInvitedBy().getTitle(),
                        invitation.getUser().getName(),
                        invitation.getUser().getOrganization(),
                        invitation.getUser().getTitle(),
                        format(invitation.getInvitedOn()),
                        format(invitation.getAcceptedOn()) });
            }
            csvWriter.writeAll(valueArrayList);
        } finally {
            try {
                csvWriter.flush();
            } finally {
                csvWriter.close();
            }
        }

        /* invitation.csv */
        csvWriter= newCSVWriter(directory, "invitation");
        try {
            valueArrayList.clear();
            csvWriter.writeNext(new String[] { "invitation.source.name",
                    "invitation.source.organization", "invitation.source.title",
                    "invitation.target.name", "invitation.target.organization",
                    "invitation.target.title", "invitation.extended_on",
                    "invitation.accepted_on" });
            final List<Invitation> invitationList = report.getFirstAcceptedInvitationList();
            for (final Invitation invitation : invitationList) {
                valueArrayList.add(new String[] {
                        invitation.getInvitedBy().getName(),
                        invitation.getInvitedBy().getOrganization(),
                        invitation.getInvitedBy().getTitle(),
                        invitation.getUser().getName(),
                        invitation.getUser().getOrganization(),
                        invitation.getUser().getTitle(),
                        format(invitation.getInvitedOn()),
                        format(invitation.getAcceptedOn()) });
            }
            csvWriter.writeAll(valueArrayList);
        } finally {
            try {
                csvWriter.flush();
            } finally {
                csvWriter.close();
            }
        }
    }
}
