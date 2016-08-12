package de.centerdevice.classcleaner.core.analyzer;

import static de.centerdevice.classcleaner.TestDataUtils.CLASS_NAME;
import static de.centerdevice.classcleaner.TestDataUtils.getClassInfo;
import static de.centerdevice.classcleaner.TestDataUtils.getReference;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
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

public class UnusedMethodAnalyzerTest {

	private static final int UNUSED_LINE = 2;
	private HashMap<ClassInfo, List<CodeReference>> references;
	private ReferenceAnalyzer analyzer;

	@Before
	public void setUp() {
		analyzer = new UnusedMethodAnalyzer();
		references = new HashMap<>();
	}

	@Test
	public void TestAnalyzeEmptyReport() {
		List<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeSingleUnusedMethod() {
		references.put(new ClassInfo(CLASS_NAME, 1), asList(getReference(null, 0, "method", UNUSED_LINE)));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.size() == 1);
		assertEquals(UNUSED_LINE, issues.get(0).getLineNumber());
	}

	@Test
	public void TestAnalyzeSingleUsedMethod() {
		references.put(getClassInfo(), asList(getReference("fromMethod", "toMethod")));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeSingleMixedMethods() {
		references.put(getClassInfo(),
				asList(getReference("fromMethod", "toMethod"), getReference(null, 0, "unused", UNUSED_LINE)));

		List<Issue> issues = runAnalyzer();
		assertTrue(issues.size() == 1);
		assertEquals(UNUSED_LINE, issues.get(0).getLineNumber());
	}

	protected List<Issue> runAnalyzer() {
		return analyzer.analyze(new ReferenceReport(ReferenceScope.Project, references));
	}
}
