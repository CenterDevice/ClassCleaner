package de.centerdevice.classcleaner.core.model;

public class CodeReference {

	private final CodeElement src;
	private final CodeElement dst;

	public CodeReference(CodeElement src, CodeElement dst) {
		this.src = src;
		this.dst = dst;
	}

	public CodeElement getDestination() {
		return dst;
	}

	public CodeElement getSource() {
		return src;
	}

	@Override
	public String toString() {
		return getClassNameWithFallback(src) + " -> " + getClassNameWithFallback(dst);
	}

	protected String getClassNameWithFallback(CodeElement element) {
		if (element == null) {
			return "none";
		}
		return element.toString();
	}
}
