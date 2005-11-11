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
 * @version 1.1.2.2
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

	/**
	 * Create an Index.
	 */
	Index() {
		this(new Vector<UUID>(20), new Hashtable<String,String>(20, 0.75F));
	}

	/**
	 * Create a Index.
	 * 
	 * @param ids
	 *            A list of parity object ids.
	 * @param idToXmlFileLookup
	 *            A lookup of parity object ids to their xml files.
	 */
	private Index(final Collection<UUID> ids,
			final Map<String, String> idToXmlFileLookup) {
		super();
		this.ids = ids;
		this.idToXmlFileLookup = idToXmlFileLookup;
	}

	/**
	 * Add an id to the index.
	 * 
	 * @param id
	 *            The id to add.
	 * @param xmlFile
	 *            The xml file related to the id.
	 * @return An existing xml file if one exists.
	 */
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

	/**
	 * Obtain the list of parity object ids.
	 * 
	 * @return A list of parity object ids.
	 */
	public Collection<UUID> getIds() {
		return Collections.unmodifiableCollection(ids);
	}

	/**
	 * Lookup the xml file for the given parity object.
	 * 
	 * @param id
	 *            The id of the parity object.
	 * @return The xml file for the parity object.
	 */
	public File lookupXmlFile(final UUID id) {
		final String xmlFileAbsolutePath = idToXmlFileLookup.get(id.toString());
		if(null == xmlFileAbsolutePath) { return null; }
		else { return new File(xmlFileAbsolutePath); }
	}
}
