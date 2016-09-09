package de.centerdevice.classcleaner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.analyzer.ForeignMethodAnalyzer;
import de.centerdevice.classcleaner.core.analyzer.MethodClusterAnalyzer;
import de.centerdevice.classcleaner.core.analyzer.UnusedMethodAnalyzer;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.model.ReferenceScope;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;
import de.centerdevice.classcleaner.core.recon.ReferenceReporter;
import de.centerdevice.classcleaner.java.JavaReferenceFindingVisitor;
import de.centerdevice.classcleaner.reporting.ClassMarker;

public class ClassCleaner {

	private static final ClassCleaner instance = new ClassCleaner();

	private final ReferenceReporter reporter = new ReferenceReporter(Arrays.asList(new JavaReferenceFindingVisitor()));

	private final ClassMarker marker = new ClassMarker();

	private ClassCleaner() {

	}

	public static ClassCleaner getInstance() {
		return instance;
	}

	public void analyzeFull(IFile resource, IProgressMonitor monitor) {
		analyze(resource, ReferenceScope.Project, monitor);
	}

	public void analyze(IFile resource, IProgressMonitor monitor) {
		analyze(resource, getScope(), monitor);
	}

	public ReferenceScope getScope() {
		String scope = ClassCleanerPlugin.getDefault().getPreferenceStore()
				.getString(ClassCleanerPlugin.DEFAULT_REFERENCE_SCOPE);
		try {
			return ReferenceScope.valueOf(scope);
		} catch (Exception e) {
			return ReferenceScope.Class;
		}
	}

	private void analyze(IFile resource, ReferenceScope scope, IProgressMonitor monitor) {
		marker.deleteMarkers(resource);

		ReferenceReport report = generateReport(resource, scope, monitor);
		Set<Issue> issues = new HashSet<>();
		if (isEnabled(ClassCleanerPlugin.ANALYZER_UNUSED_METHOD)) {
			issues.addAll(new UnusedMethodAnalyzer().analyze(report));
		}
		if (isEnabled(ClassCleanerPlugin.ANALYZER_METHOD_CLUSTER)) {
			issues.addAll(new MethodClusterAnalyzer().analyze(report));
		}
		if (isEnabled(ClassCleanerPlugin.ANALYZER_FOREIGN_METHOD)) {
			issues.addAll(new ForeignMethodAnalyzer().analyze(report));
		}

		marker.addMarker(resource, issues);
	}

	private boolean isEnabled(String analyzerUnusedMethod) {
		return ClassCleanerPlugin.getDefault().getPreferenceStore().getBoolean(analyzerUnusedMethod);
	}

	public ReferenceReport generateReport(IFile resource, ReferenceScope scope, IProgressMonitor monitor) {
		return reporter.createReport(resource, scope, monitor);
	}

	public void removeAllMarkers(IProject project) throws CoreException {
		marker.clean(project);
	}

	public void removeMarkers(IFile file) throws CoreException {
		marker.deleteMarkers(file);
	}
}
