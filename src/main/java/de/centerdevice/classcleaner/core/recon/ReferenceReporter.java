package de.centerdevice.classcleaner.core.recon;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class ReferenceReporter {
	private final List<ReferenceFinderVisitor> visitors;

	public ReferenceReporter(List<ReferenceFinderVisitor> visitors) {
		this.visitors = visitors;
	}

	public ReferenceReport createReport(IFile resource, IProgressMonitor monitor) {
		List<CodeReference> references = getAllReferences(resource, monitor);

		return new ReferenceReport(ReferenceScope.Project, references);
	}

	public ReferenceReport createReport(IFile resource) {
		return createReport(resource, null);
	}

	protected List<CodeReference> getAllReferences(IFile resource, IProgressMonitor monitor) {
		List<CodeReference> references = new ArrayList<>();
		for (ReferenceFinderVisitor visitor : visitors) {
			references.addAll(visitor.visit(resource, monitor));
		}
		return references;
	}
}
