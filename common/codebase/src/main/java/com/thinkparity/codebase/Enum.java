/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/** 
 * <b>Title:</b>  Enum
 * <br><b>Description:</b>  Enumeration provides the facility to create
 * enumerated types.  Each enumerated enumValue is ordered in the order it was created.
 * To create an enumerated enumValue, extend Enumeration, and create instances of the
 * extended enumValue that are final.
 * Example:
 *	public static final class SysLogLevel extends Enum {
 *		public static final SysLogLevel Debug = new SysLogLevel("Debug");
 *		public static final SysLogLevel Trace = new SysLogLevel("Trace");
 *		public static final SysLogLevel Sql = new SysLogLevel("Sql");
 *		public static final SysLogLevel Warning = new SysLogLevel("Warning");
 *		public static final SysLogLevel Error = new SysLogLevel("Error");
 *		public static final SysLogLevel Exception = new SysLogLevel("Exception");
 *		public static final SysLogLevel Fatal = new SysLogLevel("Fatal");
 *		public static final SysLogLevel System = new SysLogLevel("System");
 *		
 *		private LogLevel( StringUtil newLevel ) { super( newLevel ); }
 *

 *	}
 * Usage Example:
 * SysLog myLogHandle = new SysLog(SysLogLevel.Debug);
 * 
 * Updated to JDK 1.5
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class Enum implements Serializable {

	/**
	 * Represents the map of all enum types to their list of options.
	 */
	private static final Map<Class<?>, Vector<Enum>> globalEnums =
		new Hashtable<Class<?>, Vector<Enum>>();
	
	/**
	 * Represents the map of all enum types to their list of option values.  This list of
	 * enum values is maintained for the purpose of preventing registrations which
	 * are non unique.  It must not be used for any other purpose.  If using your 
	 * own objects as values, override the equals method to examine for identity.
	 */
	private static final Map<Class<?>, Vector<Object>> globalEnumValues =
		new Hashtable<Class<?>, Vector<Object>>();

	/**
	 * Locate the enumerated enumValue in the master list.  This method was given 
	 * the protected modifier because it might not always be the case that the client 
	 * of Enumeration would want to make available the ability to find a enumValue.
	 * @param myEnum <code>java.lang.Class</code>
	 * @param searchValue <code>java.lang.Object</code>
	 */
	protected static Enum find(final Class<?> myEnum, final Object searchValue) {
		if(null == myEnum)
			return null;
		if(null == searchValue)
			return null;
		Vector<Enum> allEnums = Enum.getList(myEnum);
		if(null == allEnums)
			return null;
		Enum currentEnum = null;
		for(Iterator<Enum> iAllEnums = allEnums.iterator(); iAllEnums.hasNext(); ) {
			currentEnum = iAllEnums.next();
			if(currentEnum.getEnumValue().equals(searchValue))
				return currentEnum;
		}
		return null;
	}
	
	/**
	 * Obtain the list of all types for a particular enum
	 * @param myEnum <code>java.lang.Class</code>
	 * @return <code>java.util.Collection</code> or null if there is no
	 * corresponding type list
	 */
	protected static Vector<Enum> getList(final Class<?> myEnum) {
		if(null == myEnum)
			return null;
		Vector<Enum> allEnums = globalEnums.get(myEnum);
		if(null == allEnums)
			return null;
		Vector<Enum> allEnums2 = new Vector<Enum>();
		allEnums2.addAll(allEnums);
		return allEnums2;
	}
	
	/**
	 * Obtain the number of types for an enum.
	 * @param myEnum <code>java.lang.Class</code>
	 * @return <code>int</code>
	 */
	protected static int sizeOf(final Class<?> myEnum) {
		Vector<?> list = Enum.getList(myEnum);
		if(null == list)
			return 0;
		return list.size();
	}
	
	/**
	 * Represents the current enum type
	 */
	private Object enumValue;
	
	/**
	 * Create a new Enum.  Will be strict.
	 * @param enumValue <code>java.lang.Object</code>
	 */
	protected Enum(Object enumValue) {this(enumValue, true);}
	
	/**
	 * Create a new Enum
	 * @param enumValue <code>java.lang.Object</code>
	 * @param isStrict <code>boolean</code>
	 */
	protected Enum(Object enumValue, boolean isStrict) { 
		super();
		this.enumValue = enumValue;
		/*
		 * Obtain the ordered list of enumerations for this enum
		 */
		Vector<Enum> enumList = globalEnums.get(getClass());
		Vector<Object> enumValueList = globalEnumValues.get(getClass());
		/*
		 * Create a list for this enum if it is new
		 */
		if(null == enumList) {
			enumList = new Vector<Enum>();
			globalEnums.put(getClass(), enumList);
		}
		if(null == enumValueList) {
			enumValueList = new Vector<Object>();
			globalEnumValues.put(getClass(), enumValueList);
		}
		/*
		 * Check to see if this enum has already been registered
		 */
		if(isStrict) {
			if(enumValueList.contains(enumValue))
				throw new RuntimeException(
					"The enum value "
						+ enumValue
						+ " has alread been registered.");
		}
		/*
		 * Add this enum to the list
		 */
		enumList.add(this);
		enumValueList.add(enumValue);
	}

	/**
	 * Obtain the object representing this enum
	 * @return <code>java.lang.Object</code>
	 */
	protected Object getEnumValue() {return enumValue;}

	/**
	 * Will return the toString() of the enumValue stored in the Enum.
	 * @see java.lang.Object#toString()
	 */
	public String toString() {return getEnumValue().toString();}
	
}