package com.company.aiassistant.workspace;

import com.company.aiassistant.util.CodeBlockExtractor;

/**
 * Parses MODIFY_FILE responses.
 */
public final class WorkspaceModificationParser {

    private WorkspaceModificationParser() {
    }

    public static WorkspaceModificationResult parse(
            String response) {

        WorkspaceModificationResult result =
                new WorkspaceModificationResult();

        result.setTargetFile(
                extractValue(
                        response,
                        "TARGET_FILE:"));

        result.setModificationType(
                extractValue(
                        response,
                        "MODIFICATION_TYPE:"));

        result.setMethodSignature(
                extractValue(
                        response,
                        "METHOD_SIGNATURE:"));

        result.setCode(
                CodeBlockExtractor
                        .extractFirstCodeBlock(
                                response));

        return result;
    }

    private static String extractValue(
            String text,
            String key) {

        int start =
                text.indexOf(
                        key);

        if (text == null
                || key == null) {

            return "";
        }

        if (start < 0) {

            return "";
        }

        start += key.length();

        int end =
                text.indexOf(
                        '\n',
                        start);

        if (end < 0) {

            end =
                    text.length();
        }

        return text.substring(
                start,
                end)
                .trim();
    }
}