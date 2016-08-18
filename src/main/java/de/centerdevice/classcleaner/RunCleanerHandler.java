package de.centerdevice.classcleaner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class RunCleanerHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof ICompilationUnit) {
				analyzeCompilationUnit((ICompilationUnit) firstElement);
			}
			if (firstElement instanceof IFile) {
				analyzeFile((IFile) firstElement);
			}
		}
		return null;
	}

	protected void analyzeCompilationUnit(ICompilationUnit unit) {
		try {
			IResource underlyingResource = unit.getUnderlyingResource();
			if (underlyingResource != null && underlyingResource.getType() == IResource.FILE) {
				analyzeFile((IFile) underlyingResource);
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}

	protected void analyzeFile(IFile file) {
		Job job = new Job("ClassCleaner") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				ClassCleaner.getInstance().analyze(file, monitor);
				return Status.OK_STATUS;
			}
		};
		job.setUser(true);
		job.schedule();
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}