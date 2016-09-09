package de.centerdevice.classcleaner.menu;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import de.centerdevice.classcleaner.ClassCleanerPlugin;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore preferenceStore = ClassCleanerPlugin.getDefault().getPreferenceStore();

		preferenceStore.setDefault(ClassCleanerPlugin.ANALYZER_FOREIGN_METHOD, true);
		preferenceStore.setDefault(ClassCleanerPlugin.ANALYZER_METHOD_CLUSTER, true);
		preferenceStore.setDefault(ClassCleanerPlugin.ANALYZER_UNUSED_METHOD, true);

		preferenceStore.setDefault(ClassCleanerPlugin.DEFAULT_REFERENCE_SCOPE, ReferenceScope.Class.toString());
	}

}
