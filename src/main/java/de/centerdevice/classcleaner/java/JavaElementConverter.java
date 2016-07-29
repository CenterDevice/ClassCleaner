package de.centerdevice.classcleaner.java;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.FieldElement;
import de.centerdevice.classcleaner.core.model.MethodElement;

class JavaElementConverter {

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
			return createFieldElement(element, (IField) element);
		}

		return null;
	}

	protected FieldElement createFieldElement(IJavaElement element, IField field) throws JavaModelException {
		return new FieldElement(getFullyQualifiedClassName(element), element.getElementName(),
				field.getTypeSignature());
	}

	protected MethodElement createMethodElement(IMethod method) throws JavaModelException {
		return new MethodElement(getFullyQualifiedClassName(method), method.getElementName(), method.getSignature());
	}

	protected String getFullyQualifiedClassName(IJavaElement element) {
		IJavaElement packageFragment = element.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
		IJavaElement type = element.getAncestor(IJavaElement.TYPE);
		String fullyQualifiedClassname = packageFragment.getElementName() + "." + type.getElementName();
		return fullyQualifiedClassname;
	}
}
