package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

import de.centerdevice.classcleaner.common.LineNumberProvider;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.FieldElement;
import de.centerdevice.classcleaner.core.model.MethodElement;

class JavaElementConverter {

	private final LineNumberProvider lineNumberProvider;

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

	public ClassInfo convert(IType type) {
		try {
			return new ClassInfo(type.getFullyQualifiedName(), getLineNumber(type.getSourceRange()));
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
				getLineNumber(field));
	}

	protected MethodElement createMethodElement(IMethod method) throws JavaModelException {
		return new MethodElement(getFullyQualifiedClassName(method), method.getElementName(), getParameters(method),
				getLineNumber(method));
	}

	protected List<String> getParameters(IMethod method) throws JavaModelException {
		List<String> parameters = new ArrayList<>(method.getNumberOfParameters());
		for (String parameterType : method.getParameterTypes()) {
			parameters.add(getFullyQualifiedName(method.getDeclaringType(), parameterType));
		}
		return parameters;
	}

	protected String getFullyQualifiedName(IType type, String string) throws JavaModelException {
		String[][] allResults = type.resolveType(Signature.getSignatureSimpleName(string));
		if (allResults != null) {
			String[] nameParts = allResults[0];
			if (nameParts != null) {
				return String.join(".", nameParts);
			}
		}
		return null;
	}

	protected int getLineNumber(IMember element) throws JavaModelException {
		return getLineNumber(element.getSourceRange());
	}

	protected int getLineNumber(ISourceRange sourceRange) {
		if (lineNumberProvider == null) {
			return LineNumberProvider.BAD_LOCATION;
		}
		return lineNumberProvider.getLineNumber(sourceRange.getOffset());
	}

	protected String getFullyQualifiedClassName(IMember member) {
		return member.getDeclaringType().getFullyQualifiedName();
	}
}
