package com.company.aiassistant.ai;

import org.eclipse.jface.preference.IPreferenceStore;

import com.company.aiassistant.preferences.PreferenceConstants;
import com.company.aiassistant.preferences.PreferenceStoreProvider;

public class AIServiceImpl
        implements AIService {

    @Override
    public AIResponse execute(
            AIRequest request) {

        try {

            String prompt =
                    PromptBuilder.build(request);
            

            IPreferenceStore store =
                    PreferenceStoreProvider.getStore();

            boolean useCustomEndpoint =
                    store.getBoolean(
                            PreferenceConstants.USE_CUSTOM_ENDPOINT);

            if (useCustomEndpoint) {

                return new CustomEndpointClient()
                        .execute(
                                request,
                                prompt);
            }

            return new OllamaClient()
                    .execute(prompt);

        } catch (Exception e) {

            AIResponse response =
                    new AIResponse();

            response.setSuccess(false);

            response.setErrorMessage(
                    e.getMessage());

            return response;
        }
    }
}