package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

class JavaReferenceCollector {

	private final JavaReferenceSearch searchEngine;
	private final JavaElementConverter converter;

	public JavaReferenceCollector(JavaReferenceSearch searchEngine, JavaElementConverter converter) {
		this.searchEngine = searchEngine;
		this.converter = converter;
	}

	public Map<ClassInfo, List<CodeReference>> findReferences(ICompilationUnit compilationUnit) throws CoreException {
		Map<ClassInfo, List<CodeReference>> references = new HashMap<>();
		for (IType type : compilationUnit.getTypes()) {
			references.put(converter.convert(type), findReferences(type));
		}
		return references;
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
}
