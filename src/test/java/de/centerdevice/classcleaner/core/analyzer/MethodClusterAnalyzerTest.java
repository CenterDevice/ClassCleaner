package de.centerdevice.classcleaner.core.analyzer;

import static de.centerdevice.classcleaner.TestDataUtils.getClassInfo;
import static de.centerdevice.classcleaner.TestDataUtils.getReference;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class MethodClusterAnalyzerTest {
	private HashMap<ClassInfo, List<CodeReference>> references;
	private ReferenceAnalyzer analyzer;

	@Before
	public void setUp() {
		analyzer = new MethodClusterAnalyser();
		references = new HashMap<>();
	}

	@Test
	public void TestAnalyzeEmptyReport() {
		List<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeTwoReferringMethods() {
		references.put(getClassInfo(), asList(getReference("fromMethod", "toMethod")));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeMultipleReferringMethods() {
		references.put(getClassInfo(), asList(getReference("m1", "m2"), getReference("m2", "m3")));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeMethodClusters() {
		references.put(getClassInfo(), asList(getReference("m1", "m2"), getReference("m3", "m4")));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.size() == 2);
	}

	protected List<Issue> runAnalyzer() {
		return analyzer.analyze(new ReferenceReport(ReferenceScope.Project, references));
	}
}
