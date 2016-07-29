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
	private final SearchEngine searchEngine;
	private IProgressMonitor monitor;

	public JavaReferenceSearch() {
		this(new SearchEngine(), null);
	}

	public JavaReferenceSearch(IProgressMonitor monitor) {
		this(new SearchEngine(), monitor);
	}

	public JavaReferenceSearch(SearchEngine searchEngine, IProgressMonitor monitor) {
		this.searchEngine = searchEngine;
		this.monitor = monitor;
	}

	public List<IJavaElement> findReferences(IJavaElement element) {
		SearchReferenceCollector methodReferenceCollector = new SearchReferenceCollector();

		try {
			searchEngine.search(getPattern(element),
					new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, getSearchScope(),
					methodReferenceCollector, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}

		return methodReferenceCollector.getReferences();
	}

	protected SearchPattern getPattern(IJavaElement element) {
		return SearchPattern.createPattern(element, IJavaSearchConstants.REFERENCES);
	}

	protected IJavaSearchScope getSearchScope() {
		return SearchEngine.createWorkspaceScope();
	}
}
