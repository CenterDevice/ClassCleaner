package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

public class MethodReferenceCollector extends SearchRequestor {

	private final List<IJavaElement> references = new ArrayList<>();

	@Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		if (match instanceof MethodReferenceMatch) {
			references.add(getJavaElement((MethodReferenceMatch) match));
		}
	}

	public List<IJavaElement> getReferences() {
		return references;
	}

	protected IJavaElement getJavaElement(MethodReferenceMatch match) {
		return (IJavaElement) match.getElement();
	}
}