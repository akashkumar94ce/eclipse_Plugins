package com.company.aiassistant.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses multi-file AI responses.
 */
public final class MultiFileResponseParser {

    private MultiFileResponseParser() {
    }

    public static List<GeneratedFile> parse(
            String response) {

        List<GeneratedFile> files =
                new ArrayList<GeneratedFile>();

        try {

            if (response == null) {

                return files;
            }

            String[] sections =
                    response.split(
                            "FILE_START");

            for (int i = 1;
                 i < sections.length;
                 i++) {

                GeneratedFile file =
                        parseFile(
                                sections[i]);

                if (file != null) {

                    files.add(file);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return files;
    }

    private static GeneratedFile parseFile(
            String section) {

        try {

            int endIndex =
                    section.indexOf(
                            "FILE_END");

            if (endIndex > 0) {

                section =
                        section.substring(
                                0,
                                endIndex);
            }

            GeneratedFile file =
                    new GeneratedFile();

            String path =
                    extractValue(
                            section,
                            "PATH:");

            String fileType =
                    extractValue(
                            section,
                            "FILE_TYPE:");

            file.setPath(
                    path);

            file.setFileType(
                    fileType);

            String content =
                    extractContent(
                            section);

            file.setContent(
                    content);

            return file;

        } catch (Exception e) {

            e.printStackTrace();

            return null;
        }
    }

    private static String extractValue(
            String text,
            String key) {

        int start =
                text.indexOf(
                        key);

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

    private static String extractContent(
            String section) {

        int contentStart =
                section.indexOf(
                        "\n\n");

        if (contentStart < 0) {

            return "";
        }

        return section.substring(
                contentStart + 2)
                .trim();
    }
}