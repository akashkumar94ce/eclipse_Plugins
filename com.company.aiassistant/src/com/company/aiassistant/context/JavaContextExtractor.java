package com.company.aiassistant.context;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts Java metadata from source.
 */
public final class JavaContextExtractor {

    private static final Pattern PACKAGE_PATTERN =
            Pattern.compile(
                    "package\\s+([^;]+);");

    private static final Pattern IMPORT_PATTERN =
            Pattern.compile(
                    "import\\s+([^;]+);");

    /*private static final Pattern CLASS_PATTERN =
            Pattern.compile(
                    "(?:public\\s+)?(?:final\\s+)?(?:abstract\\s+)?class\\s+(\\w+)");
*/
    private static final Pattern TYPE_PATTERN =
            Pattern.compile(
                    "(?:public\\s+)?(?:final\\s+)?(?:abstract\\s+)?"
                            + "(class|interface|enum)\\s+(\\w+)");
    private static final Pattern METHOD_PATTERN =
            Pattern.compile(
                    "(?:public|private|protected)?\\s*" +
                    "(?:static\\s+)?\\s*" +
                    "[\\w<>\\[\\]]+\\s+" +
                    "(\\w+)\\s*\\(");

    private JavaContextExtractor() {
    }

    public static void enrich(
            CodeContext context) {

        try {

            String source =
                    context.getFullFileText();

            if (source == null) {

                return;
            }

            extractPackage(
                    context,
                    source);

            extractImports(
                    context,
                    source);

           /* extractClassName(
                    context,
                    source);*/
            
            extractTypeInfo(
                    context,
                    source);

            extractMethodName(
                    context,
                    source);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void extractPackage(
            CodeContext context,
            String source) {

        Matcher matcher =
                PACKAGE_PATTERN.matcher(
                        source);

        if (matcher.find()) {

            context.setPackageName(
                    matcher.group(1));
        }
    }

    private static void extractImports(
            CodeContext context,
            String source) {

        List<String> imports =
                new ArrayList<String>();

        Matcher matcher =
                IMPORT_PATTERN.matcher(
                        source);

        while (matcher.find()) {

            imports.add(
                    matcher.group(1));
        }

        context.setImports(
                imports);
    }

    /*private static void extractClassName(
            CodeContext context,
            String source) {

        Matcher matcher =
                CLASS_PATTERN.matcher(
                        source);

        if (matcher.find()) {

            context.setClassName(
                    matcher.group(1));
        }
    }*/
    
    private static void extractTypeInfo(
            CodeContext context,
            String source) {

        Matcher matcher =
                TYPE_PATTERN.matcher(
                        source);

        if (matcher.find()) {

            context.setTypeName(
                    matcher.group(1));

            context.setClassName(
                    matcher.group(2));
        }
    }

    /**
     * V1 heuristic.
     *
     * Finds first method.
     * We'll improve later.
     */
    private static void extractMethodName(
            CodeContext context,
            String source) {

        Matcher matcher =
                METHOD_PATTERN.matcher(
                        source);

        if (matcher.find()) {

            context.setMethodName(
                    matcher.group(1));
        }
    }
}