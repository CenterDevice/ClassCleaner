package de.centerdevice.classcleaner.core.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.engine.ReferenceGraph;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class ReferenceCycleAnalyzer implements ReferenceAnalyzer {

	@Override
	public List<Issue> analyze(ReferenceReport report) {
		List<Issue> issues = new ArrayList<>();

		for (ClassInfo classInfo : report.getClasses()) {
			ReferenceGraph graph = report.getReferenceGraph(classInfo);
			Set<CodeElement> cycles = graph.getCycles();

			for (CodeElement codeElement : cycles) {
				issues.add(new Issue(getMessage(codeElement), codeElement.getLineNumber(), IMarker.SEVERITY_INFO));
			}
		}

		return issues;
	}

	private String getMessage(CodeElement codeElement) {
		return codeElement.toString() + " is part of a reference cycle";
	}

}
