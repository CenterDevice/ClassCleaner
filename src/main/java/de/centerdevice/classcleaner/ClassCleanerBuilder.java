package de.centerdevice.classcleaner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.analyzer.ForeignMethodAnalyzer;
import de.centerdevice.classcleaner.core.analyzer.MethodClusterAnalyzer;
import de.centerdevice.classcleaner.core.analyzer.ReferenceCycleAnalyzer;
import de.centerdevice.classcleaner.core.analyzer.UnusedMethodAnalyzer;
import de.centerdevice.classcleaner.core.model.Issue;
import de.centerdevice.classcleaner.core.recon.ReferenceReport;
import de.centerdevice.classcleaner.core.recon.ReferenceReporter;
import de.centerdevice.classcleaner.java.JavaReferenceFindingVisitor;
import de.centerdevice.classcleaner.reporting.ClassMarker;

public class ClassCleanerBuilder extends IncrementalProjectBuilder {

	private final ReferenceReporter reporter = new ReferenceReporter(Arrays.asList(new JavaReferenceFindingVisitor()));

	private final ClassMarker marker = new ClassMarker();

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		private final IProgressMonitor monitor;

		public SampleDeltaVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
			case IResourceDelta.REMOVED:
			case IResourceDelta.CHANGED:
				checkResource(resource, monitor);
				break;
			}
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		private final IProgressMonitor monitor;

		public SampleResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResource resource) {
			checkResource(resource, monitor);
			return true;
		}
	}

	public static final String BUILDER_ID = "ClassCleaner.classCleanerBuilder";

	@Override
	protected IProject[] build(int kind, @SuppressWarnings("rawtypes") Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	@Override
	protected void clean(IProgressMonitor monitor) throws CoreException {
		marker.clean(getProject());
	}

	void checkResource(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile) {
			analyze((IFile) resource, monitor);
		}
	}

	protected void analyze(IFile resource, IProgressMonitor monitor) {
		marker.deleteMarkers(resource);

		ReferenceReport report = reporter.createReport(resource, monitor);
		Set<Issue> issues = new HashSet<>();
		issues.addAll(new UnusedMethodAnalyzer().analyze(report));
		issues.addAll(new MethodClusterAnalyzer().analyze(report));
		issues.addAll(new ReferenceCycleAnalyzer().analyze(report));
		issues.addAll(new ForeignMethodAnalyzer().analyze(report));

		marker.addMarker(resource, issues);
	}

	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		try {
			getProject().accept(new SampleResourceVisitor(monitor));
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		delta.accept(new SampleDeltaVisitor(monitor));
	}
}
