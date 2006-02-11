/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Column {

	private String name;
	private short dataType;
	private String typeName;
	private int columnSize;
	private int decimalDigits;
	private int numberPrecisionRadix;
	private int nullable;
	private String comments;
	private String defaultValue;

	/**
	 * Create a Column.
	 */
	public Column() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the columnSize.
	 */
	public int getColumnSize() {
		return columnSize;
	}

	/**
	 * @param columnSize The columnSize to set.
	 */
	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return Returns the dataType.
	 */
	public short getDataType() {
		return dataType;
	}

	/**
	 * @param dataType The dataType to set.
	 */
	public void setDataType(short dataType) {
		this.dataType = dataType;
	}

	/**
	 * @return Returns the decimalDigits.
	 */
	public int getDecimalDigits() {
		return decimalDigits;
	}

	/**
	 * @param decimalDigits The decimalDigits to set.
	 */
	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	/**
	 * @return Returns the defaultValue.
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue The defaultValue to set.
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the nullable.
	 */
	public int getNullable() {
		return nullable;
	}

	/**
	 * @param nullable The nullable to set.
	 */
	public void setNullable(int nullable) {
		this.nullable = nullable;
	}

	/**
	 * @return Returns the numberPrecisionRadix.
	 */
	public int getNumberPrecisionRadix() {
		return numberPrecisionRadix;
	}

	/**
	 * @param numberPrecisionRadix The numberPrecisionRadix to set.
	 */
	public void setNumberPrecisionRadix(int numberPrecisionRadix) {
		this.numberPrecisionRadix = numberPrecisionRadix;
	}

	/**
	 * @return Returns the typeName.
	 */
	public String getTypeName() {
		return typeName;
	}

	/**
	 * @param typeName The typeName to set.
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

}
