/*
 * Mar 29, 2005
 */
package com.thinkparity.model.parity.util;

/**
 * Point2D
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Point2D {

	private int x;
	private int y;

	/**
	 * 
	 */
	public Point2D(final int x, final int y) {
		super();
		this.x = x;
		this.y = y;
	}

	/**
	 * Obtain the value of x.
	 * @return <code>int</code>
	 */
	public int getX() {
		return x;
	}

	/**
	 * Obtain the value of y.
	 * @return <code>int</code>
	 */
	public int getY() {
		return y;
	}

	/**
	 * Set a value for x.
	 * @param x <code>int</code>
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set a value for y.
	 * @param y <code>int</code>
	 */
	public void setY(int y) {
		this.y = y;
	}
}
