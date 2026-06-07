package com.company.aiassistant.ai;

import org.eclipse.jface.preference.IPreferenceStore;

import com.company.aiassistant.preferences.PreferenceConstants;
import com.company.aiassistant.preferences.PreferenceStoreProvider;
import com.company.aiassistant.util.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CustomEndpointClient {

    public AIResponse execute(
            AIRequest requestObj,
            String prompt) {

        AIResponse response =
                new AIResponse();

        try {

            IPreferenceStore store =
                    PreferenceStoreProvider.getStore();

            String endpointUrl =
                    store.getString(
                            PreferenceConstants.CUSTOM_ENDPOINT_URL);

            String token =
                    store.getString(
                            PreferenceConstants.CUSTOM_ENDPOINT_TOKEN);

            JsonObject request =
                    new JsonObject();

            request.addProperty(
                    "action",
                    requestObj.getActionType().name());

            request.addProperty(
                    "prompt",
                    prompt);

            String jsonResponse =
                    HttpUtil.postJson(
                            endpointUrl,
                            request.toString(),
                            token);

            JsonObject root =
                    new JsonParser()
                            .parse(jsonResponse)
                            .getAsJsonObject();

            String text =
                    root.get("response")
                            .getAsString();

            response.setSuccess(true);
            response.setResponse(text);

        } catch (Exception e) {

            response.setSuccess(false);

            response.setErrorMessage(
                    e.getMessage());

            e.printStackTrace();
        }

        return response;
    }
}