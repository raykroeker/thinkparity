/*
 * Nov 30, 2005
 */
package com.thinkparity.server.org.apache.log4j.or.com.thinkparity.server.model.artifact;

import java.util.Calendar;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.server.model.artifact.ArtifactSubscription;
import com.thinkparity.server.org.apache.log4j.or.IRendererConstants;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSubscriptionRenderer implements ObjectRenderer {

	private static final String ARTIFACT_ID = "artifactId:";

	private static final String ARTIFACT_SUBSCRIPTION_ID = ",artifactSubscriptionId:";

	private static final String CREATED_ON = ",createdOn:";

	private static final String PREFIX =
		ArtifactSubscription.class.getName() + IRendererConstants.NULL;

	private static final String UPDATED_ON = ",updatedOn:";

	private static final String USERNAME = ",username:";

	/**
	 * Create a ArtifactSubscriptionRenderer.
	 */
	public ArtifactSubscriptionRenderer() { super(); }

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
			final ArtifactSubscription as = (ArtifactSubscription) o;
			return new StringBuffer(PREFIX)
				.append(ARTIFACT_ID).append(as.getArtifactId())
				.append(ARTIFACT_SUBSCRIPTION_ID).append(as.getArtifactSubscriptionId())
				.append(CREATED_ON).append(doRender(as.getCreatedOn()))
				.append(UPDATED_ON).append(doRender(as.getUpdatedOn()))
				.append(USERNAME).append(as.getUsername())
				.append(IRendererConstants.SUFFIX).toString();
		}
	}

	private String doRender(final Calendar c) {
		if(null == c) { return IRendererConstants.NULL; }
		else { return IRendererConstants.SDF.format(c.getTime()); }
	}
}
