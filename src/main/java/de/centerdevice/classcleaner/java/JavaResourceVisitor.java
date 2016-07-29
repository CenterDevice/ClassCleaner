package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import de.centerdevice.classcleaner.ClassCleanerResourceVisitor;
import de.centerdevice.classcleaner.core.model.CodeReference;

public class JavaResourceVisitor implements ClassCleanerResourceVisitor {

	@Override
	public List<CodeReference> visit(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile && resource.getName().endsWith(".java")) {
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement instanceof ICompilationUnit) {
				return collectReferences(javaElement, monitor);
			}
		}

		return new ArrayList<>();
	}

	protected List<CodeReference> collectReferences(IJavaElement javaElement, IProgressMonitor monitor) {
		JavaReferenceCollector javaReferenceCollector = new JavaReferenceCollector(new JavaReferenceSearch(monitor),
				monitor);
		try {
			javaReferenceCollector.collect((ICompilationUnit) javaElement);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return javaReferenceCollector.getReferences();
	}
}
