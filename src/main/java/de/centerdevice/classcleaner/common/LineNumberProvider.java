package de.centerdevice.classcleaner.common;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class LineNumberProvider {

	public static final int BAD_LOCATION = -1;

	private final Document document;

	public LineNumberProvider(Document document) {
		this.document = document;
	}

	public int getLineNumber(int offset) {
		try {
			return document.getLineOfOffset(offset) + 1;
		} catch (BadLocationException e) {
			return BAD_LOCATION;
		}
	}
}
