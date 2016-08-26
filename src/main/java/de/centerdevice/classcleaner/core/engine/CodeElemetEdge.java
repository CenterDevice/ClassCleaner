package de.centerdevice.classcleaner.core.engine;

import org.jgrapht.graph.DefaultEdge;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;

public class CodeElemetEdge extends DefaultEdge {

	private static final long serialVersionUID = -1L;

	public CodeElement getSourceElement() {
		return (CodeElement) getSource();
	}

	public CodeElement getTargetElement() {
		return (CodeElement) getTarget();
	}

	public boolean isWithinClass(ClassInfo classInfo) {
		return getSourceElement() != null && getTargetElement() != null
				&& classInfo.getName().equals(getSourceElement().getClassName())
				&& classInfo.getName().equals(getTargetElement().getClassName());
	}
}
