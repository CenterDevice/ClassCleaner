package de.centerdevice.classcleaner.menu.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import de.centerdevice.classcleaner.ClassCleaner;
import de.centerdevice.classcleaner.java.JavaSelectionConverter;

public class ClearMarkersHandler extends AbstractHandler implements IHandler {

	private final JavaSelectionConverter selectionConverter = new JavaSelectionConverter();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			for (IFile file : selectionConverter.toFiles((IStructuredSelection) selection)) {
				removeMarkers(file);
			}
		}

		return null;
	}

	public void removeMarkers(IFile file) {
		try {
			ClassCleaner.getInstance().removeMarkers(file);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

}