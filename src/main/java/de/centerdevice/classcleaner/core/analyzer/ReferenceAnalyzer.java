package de.centerdevice.classcleaner.core.analyzer;

import java.util.Set;

import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public interface ReferenceAnalyzer {
	public Set<Issue> analyze(ReferenceReport report);
}
