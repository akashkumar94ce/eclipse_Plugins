package com.company.aiassistant.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents generated project.
 */
public class GeneratedProject {

    private String projectName;

    private List<String> folders =
            new ArrayList<String>();

    private List<GeneratedFile> files =
            new ArrayList<GeneratedFile>();

    public String getProjectName() {

        return projectName;
    }

    public void setProjectName(
            String projectName) {

        this.projectName =
                projectName;
    }

    public List<String> getFolders() {

        return folders;
    }

    public List<GeneratedFile> getFiles() {

        return files;
    }
}