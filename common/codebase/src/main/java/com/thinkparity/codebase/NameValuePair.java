/*
 * Oct 4, 2003
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>  NameValuePair
 * <br><b>Description:</b>  
 * @author raykroeker@gmail.com
 * @version 1.0.0
 */
public class NameValuePair extends Duality {

	/**
	 * Create a new NameValuePair
	 * @param name <code>java.lang.String</code>
	 * @param value <code>java.lang.String</code>
	 */
	public NameValuePair(String name, String value) {super(name, value);}
	
	/**
	 * Obtain the name
	 * @return <code>java.lang.String</code>
	 */
	public String getName() {return (String) super.getFirst();}
	
	/**
	 * Obtain the value
	 * @return <code>java.lang.String</code>
	 */
	public String getValue() {return (String) super.getSecond();}
	
	/**
	 * Set the name
	 * @param string <code>java.lang.String</code>
	 */
	public void setName(String string) {super.setFirst(string);}
	
	/**
	 * Set the value
	 * @param string <code>java.lang.String</code>
	 */
	public void setValue(String string) {super.setSecond(string);}
	
	/**
	 * @see com.thinkparity.codebase.Duality#getFirstLabel()
	 */
	protected String getFirstLabel() {return "name";}

	/**
	 * @see com.thinkparity.codebase.Duality#getSecondLabel()
	 */
	protected String getSecondLabel() {return "value";}

}
