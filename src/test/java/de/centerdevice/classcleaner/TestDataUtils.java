package de.centerdevice.classcleaner;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.MethodElement;

public class TestDataUtils {
	public static final int DUMMY_LINE = -1;
	public static final String SIGNATURE = "()V";
	public static final String CLASS_NAME = "class";

	public static CodeReference getReference(String fromMethod, String toMethod) {
		return getReference(fromMethod, DUMMY_LINE, toMethod, DUMMY_LINE);
	}

	public static CodeReference getReference(String fromMethod, int fromLine, String toMethod, int toLine) {
		MethodElement from = (fromMethod != null) ? new MethodElement(CLASS_NAME, fromMethod, SIGNATURE, fromLine)
				: null;
		MethodElement to = (toMethod != null) ? new MethodElement(CLASS_NAME, toMethod, SIGNATURE, toLine) : null;

		return new CodeReference(from, to);
	}

	public static ClassInfo getClassInfo(String name) {
		return new ClassInfo(name, DUMMY_LINE);
	}

	public static ClassInfo getClassInfo() {
		return getClassInfo(CLASS_NAME);
	}
}
