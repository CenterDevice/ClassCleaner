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
}
