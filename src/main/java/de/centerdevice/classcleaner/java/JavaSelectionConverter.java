package de.centerdevice.classcleaner.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.IStructuredSelection;

public class JavaSelectionConverter {
	public List<IFile> toFiles(IStructuredSelection selection) {
		List<IFile> result = new ArrayList<>();
		
		for (Iterator<?> iterator = selection.iterator(); iterator.hasNext();) {
			Object next = iterator.next();
			if (next instanceof ICompilationUnit) {
				IFile compilationUnit = analyzeCompilationUnit((ICompilationUnit) next);
				if (compilationUnit != null) {
					result.add(compilationUnit);
				}
			}
		}

		return result;
	}

	public IProject toProject(IStructuredSelection selection) {
		Object firstSelection = selection.getFirstElement();

		if (firstSelection instanceof IProject) {
			return (IProject) firstSelection;
		} else if (firstSelection instanceof IJavaProject) {
			return ((IJavaProject) firstSelection).getProject();
		}

		return null;
	}

	protected IFile analyzeCompilationUnit(ICompilationUnit unit) {
		try {
			IResource underlyingResource = unit.getUnderlyingResource();
			if (underlyingResource != null && underlyingResource.getType() == IResource.FILE) {
				return (IFile) underlyingResource;
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}

}
