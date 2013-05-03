/*
 * Nov 30, 2005
 */
package com.thinkparity.codebase.model.util.logging.or;

import com.thinkparity.common.StringUtil;
import com.thinkparity.common.StringUtil.Separator;

import com.thinkparity.codebase.model.artifact.Artifact;

import org.apache.log4j.or.ObjectRenderer;

/**
 * <b>Title:</b>thinkParity CommonModel Logging Artifact Renderer<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArtifactRenderer implements ObjectRenderer {

    /**
     * Create ArtifactRenderer.
     *
     */
	public ArtifactRenderer() {
        super();
	}

	/**
     * @see org.apache.log4j.or.ObjectRenderer#doRender(java.lang.Object)
     *
	 */
	public String doRender(final Object o) {
	    if (null == o) {
	        return Separator.Null.toString();
        } else {
            final Artifact o2 = (Artifact) o;
            return StringUtil.toString(o2.getClass(),
                    "getCreatedBy()", o2.getCreatedBy(),
                    "getCreatedOn()", o2.getCreatedOn(),
                    "getFlags()", o2.getFlags(),
                    "getId()", o2.getId(),
                    "getName()", o2.getName(),
                    "getType()", o2.getType(),
                    "getUniqueId()", o2.getUniqueId(),
                    "getUpdatedBy()", o2.getUpdatedBy(),
                    "getUpdatedOn()", o2.getUpdatedOn(),
                    "hashCode()", o2.hashCode());
        }
	}
}
