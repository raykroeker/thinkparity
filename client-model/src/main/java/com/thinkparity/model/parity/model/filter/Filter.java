/*
 * Mar 22, 2006
 */
package com.thinkparity.model.parity.model.filter;

import java.util.LinkedList;
import java.util.List;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class Filter {

	private final List<InternalFilter> filters;

	/**
	 * Create a Filter.
	 */
	public Filter() {
		super();
		this.filters = new LinkedList<InternalFilter>();
	}

	protected void addFilter(final String fieldName, final Object fieldValue,
			final Operation operation) {
		filters.add(new InternalFilter(fieldName, fieldValue, operation));
	}

	public enum Operation { AND, OR }

	private class InternalFilter {
		private final String fieldName;
		private final Object fieldValue;
		private final Operation operation;

		private InternalFilter(final String fieldName, final Object fieldValue,
				final Operation operation) {
			this.fieldName = fieldName;
			this.fieldValue = fieldValue;
			this.operation = operation;
		}
	}
}
