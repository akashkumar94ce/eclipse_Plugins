package com.company.aiassistant.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts code blocks from AI responses.
 *
 * Supports:
 *
 * ```java
 * code
 * ```
 */
public final class CodeBlockExtractor {

    private static final Pattern CODE_BLOCK_PATTERN =
            Pattern.compile(
                    "```(?:\\w+)?\\s*([\\s\\S]*?)```");

    private CodeBlockExtractor() {
    }

    /**
     * Returns first code block.
     */
    public static String extractFirstCodeBlock(
            String text) {

        try {

            if (text == null) {

                return "";
            }

            Matcher matcher =
                    CODE_BLOCK_PATTERN.matcher(
                            text);

            if (matcher.find()) {

                return matcher.group(1)
                              .trim();
            }

            return "";

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    /**
     * Returns all code blocks.
     */
    public static List<String> extractAllCodeBlocks(
            String text) {

        List<String> blocks =
                new ArrayList<String>();

        try {

            if (text == null) {

                return blocks;
            }

            Matcher matcher =
                    CODE_BLOCK_PATTERN.matcher(
                            text);

            while (matcher.find()) {

                blocks.add(
                        matcher.group(1)
                               .trim());
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return blocks;
    }
    
    /**
     * Extract code block from preferred sections.
     */
    public static String extractPreferredCodeBlock(
            String response) {

        if (response == null
                || response.trim().isEmpty()) {

            return null;
        }

        String[] markers =
                {
                    "FINAL_CODE",
                    "IMPROVED_CODE",
                    "GENERATED_CODE"
                };

        for (String marker : markers) {

            String code =
                    extractCodeBlockAfterMarker(
                            response,
                            marker);

            if (code != null
                    && !code.trim().isEmpty()) {

                return code;
            }
        }

        return null;
    }
    
    private static String extractCodeBlockAfterMarker(
            String response,
            String marker) {

        int markerIndex =
                response.indexOf(marker);

        if (markerIndex < 0) {

            return null;
        }

        int codeStart =
                response.indexOf(
                        "```",
                        markerIndex);

        if (codeStart < 0) {

            return null;
        }

        int firstNewLine =
                response.indexOf(
                        '\n',
                        codeStart);

        if (firstNewLine < 0) {

            return null;
        }

        int codeEnd =
                response.indexOf(
                        "```",
                        firstNewLine);

        if (codeEnd < 0) {

            return null;
        }

        return response.substring(
                firstNewLine + 1,
                codeEnd).trim();
    }
}