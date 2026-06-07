package com.company.aiassistant.ai;

import org.eclipse.jface.preference.IPreferenceStore;

import com.company.aiassistant.preferences.PreferenceConstants;
import com.company.aiassistant.preferences.PreferenceStoreProvider;
import com.company.aiassistant.util.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class OllamaClient {

    public AIResponse execute(String prompt) {

        AIResponse response =
                new AIResponse();

        try {

            IPreferenceStore store =
                    PreferenceStoreProvider.getStore();

            String ollamaUrl =
                    store.getString(
                            PreferenceConstants.OLLAMA_URL);

            String model =
                    store.getString(
                            PreferenceConstants.OLLAMA_MODEL);

            System.out.println(
                    "Ollama URL = [" + ollamaUrl + "]");

            System.out.println(
                    "Model = [" + model + "]");
            
            String url =
                    ollamaUrl + "/api/generate";

            JsonObject request =
                    new JsonObject();

            request.addProperty(
                    "model",
                    model);

            request.addProperty(
                    "prompt",
                    prompt);

            request.addProperty(
                    "stream",
                    false);

            String jsonResponse =
                    HttpUtil.postJson(
                            url,
                            request.toString(),
                            null);
            
            System.out.println(
                    "RAW OLLAMA RESPONSE = "
                    + jsonResponse);

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
            
            String msg =
                    e.getClass().getName()
                    + " : "
                    + e.getMessage();

            response.setErrorMessage(msg);

            System.out.println(msg);

            e.printStackTrace();

            e.printStackTrace();
        }

        return response;
    }
}