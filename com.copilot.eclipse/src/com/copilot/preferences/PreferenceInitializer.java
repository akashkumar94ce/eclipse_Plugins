package com.copilot.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.copilot.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_PROVIDER, "openai");
        store.setDefault(PreferenceConstants.P_ENDPOINT, "https://api.openai.com/v1/chat/completions");
        store.setDefault(PreferenceConstants.P_MODEL, "gpt-4");
        store.setDefault(PreferenceConstants.P_API_KEY, "");
    }
}