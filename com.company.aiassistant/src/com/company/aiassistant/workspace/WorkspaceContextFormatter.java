package com.company.aiassistant.workspace;

/**
 * Formats workspace context for AI prompts.
 */
public final class WorkspaceContextFormatter {

    private WorkspaceContextFormatter() {
    }

    public static String format(
            WorkspaceContext context) {

        StringBuilder sb =
                new StringBuilder();

        sb.append(
                "\n===== WORKSPACE CONTEXT =====\n");

        sb.append(
                "Current Project: ");

        sb.append(
                value(
                        context.getSelectedProject()));

        sb.append(
                "\n");

        sb.append(
                "Selected Package: ");

        sb.append(
                value(
                        context.getSelectedPackage()));

        sb.append(
                "\n");

        sb.append(
                "Selected File: ");

        sb.append(
                value(
                        context.getSelectedFile()));

        sb.append(
                "\n");

        sb.append(
                "Selected Class: ");

        sb.append(
                value(
                        context.getSelectedClass()));

        sb.append(
                "\n=============================\n\n");

        return sb.toString();
    }

    private static String value(
            String value) {

        return value == null
                ? ""
                : value;
    }
}