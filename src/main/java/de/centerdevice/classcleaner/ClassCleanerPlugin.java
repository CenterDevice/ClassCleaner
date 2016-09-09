package de.centerdevice.classcleaner;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ClassCleanerPlugin extends AbstractUIPlugin {

	public static final String ANALYZER_METHOD_CLUSTER = "ANALYZER_METHOD_CLUSTER";
	public static final String ANALYZER_FOREIGN_METHOD = "ANALYZER_FOREIGN_METHOD";
	public static final String ANALYZER_UNUSED_METHOD = "ANALYZER_UNUSED_METHOD";

	public static final String DEFAULT_REFERENCE_SCOPE = "DEFAULT_REFERENCE_SCOPE";

	private static ClassCleanerPlugin plugin = null;

	public static ClassCleanerPlugin getDefault() {
		if (plugin == null) {
			plugin = new ClassCleanerPlugin();
		}

		return plugin;
	}
}
