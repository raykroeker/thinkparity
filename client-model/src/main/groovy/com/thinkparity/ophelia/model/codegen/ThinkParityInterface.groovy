package com.thinkparity.ophelia.model.codegen


def srcJava = new File("local/model/src/main/java")
srcJava.eachFileRecurse { f ->
	if (f.name.endsWith(".java"))
	    println f.name
}
