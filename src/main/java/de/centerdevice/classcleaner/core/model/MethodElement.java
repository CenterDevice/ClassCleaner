package de.centerdevice.classcleaner.core.model;

import java.util.Collections;
import java.util.List;

public class MethodElement extends CodeElement {

	private final List<String> parameters;

	public MethodElement(String className, String methodName, List<String> parameters, int line) {
		super(className, methodName, line);
		this.parameters = Collections.unmodifiableList(parameters);
	}

	public List<String> getParameters() {
		return parameters;
	}

	public String getSignature() {
		return "(" + String.join(";", parameters) + ")";
	}

	@Override
	public String toSimpleString() {
		return super.toSimpleString() + getSignature();
	}

	@Override
	public String toString() {
		return super.toString() + getSignature();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parameters == null) ? 0 : parameters.hashCode());
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
		if (parameters == null) {
			if (other.parameters != null) {
				return false;
			}
		} else if (!parameters.equals(other.parameters)) {
			return false;
		}
		return true;
	}

}
