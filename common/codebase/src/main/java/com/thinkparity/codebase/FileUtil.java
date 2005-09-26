/*
 * Sep 14, 2003
 */
package com.thinkparity.codebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;

import com.thinkparity.codebase.StringUtil.Charset;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.SystemUtil.SystemProperty;
import com.thinkparity.codebase.config.Config;
import com.thinkparity.codebase.config.ConfigFactory;

/**
 * <b>Title:</b>  FileUtil
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class FileUtil {

	/**
	 * Config for fileUtil
	 */
	private static final Config fileUtilConfig =
		ConfigFactory.newInstance(FileUtil.class);
	
	/**
	 * Size of each block to be read/written
	 */
	private static final int BLOCK_SIZE =
		Integer.parseInt(fileUtilConfig.getProperty("block.size"));

	/**
	 * Default character set to use
	 */
	private static final Charset DEFAULT_CHARSET =
		Charset.find(fileUtilConfig.getProperty("default.charset"));

	/**
	 * Create a new FileUtil [Singleton]
	 */
	private FileUtil() {super();}

	/**
	 * Create an application's home directory. The path to the application home
	 * directory is determined by using the users's home directory and adding
	 * the '.' character as well as the lower case application name. The
	 * application name is provided within applicationConfig and is located at
	 * key.
	 * 
	 * @param key
	 *            <code>java.lang.String</code>
	 * @param config
	 *            <code>Config</code>
	 * @return <code>java.lang.Boolean</code>
	 */
	public static synchronized Boolean createApplicationDirectory(
			final String key, Config config) {
		final File applicationDirectory = FileUtil.getApplicationDirectory(key,
				config);
		Boolean isCreated = Boolean.TRUE;
		if(!applicationDirectory.exists()) {
			isCreated = applicationDirectory.mkdirs();
		}
		return isCreated;
	}

	/**
	 * Create a new directory.
	 * @param directory <code>java.io.File</code>
	 * @return <code>java.lang.Boolean</code>
	 * TRUE:  directory was created
	 * FALSE: directory was not created
	 */
	public static synchronized Boolean createDirectory(final File directory) {
		return directory.mkdir();
	}

	/**
	 * Delete a file.
	 * @param file <code>java.io.File</code>
	 * @return <code>java.lang.Boolean</code>
	 */
	public static synchronized Boolean delete(final File file) {
		return file.delete();
	}

	private static synchronized File getApplicationDirectory(final String key,
			final Config config) {
		return new File(FileUtil.getApplicationDirectoryPath(key,config));
	}

	/**
	 * Obtain the path to the application directory.  If the application's name is not
	 * provided, or empty, null is returned.  The path is determined by using the
	 * user's home directory and adding the '.' character as well as the lower case
	 * application name.  The application name is provided within applicationConfig
	 * and is located at key applicationNameKey.
	 * @param applicationNameKey <code>java.lang.String</code>
	 * @param applicationConfig <code>Config</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getApplicationDirectoryPath(
		String applicationNameKey,
		Config applicationConfig) {
		final String applicationName =
			applicationConfig.getProperty(applicationNameKey, "");
		if(null == applicationName || "".equals(applicationName))
			return null;
		return new StringBuffer()
			.append(FileUtil.getHomeDirectoryPath())
			.append(File.separator)
			.append('.')
			.append(applicationName.toLowerCase()).toString();
	}

	/**
	 * Obtain the file extension of the File.  If none exists, null is returned.  Is the 
	 * same as:
	 * <code>
	 * FileUtil.getFileNameExtension(file.getName());
	 * </code>
	 * @param file <code>java.io.File</code>
	 * @return <code>java.lang.String</code> or null if no file extension exists
	 */
	public static synchronized String getFileNameExtension(File file) {
		if(!file.exists())
			throw new IllegalArgumentException(
				"File " + file.getAbsolutePath() + " does not exist.");
		if(!file.isFile())
			throw new IllegalArgumentException(
				"File " + file.getAbsolutePath() + " is not a file.");
		return FileUtil.getFileNameExtension(file.getName());
	}

	/**
	 * Obtain the file extension of a file name.  If none exists null is returned.
	 * @param fileName <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */	
	public static synchronized String getFileNameExtension(String fileName) {
		if(-1 != fileName.indexOf(Separator.Period.toString()))
			return fileName.substring(
				fileName.lastIndexOf(Separator.Period.toString()));
		return null;
	}
	
	/**
	 * Obtain the file name without its extension, of the File.  If there is not 
	 * extension the entire name is returned.
	 * @param file <code>java.io.File</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getFileName(File file) {
		if(!file.exists())
			throw new IllegalArgumentException(
				"File " + file.getAbsolutePath() + " does not exist.");
		if(!file.isFile())
			throw new IllegalArgumentException(
				"File " + file.getAbsolutePath() + " is not a file.");
		final String fileName = file.getName();
		if(-1 != fileName.indexOf(Separator.Period.toString()))
			return fileName.substring(
				0,
				fileName.lastIndexOf(Separator.Period.toString()));
		return fileName;
	}
	
	/**
	 * Obtain the path for the file.  If the file is a file, the path of the parent is 
	 * returned; if it is a directory, return its absolute path.  If it is neither, return
	 * null.
	 * @param file <code>java.io.File</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getFilePath(File file) {
		if(!file.exists())
			throw new IllegalArgumentException(
				"File " + file.getAbsolutePath() + " does not exist.");
		if(file.isFile())
			return file.getParent();
		else if(file.isDirectory())
			return file.getAbsolutePath();
		else
			return null;
	}
	
	/**
	 * Obtain the File which represents the user's home directory.
	 * @return <code>java.io.File</code>
	 */
	public static synchronized File getHomeDirectory() {
		return new File(FileUtil.getHomeDirectoryPath());
	}

	public static synchronized File getTempDirectory(
			final String applicationName) {
		final String tempDirectoryPath =
			FileUtil.getTempDirectoryPath(applicationName);
		final File tempDirectory = new File(tempDirectoryPath);
		if(!tempDirectory.exists())
			tempDirectory.mkdir();
		return tempDirectory;
	}

	public static synchronized String getTempDirectoryPath(
			final String applicationName) {
		return new StringBuffer()
			.append(SystemUtil.getSystemProperty(SystemProperty.TempDirectory))
			.append(File.separatorChar)
			.append(".")
			.append(applicationName).toString();
	}

	/**
	 * Obtain the path which represents the user's home directory.
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getHomeDirectoryPath() {
		return SystemUtil.getSystemProperty(SystemProperty.HomeDirectory);
	}
	
	/**
	 * Obtain the File which represents the working directory of the jvm.
	 * @return <code>java.io.File</code>
	 */
	public static synchronized File getWorkingDirectory() {
		return new File(FileUtil.getWorkingDirectoryPath());
	}
	
	/**
	 * Obtain the path which represents the working directory of the jvm.
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String getWorkingDirectoryPath() {
		return  SystemUtil.getSystemProperty(SystemProperty.WorkingDirectory);
	}

	public static synchronized StringBuffer readURL(URL url) throws IOException {
		StringBuffer content = new StringBuffer();
		InputStreamReader inputStreamReader =
			new InputStreamReader(url.openStream());
		int characterRead = inputStreamReader.read();
		while(-1 != characterRead) {
			content.append((char) characterRead);
			characterRead = inputStreamReader.read();
		}
		inputStreamReader.close();
		return content;
	}

	/**
	 * Read file into a byte[]
	 * @param file <code>File</code>
	 * @return <code>byte[]</code>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static byte[] readFile(File file) throws FileNotFoundException,
			IOException {
		final FileInputStream fis = new FileInputStream(file);
		final ByteBuffer byteBuffer = ByteBuffer.allocate(fis.available());
		final byte[] fileContentBuffer = new byte[1024];
		int numBytesRead = fis.read(fileContentBuffer);
		while(-1 != numBytesRead) {
			byteBuffer.put(fileContentBuffer, 0, numBytesRead);
			// re-read from the stream
			numBytesRead = fis.read(fileContentBuffer);
		}
		fis.close();
		return byteBuffer.array();
	}
	
	/**
	 * Write the content of a StringBuffer to a File
	 * @param file <code>java.io.File</code>
	 * @param content <code>java.lang.StringBuffer</code>
	 * @throws IOException
	 */
	public static synchronized void writeFile(File file, StringBuffer content, Charset charset)
		throws IOException {
		FileUtil.writeFile(file, content.toString().getBytes(charset.toString()));
	}
	
	/**
	 * Write the content of a StringBuffer to a File using the default character set.
	 * @param file <code>java.io.File</code>
	 * @param content <code>java.lang.StringBuffer</code>
	 */
	public static synchronized void writeFile(File file, StringBuffer content)
		throws IOException {
		FileUtil.writeFile(file, content, DEFAULT_CHARSET);
	}
	
	/**
	 * Write the content of a byte[] to a File
	 * @param file <code>java.io.File</code>
	 * @param content <code>byte[]</code>
	 * @throws IOException
	 */
	public static synchronized void writeFile(File file, byte[] content)
		throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		int amountWritten = 0;
		final int contentLength = content.length;
		int amountToWrite;
		while(amountWritten < contentLength) {
			amountToWrite = BLOCK_SIZE > contentLength - amountWritten
				?contentLength - amountWritten
				:BLOCK_SIZE;
			fileOutputStream.write(content, amountWritten, amountToWrite);
			amountWritten += amountToWrite;
		}
		fileOutputStream.flush();
		fileOutputStream.close();
	}

	public static synchronized File writeTempFile(final String applicationName,
			final String filename, final byte[] content) throws IOException {
		final File tempFile =
			new File(FileUtil.getTempDirectory(applicationName), filename);
		if(tempFile.exists())
			tempFile.delete();
		FileUtil.writeFile(tempFile, content);
		tempFile.deleteOnExit();
		return tempFile;
	}
}
