package com.company.aiassistant.context;

import java.util.List;

/**
 * Builds prompt context block.
 */
public final class ContextBuilder {

    private ContextBuilder() {
    }

    public static String buildContext(
            CodeContext context) {

        StringBuilder builder =
                new StringBuilder();

        try {

            appendFileSection(
                    builder,
                    context);

            appendJavaSection(
                    builder,
                    context);

            appendSelectedCodeSection(
                    builder,
                    context);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return builder.toString();
    }

    private static void appendFileSection(
            StringBuilder builder,
            CodeContext context) {

        builder.append(
                "===== FILE =====\n\n");

        builder.append(
                "Name: ")
               .append(
                       safe(
                               context.getFileName()))
               .append(
                       "\n");

        builder.append(
                "Extension: ")
               .append(
                       safe(
                               context.getFileExtension()))
               .append(
                       "\n\n");
    }

    private static void appendJavaSection(
            StringBuilder builder,
            CodeContext context) {

        builder.append(
                "===== JAVA CONTEXT =====\n\n");

        builder.append(
                "Package: ")
               .append(
                       safe(
                               context.getPackageName()))
               .append(
                       "\n");

        builder.append(
                "Class: ")
               .append(
                       safe(
                               context.getClassName()))
               .append(
                       "\n");
        
        builder.append(
                "Type: ")
               .append(
                       safe(
                               context.getTypeName()))
               .append(
                       "\n");

        builder.append(
                "Method: ")
               .append(
                       safe(
                               context.getMethodName()))
               .append(
                       "\n\n");

        builder.append(
                "Imports:\n");

        List<String> imports =
                context.getImports();

        if (imports != null) {

            for (String value
                    : imports) {

                builder.append(" - ")
                       .append(value)
                       .append("\n");
            }
        }

        builder.append("\n");
    }

    private static void appendSelectedCodeSection(
            StringBuilder builder,
            CodeContext context) {

      /*  builder.append(
                "===== SELECTED CODE =====\n\n");

        builder.append(
                safe(
                        context.getSelectedText()));

        builder.append(
                "\n\n");*/
    	
    	builder.append(
    	        "===== CURRENT SOURCE CONTEXT =====\n\n");
    }

    private static String safe(
            String value) {

        if (value == null) {

            return "";
        }

        return value;
    }
}