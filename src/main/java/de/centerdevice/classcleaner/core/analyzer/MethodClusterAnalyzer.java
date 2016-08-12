package de.centerdevice.classcleaner.core.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.engine.ReferenceGraph;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class MethodClusterAnalyzer implements ReferenceAnalyzer {

	private static final int SEVERITY = IMarker.SEVERITY_INFO;

	@Override
	public Set<Issue> analyze(ReferenceReport report) {
		Set<Issue> issues = new HashSet<>();

		for (ClassInfo classInfo : report.getClasses()) {
			issues.addAll(analyzeIssuesForClass(report, classInfo));
		}

		return issues;
	}

	protected List<Issue> analyzeIssuesForClass(ReferenceReport report, ClassInfo classInfo) {
		ReferenceGraph clustering = report.getReferenceGraph(classInfo);
		List<Set<CodeElement>> referenceGroups = clustering.getReferenceGroups();
		List<Issue> issues = new ArrayList<>();
		if (referenceGroups.size() > 1) {
			for (Set<CodeElement> set : referenceGroups) {
				issues.add(new Issue(getMessage(set), classInfo.getLineNumber(), SEVERITY));
			}
		}
		return issues;
	}

	protected String getMessage(Set<CodeElement> set) {
		StringBuilder classSuggestion = new StringBuilder("Consider extracting class with methods: ");
		for (CodeElement codeElement : set) {
			classSuggestion.append(" " + codeElement.toSimpleString());
		}
		return classSuggestion.toString();
	}

}
