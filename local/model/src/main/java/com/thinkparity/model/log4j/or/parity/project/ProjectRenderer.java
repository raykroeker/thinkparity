/*
 * 19-Oct-2005
 */
package com.thinkparity.model.log4j.or.parity.project;

import org.apache.log4j.or.ObjectRenderer;

import com.thinkparity.model.log4j.or.IRendererConstants;
import com.thinkparity.model.parity.model.project.Project;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public class ProjectRenderer implements ObjectRenderer {

	private static final String NAME = ",name:";
	private static final String PREFIX =
		Project.class.getName() + IRendererConstants.PREFIX_SUFFIX;

	/**
	 * Create a ProjectRenderer.
	 */
	public ProjectRenderer() { super(); }

	/**
	 * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
	 */
	public String doRender(Object o) {
		if(null == o) {
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.NULL)
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
		else {
			final Project project = (Project) o;
			return new StringBuffer(PREFIX)
				.append(IRendererConstants.ID).append(project.getId())
				.append(NAME).append(project.getName())
				.append(IRendererConstants.SUFFIX)
				.toString();
		}
	}

}
