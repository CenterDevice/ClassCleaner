package de.centerdevice.classcleaner.java;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.search.MethodReferenceMatch;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchRequestor;

public class JavaSearchRequestor extends SearchRequestor {

	@Override
	public void acceptSearchMatch(SearchMatch match) throws CoreException {
		if (match instanceof MethodReferenceMatch) {
			MethodReferenceMatch methodMatch = (MethodReferenceMatch) match;
			System.out.println(methodMatch);
		}
	}
}