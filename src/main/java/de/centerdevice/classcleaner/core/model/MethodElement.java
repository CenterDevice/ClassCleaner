package de.centerdevice.classcleaner.core.model;

public class MethodElement extends CodeElement {

	private final String signature;

	public MethodElement(String className, String methodName, String signature, int line) {
		super(className, methodName, line);
		this.signature = signature;
	}

	@Override
	public String toSimpleString() {
		return super.toSimpleString() + signature;
	}

	@Override
	public String toString() {
		return super.toString() + signature;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((signature == null) ? 0 : signature.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MethodElement other = (MethodElement) obj;
		if (signature == null) {
			if (other.signature != null) {
				return false;
			}
		} else if (!signature.equals(other.signature)) {
			return false;
		}
		return true;
	}

}
