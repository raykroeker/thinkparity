/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.artifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Filter<T> {
	public Boolean doInclude(T o1);
}
