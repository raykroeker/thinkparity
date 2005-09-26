/*
 * May 15, 2005
 */
package com.thinkparity.codebase.log4j;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import com.thinkparity.codebase.StringUtil.Separator;

/**
 * LoggerFormatter Used to format common java types such that they can be
 * logged.
 * 
 * @author raykroeker@gmail.com
 */
public class LoggerFormatter {

	/**
	 * Create a LoggerFormatter
	 */
	public LoggerFormatter() { super(); }

	/**
	 * Format a list of strings.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @param stringList
	 *            <code>java.util.Collection&lt;java.lang.String&gt;</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context,
			final Collection<String> stringList) {
		final StringBuffer message = new StringBuffer(openContext(context));
		if(null != stringList) {
			int index = 0;
			for(Iterator<String> i = stringList.iterator(); i.hasNext();) {
				message.append(Separator.SystemNewLine).append(Separator.Tab).append(Separator.Tab)
					.append("<item.").append(index++).append(">")
					.append(i.next())
					.append("</item.").append(index).append(">");
			}
		}
		return message.append(closeContext(context));
	}

	/**
	 * Format a universally unique id.
	 * @param context <code>java.lang.String</code>
	 * @param uuid <code>java.util.UUID</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context, final UUID uuid) {
		return format(context, uuid.toString());
	}

	/**
	 * Format a file.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @param file
	 *            <code>java.io.File</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context, final File file) {
		return new StringBuffer(openContextInline(context))
			.append(null == file ? "null" : file.getAbsolutePath())
			.append(closeContextInline(context));
	}

	/**
	 * Format an integer.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @param integer
	 *            <code>java.lang.Integer</code>
	 * @return <code>java.lang.Stringbuffer</code>
	 */
	public StringBuffer format(final String context, final Integer integer) {
		return new StringBuffer(openContextInline(context))
			.append(integer)
			.append(closeContextInline(context));
	}
/*
	public StringBuffer format(final String context,
			final Collection<Loggable> loggables) {
		if(null == loggables) {
			return new StringBuffer(openContextInline(context))
				.append((String) null)
				.append(closeContextInline(context));
		}
		else {
			final StringBuffer format = new StringBuffer(openContext(context))
				.append(openContext("loggables"));
			for(Iterator<Loggable> i = loggables.iterator(); i.hasNext();) {
				format.append(i.next().logMe());
			}
			return format.append(closeContext("loggables"))
				.append(closeContext(context));
		}
	}
*/
	/**
	 * Format a loggable type.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @param loggable
	 *            <code>com.raykroeker.codebase.util.log4j.Loggable</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context, final Loggable loggable) {
		if(null == loggable) {
			return new StringBuffer(openContextInline(context))
				.append((String) null)
				.append(closeContextInline(context));
		}
		else {
			return new StringBuffer(openContext(context))
				.append(loggable.logMe())
				.append(closeContext(context));
		}
	}

	/**
	 * Format a string.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @param string
	 *            <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context, final String string) {
		return new StringBuffer(openContextInline(context))
			.append(string)
			.append(closeContextInline(context));
	}

	/**
	 * Write a closing xml tag for the context including an additional newline
	 * character.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	protected StringBuffer closeContext(final String context) {
		final StringBuffer contextBuffer = new StringBuffer()
			.append(Separator.SystemNewLine)
			.append("</");
		if(null == context) { contextBuffer.append("null"); }
		else { contextBuffer.append(context.toLowerCase()); }
		return contextBuffer.append(">");
	}

	/**
	 * Write a closing xml tag for the context without adding an additional
	 * newline character.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	protected StringBuffer closeContextInline(final String context) {
		final StringBuffer contextBuffer = new StringBuffer()
			.append("</");
		if(null == context) { contextBuffer.append("null"); }
		else { contextBuffer.append(context.toLowerCase()); }
		return contextBuffer.append(">");
	}

	/**
	 * Write a opening xml tag for the context adding an additional
	 * newline character, followed by a newline and a tab.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	protected StringBuffer openContext(final String context) {
		final StringBuffer contextBuffer = new StringBuffer()
			.append(Separator.SystemNewLine)
			.append("<");
		if(null == context) { contextBuffer.append("null"); }
		else { contextBuffer.append(context.toLowerCase()); }
		return contextBuffer.append(">")
			.append(Separator.SystemNewLine)
			.append(Separator.Tab);
	}

	/**
	 * Write a opening xml tag for the context without adding trailing
	 * newline and tab characters.
	 * 
	 * @param context
	 *            <code>java.lang.String</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	protected StringBuffer openContextInline(final String context) {
		final StringBuffer contextBuffer = new StringBuffer()
			.append(Separator.SystemNewLine)
			.append("<");
		if(null == context) { contextBuffer.append("null"); }
		else { contextBuffer.append(context.toLowerCase()); }
		return contextBuffer.append(">");
	}
}
