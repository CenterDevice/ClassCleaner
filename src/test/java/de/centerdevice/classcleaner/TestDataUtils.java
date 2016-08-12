package de.centerdevice.classcleaner;

import static java.util.Arrays.asList;

import java.util.List;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.MethodElement;

public class TestDataUtils {
	public static final int DUMMY_LINE = -1;
	public static final List<String> PARAMETERS = asList(String.class.getName());
	public static final String CLASS_NAME = "class";

	public static CodeReference getReference(String fromMethod, String toMethod) {
		return getReference(fromMethod, DUMMY_LINE, toMethod, DUMMY_LINE);
	}

	public static CodeReference getReference(String fromMethod, int fromLine, String toMethod, int toLine) {
		return getReference(CLASS_NAME, fromMethod, fromLine, CLASS_NAME, toMethod, toLine);
	}

	public static CodeReference getReference(String fromClass, String fromMethod, String toClass, String toMethod) {
		return getReference(fromClass, fromMethod, DUMMY_LINE, toClass, toMethod, DUMMY_LINE);
	}

	public static CodeReference getReference(String fromClass, String fromMethod, int fromLine, String toClass,
			String toMethod, int toLine) {
		MethodElement from = (fromMethod != null) ? new MethodElement(fromClass, fromMethod, PARAMETERS, fromLine)
				: null;
		MethodElement to = (toMethod != null) ? new MethodElement(toClass, toMethod, PARAMETERS, toLine) : null;

		return new CodeReference(from, to);
	}

	public static ClassInfo getClassInfo(String name) {
		return new ClassInfo(name, DUMMY_LINE);
	}

	public static ClassInfo getClassInfo() {
		return getClassInfo(CLASS_NAME);
	}
}
