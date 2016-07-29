package de.centerdevice.classcleaner;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;

public interface ClassCleanerResourceVisitor {

	void visit(IResource resource, IProgressMonitor monitor);

}