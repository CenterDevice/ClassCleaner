package de.centerdevice.classcleaner.menu;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.core.resources.IFolder;

public class DirectoryPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		return receiver instanceof IFolder;
	}
}
