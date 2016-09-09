package de.centerdevice.classcleaner;

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

public class ClassCleanerBuilder extends IncrementalProjectBuilder {

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		private final IProgressMonitor monitor;

		public SampleDeltaVisitor(IProgressMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public boolean visit(IResourceDelta delta) throws CoreException {
			checkResource(delta.getResource(), monitor);
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
		ClassCleaner.getInstance().removeAllMarkers(getProject());
	}

	void checkResource(IResource resource, IProgressMonitor monitor) {
		if (resource instanceof IFile) {
			ClassCleaner.getInstance().analyze((IFile) resource, monitor);
		}
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
