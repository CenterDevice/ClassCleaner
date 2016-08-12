package de.centerdevice.classcleaner.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class UnusedMethodAnalyser implements ReferenceAnalyzer {

	private static final int SEVERITY = IMarker.SEVERITY_INFO;

	@Override
	public List<Issue> analyze(ReferenceReport report) {
		List<Issue> issues = new ArrayList<>();

		for (CodeReference codeReferences : report.getReferences()) {
			if (codeReferences.getSource() == null) {
				issues.add(createIssue(codeReferences));
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
