package com.company.aiassistant.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public final class HttpUtil {

    private HttpUtil() {
    }

    public static String postJson(
            String url,
            String jsonBody,
            String bearerToken)
            throws Exception {

        CloseableHttpClient client =
                HttpClients.createDefault();

        try {

            HttpPost post =
                    new HttpPost(url);

            post.setHeader(
                    "Content-Type",
                    "application/json");

            if (bearerToken != null
                    && bearerToken.trim().length() > 0) {

                post.setHeader(
                        "Authorization",
                        "Bearer " + bearerToken);
            }

            post.setEntity(
			        new StringEntity(
			                jsonBody,
			                "UTF-8"));

            CloseableHttpResponse response =
                    client.execute(post);

            try {

                HttpEntity entity =
                        response.getEntity();

                if (entity == null) {
                    return "";
                }

                InputStream is =
                        entity.getContent();

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        is,
                                        "UTF-8"));

                StringBuilder sb =
                        new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {

                    sb.append(line);
                }

                return sb.toString();

            } finally {
                response.close();
            }

        } finally {
            client.close();
        }
    }
}