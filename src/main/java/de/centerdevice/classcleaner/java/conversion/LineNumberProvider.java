package de.centerdevice.classcleaner.java.conversion;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class LineNumberProvider {

	private static final int BAD_LOCATION = -1;

	private final Document document;

	public LineNumberProvider(ICompilationUnit compilationUnit) {
		document = getDocument(compilationUnit);
	}

	protected Document getDocument(ICompilationUnit compilationUnit) {
		try {
			return new Document(compilationUnit.getSource());
		} catch (JavaModelException e) {
			return null;
		}
	}

	public int getNewLineNumber(ISourceRange offset) throws JavaModelException {
		if (document == null) {
			return BAD_LOCATION;
		}

		try {
			return document.getLineOfOffset(offset.getOffset()) + 1;
		} catch (BadLocationException e) {
			return BAD_LOCATION;
		}
	}
}
