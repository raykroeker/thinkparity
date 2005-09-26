/*
 * Sep 13, 2003
 */
package com.thinkparity.codebase;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import com.thinkparity.codebase.SystemUtil.SystemProperty;

/**
 * <b>Title:</b>  StringUtil
 * <br><b>Description:</b>  Contains utilities and constants commonly used in
 * String and StringBuffer manipulation.
 * @author raykroeker@gmail.com
 * @version 1.0.1
 */
public abstract class StringUtil {
	
	/**
	 * <b>Title:</b>  Charset
	 * <br><b>Description:</b>  Charset is used as a quick-reference for
	 * commonly used character sets supported by the java implementation.
	 * @author raykroeker@gmail.com
	 * @version 1.0.0
	 */
	public static final class Charset extends Enum {

		private static final long serialVersionUID = 1;
	
		/**
		 * 7-bit ASCII
		 */
		public static final Charset Us_Ascii = new Charset("US-ASCII");
		
		/**
		 * ISO Latin Alphabet 1
		 */
		public static final Charset Iso_8859_1 = new Charset("ISO-8859-1");

		/**
		 * UTF-8
		 */
		public static final Charset Utf_8 = new Charset("UTF-8");
		
		/**
		 * UTF-16-BE (Big Endian)
		 */
		public static final Charset Utf_16_Be = new Charset("UTF-16BE");
		
		/**
		 * UTF-16-LE (Little Endian)
		 */
		public static final Charset Utf_16_Le = new Charset("UTF-16LE");
		
		/**
		 * UTF-16
		 */
		public static final Charset Utf_16 = new Charset("UTF-16");

		/**
		 * Find a named character set.
		 * @param charset <code>java.lang.String</code>
		 * @return <code>StringUtil$Charset</code>
		 */
		public static Charset find(String charset) {
			return (Charset) Enum.find(Charset.class, charset);
		}

		/**
		 * Create a new Charset
		 * @param charset <code>java.lang.String</code>
		 */
		private Charset(String charset) {super(charset);}
	}
	
	/**
	 * <b>Title:</b>  Separator
	 * <br><b>Description:</b>  Separator is used as a place holder for various 
	 * string constants commonly used in String manipulation.  
	 * @author raykroeker@gmail.com
	 * @version 1.0.1
	 */
	public static final class Separator extends Enum {

		private static final long serialVersionUID = 1;

		/**
		 * Comma
		 */
		public static final Separator Comma = new Separator(",");

		/**
		 * Dash
		 */
		public static final Separator Dash = new Separator("-");
		
		/**
		 * Double quote
		 */
		public static final Separator DblQuote = new Separator("\"");

		/**
		 * Equals
		 */
		public static final Separator Equals = new Separator("=");

		/**
		 * Empty String
		 */
		public static final Separator EmptyString = new Separator("");

		/**
		 * Full colon
		 */
		public static final Separator FullColon = new Separator(":");

		/**
		 * Greater Than
		 */
		public static final Separator GreaterThan = new Separator(">");

		/**
		 * Less Than
		 */
		public static final Separator LessThan = new Separator("<");
		
		/**
		 * Nix new line
		 */
		public static final Separator NixNewLine = new Separator("\n");
		
		/**
		 * Double nix new line
		 */
		public static final Separator DoubleNixNewLine =
			new Separator(NixNewLine.append(NixNewLine));

		/**
		 * Period
		 */
		public static final Separator Period = new Separator(".");

		/**
		 * Open parenthesis
		 */
		public static final Separator OpenParenthesis = new Separator("(");
		
		/**
		 * Close parenthesis
		 */
		public static final Separator CloseParenthesis = new Separator(")");

		/**
		 * Question mark
		 */
		public static final Separator QuestionMark = new Separator("?");

		/**
		 * Semi colon
		 */
		public static final Separator SemiColon = new Separator(";");

		/**
		 * Single quote
		 */
		public static final Separator SingleQuote = new Separator("\'");

		/**
		 * Space
		 */
		public static final Separator Space = new Separator(" ");

		/**
		 * Comma space
		 */
		public static final Separator CommaSpace =
			new Separator(Comma.append(Space));

		/**
		 * Double space
		 */
		public static final Separator DoubleSpace =
			new Separator(Space.append(Space));

		/**
		 * Period double space
		 */
		public static final Separator PeriodDblSpace =
			new Separator(Period.append(DoubleSpace));

		/**
		 * Quadruple space
		 */
		public static final Separator QuadrupleSpace =
			new Separator(DoubleSpace.append(DoubleSpace));

		/**
		 * Space dash space
		 */
		public static final Separator SpaceDashSpace = 
			new Separator(Space.append(Dash).append(Space));

		/**
		 * Open square bracket
		 */
		public static final Separator OpenSquareBracket =
			new Separator("[");

		/**
		 * Close square bracket
		 */
		public static final Separator CloseSquareBracket =
			new Separator("]");

		/**
		 * System's new line
		 */
		public static final Separator SystemNewLine =
			new Separator(SystemUtil.getSystemProperty(SystemProperty.LineSeparator));

		/**
		 * Double system new line
		 */
		public static final Separator DoubleSystemNewLine =
			new Separator(SystemNewLine.append(SystemNewLine));

		/**
		 * Tab
		 */
		public static final Separator Tab = new Separator("\t");

		/**
		 * Double tab
		 */
		public static final Separator DoubleTab = new Separator(Tab.append(Tab));

		/**
		 * Underscore
		 */
		public static final Separator Underscore = new Separator("_");

		/**
		 * Windows new line
		 */
		public static final Separator WindowsNewLine = new Separator("\r\n");
		
		/**
		 * Double windows new line
		 */
		public static final Separator DoubleWindowsNewLine =
			new Separator(WindowsNewLine.append(WindowsNewLine));

		/**
		 * Windows new line tab
		 */
		public static final Separator WindowsNewLineTab =
			new Separator(WindowsNewLine.append(Tab));

		/**
		 * Create a new Separator
		 * @param separator <code>java.lang.String</code>
		 */
		private Separator(String separator) {
			this(new StringBuffer().append(separator));
		}

		/**
		 * Create a new Separator
		 * @param separator <code>java.lang.StringBuffer</code>
		 */
		private Separator(StringBuffer separator) {super(separator);}
		
		/**
		 * Append to the end of this Separator, the value of separator
		 * @param separator <code>StringUtil$Separator</code>
		 * @return <code>java.lang.StringBuffer</code>
		 */
		private StringBuffer append(Separator separator) {return toBuffer().append(separator);}
		
		/**
		 * Obtain this Separator's value as a StringBuffer
		 * @return <code>java.lang.StringBuffer</code>
		 */
		private StringBuffer toBuffer() {return new StringBuffer().append(toString());}
	}

	/**
	 * Build a name StringBuffer checking for null.
	 * @param first <code>java.lang.String</code>
	 * @param second <code>java.lang.String</code>
	 * @param separator <code>StringUtil$Separator</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */	
	public static synchronized StringBuffer buildName(
		String first,
		String second,
		Separator separator) {
		if(null == separator)
			throw new NullPointerException();
		StringBuffer name = new StringBuffer();
		if(null != first && 0 < first.length()) {
			if(null != second && 0 < second.length()) {
				name.append(second)
					.append(separator)
					.append(first);
			}
			else {
				name.append(first);
			}
		}
		else {
			if(null != second  && 0 < second.length()) {
				name.append(second);
			}
		}
		return name;
	}

	/**
	 * Build a name StringBuffer checking for null.
	 * @param first <code>java.lang.StringBuffer</code>
	 * @param second <code>java.lang.StringBuffer</code>
	 * @param separator <code>StringUtil$Separator</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public static synchronized StringBuffer buildName(
		StringBuffer first,
		StringBuffer second,
		Separator separator) {
		return buildName(
			null == first ? null : first.toString(),
			null == second ? null : second.toString(),
			separator);
	}

	/**
	 * Convert a list of objects into a separated list.
	 * @param list <code>java.util.Collection</code> of
	 * <code>java.lang.Object</code>s
	 * @param separator <code>StringUtil$Separator</code>
	 * @return <code>java.lang.StringBuffer</code> or null if list is null
	 * @throws NullPointerException if separator is null
	 */
	public static synchronized StringBuffer convertFromList(Collection<Object> list, Separator separator) {
		if(null == list)
			return null;
		if(null == separator)
			throw new NullPointerException();
		StringBuffer convertedList = null;
		int listIndex = 0;
		for(Iterator<Object> iList = list.iterator(); iList.hasNext(); listIndex++) {
			if(0 == listIndex) { convertedList = new StringBuffer(); }
			else { convertedList.append(separator); }
			convertedList.append(iList.next());
		}
		return convertedList;
	}

	public static synchronized StringBuffer[] convertToArrayList(String list, String separator) {
		final Collection<StringBuffer> convertedList =
		    StringUtil.convertToList(list, separator);
		return null == convertedList
			? null
			: (StringBuffer[]) convertedList.toArray(new StringBuffer[0]);
	}

	public static synchronized StringBuffer[] convertToArrayList(String list, StringBuffer separator) {
		return StringUtil.convertToArrayList(list,  null == separator ? null : separator.toString());
	}
	
	public static synchronized StringBuffer[] convertToArrayList(String list, Separator separator) {
		return StringUtil.convertToArrayList(list,  null == separator ? null : separator.toString());
	}

	public static StringBuffer[] convertToArrayList(StringBuffer list, String separator) {
		return StringUtil.convertToArrayList(null == list ? null : list.toString(),  null == separator ? null : separator.toString());
	}

	public static StringBuffer[] convertToArrayList(StringBuffer list, StringBuffer separator) {
		return StringUtil.convertToArrayList(list,  null == separator ? null : separator.toString());
	}
	
	public static StringBuffer[] convertToArrayList(StringBuffer list, Separator separator) {
		return StringUtil.convertToArrayList(list,  null == separator ? null : separator.toString());
	}

	public static synchronized Collection<StringBuffer> convertToList(String list, String separator) {
		if(null == separator)
			throw new NullPointerException();
		if(null == list || 0 == list.length())
			return null;
		Collection<StringBuffer> convertedList = null;
		int currentIndex = 0;
		int nextIndex = list.indexOf(separator.toString());
		if(-1 == nextIndex) {	// The separator doesn't exist in the list at all
			convertedList = new Vector<StringBuffer>(1);
			convertedList.add(new StringBuffer().append(list));
		}
		else {
			while(-1 != nextIndex) {
				if(null == convertedList)
					convertedList = new Vector<StringBuffer>();
				convertedList.add(
					new StringBuffer().append(
						list.substring(currentIndex, nextIndex)));
				currentIndex = nextIndex + 1;
				nextIndex = list.indexOf(separator.toString(), currentIndex);
			}
		}
		return convertedList;
	}

	public static synchronized Collection<StringBuffer> convertToList(String list, StringBuffer separator) {
		return StringUtil.convertToList(list, null == separator ? null : separator.toString());
	}
	
	public static synchronized Collection<StringBuffer> convertToList(String list, Separator separator) {
		return StringUtil.convertToList(list, null == separator ? null : separator.toString());
	}

	public static Collection<StringBuffer> convertToList(StringBuffer list, String separator) {
		return StringUtil.convertToList(null == list ? null : list.toString(), separator);
	}

	public static Collection<StringBuffer> convertToList(StringBuffer list, StringBuffer separator) {
		return StringUtil.convertToList(null == list ? null : list.toString(),  null == separator ? null : separator.toString());
	}
	
	public static Collection<StringBuffer> convertToList(StringBuffer list, Separator separator) {
		return StringUtil.convertToList(null == list ? null : list.toString(),  null == separator ? null : separator.toString());
	}
	


	/**
	 * Remove the whitespace from target, replacing it with replacement or the 
	 * empty string if replacement is <code>null</code>.
	 * @param target <code>java.lang.String</code>
	 * @param replacement <code>StringUtil$Separator</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */	
	public static synchronized StringBuffer removeWhitespace(
		String target,
		Separator replacement) {
		return StringUtil.removeWhitespace(
			target,
			null == replacement ? null : replacement.toString());
	}

	/**
	 * Remove the whitespace from target, replacing it with replacement or the
	 * empty string if replacement is <code>null</code>.
	 * @param target <code>java.lang.StringBuffer</code>
	 * @param replacement <code>java.lang.StringBuffer</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public static synchronized StringBuffer removeWhitespace(
		StringBuffer target,
		StringBuffer replacement) {
		return StringUtil.removeWhitespace(
			null == target ? null : target.toString(),
			null == replacement ? null : replacement.toString());
	}

	/**
	 * Remove the whitespace from target, replacing it with replacement or the
	 * empty string if replacement is <code>null</code>.
	 * @param target <code>java.lang.String</code>
	 * @param replacement <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public static final StringBuffer removeWhitespace(
		String target,
		String replacement) {
		if(null == target)
			return null;
		if(null == replacement)
			replacement = "";
		StringBuffer buffer = new StringBuffer();
		char[] targetChars = target.toCharArray();
		for(int i = 0; i < targetChars.length; i++) {
			if( Character.isWhitespace(targetChars[i]))
				buffer.append(replacement);
			else
				buffer.append(targetChars[i]);
		}
		return buffer;
	}

    /**
     * Does a search and replace on a string based on the find and replace
     * criteria.  This method will replace all occurances of find in search 
     * with replace.
     * @param search <code>java.lang.StringUtil</code> the string to 
     * search through
     * @param find <code>java.lang.StringUtil</code> the text to look for
     * @param replace <code>java.lang.StringUtil</code> the text to replace
     * @return <code>java.lang.StringBuffer</code> the new text with 
     * the text replaced
     */
	public static StringBuffer searchAndReplace(
		String search,
		String find,
		String replace) {
    	if(null == search)
    		return null;
    	if(null == find)
    		return new StringBuffer(search);
    	if(null == replace)
    		throw new IllegalArgumentException("replace must be provided.");
        StringBuffer processedString = new StringBuffer();
        int findStart = search.indexOf(find);
        int currentPos = 0;
        while(-1 != findStart) {
            processedString.append(search.substring(currentPos, findStart));
            processedString.append(replace);
            currentPos = findStart + find.length();
            findStart = search.indexOf(find, currentPos);
        }
        processedString.append(search.substring(currentPos));
        return processedString;
    }

    /**
     * Does a search and replace on a string based on the find and replace
     * criteria.  This method will replace all occurances of find in search 
     * with replace.
     * @param search <code>java.lang.StringUtil</code> the string to 
     * search through
     * @param find <code>java.lang.StringUtil</code> the text to look for
     * @param replace <code>java.lang.StringUtil</code> the text to replace
     * @return <code>java.lang.StringBuffer</code> the new text with 
     * the text replaced
     */
	public static StringBuffer searchAndReplace(
		StringBuffer search,
		String find,
		String replace) {
		return StringUtil.searchAndReplace(
			null == search ? null : search.toString(),
			find,
			replace);
	}

	/**
	 * Does a search and replace on a string based on the find and replace
	 * criteria.  This method will replace all occurances of find in search 
	 * with replace.
	 * @param search <code>java.lang.StringBuffer</code> the string to 
	 * search through
	 * @param find <code>java.lang.StringBuffer</code> the text to look for
	 * @param replace <code>java.lang.StringBuffer</code> the text to replace
	 * @return <code>java.lang.StringBufferBuffer</code> the new text with 
	 * the text replaced
	 */
	public static StringBuffer searchAndReplace(
		StringBuffer search,
		StringBuffer find,
		StringBuffer replace) {
		return StringUtil.searchAndReplace(
			null == search ? null : search.toString(),
			null == find ? null : find.toString(),
			null == replace ? null : replace.toString());
    }

	/**
	 * Remove any characters after the find string within the search string.
	 * @param search String
	 * @param find String
	 * @return String
	 */
	public static synchronized String removeAfter(final String search,
			final String find) {
		if(null == search)
			return null;
		if(null == find)
			return search;
		final Integer findIndex = search.lastIndexOf(find);
		if(findIndex > 0)
			return search.substring(0, findIndex);
		return search;
	}

	public static String convertFrom(final byte[] bytes)
			throws UnsupportedEncodingException {
		return new String(bytes, Charset.Iso_8859_1.toString());
	}

	public static byte[] convertToBytes(final String string)
			throws UnsupportedEncodingException {
		return string.getBytes(Charset.Iso_8859_1.toString());
	}

	/**
	 * Create a StringUtil [Singleton]
	 */
	private StringUtil() { super(); }
}
