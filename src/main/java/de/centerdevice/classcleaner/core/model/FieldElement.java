package de.centerdevice.classcleaner.core.model;

public class FieldElement extends CodeElement {

	private String fieldName;
	private String signature;

	public FieldElement(String className, String fieldName, String signature) {
		super(className);
		this.fieldName = fieldName;
		this.signature = signature;
	}

	@Override
	public String getName() {
		return super.getName() + "." + fieldName + ";" + signature;
	}
}
