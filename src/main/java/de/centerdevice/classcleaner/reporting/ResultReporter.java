package de.centerdevice.classcleaner.reporting;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.model.CodeReference;

public class ResultReporter {

	private ClassMarker marker;

	public ResultReporter(ClassMarker marker) {
		this.marker = marker;
	}

	public void report(IFile file, List<CodeReference> references) {
		for (CodeReference codeReferences : references) {
			if (codeReferences.getSource() == null) {
				marker.addMarker(file, "Thingy is never used", 1, IMarker.SEVERITY_INFO);
			}
		}
	}

	public void clean(IFile resource) {
		marker.deleteMarkers(resource);
	}
}
