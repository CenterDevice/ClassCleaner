package de.centerdevice.classcleaner.core.recon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class ReferenceReporter {
	private final List<ReferenceFindingVisitor> visitors;

	public ReferenceReporter(List<ReferenceFindingVisitor> visitors) {
		this.visitors = visitors;
	}

	public ReferenceReport createReport(IFile resource, ReferenceScope scope, IProgressMonitor monitor) {
		return new ReferenceReport(scope, getAllReferences(resource, scope, monitor));
	}

	public ReferenceReport createReport(IFile resource, ReferenceScope scope) {
		return createReport(resource, scope, null);
	}

	protected Map<ClassInfo, List<CodeReference>> getAllReferences(IFile resource, ReferenceScope scope,
			IProgressMonitor monitor) {
		Map<ClassInfo, List<CodeReference>> references = new HashMap<>();
		for (ReferenceFindingVisitor visitor : visitors) {
			merge(references, visitor.visit(resource, scope, monitor));
		}
		return references;
	}

	private void merge(Map<ClassInfo, List<CodeReference>> allReferences,
			Map<ClassInfo, List<CodeReference>> newReferences) {
		for (Entry<ClassInfo, List<CodeReference>> entry : newReferences.entrySet()) {
			if (allReferences.containsKey(entry.getKey())) {
				allReferences.get(entry.getKey()).addAll(entry.getValue());
			} else {
				allReferences.put(entry.getKey(), entry.getValue());
			}
		}
	}
}
