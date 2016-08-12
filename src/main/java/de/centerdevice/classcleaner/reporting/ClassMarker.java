package de.centerdevice.classcleaner.reporting;

import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import de.centerdevice.classcleaner.core.model.Issue;

public class ClassMarker {
	private static final String MARKER_TYPE = "ClassCleaner.xmlProblem";

	public void clean(IProject project) throws CoreException {
		project.deleteMarkers(MARKER_TYPE, true, IResource.DEPTH_INFINITE);
	}

	public void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
			ce.printStackTrace();
		}
	}

	public void addMarker(IFile file, Collection<Issue> issues) {
		for (Issue issue : issues) {
			addMarker(file, issue);
		}
	}

	public void addMarker(IFile file, Issue issue) {
		try {
			IMarker marker = file.createMarker(MARKER_TYPE);
			marker.setAttribute(IMarker.MESSAGE, issue.getMessage());
			marker.setAttribute(IMarker.SEVERITY, issue.getSeverity());
			marker.setAttribute(IMarker.LINE_NUMBER, getLineNumber(issue));
		} catch (CoreException e) {
		}
	}

	protected int getLineNumber(Issue issue) {
		return issue.getLineNumber() == -1 ? 1 : issue.getLineNumber();
	}
}
