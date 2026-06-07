package com.company.aiassistant.ai;

import java.util.ArrayList;
import java.util.List;

import com.company.aiassistant.model.ChatMessage;
import com.company.aiassistant.model.MessageType;
import com.company.aiassistant.ui.ChatRenderer;

/**
 * Converts markdown-like AI responses
 * into simple HTML.
 *
 * Phase 1 Support:
 * - # Heading
 * - ## Heading
 * - ``` code blocks
 * - normal text
 */
public final class ResponseFormatter {

    private ResponseFormatter() {
    }

    public static String toHtml(
            String text) {

        if (text == null) {

            return "";
        }

        StringBuilder html =
                new StringBuilder();

        String[] lines =
                text.split("\\r?\\n");

        boolean inCodeBlock = false;

        for (int i = 0;
                i < lines.length;
                i++) {

            String line =
                    escapeHtml(
                            lines[i]);

            /*
             * Code block start/end
             */
            if (line.startsWith("```")) {

                if (!inCodeBlock) {

                    html.append(
                            "<pre class='code-block'>");

                    inCodeBlock = true;

                } else {

                    html.append(
                            "</pre>");

                    inCodeBlock = false;
                }

                continue;
            }

            /*
             * Inside code block
             */
            if (inCodeBlock) {

                html.append(line)
                    .append("\n");

                continue;
            }

            /*
             * H1
             */
            if (line.startsWith("# ")) {

                html.append(
                        "<h1>")
                    .append(
                        line.substring(2))
                    .append(
                        "</h1>");

                continue;
            }

            /*
             * H2
             */
            if (line.startsWith("## ")) {

                html.append(
                        "<h2>")
                    .append(
                        line.substring(3))
                    .append(
                        "</h2>");

                continue;
            }

            /*
             * Empty line
             */
            if (line.trim()
                    .length() == 0) {

                html.append(
                        "<br/>");

                continue;
            }

            /*
             * Normal text
             */
            html.append(
                    "<p>")
                .append(line)
                .append(
                    "</p>");
        }

        return html.toString();
    }

    /**
     * Escapes HTML special chars.
     */
    private static String escapeHtml(
            String text) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
    
    public static void main(String[] args){
    	/*String html =
    		    ResponseFormatter.toHtml(
    		        "# Heading\n\n"
    		      + "Normal Text\n\n"
    		      + "```java\n"
    		      + "System.out.println(\"Hello\");\n"
    		      + "```");

    		System.out.println(html);*/
    		
    		
    		List<ChatMessage> messages =
    		        new ArrayList<ChatMessage>();

    		messages.add(
    		        new ChatMessage(
    		                MessageType.USER,
    		                "Generate Java addition program"));

    		messages.add(
    		        new ChatMessage(
    		                MessageType.AI,
    		                "# Project Structure\n\n"
    		                + "```java\n"
    		                + "public class Main {}\n"
    		                + "```"));

    		String html =
    		        ChatRenderer.render(
    		                messages);

    		System.out.println(html);
    		
    }
    
}