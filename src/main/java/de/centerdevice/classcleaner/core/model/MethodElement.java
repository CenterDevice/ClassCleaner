package de.centerdevice.classcleaner.core.model;

public class MethodElement extends CodeElement {

	private String methodName;
	private String signature;

	public MethodElement(String className, String methodName, String signature) {
		super(className);
		this.methodName = methodName;
		this.signature = signature;
	}

	@Override
	public String getName() {
		return super.getName() + "." + methodName + signature;
	}

}
