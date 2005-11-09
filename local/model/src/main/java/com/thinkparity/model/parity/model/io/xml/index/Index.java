/*
 * Nov 8, 2005
 */
package com.thinkparity.model.parity.model.io.xml.index;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;


/**
 * The index object provides an index of parity objects in a single location.
 * The current index is only on the parity object's id.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Index {

	/**
	 * List of all of the unique ids.
	 */
	private final Collection<UUID> ids;

	/**
	 * Index entries.
	 */
	private final Map<String,String> idToXmlFileLookup;

	public Index() {
		this(new Vector<UUID>(20), new Hashtable<String,String>(20, 0.75F));
	}

	public Index(final Collection<UUID> ids,
			final Map<String, String> idToXmlFileLookup) {
		super();
		this.ids = ids;
		this.idToXmlFileLookup = idToXmlFileLookup;
	}

	public File addLookup(final UUID id, final File xmlFile) {
		if(!ids.contains(id)) {
			ids.add(id);
			idToXmlFileLookup.put(id.toString(), xmlFile.getAbsolutePath());
			return null;
		}
		else {
			return new File(
					idToXmlFileLookup.put(id.toString(),
							xmlFile.getAbsolutePath()));
		}
	}

	public Collection<UUID> getIds() {
		return Collections.unmodifiableCollection(ids);
	}

	public File lookupXmlFile(final UUID id) {
		final String xmlFileAbsolutePath = idToXmlFileLookup.get(id.toString());
		if(null == xmlFileAbsolutePath) { return null; }
		else { return new File(xmlFileAbsolutePath); }
	}
}
