package com.copilot.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.copilot.Activator;

public class CopilotPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    private ComboFieldEditor providerEditor;
    private StringFieldEditor endpointEditor;
    private StringFieldEditor apiKeyEditor;
    private StringFieldEditor modelEditor;

    public CopilotPreferencePage() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("Configure your preferred AI Engine configurations below:");
    }

    public void createFieldEditors() {
        String[][] providers = {
            {"ChatGPT", "openai"},
            {"Gemini", "gemini"},
            {"Ollama", "ollama"},
            {"AWS Bedrock", "aws"},
            {"Grok", "grok"},
            {"Custom Endpoint", "custom"}
        };

        providerEditor = new ComboFieldEditor(PreferenceConstants.P_PROVIDER, "AI Provider:", providers, getFieldEditorParent());
        addField(providerEditor);

        endpointEditor = new StringFieldEditor(PreferenceConstants.P_ENDPOINT, "Endpoint URL:", getFieldEditorParent());
        addField(endpointEditor);

        apiKeyEditor = new StringFieldEditor(PreferenceConstants.P_API_KEY, "Secret API Key:", getFieldEditorParent());
        addField(apiKeyEditor);

        modelEditor = new StringFieldEditor(PreferenceConstants.P_MODEL, "Model ID Identifier:", getFieldEditorParent());
        addField(modelEditor);
    }

    @Override
    protected void initialize() {
        super.initialize();
        // Load the initial UI state based on whatever is saved in the preference store
        adjustFields(getPreferenceStore().getString(PreferenceConstants.P_PROVIDER));
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
        // The JFace way to listen: check if the event came from our dropdown editor
        if (event.getSource() == providerEditor) {
            // event.getNewValue() returns the underlying value (e.g., "ollama"), not the label
            adjustFields(event.getNewValue().toString());
        }
    }

    private void adjustFields(String providerValue) {
        if ("ollama".equals(providerValue)) {
            endpointEditor.setStringValue("http://localhost:11434/api/generate");
            apiKeyEditor.setEnabled(false, getFieldEditorParent());
        } else {
            apiKeyEditor.setEnabled(true, getFieldEditorParent());
        }
    }

    public void init(IWorkbench workbench) {}
}