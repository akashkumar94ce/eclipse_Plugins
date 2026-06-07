package com.company.aiassistant.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.company.aiassistant.Activator;


public class AIPreferencePage
        extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {

	public AIPreferencePage() {

	    super(GRID);

	    setPreferenceStore(
	            PreferenceStoreProvider.getStore());

	    setDescription(
	            "AI Assistant Configuration");
	}

    @Override
    public void createFieldEditors() {

        addField(
            new BooleanFieldEditor(
                PreferenceConstants.USE_CUSTOM_ENDPOINT,
                "Use Custom Endpoint",
                getFieldEditorParent()));

        addField(
        	    new StringFieldEditor(
        	        PreferenceConstants.OLLAMA_URL,
        	        "Ollama URL",
        	        getFieldEditorParent()));

        addField(
        	    new StringFieldEditor(
        	        PreferenceConstants.OLLAMA_MODEL,
        	        "Ollama Model",
        	        getFieldEditorParent()));

        addField(
            new StringFieldEditor(
                PreferenceConstants.CUSTOM_ENDPOINT_URL,
                "Custom Endpoint URL",
                getFieldEditorParent()));

        addField(
            new StringFieldEditor(
                PreferenceConstants.CUSTOM_ENDPOINT_TOKEN,
                "Custom Endpoint Token",
                getFieldEditorParent()));

        addField(
            new StringFieldEditor(
                PreferenceConstants.TEMPERATURE,
                "Temperature",
                getFieldEditorParent()));

        addField(
            new StringFieldEditor(
                PreferenceConstants.MAX_TOKENS,
                "Max Tokens",
                getFieldEditorParent()));
    }

    @Override
    public void init(IWorkbench workbench) {
        // no-op
    }
    
    @Override
    public boolean performOk() {

        boolean result = super.performOk();

        PreferenceStoreProvider.save();

        return result;
    }
    
    @Override
    protected void performApply() {

        super.performApply();

        PreferenceStoreProvider.save();
    }
    
}