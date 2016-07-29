package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;

class JavaReferenceCollector {

	private static final JavaElementConverter CONVERTER = new JavaElementConverter();

	private final JavaReferenceSearch searchEngine;

	private List<CodeReference> references = new ArrayList<>();

	public JavaReferenceCollector(JavaReferenceSearch searchEngine, IProgressMonitor monitor) {
		this.searchEngine = searchEngine;
	}

	public void collect(ICompilationUnit compilationUnit) throws CoreException {
		for (IType type : compilationUnit.getTypes()) {
			anaylzeType(type);
		}
	}

	public List<CodeReference> getReferences() {
		return references;
	}

	protected void anaylzeType(IType type) throws CoreException {
		for (IMethod method : type.getMethods()) {
			if (!method.isMainMethod()) {
				recordReferences(method, searchEngine.findReferences(method));
			}
		}
		for (IField field : type.getFields()) {
			recordReferences(field, searchEngine.findReferences(field));
		}
	}

	protected void recordReferences(IJavaElement method, List<IJavaElement> elements) {
		CodeElement targetElement = CONVERTER.convert(method);

		if (elements.isEmpty()) {
			references.add(new CodeReference(null, targetElement));
		} else {
			for (IJavaElement methodReference : elements) {
				references.add(new CodeReference(CONVERTER.convert(methodReference), targetElement));
			}
		}
	}
}
