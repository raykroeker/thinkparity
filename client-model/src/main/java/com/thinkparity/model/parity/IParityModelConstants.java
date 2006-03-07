/*
 * Oct 11, 2005
 */
package com.thinkparity.model.parity;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.model.Version;
import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * Parity model constants.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IParityModelConstants {

	public static final String DIRECTORY_NAME_LOCAL_DATA = "local";

	public static final String DIRECTORY_NAME_XML_DATA = "xml.io";
	public static final String DIRECTORY_NAME_DB_DATA = "db.io";
	public static final String FILE_NAME_DB_DATA = "db";
	public static final Long FILE_SIZE_UPPER_BOUNDS = 1048576L;

	public static final Calendar PROJECT_CREATED_ON = DateUtil.getInstance();
	public static final Calendar PROJECT_CREATED_ON_INBOX = PROJECT_CREATED_ON;
	public static final Calendar PROJECT_CREATED_ON_MYPROJECTS = PROJECT_CREATED_ON;

	public static final String PROJECT_DESCRIPTION_INBOX = "System Project:  Inbox";
	public static final String PROJECT_DESCRIPTION_MYPROJECTS = "System Project:  My Projects";

	public static final UUID PROJECT_ID_INBOX = UUIDGenerator.nextUUID();
	public static final UUID PROJECT_ID_MYPROJECTS = UUIDGenerator.nextUUID();

	public static final String PROJECT_NAME_INBOX = "Inbox";
	public static final String PROJECT_NAME_MYPROJECTS = "My Projects";

	public static final Calendar PROJECT_UPDATED_ON = PROJECT_CREATED_ON;
	public static final Calendar PROJECT_UPDATED_ON_INBOX = PROJECT_UPDATED_ON;
	public static final Calendar PROJECT_UPDATED_ON_MYPROJECTS = PROJECT_UPDATED_ON;

	public static final Integer XMPP_CONFIRM_TIMEOUT = 1000 * 1;	// 1s

	public static final String PARITY_CONNECTION_RESOURCE = "parity";

	public static final String ARCHIVE_TEMP_DIRECTORY = Version.getName() + ".archive";
}
