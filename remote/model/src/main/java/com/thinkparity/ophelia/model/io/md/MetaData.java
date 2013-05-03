/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.io.md;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MetaData {

	private Long Id;
	private String key;
	private MetaDataType type;
	private Object value;

	public MetaData() { super(); }

	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return Id;
	}

	/**
	 * @return Returns the key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @return Returns the type.
	 */
	public MetaDataType getType() {
		return type;
	}

	/**
	 * @return Returns the value.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		Id = id;
	}

	/**
	 * @param key The key to set.
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param type The type to set.
	 */
	public void setType(MetaDataType type) {
		this.type = type;
	}

	/**
	 * @param value The value to set.
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	
}
