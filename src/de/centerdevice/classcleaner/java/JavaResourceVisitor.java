package de.centerdevice.classcleaner.java;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;

import de.centerdevice.classcleaner.ClassCleanerResourceVisitor;
import de.centerdevice.classcleaner.ClassMarker;

public class JavaResourceVisitor implements ClassCleanerResourceVisitor {

	private SearchEngine searchEngine;
	private ClassMarker marker;

	public JavaResourceVisitor(ClassMarker marker) {
		this.marker = marker;
		this.searchEngine = new SearchEngine();
	}

	@Override
	public void visit(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile && resource.getName().endsWith(".java")) {
			marker.deleteMarkers((IFile) resource);
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement instanceof ICompilationUnit) {
				anlyzeCompilationUnit((ICompilationUnit) javaElement, monitor);
			}
		}
	}

	protected void anlyzeCompilationUnit(ICompilationUnit compilationUnit, IProgressMonitor monitor) {
		try {
			for (IType type : compilationUnit.getTypes()) {
				anaylzeType(monitor, type);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	protected void anaylzeType(IProgressMonitor monitor, IType type) throws JavaModelException, CoreException {
		for (IMethod method : type.getMethods()) {
			if (!method.isMainMethod()) {
				findReferences(method, monitor);
			}
		}
		for (IField method : type.getFields()) {
			findReferences(method, monitor);
		}
	}

	protected void findReferences(IJavaElement method, IProgressMonitor monitor) throws CoreException {
		MethodReferenceCollector methodReferenceCollector = new MethodReferenceCollector();
		searchEngine.search(getPattern(method), new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				getSearchScope(), methodReferenceCollector, monitor);

		if (methodReferenceCollector.getReferences().isEmpty()) {
			System.out.println(method + " has no references");
		} else {
			System.out.println(methodReferenceCollector.getReferences());
		}
	}

	protected SearchPattern getPattern(IJavaElement method) {
		SearchPattern pattern = SearchPattern.createPattern(method, IJavaSearchConstants.REFERENCES);
		return pattern;
	}

	protected IJavaSearchScope getSearchScope() {
		return SearchEngine.createWorkspaceScope();
	}
}
