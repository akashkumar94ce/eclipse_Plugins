package com.copilot.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;

public class AIClient1 {

    public static String postRequest(String targetUrl, String apiKey, String model, String prompt) throws Exception {
        URL url = new URL(targetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
        }
        conn.setDoOutput(true);

        // --- Build a Universal Payload ---
        JsonObject body = new JsonObject();
        body.addProperty("model", model);
        
        // CRITICAL FOR OLLAMA: Disable streaming to get a single unified JSON response
        body.addProperty("stream", false); 
        
        // Support for Ollama's native /api/generate endpoint
        body.addProperty("prompt", prompt);

        // Support for OpenAI / v1/chat/completions / Ollama /api/chat endpoints
        JsonArray messages = new JsonArray();
        JsonObject messageObj = new JsonObject();
        messageObj.addProperty("role", "user");
        messageObj.addProperty("content", prompt);
        messages.add(messageObj);
        body.add("messages", messages);

        OutputStream os = conn.getOutputStream();
        try {
            os.write(body.toString().getBytes("UTF-8"));
        } finally {
            os.close();
        }

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
            // 2. Ollama native /api/generate format
            else if (jsonObject.has("response")) {
                return jsonObject.get("response").getAsString();
            }
            // 3. Ollama native /api/chat format
            else if (jsonObject.has("message")) {
                return jsonObject.getAsJsonObject("message").get("content").getAsString();
            }

            // Fallback: If we still don't recognize the structure, print raw
            return rawJson;
        } catch (Exception e) {
            return "Failed parsing response sequence. Raw Output: " + rawJson;
        }
    }
    
    // NEW METHOD: Dedicated Gemini Implementation
    public static String postGeminiRequest(String apiKey, String model, String prompt) throws Exception {
        // Construct the specific Google REST URL
        String targetUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
        
        URL url = new URL(targetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);

        // --- Build Gemini Specific Payload ---
        JsonObject body = new JsonObject();
        JsonArray contents = new JsonArray();
        JsonObject contentObj = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject partObj = new JsonObject();
        
        // {"contents": [{"parts": [{"text": "Your prompt here"}]}]}
        partObj.addProperty("text", prompt);
        parts.add(partObj);
        contentObj.add("parts", parts);
        contents.add(contentObj);
        body.add("contents", contents);

        OutputStream os = conn.getOutputStream();
        try {
            os.write(body.toString().getBytes("UTF-8"));
        } finally {
            os.close();
        }

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
            throw new RuntimeException("Gemini API Error: Http Code " + status + " -> " + response.toString());
        }

        return parseModelOutput1(response.toString());
    }

    // UPDATED: Added Gemini parsing support
    private static String parseModelOutput1(String rawJson) {
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
            // 2. NEW: Google Gemini format
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