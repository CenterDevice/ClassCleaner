package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jface.text.Document;

import de.centerdevice.classcleaner.common.LineNumberProvider;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

class JavaReferenceFinder {

	private final JavaReferenceSearch searchEngine;
	private final JavaElementConverter converter;

	public JavaReferenceFinder(ICompilationUnit element, ReferenceScope scope, IProgressMonitor monitor) {
		this.searchEngine = new JavaReferenceSearch(monitor, getSearchScope(scope, element));
		this.converter = new JavaElementConverter(getLineNumberProvider(element));
	}

	public Map<ClassInfo, List<CodeReference>> findReferences(ICompilationUnit compilationUnit) throws CoreException {
		Map<ClassInfo, List<CodeReference>> references = new HashMap<>();
		for (IType type : compilationUnit.getTypes()) {
			findReferences(type, references);
		}
		return references;
	}

	protected void findReferences(IType type, Map<ClassInfo, List<CodeReference>> references) throws CoreException {
		references.put(converter.convert(type), findReferences(type));

		for (IType subType : type.getTypes()) {
			findReferences(subType, references);
		}
	}

	protected List<CodeReference> findReferences(IType type) throws CoreException {
		List<CodeReference> references = new ArrayList<>();

		for (IMethod method : type.getMethods()) {
			if (!method.isMainMethod()) {
				references.addAll(findReferences(method));
			}
		}
		for (IField field : type.getFields()) {
			references.addAll(findReferences(field));
		}

		return references;
	}

	protected List<CodeReference> findReferences(IJavaElement element) {
		CodeElement targetElement = converter.convert(element);
		List<IJavaElement> referenceElements = searchEngine.findReferences(element);
		List<CodeReference> references = new ArrayList<>();

		if (referenceElements.isEmpty()) {
			references.add(new CodeReference(null, targetElement));
		} else {
			for (IJavaElement reference : referenceElements) {
				references.add(new CodeReference(converter.convert(reference), targetElement));
			}
		}

		return references;
	}

	protected IJavaSearchScope getSearchScope(ReferenceScope scope, ICompilationUnit compilationUnit) {
		if (scope == ReferenceScope.Class) {
			return SearchEngine.createJavaSearchScope(new IJavaElement[] { compilationUnit });
		}

		return SearchEngine.createWorkspaceScope();
	}

	protected LineNumberProvider getLineNumberProvider(ICompilationUnit compilationUnit) {
		try {
			return new LineNumberProvider(new Document(compilationUnit.getSource()));
		} catch (JavaModelException e) {
			return null;
		}
	}
}
