package de.centerdevice.classcleaner.core.model;

public class CodeElement {
	private final String className;
	private final String elementName;
	private final int lineNumber;

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

	public String toSimpleString() {
		return getElementName();
	}

	@Override
	public String toString() {
		return getClassName() + "." + getElementName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((elementName == null) ? 0 : elementName.hashCode());
		result = prime * result + lineNumber;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		CodeElement other = (CodeElement) obj;
		if (className == null) {
			if (other.className != null) {
				return false;
			}
		} else if (!className.equals(other.className)) {
			return false;
		}
		if (elementName == null) {
			if (other.elementName != null) {
				return false;
			}
		} else if (!elementName.equals(other.elementName)) {
			return false;
		}
		if (lineNumber != other.lineNumber) {
			return false;
		}
		return true;
	}

}
