/*
 * Created On: Oct 16, 2006 14:36 PM
 */

binding.getVariable("builder")
	.find("BioSpace Partnership Agreement")
	.createDraft()
	.modifyDocument("GreenTouch Partnership Agreement.doc",
	        "GreenTouch Partnership Agreement.v2.doc")
	.publish("I updated the partnership agreement - Mark")
