/*
 * Nov 30, 2005
 */
package com.thinkparity.desdemona.util.logging.or;

import java.util.Calendar;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.codebase.model.artifact.Artifact;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactRenderer implements ObjectRenderer {

	private static final String ARTIFACT_ID = "artifactId:";

	private static final String ARTIFACT_UUID = ",artifactUUID:";

	private static final String CREATED_ON = ",createdOn:";

	private static final String PREFIX =
		Artifact.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	private static final String UPDATED_ON = ",updatedOn:";

	/**
	 * Create a ArtifactRenderer.
	 */
	public ArtifactRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX).toString();
		}
		else {
			final Artifact a = (Artifact) o;
			return new StringBuffer(PREFIX)
				.append(ARTIFACT_ID).append(a.getId())
				.append(ARTIFACT_UUID).append(a.getUniqueId())
				.append(CREATED_ON).append(doRender(a.getCreatedOn()))
				.append(UPDATED_ON).append(doRender(a.getUpdatedOn()))
				.append(IRendererConstants.SUFFIX).toString();
		}
	}

	private String doRender(final Calendar c) {
		if(null == c) { return IRendererConstants.NULL; }
		else { return IRendererConstants.SDF.format(c.getTime()); }
	}
}
