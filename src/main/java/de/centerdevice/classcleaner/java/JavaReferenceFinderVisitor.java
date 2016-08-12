package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.Document;

import de.centerdevice.classcleaner.common.LineNumberProvider;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.recon.ReferenceFinderVisitor;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

public class JavaReferenceFinderVisitor implements ReferenceFinderVisitor {

	@Override
	public List<CodeReference> visit(IFile resource, IProgressMonitor monitor) {
		if (resource.getName().endsWith(".java")) {
			IJavaElement javaElement = JavaCore.create(resource);
			if (javaElement instanceof ICompilationUnit) {
				return collectReferences((ICompilationUnit) javaElement, new JavaReferenceSearch(monitor));
			}
		}

		return new ArrayList<>();
	}

	protected List<CodeReference> collectReferences(ICompilationUnit compilationUnit, JavaReferenceSearch search) {
		return collectReferences(compilationUnit, search,
				new JavaElementConverter(getLineNumberProvider(compilationUnit)));
	}

	protected LineNumberProvider getLineNumberProvider(ICompilationUnit compilationUnit) {
		try {
			return new LineNumberProvider(new Document(compilationUnit.getSource()));
		} catch (JavaModelException e) {
			return null;
		}
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
