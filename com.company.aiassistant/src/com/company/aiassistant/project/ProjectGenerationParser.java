package com.company.aiassistant.project;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses AI project generation response.
 */
public final class ProjectGenerationParser {

    private static final Pattern ACTION_PATTERN =
            Pattern.compile(
                    "PROJECT_ACTION:\\s*(.+)");

    private static final Pattern PROJECT_PATTERN =
            Pattern.compile(
                    "PROJECT_NAME:\\s*(.+)");

    private static final Pattern FOLDER_PATTERN =
            Pattern.compile(
                    "FOLDER:\\s*(.+)");

    private static final Pattern FILE_PATTERN =
            Pattern.compile(
                    "FILE:\\s*(.+)");
    
    private static final Pattern PACKAGE_PATTERN =
            Pattern.compile(
                    "PACKAGE:\\s*(.+)");

    private static final Pattern FILE_NAME_PATTERN =
            Pattern.compile(
                    "FILE_NAME:\\s*(.+)");
    
    private static final Pattern PROJECT_TYPE_PATTERN =
            Pattern.compile(
                    "PROJECT_TYPE:\\s*(.+)");

    private ProjectGenerationParser() {
    }

    public static ProjectGenerationResult parse(
            String response) {

        ProjectGenerationResult result =
                new ProjectGenerationResult();
        
        result.setRawResponse(
                response);

        try {

            if (response == null) {

                return result;
            }

            parseAction(
                    response,
                    result);

            parseProjectName(
                    response,
                    result);
            
            parseProjectType(
                    response,
                    result);

            parseFolders(
                    response,
                    result);

            parseFiles(
                    response,
                    result);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return result;
    }

    private static void parseAction(
            String response,
            ProjectGenerationResult result) {

        Matcher matcher =
                ACTION_PATTERN.matcher(
                        response);

        if (matcher.find()) {

            result.setActionType(
                    matcher.group(1).trim());
        }
    }

    private static void parseProjectName(
            String response,
            ProjectGenerationResult result) {

        Matcher matcher =
                PROJECT_PATTERN.matcher(
                        response);

        if (matcher.find()) {

            result.getProject()
                  .setProjectName(
                          matcher.group(1).trim());
        }
    }

    private static void parseFolders(
            String response,
            ProjectGenerationResult result) {

        Matcher matcher =
                FOLDER_PATTERN.matcher(
                        response);

        while (matcher.find()) {

            result.getProject()
                  .getFolders()
                  .add(
                          matcher.group(1).trim());
        }
    }

    private static void parseFiles(
            String response,
            ProjectGenerationResult result) {

        if (response.contains(
                "FILE:")) {

            parsePathBasedFiles(
                    response,
                    result);

            return;
        }

        if (response.contains(
                "PACKAGE:")
                && response.contains(
                        "FILE_NAME:")) {

            GeneratedFile file =
                    parseSingleFile(
                            response);

            if (file != null) {

                result.getProject()
                      .getFiles()
                      .add(file);
            }
        }
    }

    private static GeneratedFile parseFile(
            String section) {

        try {

            String trimmed =
                    section.trim();

            int firstNewLine =
                    trimmed.indexOf('\n');

            if (firstNewLine < 0) {

                return null;
            }

            String path =
                    trimmed.substring(
                            0,
                            firstNewLine)
                           .trim();

            String code =
                    extractCodeBlock(
                            trimmed);

            GeneratedFile file =
                    new GeneratedFile();

            file.setPath(path);

            file.setContent(code);

            return file;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    private static String extractCodeBlock(
            String text) {

        int start =
                text.indexOf("```");

        if (start < 0) {

            return "";
        }

        int firstNewLine =
                text.indexOf(
                        '\n',
                        start);

        if (firstNewLine < 0) {

            return "";
        }

        int end =
                text.indexOf(
                        "```",
                        firstNewLine);

        if (end < 0) {

            return "";
        }

        return text.substring(
                firstNewLine + 1,
                end).trim();
    }
    
    private static void parsePathBasedFiles(
            String response,
            ProjectGenerationResult result) {

        String[] sections =
                response.split(
                        "\nFILE:");

        for (int i = 1;
             i < sections.length;
             i++) {

            GeneratedFile file =
                    parseFile(
                            sections[i]);

            if (file != null) {

                result.getProject()
                      .getFiles()
                      .add(file);
            }
        }
    }
    
    private static GeneratedFile parseSingleFile(
            String response) {

        try {

            GeneratedFile file =
                    new GeneratedFile();

            Matcher packageMatcher =
                    PACKAGE_PATTERN.matcher(
                            response);

            if (packageMatcher.find()) {

                file.setPackageName(
                        packageMatcher.group(1)
                                      .trim());
            }

            Matcher fileMatcher =
                    FILE_NAME_PATTERN.matcher(
                            response);

            if (fileMatcher.find()) {

                file.setFileName(
                        fileMatcher.group(1)
                                   .trim());
            }

            file.setContent(
                    extractCodeBlock(
                            response));

            return file;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }
    
    private static void parseProjectType(
            String response,
            ProjectGenerationResult result) {

        Matcher matcher =
                PROJECT_TYPE_PATTERN.matcher(
                        response);

        if (matcher.find()) {

            result.setProjectType(
                    matcher.group(1)
                           .trim());
        }
    }
}