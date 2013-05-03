/*
 * Created On: Sep 24, 2006 4:08:23 PM
 */
package com.thinkparity.ophelia.browser.plugin.archive.action;

import java.io.File;
import java.util.UUID;

import com.thinkparity.codebase.model.document.DocumentVersion;

import com.thinkparity.ophelia.model.archive.monitor.OpenMonitor;
import com.thinkparity.ophelia.model.archive.monitor.OpenStage;
import com.thinkparity.ophelia.model.util.Opener;

import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingMonitor;
import com.thinkparity.ophelia.browser.platform.action.ThinkParitySwingWorker;
import com.thinkparity.ophelia.browser.platform.plugin.PluginServices;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtensionAction;
import com.thinkparity.ophelia.browser.util.jdic.DesktopUtil;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OpenDocumentVersion extends ActionExtensionAction {

    /**
     * Create OpenDocumentVersion.
     * 
     */
    public OpenDocumentVersion(final PluginServices services,
            final ActionExtension extension) {
        super(services, extension);
    }

    /**
     * @see com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtensionAction#invoke(java.lang.Object)
     */
    @Override
    protected void invoke(final Object selection) {
        final DocumentVersion documentVersion = (DocumentVersion) selection;
        if(confirm("ConfirmOpen", documentVersion.getName())) {
            final ThinkParitySwingMonitor monitor = new ThinkParitySwingMonitor() {
                public void monitor() {}
                public void setStep(final int step, final String note) {}
                public void setStep(final int step) {}
                public void setSteps(final int steps) {}
            };
            final OpenDocumentVersionWorker worker = new OpenDocumentVersionWorker(
                    this, documentVersion.getArtifactUniqueId(),
                    documentVersion.getVersionId(), documentVersion.getName(),
                    documentVersion.getSize());
            worker.setMonitor(monitor);
            worker.start();
        }
    }

    /** An open document version action worker object. */
    private static class OpenDocumentVersionWorker extends ThinkParitySwingWorker {
        private final OpenDocumentVersion openDocumentVersion;
        private final OpenMonitor openMonitor;
        private final UUID uniqueId;
        private final Long versionId;
        private final String versionName;
        private final Long versionSize;
        private OpenDocumentVersionWorker(
                final OpenDocumentVersion openDocumentVersion,
                final UUID uniqueId, final Long versionId,
                final String versionName, final Long versionSize) {
            super(openDocumentVersion);
            this.openDocumentVersion = openDocumentVersion;
            this.uniqueId = uniqueId;
            this.versionId = versionId;
            this.versionName = versionName;
            this.versionSize = versionSize;
            this.openMonitor = new OpenMonitor() {
                private Integer stageIndex;
                private Integer stages;
                public void determine(final Integer stages) {
                    this.stageIndex = 0;
                    this.stages = stages;
                    monitor.setSteps(stages);
                    monitor.setStep(stageIndex);
                }
                public void processBegin() {
                    monitor.monitor();
                }
                public void processEnd() {}
                public void stageBegin(final OpenStage stage,
                        final Object data) {
                    if (null != stages && stages.intValue() > 0) {
                        if (OpenStage.DownloadStream == stage)
                            monitor.setStep(stageIndex, (String) data);
                        else
                            monitor.setStep(stageIndex);
                    }
                }
                public void stageEnd(final OpenStage stage) {
                    if (null != stages && stages.intValue() > 0) {
                        stageIndex++;
                        monitor.setStep(stageIndex);
                    }
                }
            };
        }
        @Override
        public Object construct() {
            openDocumentVersion.getArchiveModel().openDocumentVersion(
                    openMonitor, uniqueId, versionId, versionName, versionSize,
                    new Opener() {
                        public void open(final File file) {
                            try {
                                DesktopUtil.open(file);
                            } catch (final Exception x) {
                                throw openDocumentVersion.translateError(x);
                            }
                        }
                    });
            return Void.TYPE;
        }
    }
}
