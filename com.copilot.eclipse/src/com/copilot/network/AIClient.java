package com.copilot.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class AIClient {

    public static String postRequest(String targetUrl, String apiKey, String model, String prompt) throws Exception {
        // 1. Determine if the request is for a Gemini model
        boolean isGemini = model != null && model.toLowerCase().contains("gemini");
        
        // 2. Set the correct URL
        String finalUrl = targetUrl;
        if (isGemini) {
            finalUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
        }

        URL url = new URL(finalUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        
        // Gemini uses the API key in the URL, whereas OpenAI/Ollama use the Bearer token
        if (!isGemini && apiKey != null && !apiKey.trim().isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        }
        
        conn.setDoOutput(true);

        // 3. Build the conditional payload
        JsonObject body = new JsonObject();
        
        if (isGemini) {
            // --- Build Gemini Specific Payload ---
            JsonArray contents = new JsonArray();
            JsonObject contentObj = new JsonObject();
            JsonArray parts = new JsonArray();
            JsonObject partObj = new JsonObject();
            
            partObj.addProperty("text", prompt);
            parts.add(partObj);
            contentObj.add("parts", parts);
            contents.add(contentObj);
            body.add("contents", contents);
        } else {
            // --- Build Universal Payload (OpenAI / Ollama) ---
            body.addProperty("model", model);
            body.addProperty("stream", false); 
            body.addProperty("prompt", prompt);

            JsonArray messages = new JsonArray();
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("role", "user");
            messageObj.addProperty("content", prompt);
            messages.add(messageObj);
            body.add("messages", messages);
        }

        // 4. Send Request
        OutputStream os = conn.getOutputStream();
        try {
            os.write(body.toString().getBytes("UTF-8"));
        } finally {
            os.close();
        }

        // 5. Read Response
        int status = conn.getResponseCode();
        BufferedReader br;
        if (status >= 200 && status < 300) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            response.append(line);
        }
        br.close();

        if (status != 200) {
            throw new RuntimeException("API Error: Http Code " + status + " -> " + response.toString());
        }

        // 6. Parse the unified output
        return parseModelOutput(response.toString());
    }

    private static String parseModelOutput(String rawJson) {
        try {
            JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();

            // 1. Standard OpenAI format (or Ollama running via v1/chat/completions)
            if (jsonObject.has("choices")) {
                JsonArray choices = jsonObject.getAsJsonArray("choices");
                if (choices.size() > 0) {
                    JsonObject firstChoice = choices.get(0).getAsJsonObject();
                    if (firstChoice.has("message")) {
                        return firstChoice.getAsJsonObject("message").get("content").getAsString();
                    }
                }
            }
            // 2. Google Gemini format
            else if (jsonObject.has("candidates")) {
                JsonArray candidates = jsonObject.getAsJsonArray("candidates");
                if (candidates.size() > 0) {
                    JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                    if (firstCandidate.has("content")) {
                        JsonObject contentObj = firstCandidate.getAsJsonObject("content");
                        if (contentObj.has("parts")) {
                            JsonArray parts = contentObj.getAsJsonArray("parts");
                            if (parts.size() > 0) {
                                return parts.get(0).getAsJsonObject().get("text").getAsString();
                            }
                        }
                    }
                }
            }
            // 3. Ollama native /api/generate format
            else if (jsonObject.has("response")) {
                return jsonObject.get("response").getAsString();
            }
            // 4. Ollama native /api/chat format
            else if (jsonObject.has("message")) {
                return jsonObject.getAsJsonObject("message").get("content").getAsString();
            }

            // Fallback: If we still don't recognize the structure, print raw
            return rawJson;
        } catch (Exception e) {
            return "Failed parsing response sequence. Raw Output: " + rawJson;
        }
    }
}