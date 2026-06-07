package com.company.aiassistant.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;

public class PreferenceInitializer
        extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {

        IPreferenceStore store =
                PreferenceStoreProvider.getStore();

        store.setDefault(
                PreferenceConstants.USE_CUSTOM_ENDPOINT,
                false);

        /*
         * Ollama Defaults
         */
        store.setDefault(
                PreferenceConstants.OLLAMA_URL,
                "http://localhost:11434");

        store.setDefault(
                PreferenceConstants.OLLAMA_MODEL,
                "mistral:7b-instruct");

        /*
         * Custom Endpoint Defaults
         */
        store.setDefault(
                PreferenceConstants.CUSTOM_ENDPOINT_URL,
                "");

        store.setDefault(
                PreferenceConstants.CUSTOM_ENDPOINT_TOKEN,
                "");

        /*
         * AI Parameters
         */
        store.setDefault(
                PreferenceConstants.TEMPERATURE,
                "0.2");

        store.setDefault(
                PreferenceConstants.MAX_TOKENS,
                "4000");
    }
}