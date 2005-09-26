/*
 * Sep 21, 2003
 */
package com.thinkparity.codebase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;

/**
 * <b>Title:</b>  ConsoleUI
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public abstract class ConsoleUI {

	/**
	 * The print stream to write to
	 */
	private static final PrintStream consoleOut = System.out;
	
	/**
	 * The input stream to read from
	 */
	private static final InputStream consoleIn = System.in;

	/**
	 * A helper object to read from the input stream
	 */
	private static BufferedReader consoleReader;
	
	/**
	 * Indicates whether or not the initialization has been performed
	 */
	private static boolean isInitialized = false;
	
	/**
	 * Create a new ConsoleUI
	 */
	private ConsoleUI() {super();}
	
	/**
	 * Initialize the consoleReader.  If doReInitialize, then the reader is re-created.
	 * @param doReInitialize <code>boolean</code>
	 */
	private static synchronized void initialize(boolean doReInitialize) {
		if(false == isInitialized || true == doReInitialize) {
			consoleReader =
				new BufferedReader(new InputStreamReader(consoleIn));
		}
	}
	
	/**
	 * Search through args for the key, and return the value immediately proceding
	 * it.  If it cannot be found, return null.
	 * @param args <code>java.lang.String[]</code>
	 * @param key <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 */
	public static synchronized String findArg(String[] args, String key) {
		String value = null;
		for(int index = 0; index < args.length; index++) {
			if(args[index].equals(key))
				value = (args.length > (index + 1)
					?args[index + 1]
					:null);
		}
		return value;
	}

	/**
	 * Obtain Boolean input from the console.  If acceptableTrue is provided, then
	 * the input is compared to every element in the list and if it matches, true is
	 * returned.
	 * @param prompt <code>java.lang.StringBuffer</code>
	 * @param acceptableTrue <code>java.lang.String[]</code>
	 * @param doIgnoreCase <code>boolean</code>
	 * @param doReprompt <code>boolean</code>
	 * @return <code>java.lang.Boolean</code>
	 * @throws IOException
	 */
	public static synchronized Boolean getBooleanInput(
		StringBuffer prompt,
		String[] acceptableTrue,
		boolean doIgnoreCase,
		boolean doReprompt) throws IOException {
		ConsoleUI.initialize(false);

		String input = null;
		boolean isFirstIteration = true;
		while(true == isFirstIteration || true == doReprompt) {
			isFirstIteration = false;
			input = ConsoleUI.getInput(prompt);
			if(null != acceptableTrue && 0 < acceptableTrue.length) {
				for(int trueIndex = 0; trueIndex < acceptableTrue.length; trueIndex++) {
					if(true == doIgnoreCase && true == acceptableTrue[trueIndex].equalsIgnoreCase(input))
						return new Boolean(true);
					else if(acceptableTrue[trueIndex].equals(input))
						return new Boolean(true);
				}
			}
			return new Boolean(false);
		}
		return null;
	}
	
	/**
	 * Obtain a file which must match the acceptableInput 
	 * <code>FilenameFilter</code>s as well as the mustExist, mustRead, and 
	 * mustWrite flags.
	 * @param prompt <code>java.lang.StringBuffer</code>
	 * @param acceptableInput <code>java.io.FilenameFilter[]</code>
	 * @param mustExist <code>boolean</code>
	 * @param mustRead <code>boolean</code>
	 * @param mustWrite <code>boolean</code>
	 * @param doReprompt <code>boolean</code>
	 * @return <code>java.io.File</code>
	 * @throws IOException
	 */
	public static synchronized File getFileInput(
		StringBuffer prompt,
		FilenameFilter[] acceptableInput,
		boolean mustExist,
		boolean mustRead,
		boolean mustWrite,
		boolean doReprompt)
		throws IOException {
		ConsoleUI.initialize(false);
		
		String filePath = null;
		File input = null;
		boolean isFirstIteration = true;
		while(true == isFirstIteration || true == doReprompt) {
			isFirstIteration = false;
			filePath = ConsoleUI.getInput(prompt);
			input = new File(filePath);
			if(mustExist) {
				if(!input.exists()) {
					ConsoleUI.println(filePath + " does not exist.");
					continue;
				}
			}
			if(mustRead) {
				if(!input.canRead()) {
					ConsoleUI.println(filePath + " cannot be read.");
					continue;
				}
			}
			if(mustWrite) {
				if(!input.canWrite()) {
					ConsoleUI.println(filePath + " cannot be written to.");
					continue;
				}
			}
			if(null != acceptableInput && 0 < acceptableInput.length) {
				boolean didMatch = false;
				for(int acceptableIndex = 0; acceptableIndex < acceptableInput.length; acceptableIndex++) {
					if(acceptableInput[acceptableIndex].accept(input.getParentFile(), input.getName()))
						didMatch = true;
				}
				if(didMatch)
					return input;
				else {
					ConsoleUI.println(filePath + " did not match filter crieteria.");
					continue; 
				}
			}
			else {
				/* There is no acceptable input, so any file will do. */
				return input;
			}
		}
		return null;
	}
	
	/**
	 * Obtain Integer input from the console.  If acceptablieInput is provided, then
	 * all user input is checkec against it for vailidity.  If not, all user input is returned.
	 * If doRePrompt is set to true, this method will lopp the prompt until the criteria
	 * as mentioned above for acceptableInput has been met.
	 * @param prompt <code>java.lang.StringBuffer</code>
	 * @param acceptableInput <code>java.lang.Integer[]</code>
	 * @param doRePrompt <code>boolean</code>
	 * @return <code>java.lang.Integer</code>
	 * @throws IOException
	 */
	public static synchronized Integer getIntegerInput(
		StringBuffer prompt,
		Integer[] acceptableInput,
		boolean doRePrompt)
		throws IOException {
		ConsoleUI.initialize(false);
		
		Integer input = null;
		boolean isFirstIteration = true;
		while(true == isFirstIteration || true == doRePrompt) {
			isFirstIteration = false;
			try {
				input = new Integer(Integer.parseInt(ConsoleUI.getInput(prompt)));
			}
			catch(NumberFormatException nfx) {}	// Do Nothing
			if(null != acceptableInput && 0 < acceptableInput.length) {
				for(int index = 0; index < acceptableInput.length; index++) {
					if(acceptableInput[index].intValue() == input.intValue()) {
						return input;
					}
				}
			}
			else {
				return input;
			}
		}
		return null;
	}

	/**
	 * Obtain String input from the console.  If acceptableInput is provided, then
	 * all user input is checked against it for validity.  If not, all user input is
	 * returned.  If doRePrompt is set to true, this method will loop the prompt
	 * until the criteria as mentioned above for acceptableInput has been met.
	 * If not, then null is returned. 
	 * @param prompt <code>java.lang.StringBuffer</code>
	 * @param acceptableInput <code>java.lang.String[]</code>
	 * @param doRePrompt <code>boolean</code>
	 * @return <code>java.lang.String</code>
	 * @throws IOException
	 */
	public static synchronized String getStringInput(
		StringBuffer prompt,
		String[] acceptableInput,
		boolean doIgnoreCase,
		boolean doRePrompt)
		throws IOException {
		ConsoleUI.initialize(false);

		String input = null;
		boolean isFirstIteration = true;
		while(true == isFirstIteration || true == doRePrompt) {
			isFirstIteration = false;
			input = ConsoleUI.getInput(prompt);
			if(null != acceptableInput && 0 < acceptableInput.length) {
				for(int index = 0; index < acceptableInput.length; index++) {
						if(true == doIgnoreCase) {
							if(acceptableInput[index].equalsIgnoreCase(input)) {
								return input;
							}
						}
						else {
							if(acceptableInput[index].equals(input)) {
								return input;
							}
						}
				}
			}
			else {
				/*
				 * If no acceptableInput is provided, then any input is acceptable.
				 */
				return input;
			}
		}
		return null;
	}

	public static synchronized void println(boolean boolean1) {
		ConsoleUI.println(String.valueOf(boolean1));
	}

	public static synchronized void println(byte byte1) {
		ConsoleUI.println(String.valueOf(byte1));
	}

	public static synchronized void println(char char1) {
		ConsoleUI.println(String.valueOf(char1));
	}
	
	public static synchronized void println(double double1) {
		ConsoleUI.println(String.valueOf(double1));
	}

	public static synchronized void println(float float1) {
		ConsoleUI.println(String.valueOf(float1));
	}

	public static synchronized void println(int int1) {
		ConsoleUI.println(String.valueOf(int1));
	}

	public static synchronized void println(long long1) {
		ConsoleUI.println(String.valueOf(long1));
	}

	public static synchronized void println(short short1) {
		ConsoleUI.println(String.valueOf(short1));
	}

	public static synchronized void println(Object object) {
		ConsoleUI.println(String.valueOf(object));
	}
	
	public static synchronized void println(String string) {
		consoleOut.println(string);
	}
	
	public static synchronized void print(boolean boolean1) {
		ConsoleUI.print(String.valueOf(boolean1));
	}

	public static synchronized void print(byte byte1) {
		ConsoleUI.print(String.valueOf(byte1));
	}

	public static synchronized void print(char char1) {
		ConsoleUI.print(String.valueOf(char1));
	}
	
	public static synchronized void print(double double1) {
		ConsoleUI.print(String.valueOf(double1));
	}

	public static synchronized void print(float float1) {
		ConsoleUI.print(String.valueOf(float1));
	}

	public static synchronized void print(int int1) {
		ConsoleUI.print(String.valueOf(int1));
	}

	public static synchronized void print(long long1) {
		ConsoleUI.print(String.valueOf(long1));
	}

	public static synchronized void print(short short1) {
		ConsoleUI.print(String.valueOf(short1));
	}

	public static synchronized void print(Object object) {
		ConsoleUI.print(String.valueOf(object));
	}

	public static synchronized void print(Properties properties) {
		if(null != properties)
			properties.list(consoleOut);
	}
	
	public static synchronized void print(String string) {
		consoleOut.print(string);
	}

	/**
	 * Write the prompt to consoleOut and read the response from consoleIn. 
	 * @param prompt <code>java.lang.StringBuffer</code>
	 * @return <code>java.lang.String</code>
	 * @throws IOException
	 */
	private static synchronized String getInput(StringBuffer prompt)
		throws IOException {
		consoleOut.print(prompt);
		return consoleReader.readLine();
	}
}
