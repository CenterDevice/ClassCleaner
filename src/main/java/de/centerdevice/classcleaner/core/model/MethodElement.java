package de.centerdevice.classcleaner.core.model;

public class MethodElement extends CodeElement {

	private String signature;

	public MethodElement(String className, String methodName, String signature, int line) {
		super(className, methodName, line);
		this.signature = signature;
	}

	@Override
	public String toString() {
		return super.toString() + signature;
	}

}
