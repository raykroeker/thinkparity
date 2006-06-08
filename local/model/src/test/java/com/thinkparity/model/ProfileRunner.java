/*
 * Feb 10, 2006
 */
package com.thinkparity.model;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProfileRunner {

	/**
	 * Create a simple test suite of all tests; and use the text ui runner to
	 * execute them.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		TestSuite suite= new TestSuite();
		suite.addTest(new com.thinkparity.model.parity.model.document.CreateTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.CreateVersionTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.FlagTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.GetContentTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.GetTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.GetVersionContentTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.GetVersionTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.ListTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.ListVersionsTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.LockTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.OpenTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.OpenVersionTest());
		suite.addTest(new com.thinkparity.model.parity.model.document.UnlockTest());
		suite.addTest(new com.thinkparity.model.parity.model.io.db.hsqldb.util.HypersonicValidatorTest());
		suite.addTest(new com.thinkparity.model.parity.model.session.ReadContactsTest());
		suite.addTest(new com.thinkparity.model.parity.model.session.LoginTest());
		suite.addTest(new com.thinkparity.model.parity.model.session.SendTest());
		suite.addTest(new com.thinkparity.model.parity.model.workspace.GetWorkspaceTest());
		TestRunner.run(suite);
	}

	/**
	 * Create a ProfileRunner.
	 * 
	 */
	public ProfileRunner() { super(); }
}
