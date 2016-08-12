package de.centerdevice.classcleaner.core.analyzer;

import java.util.List;

import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public interface ReferenceAnalyzer {
	public List<Issue> analyze(ReferenceReport report);
}
