package de.centerdevice.classcleaner.core.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IMarker;

import de.centerdevice.classcleaner.core.engine.ReferenceGraph;
import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeElement;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class ForeignMethodAnalyzer implements ReferenceAnalyzer {

	private static final int SEVERITY = IMarker.SEVERITY_INFO;

	@Override
	public List<Issue> analyze(ReferenceReport report) {
		List<Issue> issues = new ArrayList<>();

		for (ClassInfo classInfo : report.getClasses()) {
			ReferenceGraph graph = report.getReferenceGraph(classInfo);

			for (CodeElement codeElement : graph.getAllElements()) {
				if (codeElement.getClassName().equals(classInfo.getName())) {
					Set<CodeElement> referringElements = graph.getReferringElements(codeElement);
					Set<CodeElement> referencedElements = graph.getReferencedElements(codeElement);

					Set<String> referringClassNames = getClassNames(referringElements);
					Set<String> referencedClassNames = getClassNames(referencedElements);

					if (referringClassNames.size() == 1 && !referringClassNames.contains(classInfo.getName())
							&& !referencedClassNames.contains(classInfo.getName())) {
						issues.add(new Issue(getMessage(codeElement, referringClassNames.iterator().next()),
								codeElement.getLineNumber(), SEVERITY));
					}
				}
			}
		}

		return issues;
	}

	private String getMessage(CodeElement codeElement, String referringClass) {
		return codeElement.toString() + " is only used by " + referringClass + ". Consider moving it there.";
	}

	protected Set<String> getClassNames(Set<CodeElement> elements) {
		return elements.stream().map(element -> element.getClassName()).collect(Collectors.toSet());
	}

}
