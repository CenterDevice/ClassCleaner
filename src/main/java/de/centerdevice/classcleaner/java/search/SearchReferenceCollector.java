package de.centerdevice.classcleaner.java.search;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.ReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

class SearchReferenceCollector extends SearchRequestor {

	private final List<IJavaElement> references = new ArrayList<>();

	@Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		if (match instanceof ReferenceMatch) {
			references.add(getJavaElement((ReferenceMatch) match));
		}

	}

	public List<IJavaElement> getReferences() {
		return references;
	}

	protected IJavaElement getJavaElement(ReferenceMatch match) {
		return (IJavaElement) match.getElement();
	}
}