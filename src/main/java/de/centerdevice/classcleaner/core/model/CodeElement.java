package de.centerdevice.classcleaner.core.model;

public class CodeElement {
	private String className;

	public CodeElement(String className) {
		this.className = className;
	}

	public String getName() {
		return this.className;
	}
}
