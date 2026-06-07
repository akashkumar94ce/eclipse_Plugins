package com.company.aiassistant.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.company.aiassistant.ai.AIActionType;
import com.company.aiassistant.ai.PromptBuilder;
import com.company.aiassistant.util.EditorUtil;
import com.company.aiassistant.util.ViewUtil;
import com.company.aiassistant.views.AIChatView;

public class ExplainCodeHandler
        extends AbstractHandler {

    @Override
    public Object execute(
            ExecutionEvent event)
            throws ExecutionException {

        try {

            String selectedText =
                    EditorUtil.getSelectedText();

            if (selectedText == null
                    || selectedText.trim().length() == 0) {

                return null;
            }

            AIChatView view =
                    ViewUtil.getAIChatView();
            
            view.setActionType(
                    AIActionType.EXPLAIN);

            /*String prompt =
                    buildPrompt(
                            selectedText);
            */
            String prompt =
                    PromptBuilder
                        .buildExplainPrompt(
                                selectedText);

            view.setPrompt(
                    prompt);
            

            view.sendPrompt();

        } catch (Exception e) {

            throw new ExecutionException(
                    "Failed to explain code",
                    e);
        }

        return null;
    }

    /**
     * Builds explain prompt.
     */
    private String buildPrompt(
            String code) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
            "Explain the following code in detail.\n\n");

        sb.append(
            "Include:\n");

        sb.append(
            "1. Purpose of the code\n");

        sb.append(
            "2. Line by line explanation\n");

        sb.append(
            "3. Important logic\n");

        sb.append(
            "4. Possible issues\n");

        sb.append(
            "5. Suggestions for improvement\n\n");

        sb.append(
            "Code:\n");

        sb.append(
            code);

        return sb.toString();
    }
}