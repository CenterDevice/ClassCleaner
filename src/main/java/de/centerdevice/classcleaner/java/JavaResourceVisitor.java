package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;

import de.centerdevice.classcleaner.ClassCleanerResourceVisitor;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.java.conversion.JavaElementConverter;
import de.centerdevice.classcleaner.java.conversion.LineNumberProvider;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

public class JavaResourceVisitor implements ClassCleanerResourceVisitor {

	@Override
	public List<CodeReference> visit(IFile resource, IProgressMonitor monitor) {
		if (resource.getName().endsWith(".java")) {
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement instanceof ICompilationUnit) {
				return collectReferences((ICompilationUnit) javaElement, monitor);
			}
		}

		return new ArrayList<>();
	}

	protected List<CodeReference> collectReferences(ICompilationUnit javaElement, IProgressMonitor monitor) {
		JavaElementConverter converter = new JavaElementConverter(new LineNumberProvider(javaElement));
		return collectReferences(javaElement, new JavaReferenceSearch(monitor), converter);
	}

	protected List<CodeReference> collectReferences(ICompilationUnit javaElement, JavaReferenceSearch searchEngine,
			JavaElementConverter elementConverter) {
		JavaReferenceCollector javaReferenceCollector = new JavaReferenceCollector(searchEngine, elementConverter);
		try {
			javaReferenceCollector.collect(javaElement);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return javaReferenceCollector.getReferences();
	}
}
