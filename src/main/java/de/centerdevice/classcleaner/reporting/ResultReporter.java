package de.centerdevice.classcleaner.reporting;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;

public class ResultReporter {

	private ClassMarker marker;

	public ResultReporter(ClassMarker marker) {
		this.marker = marker;
	}

	public void report(IFile file, List<CodeReference> references) {
		for (CodeReference codeReferences : references) {
			if (codeReferences.getSource() == null) {
				reportUnusedElement(file, codeReferences.getDestination());
			}
		}
	}

	protected void reportUnusedElement(IFile file, CodeElement source) {
		if (source.getLineNumber() > 0) {
			marker.addMarker(file, source.getElementName() + " is never used", source.getLineNumber(),
					IMarker.SEVERITY_INFO);
		}
	}

	public void clean(IFile resource) {
		marker.deleteMarkers(resource);
	}
}
