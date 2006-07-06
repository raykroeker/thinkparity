import java.io.*;
import java.text.*;
import java.util.*;
import groovy.text.*;

class Environment {

	public final String author;
	public final String generatedOn;
	public final String model;
	public final String target;
	private final File java;
	private final File scripts;

	public Environment(final String[] args) {
		this.author = "CreateModel.groovy";
		final Calendar now = Calendar.getInstance();
		this.generatedOn = new SimpleDateFormat("MMM dd yyy hh:mm:ss a").format(now.getTime());
		final File user = new File(System.getProperty("user.dir"));
		this.target = args[0];
		this.model = args[1];
		this.java = new File(user, new StringBuffer("src")
				.append(File.separator).append("main")
				.append(File.separator).append("java").toString());
		this.scripts = new File(user, new StringBuffer("src")
				.append(File.separator).append("main")
				.append(File.separator).append("scripts").toString());
	}

	public File getJava() { return java; }

	public File getScripts() { return scripts; }

	public String toString() {
		return new StringBuffer(getClass().getName())
			.append("//").append("java:").append(java.getAbsolutePath())
			.append("/").append("scripts:").append(scripts.getAbsolutePath())
			.toString();
	}
}

abstract class ModelClass {

	public final String classModifier;
	public final String article;
	public final String className;
	public final String interfaceClassNames;
	public final String name;
	public final String packageName;
	public final String superClassName;
	private Template template;
	private final Environment environment;

	public ModelClass(final ModelGenerator modelGenerator, final String classModifier, final String className, final String superClassName, final String interfaceClassNames) {
		this.article = modelGenerator.getArticle();
		this.environment = modelGenerator.getEnvironment();
		this.classModifier = classModifier;
		this.className = className;
		this.interfaceClassNames = interfaceClassNames;
		this.name = modelGenerator.getName();
		this.packageName = "com.thinkparity.server.model." + modelGenerator.getName().toLowerCase();
		this.superClassName = superClassName;
	}

	private File loadGeneratedFile() {
		return new File(environment.getJava(), new StringBuffer()
				.append("com").append(File.separator)
				.append("thinkparity").append(File.separator)
				.append("server").append(File.separator)
				.append("model").append(File.separator)
				.append(name.toLowerCase()).append(File.separator)
				.append(className).append(".java")
				.toString());
	}

	public void destroy(){
		final File generatedFile = loadGeneratedFile();
		final File parentFile = generatedFile.getParentFile();
		generatedFile.delete();
		if(parentFile.listFiles().length < 1) { parentFile.delete(); }
	}

	public void loadTemplate() {
		final File templateFile = new File(environment.getScripts(), new StringBuffer()
				.append(getClass().getSimpleName())
				.append(".template")
				.toString());
		template = new SimpleTemplateEngine().createTemplate(templateFile);
	}

	public void write(final Map binding) {
		final File sourceFile = loadGeneratedFile();
		final File parentFile = sourceFile.getParentFile();
		if(!parentFile.exists()) { parentFile.mkdirs(); }
		template.make(binding).writeTo(new FileWriter(sourceFile));
	}

	public String getClassAccessModifier() { return classAccessModifier; }

	public String getClassName() { return className; }

	public String getPackageName() { return packageName; }

	public String toString() {
		return new StringBuffer("groovy://")
			.append(getClass().getName())
			.append("?className=").append(className)
			.append("&packageName=").append(packageName)
			.toString();
	}
}

class Model extends ModelClass {
    public Model(final ModelGenerator modelGenerator) {
        super(modelGenerator,
                "public ",
                modelGenerator.getName(),
                "",
                "");
    }
}
class ModelProxy extends ModelClass {
	public ModelProxy(final ModelGenerator modelGenerator) {
		super(modelGenerator,
		        "public ",
		        modelGenerator.getName() + "Model",
		        "",
		        "");
	}
}
class ModelProxyImpl extends ModelClass {
	public ModelProxyImpl(final ModelGenerator modelGenerator) {
		super(modelGenerator,
		        "",
		        modelGenerator.getName() + "ModelImpl",
		        "AbstractModelImpl",
		        "");
	}
}
class ModelProxyInternal extends ModelClass {
	public ModelProxyInternal(final ModelGenerator modelGenerator) {
		super(modelGenerator,
		        "public ",
		        "Internal" + modelGenerator.getName() + "Model",
		        modelGenerator.getName() + "Model",
		        "InternalModel");
	}
}

class ModelGenerator {

	private final Environment environment;
	private final String name;
	private final Model model;
	private final ModelProxy proxy;
	private final ModelProxyImpl proxyImpl;
	private final ModelProxyInternal proxyInternal;

	public ModelGenerator(final Environment environment) {
		this.environment = environment;
		this.name = environment.model;
		this.model = new Model(this);
		this.proxy = new ModelProxy(this);
		this.proxyImpl = new ModelProxyImpl(this);
		this.proxyInternal = new ModelProxyInternal(this);
	}

	private Map createBinding() {
		return ["environment": environment,
		        "model": model,
				"proxy": proxy,
				"proxyImpl": proxyImpl,
				"proxyInternal": proxyInternal];
	}

	public void destroy() {
		proxy.destroy();
		proxyImpl.destroy();
		proxyInternal.destroy();
	}

	public void generate() {
		final Map binding = createBinding();

		model.loadTemplate();
		model.write(binding);

		proxy.loadTemplate();
		proxy.write(binding);

		proxyImpl.loadTemplate();
		proxyImpl.write(binding);
	}

	public String getArticle() {
	    final char firstChar = getName().toLowerCase().charAt(0);
	    switch(firstChar) {
		    case 'a':
		        return "an";
		    case 'e':
		        return "an";
		    case 'i':
		        return "an";
		    case 'o':
		        return "an";
		    case 'u':
		        return "an";
        	default: return "a";
	    }
	}

	public Environment getEnvironment() { return environment; }

	public String getName() { return name; }

	public String toString() {
		return new StringBuffer("groovy://")
			.append(getClass().getName())
			.append("?name=").append(name)
			.append("&proxy=").append(proxy.toString())
			.append("&proxyInternal=").append(proxyInternal.toString())
			.append("&proxyImpl=").append(proxyImpl.toString())
			.toString();
	}
}

final Environment environment = new Environment(args);
final ModelGenerator modelGenerator = new ModelGenerator(environment);
if("generate".equals(environment.target)) {
    modelGenerator.generate();
}
else if("regenerate".equals(environment.target)) {
    modelGenerator.destroy();
    modelGenerator.generate();
}
else { println("CodeGenerator.groovy generate|regenerate model"); }

