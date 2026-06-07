package com.company.aiassistant.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.company.aiassistant.ai.AIActionType;
import com.company.aiassistant.ai.PromptBuilder;
import com.company.aiassistant.util.EditorUtil;
import com.company.aiassistant.util.ViewUtil;
import com.company.aiassistant.views.AIChatView;

public class FixBugHandler
        extends AbstractHandler {

    @Override
    public Object execute(
            ExecutionEvent event)
            throws ExecutionException {

        try {

            String code =
                    EditorUtil.getSelectedText();

            if (code == null
                    || code.trim().length() == 0) {

                return null;
            }

            AIChatView view =
                    ViewUtil.getAIChatView();
            
            view.setActionType(
                    AIActionType.FIX_BUG);

            view.setPrompt(
                    PromptBuilder
                        .buildFixBugPrompt(
                                code));

            view.sendPrompt();

        } catch (Exception e) {

            throw new ExecutionException(
                    "Failed to fix code",
                    e);
        }

        return null;
    }
}