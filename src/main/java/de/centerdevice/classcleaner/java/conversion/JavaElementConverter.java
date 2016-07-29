package de.centerdevice.classcleaner.java.conversion;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.FieldElement;
import de.centerdevice.classcleaner.core.model.MethodElement;

public class JavaElementConverter {

	private LineNumberProvider lineNumberProvider;

	public JavaElementConverter(LineNumberProvider lineNumberProvider) {
		this.lineNumberProvider = lineNumberProvider;
	}

	public CodeElement convert(IJavaElement element) {
		try {
			return createCodeElement(element);
		} catch (JavaModelException e) {
			return null;
		}
	}

	protected CodeElement createCodeElement(IJavaElement element) throws JavaModelException {
		if (element instanceof IMethod) {
			return createMethodElement((IMethod) element);
		} else if (element instanceof IField) {
			return createFieldElement((IField) element);
		}

		return null;
	}

	protected FieldElement createFieldElement(IField field) throws JavaModelException {
		return new FieldElement(getFullyQualifiedClassName(field), field.getElementName(), field.getTypeSignature(),
				lineNumberProvider.getNewLineNumber(field.getSourceRange()));
	}

	protected MethodElement createMethodElement(IMethod method) throws JavaModelException {
		return new MethodElement(getFullyQualifiedClassName(method), method.getElementName(), method.getSignature(),
				lineNumberProvider.getNewLineNumber(method.getSourceRange()));
	}

	protected String getFullyQualifiedClassName(IJavaElement element) {
		IJavaElement packageFragment = element.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
		IJavaElement type = element.getAncestor(IJavaElement.TYPE);
		return packageFragment.getElementName() + "." + type.getElementName();
	}
}
