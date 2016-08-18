package de.centerdevice.classcleaner.core.analyzer;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class UnusedMethodAnalyzer implements ReferenceAnalyzer {

	private static final int SEVERITY = IMarker.SEVERITY_INFO;

	@Override
	public Set<Issue> analyze(ReferenceReport report) {
		Set<Issue> issues = new HashSet<>();
		if (report.getScope() != ReferenceScope.Project) {
			return issues;
		}

		for (ClassInfo classInfo : report.getClasses()) {
			for (CodeReference codeReferences : report.getReferences(classInfo)) {
				if (codeReferences.getSource() == null) {
					issues.add(createIssue(codeReferences));
				}
			}
		}

		return issues;
	}

	private Issue createIssue(CodeReference codeReferences) {
		return new Issue(getMessage(codeReferences.getDestination()), codeReferences.getDestination().getLineNumber(),
				SEVERITY);
	}

	private String getMessage(CodeElement destination) {
		return destination.getElementName() + " is never used";
	}

}
