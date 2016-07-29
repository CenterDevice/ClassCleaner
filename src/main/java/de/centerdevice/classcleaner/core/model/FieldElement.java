package de.centerdevice.classcleaner.core.model;

public class FieldElement extends CodeElement {

	private String signature;

	public FieldElement(String className, String fieldName, String signature, int line) {
		super(className, fieldName, line);
		this.signature = signature;
	}

	@Override
	public String toString() {
		return super.toString() + ";" + signature;
	}
}
