package com.company.aiassistant.workspace;

import com.company.aiassistant.util.EditorUtil;

/**
 * Executes file modifications.
 */
public final class FileModificationExecutor {

    private FileModificationExecutor() {
    }

    public static void addMethod(
            WorkspaceModificationResult result)
            throws Exception {

        String editorText =
                EditorUtil.getCurrentEditorText();

        if (editorText == null
                || editorText.trim().isEmpty()) {

            throw new Exception(
                    "No active editor found.");
        }

        String methodCode =
                result.getCode();

        if (methodCode == null
                || methodCode.trim().isEmpty()) {

            throw new Exception(
                    "No method code found.");
        }

        int lastBrace =
                editorText.lastIndexOf("}");

        if (lastBrace < 0) {

            throw new Exception(
                    "Unable to find class closing brace.");
        }

        StringBuilder updated =
                new StringBuilder();

        updated.append(
                editorText.substring(
                        0,
                        lastBrace));

        updated.append("\n\n");

        updated.append(methodCode);

        updated.append("\n");

        updated.append(
                editorText.substring(
                        lastBrace));

        boolean success =
                EditorUtil
                        .replaceEditorContent(
                                updated.toString());

        if (!success) {

            throw new Exception(
                    "Failed to update editor.");
        }
    }
}