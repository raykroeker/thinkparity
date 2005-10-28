/*
 * Oct 11, 2005
 */
package com.thinkparity.model.parity;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;

import com.thinkparity.model.parity.util.UUIDGenerator;

/**
 * Parity model constants.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IParityModelConstants {
	public static final String DIRECTORY_NAME_CACHE_DATA = "cache";
	public static final String DIRECTORY_NAME_XML_DATA = "xml.io";

	public static final String PROJECT_CREATED_BY = "system";
	public static final String PROJECT_CREATED_BY_INBOX = PROJECT_CREATED_BY;
	public static final String PROJECT_CREATED_BY_MYPROJECTS = PROJECT_CREATED_BY;

	public static final Calendar PROJECT_CREATED_ON = DateUtil.getInstance();
	public static final Calendar PROJECT_CREATED_ON_INBOX = PROJECT_CREATED_ON;
	public static final Calendar PROJECT_CREATED_ON_MYPROJECTS = PROJECT_CREATED_ON;

	public static final String PROJECT_DESCRIPTION_INBOX = "System Project:  Inbox";
	public static final String PROJECT_DESCRIPTION_MYPROJECTS = "System Project:  My Projects";

	public static final UUID PROJECT_ID_INBOX = UUIDGenerator.nextUUID();
	public static final UUID PROJECT_ID_MYPROJECTS = UUIDGenerator.nextUUID();

	public static final String PROJECT_NAME_INBOX = "Inbox";
	public static final String PROJECT_NAME_MYPROJECTS = "My Projects";
}
