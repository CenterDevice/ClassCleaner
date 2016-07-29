package de.centerdevice.classcleaner.core.model;

public class CodeElement {
	private String className;
	private String elementName;
	private int lineNumber;

	public CodeElement(String className, String elementName, int lineNumber) {
		this.className = className;
		this.elementName = elementName;
		this.lineNumber = lineNumber;
	}

	public String getClassName() {
		return className;
	}

	public String getElementName() {
		return elementName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	@Override
	public String toString() {
		return getClassName() + "." + getElementName();
	}
}
