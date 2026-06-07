package com.company.aiassistant.context;

import com.company.aiassistant.util.EditorUtil;

/**
 * Extracts context from current editor.
 */
public final class ContextExtractor {

    private ContextExtractor() {
    }

    /**
     * Extract context from active editor.
     */
    public static CodeContext extract() {

        CodeContext context =
                new CodeContext();

        try {

            context.setFileName(
                    EditorUtil.getCurrentFileName());

            context.setFileExtension(
                    EditorUtil.getCurrentFileExtension());

            context.setSelectedText(
                    EditorUtil.getSelectedText());

            context.setFullFileText(
                    EditorUtil.getCurrentEditorText());

            enrichLanguageSpecificContext(
                    context);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return context;
    }

    /**
     * Language-specific extraction.
     */
    private static void enrichLanguageSpecificContext(
            CodeContext context) {

        String extension =
                context.getFileExtension();

        if (extension == null) {

            return;
        }

        if ("java".equalsIgnoreCase(
                extension)) {

            JavaContextExtractor.enrich(
                    context);
        }
    }
}