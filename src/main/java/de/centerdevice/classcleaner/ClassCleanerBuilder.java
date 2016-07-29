package de.centerdevice.classcleaner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.centerdevice.classcleaner.core.engine.ReferenceClustering;
import de.centerdevice.classcleaner.core.model.CodeReference;
import de.centerdevice.classcleaner.java.JavaResourceVisitor;
import de.centerdevice.classcleaner.reporting.ClassMarker;
import de.centerdevice.classcleaner.reporting.ResultReporter;

public class ClassCleanerBuilder extends IncrementalProjectBuilder {

	private List<ClassCleanerResourceVisitor> visitors = Arrays.asList(new JavaResourceVisitor());

	private ClassMarker marker = new ClassMarker();

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		private IProgressMonitor monitor;

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
		private IProgressMonitor monitor;

		public SampleResourceVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

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

	protected void clean(IProgressMonitor monitor) throws CoreException {
		marker.clean(getProject());
	}

	void checkResource(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile) {
			ReferenceClustering clustering = new ReferenceClustering();
			ResultReporter reporter = new ResultReporter(marker);

			reporter.clean((IFile) resource);
			List<CodeReference> references = getAllReferences((IFile) resource, monitor);
			for (CodeReference codeReference : references) {
				clustering.addReference(codeReference);
			}

			reporter.report((IFile) resource, references);
			System.out.println(clustering.getReferenceGroups());
		}
	}

	protected List<CodeReference> getAllReferences(IFile resource, IProgressMonitor monitor) {
		List<CodeReference> references = new ArrayList<>();
		for (ClassCleanerResourceVisitor visitor : visitors) {
			references.addAll(visitor.visit(resource, monitor));
		}
		return references;
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
