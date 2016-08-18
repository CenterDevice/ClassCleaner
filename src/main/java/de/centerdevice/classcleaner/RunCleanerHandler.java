package de.centerdevice.classcleaner;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.centerdevice.classcleaner.java.JavaSelectionConverter;

public class RunCleanerHandler extends AbstractHandler implements IHandler {

	private final JavaSelectionConverter selectionConverter = new JavaSelectionConverter();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (IFile file : selectionConverter.toFiles((IStructuredSelection) selection)) {
				analyzeFile(file);
			}
		}
		return null;
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