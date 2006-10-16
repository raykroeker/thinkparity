/*
 * Created On: Sep 27, 2006 12:42:41 PM
 */
package com.thinkparity.ophelia;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.Context;

import com.thinkparity.ophelia.model.TestModel;
import com.thinkparity.ophelia.model.archive.ArchiveModel;
import com.thinkparity.ophelia.model.archive.InternalArchiveModel;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;
import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.InternalContactModel;
import com.thinkparity.ophelia.model.container.ContainerModel;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.document.DocumentModel;
import com.thinkparity.ophelia.model.document.InternalDocumentModel;
import com.thinkparity.ophelia.model.message.InternalSystemMessageModel;
import com.thinkparity.ophelia.model.message.SystemMessageModel;
import com.thinkparity.ophelia.model.migrator.InternalLibraryModel;
import com.thinkparity.ophelia.model.migrator.InternalReleaseModel;
import com.thinkparity.ophelia.model.migrator.LibraryModel;
import com.thinkparity.ophelia.model.migrator.ReleaseModel;
import com.thinkparity.ophelia.model.profile.InternalProfileModel;
import com.thinkparity.ophelia.model.profile.ProfileModel;
import com.thinkparity.ophelia.model.script.InternalScriptModel;
import com.thinkparity.ophelia.model.script.ScriptModel;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.session.SessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.UserModel;
import com.thinkparity.ophelia.model.workspace.InternalWorkspaceModel;
import com.thinkparity.ophelia.model.workspace.WorkspaceModel;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OpheliaTestModelFactory {

    private static final OpheliaTestModelFactory SINGLETON;

    static {
        SINGLETON = new OpheliaTestModelFactory();
    }

    public static OpheliaTestModelFactory getInstance(
            final OpheliaTestCase testCase) {
        Assert.assertNotNull(testCase, "Cannot obtain model factory with a null test case.");
        return SINGLETON;
    }

    private final Context testContext;

    private OpheliaTestModelFactory() {
        super();
        this.testContext = TestModel.getTestContext();
    }

    public InternalArchiveModel getArchiveModel(final OpheliaTestUser testUser) {
        return ArchiveModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalArtifactModel getArtifactModel(final OpheliaTestUser testUser) {
        return ArtifactModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalContactModel getContactModel(final OpheliaTestUser testUser) {
        return ContactModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalContainerModel getContainerModel(final OpheliaTestUser testUser) {
        return ContainerModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalDocumentModel getDocumentModel(final OpheliaTestUser testUser) {
        return DocumentModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalLibraryModel getLibraryModel(final OpheliaTestUser testUser) {
        return LibraryModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalProfileModel getProfileModel(final OpheliaTestUser testUser) {
        return ProfileModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalReleaseModel getReleaseModel(final OpheliaTestUser testUser) {
        return ReleaseModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalScriptModel getScriptModel(final OpheliaTestUser testUser) {
        return ScriptModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalSessionModel getSessionModel(final OpheliaTestUser testUser) {
        return SessionModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalSystemMessageModel getSystemMessageModel(final OpheliaTestUser testUser) {
        return SystemMessageModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalUserModel getUserModel(final OpheliaTestUser testUser) {
        return UserModel.getInternalModel(testContext, testUser.getEnvironment(), testUser.getWorkspace());
    }

    public InternalWorkspaceModel getWorkspaceModel(final OpheliaTestUser testUser) {
        return WorkspaceModel.getInternalModel(testContext, testUser.getEnvironment());
    }
}
