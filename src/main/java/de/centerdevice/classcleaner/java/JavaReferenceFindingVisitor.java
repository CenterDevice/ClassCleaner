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
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jface.text.Document;

import de.centerdevice.classcleaner.common.LineNumberProvider;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceFindingVisitor;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

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
		return findReferences(javaElement, new JavaReferenceSearch(monitor, getSearchScope(scope, javaElement)));
	}

	protected IJavaSearchScope getSearchScope(ReferenceScope scope, ICompilationUnit compilationUnit) {
		if (scope == ReferenceScope.Class) {
			return SearchEngine.createJavaSearchScope(new IJavaElement[] { compilationUnit });
		}

		return SearchEngine.createWorkspaceScope();
	}

	protected Map<ClassInfo, List<CodeReference>> findReferences(ICompilationUnit compilationUnit,
			JavaReferenceSearch search) {
		return findReferences(compilationUnit, search,
				new JavaElementConverter(getLineNumberProvider(compilationUnit)));
	}

	protected LineNumberProvider getLineNumberProvider(ICompilationUnit compilationUnit) {
		try {
			return new LineNumberProvider(new Document(compilationUnit.getSource()));
		} catch (JavaModelException e) {
			return null;
		}
	}

	protected Map<ClassInfo, List<CodeReference>> findReferences(ICompilationUnit javaElement,
			JavaReferenceSearch searchEngine, JavaElementConverter elementConverter) {
		JavaReferenceCollector javaReferenceCollector = new JavaReferenceCollector(searchEngine, elementConverter);
		try {
			return javaReferenceCollector.findReferences(javaElement);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return new HashMap<>(0);
	}
}
