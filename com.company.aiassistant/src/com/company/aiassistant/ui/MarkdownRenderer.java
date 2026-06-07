package com.company.aiassistant.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MarkdownRenderer {

    private MarkdownRenderer() {
    }

    public static String toHtml(String markdown) {

        if (markdown == null) {
            return "";
        }

        String html = markdown;

        html = escapeHtml(html);

        html = processCodeBlocks(html);

        html = processHeaders(html);

        html = processHorizontalRules(html);

        html = processTables(html);

        html = processBulletLists(html);

        html = processNumberedLists(html);

        html = processInlineFormatting(html);

        html = processParagraphs(html);

        return html;
    }

    private static String escapeHtml(String text) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }

    private static String processCodeBlocks(String text) {

        Pattern pattern =
                Pattern.compile(
                        "```(?:\\w+)?\\s*([\\s\\S]*?)```");

        Matcher matcher =
                pattern.matcher(text);

        StringBuffer buffer =
                new StringBuffer();

        while (matcher.find()) {

            String code =
                    matcher.group(1);

            String replacement =
                    "<pre class='code-block'>"
                            + code
                            + "</pre>";

            matcher.appendReplacement(
                    buffer,
                    Matcher.quoteReplacement(
                            replacement));
        }

        matcher.appendTail(buffer);

        return buffer.toString();
    }

    private static String processHeaders(String text) {

        text = text.replaceAll(
                "(?m)^###\\s+(.*)$",
                "<h3>$1</h3>");

        text = text.replaceAll(
                "(?m)^##\\s+(.*)$",
                "<h2>$1</h2>");

        text = text.replaceAll(
                "(?m)^#\\s+(.*)$",
                "<h1>$1</h1>");

        return text;
    }

    private static String processHorizontalRules(
            String text) {

        return text.replaceAll(
                "(?m)^---+$",
                "<hr/>");
    }

    private static String processInlineFormatting(
            String text) {

        text = text.replaceAll(
                "\\*\\*(.*?)\\*\\*",
                "<b>$1</b>");

        text = text.replaceAll(
                "\\*(.*?)\\*",
                "<i>$1</i>");

        text = text.replaceAll(
                "`([^`]+)`",
                "<code>$1</code>");

        return text;
    }

    private static String processBulletLists(
            String text) {

        String[] lines =
                text.split("\\r?\\n");

        StringBuilder html =
                new StringBuilder();

        boolean inList = false;

        for (String line : lines) {

            if (line.trim().startsWith("- ")) {

                if (!inList) {

                    html.append("<ul>");
                    inList = true;
                }

                html.append("<li>")
                        .append(
                                line.trim()
                                        .substring(2))
                        .append("</li>");
            }
            else {

                if (inList) {

                    html.append("</ul>");
                    inList = false;
                }

                html.append(line)
                        .append("\n");
            }
        }

        if (inList) {

            html.append("</ul>");
        }

        return html.toString();
    }

    private static String processNumberedLists(
            String text) {

        String[] lines =
                text.split("\\r?\\n");

        StringBuilder html =
                new StringBuilder();

        boolean inList = false;

        for (String line : lines) {

            if (line.matches("^\\d+\\.\\s+.*")) {

                if (!inList) {

                    html.append("<ol>");
                    inList = true;
                }

                String value =
                        line.replaceFirst(
                                "^\\d+\\.\\s+",
                                "");

                html.append("<li>")
                        .append(value)
                        .append("</li>");
            }
            else {

                if (inList) {

                    html.append("</ol>");
                    inList = false;
                }

                html.append(line)
                        .append("\n");
            }
        }

        if (inList) {

            html.append("</ol>");
        }

        return html.toString();
    }

    private static String processTables(
            String text) {

        String[] lines =
                text.split("\\r?\\n");

        StringBuilder html =
                new StringBuilder();

        boolean inTable = false;

        for (int i = 0; i < lines.length; i++) {

            String line =
                    lines[i];
            
            if (line.matches("^\\|[-\\s|]+\\|$")) {
                continue;
            }

            if (line.trim().startsWith("|")
                    && line.trim().endsWith("|")) {

                if (!inTable) {

                    html.append("<table>");
                    inTable = true;
                }

                String[] cols =
                        line.substring(
                                1,
                                line.length() - 1)
                                .split("\\|");

                html.append("<tr>");

                for (String col : cols) {

                    html.append("<td>")
                            .append(col.trim())
                            .append("</td>");
                }

                html.append("</tr>");
            }
            else {

                if (inTable) {

                    html.append("</table>");
                    inTable = false;
                }

                html.append(line)
                        .append("\n");
            }
        }

        if (inTable) {

            html.append("</table>");
        }

        return html.toString();
    }

    private static String processParagraphs(
            String text) {

        return text.replace("\n",
                "<br/>");
    }
}