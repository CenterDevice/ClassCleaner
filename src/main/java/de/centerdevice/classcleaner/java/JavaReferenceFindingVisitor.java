package de.centerdevice.classcleaner.java;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceFindingVisitor;

public class JavaReferenceFindingVisitor implements ReferenceFindingVisitor {

	@Override
	public Map<ClassInfo, List<CodeReference>> visit(IFile resource, ReferenceScope scope, IProgressMonitor monitor) {
		if (resource.getName().endsWith(".java")) {
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement instanceof ICompilationUnit) {
				return findReferencesInScope(scope, (ICompilationUnit) javaElement, monitor);
			}
		}

		return new HashMap<>(0);
	}

	protected Map<ClassInfo, List<CodeReference>> findReferencesInScope(ReferenceScope scope,
			ICompilationUnit javaElement, IProgressMonitor monitor) {
		JavaReferenceFinder javaReferenceFinder = new JavaReferenceFinder(javaElement, scope, monitor);
		try {
			return javaReferenceFinder.findReferences(javaElement);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new HashMap<>(0);
	}
}
