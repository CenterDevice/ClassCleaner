package de.centerdevice.classcleaner.core.recon;

import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public interface ReferenceFindingVisitor {

	Map<ClassInfo, List<CodeReference>> visit(IFile resource, ReferenceScope scope, IProgressMonitor monitor);

}