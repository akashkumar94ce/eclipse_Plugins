package com.company.aiassistant.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.company.aiassistant.ai.AIActionType;
import com.company.aiassistant.ai.PromptBuilder;
import com.company.aiassistant.util.ViewUtil;
import com.company.aiassistant.views.AIChatView;

public class GenerateCodeHandler
        extends AbstractHandler {

    @Override
    public Object execute(
            ExecutionEvent event)
            throws ExecutionException {

        try {

            AIChatView view =
                    ViewUtil.getAIChatView();

            view.setActionType(
                    AIActionType.GENERATE_CODE);

            String propmt = PromptBuilder.buildGeneratePrompt("");
           
           /* view.setPrompt(
                    "Generate production quality code for:\n");
            */
            view.setPrompt(propmt+"\n\n");

            view.setFocus();

        } catch (Exception e) {

            throw new ExecutionException(
                    "Failed to generate code",
                    e);
        }

        return null;
    }
}