package com.company.aiassistant.util;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.company.aiassistant.views.AIChatView;

public final class ViewUtil {

    private ViewUtil() {
    }

    /**
     * Opens AI Chat View if needed
     * and returns its instance.
     */
    public static AIChatView getAIChatView()
            throws PartInitException {

        IWorkbenchPage page =
                PlatformUI.getWorkbench()
                          .getActiveWorkbenchWindow()
                          .getActivePage();

        return (AIChatView)
                page.showView(
                        AIChatView.ID);
    }
}