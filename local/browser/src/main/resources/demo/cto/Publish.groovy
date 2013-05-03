/*
 * Created On: Oct 16, 2006 14:36 PM
 */

binding.getVariable("builder").find("Package")
	.createDraft()
	.addDocument("Corporate Structure.txt")
	.addDocument("Feasibility Analysis.txt")
	.publish("I added some documents.")
