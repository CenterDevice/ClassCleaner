package de.centerdevice.classcleaner.java.search;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;

public class JavaReferenceSearch {
	private static final SearchParticipant[] SEARCH_PARTICIPANTS = new SearchParticipant[] {
			SearchEngine.getDefaultSearchParticipant() };

	private final SearchEngine searchEngine;
	private final IJavaSearchScope scope;
	private final IProgressMonitor monitor;

	public JavaReferenceSearch(IProgressMonitor monitor, IJavaSearchScope scope) {
		this(new SearchEngine(), scope, monitor);
	}

	public JavaReferenceSearch(SearchEngine searchEngine, IJavaSearchScope scope, IProgressMonitor monitor) {
		this.searchEngine = searchEngine;
		this.scope = scope;
		this.monitor = monitor;
	}

	public List<IJavaElement> findReferences(IJavaElement element) {
		SearchReferenceCollector methodReferenceCollector = new SearchReferenceCollector();

		try {
			searchEngine.search(getPattern(element), SEARCH_PARTICIPANTS, scope, methodReferenceCollector, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return methodReferenceCollector.getReferences();
	}

	protected SearchPattern getPattern(IJavaElement element) {
		return SearchPattern.createPattern(element, IJavaSearchConstants.REFERENCES);
	}
}
