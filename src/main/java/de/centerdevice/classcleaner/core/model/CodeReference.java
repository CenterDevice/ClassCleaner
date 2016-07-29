package de.centerdevice.classcleaner.core.model;

public class CodeReference {

	private CodeElement src;
	private CodeElement dst;

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
		return src.getClassName() + " -> " + dst.getClassName();
	}
}
