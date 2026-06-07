package com.company.aiassistant.preferences;

import org.eclipse.jface.preference.PreferenceStore;

public final class PreferenceStoreProvider {

    private static final PreferenceStore STORE =
            new PreferenceStore("aiassistant.properties");

    static {

        try {
            STORE.load();
        }
        catch (Exception e) {
            // First run, file doesn't exist yet
        }
    }

    private PreferenceStoreProvider() {
    }

    public static PreferenceStore getStore() {
        return STORE;
    }

    public static void save() {

        try {
            STORE.save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}