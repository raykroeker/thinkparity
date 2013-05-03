/*
 * Created On: Oct 15, 2006 3:14:24 PM
 */

def builder = binding.getVariable("builder")

builder.create("Package 1")
	.create("Package 2")
	.find("Package 1")
	.addDocument("JUnitTestFramework.doc")

print builder
