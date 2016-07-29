package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.java.search.JavaReferenceSearch;

class JavaReferenceCollector {

	private final JavaReferenceSearch searchEngine;
	private final JavaElementConverter converter;

	private List<CodeReference> references = new ArrayList<>();

	public JavaReferenceCollector(JavaReferenceSearch searchEngine, JavaElementConverter converter) {
		this.searchEngine = searchEngine;
		this.converter = converter;
	}

	public void collect(ICompilationUnit compilationUnit) throws CoreException {
		for (IType type : compilationUnit.getTypes()) {
			recordReferences(type);
		}
	}

	public List<CodeReference> getReferences() {
		return references;
	}

	protected void recordReferences(IType type) throws CoreException {
		for (IMethod method : type.getMethods()) {
			if (!method.isMainMethod()) {
				recordReferences(method);
			}
		}
		for (IField field : type.getFields()) {
			recordReferences(field);
		}
	}

	protected void recordReferences(IJavaElement element) {
		CodeElement targetElement = converter.convert(element);
		List<IJavaElement> referenceElements = searchEngine.findReferences(element);

		if (referenceElements.isEmpty()) {
			references.add(new CodeReference(null, targetElement));
		} else {
			for (IJavaElement reference : referenceElements) {
				references.add(new CodeReference(converter.convert(reference), targetElement));
			}
		}
	}
}
