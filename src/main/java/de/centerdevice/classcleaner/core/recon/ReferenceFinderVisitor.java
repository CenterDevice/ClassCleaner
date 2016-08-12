package de.centerdevice.classcleaner.core.recon;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.model.CodeReference;

public interface ReferenceFinderVisitor {

	List<CodeReference> visit(IFile resource, IProgressMonitor monitor);

}