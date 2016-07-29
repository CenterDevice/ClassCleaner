package de.centerdevice.classcleaner;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.model.CodeReference;

public interface ClassCleanerResourceVisitor {

	List<CodeReference> visit(IFile resource, IProgressMonitor monitor);

}