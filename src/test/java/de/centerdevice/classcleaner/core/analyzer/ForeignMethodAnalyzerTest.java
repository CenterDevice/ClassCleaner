package de.centerdevice.classcleaner.core.analyzer;

import static de.centerdevice.classcleaner.TestDataUtils.getClassInfo;
import static de.centerdevice.classcleaner.TestDataUtils.getReference;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.centerdevice.classcleaner.core.model.ClassInfo;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;

public class ForeignMethodAnalyzerTest {
	private HashMap<ClassInfo, List<CodeReference>> references;
	private ReferenceAnalyzer analyzer;

	@Before
	public void setUp() {
		analyzer = new ForeignMethodAnalyzer();
		references = new HashMap<>();
	}

	@Test
	public void TestAnalyzeEmptyReport() {
		Set<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeReferringMethodsInSameClass() {
		references.put(getClassInfo(), asList(getReference("fromMethod", "toMethod")));

		Set<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	@Test
	public void TestAnalyzeMethodOnlyReferredToByOtherClass() {
		references.put(getClassInfo("toClass"), asList(getReference("fromClass", "method", "toClass", "method")));

		Set<Issue> issues = runAnalyzer();
		assertTrue(issues.size() == 1);
	}

	@Test
	public void TestAnalyzeMethodThatReferencesOtherClassMethod() {
		references.put(getClassInfo("toClass"), asList(getReference("fromClass", "method", "toClass", "method"),
				getReference("toClass", "method", "toClass", "method2")));

		Set<Issue> issues = runAnalyzer();
		assertTrue(issues.isEmpty());
	}

	protected Set<Issue> runAnalyzer() {
		return analyzer.analyze(new ReferenceReport(ReferenceScope.Project, references));
	}
}
