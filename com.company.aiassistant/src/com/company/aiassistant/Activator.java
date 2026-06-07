package com.company.aiassistant;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.company.aiassistant";

    private static Activator plugin;

    public Activator() {

        System.out.println(
            "Activator Constructor Called");
    }

    @Override
    public void start(BundleContext context)
            throws Exception {

        System.out.println(
            "Activator.start Called");

        super.start(context);

        plugin = this;
    }
    
    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }
}