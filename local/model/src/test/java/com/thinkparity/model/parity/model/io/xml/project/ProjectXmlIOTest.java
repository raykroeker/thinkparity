/*
 * 25-Oct-2005
 */
package com.thinkparity.model.parity.model.io.xml.project;

import java.io.File;
import java.util.Vector;

import com.thinkparity.model.ModelTestCase;
import com.thinkparity.model.parity.model.project.Project;
import com.thinkparity.model.parity.model.project.ProjectModel;

/**
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProjectXmlIOTest extends ModelTestCase {

	private class GetXmlFileData {
		private final Project project;
		private final ProjectXmlIO projectXmlIO;
		private GetXmlFileData(final Project project, final ProjectXmlIO projectXmlIO) {
			this.project = project;
			this.projectXmlIO = projectXmlIO;
		}
	}

	private Vector<GetXmlFileData> getXmlFileData;
	
	/**
	 * Create a ProjectXmlIOTest.
	 */
	public ProjectXmlIOTest() { super("Project xml io"); }

	public void testGetXmlFile() {
		try {
			File xmlFile;
			for(GetXmlFileData data : getXmlFileData) {
				xmlFile = data.projectXmlIO.getXmlFile(data.project);
				ProjectXmlIOTest.assertNotNull(xmlFile);
				ProjectXmlIOTest.assertTrue(xmlFile.exists());
				ProjectXmlIOTest.assertTrue(xmlFile.canRead());
				ProjectXmlIOTest.assertTrue(xmlFile.canWrite());
			}
		}
		catch(Throwable t) { fail(getFailMessage(t)); }
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		setUpGetXmlFile();
	}

	protected void setUpGetXmlFile() throws Exception {
		getXmlFileData = new Vector<GetXmlFileData>(8);
		final Project testProject = createTestProject("testGetXmlFile");
		final ProjectModel projectModel = getProjectModel();
		final ProjectXmlIO projectXmlIO = new ProjectXmlIO(getWorkspace());
		String name, description;
		Project project;

		name = "1";
		description = name;
		project = projectModel.create(testProject, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "1.1";
		description = name;
		project = projectModel.create(project, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "1.2";
		description = name;
		project = projectModel.create(project, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "1.3";
		description = name;
		project = projectModel.create(project, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "1.4";
		description = name;
		project = projectModel.create(project, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "2";
		description = name;
		project = projectModel.create(testProject, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "3";
		description = name;
		project = projectModel.create(testProject, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));

		name = "4";
		description = name;
		project = projectModel.create(testProject, name, description);
		getXmlFileData.add(new GetXmlFileData(project, projectXmlIO));
	}

	/**
	 * @see com.thinkparity.model.ModelTestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		tearDownGetXmlFile();
	}

	protected void tearDownGetXmlFile() throws Exception {
		getXmlFileData.clear();
		getXmlFileData = null;
	}
}
