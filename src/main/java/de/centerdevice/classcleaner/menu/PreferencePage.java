package de.centerdevice.classcleaner.menu;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.RadioGroupFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.centerdevice.classcleaner.ClassCleanerPlugin;
import de.centerdevice.classcleaner.core.model.ReferenceScope;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	@Override
	public void init(IWorkbench arg0) {
		setDescription("Select analyzers to run");
		setPreferenceStore(ClassCleanerPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(ClassCleanerPlugin.ANALYZER_METHOD_CLUSTER,
				"Analyze method clusters that do not refer to each other", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ClassCleanerPlugin.ANALYZER_UNUSED_METHOD,
				"Detect public methods that are not used", getFieldEditorParent()));
		addField(new BooleanFieldEditor(ClassCleanerPlugin.ANALYZER_FOREIGN_METHOD,
				"Detect method that should be moved to a differen class", getFieldEditorParent()));

		String[][] strings = { { "Class", ReferenceScope.Class.toString() },
				{ "Project", ReferenceScope.Project.toString() } };
		addField(new RadioGroupFieldEditor(ClassCleanerPlugin.DEFAULT_REFERENCE_SCOPE,
				"Default scope to analyze during compilation", 1, strings, getFieldEditorParent()));
	}
}
