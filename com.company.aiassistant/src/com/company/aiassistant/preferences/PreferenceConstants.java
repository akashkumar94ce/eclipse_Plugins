package com.company.aiassistant.preferences;

public final class PreferenceConstants {

    private PreferenceConstants() {
    }

    public static final String USE_CUSTOM_ENDPOINT =
            "USE_CUSTOM_ENDPOINT";

    /*
     * Ollama Configuration
     */
    public static final String OLLAMA_URL =
            "OLLAMA_URL";

    public static final String OLLAMA_MODEL =
            "OLLAMA_MODEL";

    /*
     * Custom Endpoint Configuration
     */
    public static final String CUSTOM_ENDPOINT_URL =
            "CUSTOM_ENDPOINT_URL";

    public static final String CUSTOM_ENDPOINT_TOKEN =
            "CUSTOM_ENDPOINT_TOKEN";

    /*
     * AI Parameters
     */
    public static final String TEMPERATURE =
            "TEMPERATURE";

    public static final String MAX_TOKENS =
            "MAX_TOKENS";
}