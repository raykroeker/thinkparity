/*
 * Feb 27, 2005
 */
package com.thinkparity.model.parity.model.document;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.api.ParityObject;
import com.thinkparity.model.parity.api.ParityObjectType;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.util.MD5Util;

/**
 * Document
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Document extends ParityObject {

	/**
	 * The document content.
	 */
	private byte[] content;

	/**
	 * The document content's checksum.
	 */
	private String contentChecksum;

	/**
	 * The directory this document resides in.
	 */
	private File directory;

	/**
	 * The versions of this document.
	 */
	private final Collection<DocumentVersion> versions;

	/**
	 * Create a Document.
	 * 
	 * @param parent
	 *            The parent project.
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param keyHolder
	 *            The document keyholder.
	 * @param description
	 *            The document description.
	 * @param directory
	 *            The document directory.
	 * @param id
	 *            The document id.
	 * @param content
	 *            The document content.
	 */
	public Document(final Project parent, final String name,
			final Calendar createdOn, final String createdBy,
			final String keyHolder, final String description,
			final File directory, final UUID id, final byte[] content) {
		super(parent, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		this.content = content;
		this.contentChecksum = MD5Util.md5Hex(content);
		this.versions = new Vector<DocumentVersion>(1);
	}

	/**
	 * Create a Document.
	 * 
	 * @param name
	 *            The document name.
	 * @param createdOn
	 *            The document creation date.
	 * @param createdBy
	 *            The document creator.
	 * @param keyHolder
	 *            The document key holder.
	 * @param description
	 *            The document description.
	 * @param directory
	 *            The document directory.
	 * @param id
	 *            The document id.
	 * @param content
	 *            The document content.
	 * @param contentChecksum
	 *            The document content's checksum.
	 */
	public Document(final String name,
			final Calendar createdOn, final String createdBy,
			final String keyHolder, final String description,
			final File directory, final UUID id, final byte[] content,
			final String contentChecksum) {
		super(null, name, description, createdOn, createdBy, keyHolder, id);
		this.directory = directory;
		this.content = content;
		this.contentChecksum = contentChecksum;
		this.versions = new Vector<DocumentVersion>(3);
	}

	/**
	 * Add a version to this document.
	 * 
	 * @param version
	 *            The version to add.
	 */
	public void add(final DocumentVersion version) {
		Assert.assertNotNull("add(DocumentVersion)", version);
		Assert.assertNotTrue(
				"add(DocumentVersion) - contains(DocumentVersion)",
				contains(version));
		versions.add(version);
	}

	/**
	 * Obtain the document content.
	 * 
	 * @return The document content.
	 */
	public byte[] getContent() { return content; }

	/**
	 * Obtain the document content's checksum.
	 * 
	 * @return The document content's checksum.
	 */
	public String getContentChecksum() { return contentChecksum; }

	/**
	 * Obtain the directory this document resides in.
	 * 
	 * @return The directory this document resides in.
	 * @see com.thinkparity.model.parity.api.ParityObject#getDirectory()
	 */
	public File getDirectory() { return directory; }

	/**
	 * Obtain the absolute path of the document on disk.
	 * 
	 * @return The document's absolute path.
	 */
	public String getDocumentAbsolutePath() {
		return new StringBuffer(directory.getAbsolutePath())
			.append(File.separator)
			.append(getName()).toString();
	}

	/**
	 * Obtain a copy of the document versions.
	 * 
	 * @return The list of document versions.
	 */
	public Collection<DocumentVersion> getVersions() {
		final Collection<DocumentVersion> copy =
			new Vector<DocumentVersion>(versions.size());
		copy.addAll(versions);
		return copy;
	}

	/**
	 * Obtain the document versions, with a set of exclusions.
	 * 
	 * @param exclusions
	 *            The document versions to exclude from the list.
	 * @return A list of document versions.
	 */
	public Collection<DocumentVersion> getVersionsExclude(
			final Collection<DocumentVersion> exclusions) {
		final Collection<DocumentVersion> copy = getVersions();
		if(null == exclusions || 0 == exclusions.size()) { return copy; }
		else {
			final Collection<DocumentVersion> modList =
				new Vector<DocumentVersion>(copy.size() - exclusions.size());
			Boolean doExclude = Boolean.FALSE;
			for(DocumentVersion copyVersion : copy) {
				for(DocumentVersion exclusion : exclusions) {
					if(copyVersion.getVersion().equals(exclusion.getVersion())) {
						doExclude = Boolean.TRUE;
						break;
					}
				}
				if(Boolean.FALSE == doExclude) { modList.add(copyVersion); } 
			}
			return modList;
		}
	}

	/**
	 * Obtain the path of the document.
	 * 
	 * @return The path of the document.
	 * @see com.thinkparity.model.parity.api.ParityObject#getPath()
	 */
	public StringBuffer getPath() {
		return getParent().getPath().append("/").append(getCustomName());
	}

	/**
	 * Obtain the type of parity object.
	 * 
	 * @return The type of parity object.
	 * @see ParityObject#getType()
	 * @see ParityObjectType#DOCUMENT
	 */
	public ParityObjectType getType() { return ParityObjectType.DOCUMENT; }

	/**
	 * Remove a version from this document.
	 * 
	 * @param version
	 *            The version to remove.
	 * @return Whether or not the list of versions was modified via this
	 *         operation.
	 */
	public Boolean remove(final DocumentVersion version) {
		Assert.assertNotNull("remove(DocumentVersion)", version);
		final Integer size = versions.size();
		final Collection<DocumentVersion> modList =
			new Vector<DocumentVersion>(size - 1);
		for(DocumentVersion dv : versions) {
			if(!dv.getVersion().equals(version.getVersion())) {
				modList.add(dv);
			}
		}
		versions.clear();
		versions.addAll(modList);
		return (size != versions.size());
	}

	/**
	 * Set the project for this document.
	 * 
	 * @param project
	 *            The new project.
	 */
	public void setProject(final Project project) { setParent(project); }

	/**
	 * Set the content of the document.
	 * 
	 * @param content
	 *            The content of the document.
	 */
	void setContent(byte[] content) {
		this.content = new byte[content.length];
		System.arraycopy(content, 0, this.content, 0, content.length);
	}

	/**
	 * Determine whether the list of versions contains this version. A simple
	 * equals check is made on the version id.
	 * 
	 * @param version
	 *            The version to look for.
	 * @return True if the list of versions contains the provided version.
	 */
	private Boolean contains(final DocumentVersion version) {
		for(DocumentVersion dv : versions) {
			if(dv.getVersion().equals(version.getVersion())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
