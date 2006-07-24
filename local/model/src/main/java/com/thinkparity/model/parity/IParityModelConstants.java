/*
 * Oct 11, 2005
 */
package com.thinkparity.model.parity;


/**
 * Parity model constants.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface IParityModelConstants {

	public static final String DIRECTORY_NAME_LOCAL_DATA = "local";
	public static final String DIRECTORY_NAME_DB_DATA = "db.io";
	public static final String FILE_NAME_DB_DATA = "db";
	public static final Long FILE_SIZE_UPPER_BOUNDS = 1048576L;
	public static final Integer XMPP_CONFIRM_TIMEOUT = 1000 * 1;	// 1s

	public static final String PARITY_CONNECTION_RESOURCE = "parity";

	public static final String ARCHIVE_TEMP_DIRECTORY = ".archive";

    public static final String ROOT_DIRECTORY = "thinkParity";

    public static final String ARCHIVE_DIRECTORY = "Archive";

    public static final String EXPORT_DIRECTORY = "Export";
}
