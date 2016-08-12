package de.centerdevice.classcleaner.core.model;

public class Issue {
	private final String message;
	private final int lineNumber;
	private final int severity;

	public Issue(String message, int lineNumber, int severity) {
		this.message = message;
		this.lineNumber = lineNumber;
		this.severity = severity;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getMessage() {
		return message;
	}

	public int getSeverity() {
		return severity;
	}

	@Override
	public String toString() {
		return getLineNumber() + ": " + getMessage() + " (" + getSeverity() + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + lineNumber;
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + severity;
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
		Issue other = (Issue) obj;
		if (lineNumber != other.lineNumber) {
			return false;
		}
		if (message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!message.equals(other.message)) {
			return false;
		}
		if (severity != other.severity) {
			return false;
		}
		return true;
	}
}
