package com.company.aiassistant.workspace;

/**
 * Current workspace selection context.
 */
public class WorkspaceContext {

    private String selectedProject;

    private String selectedPackage;

    private String selectedFile;

    private String selectedClass;

    public String getSelectedProject() {

        return selectedProject;
    }

    public void setSelectedProject(
            String selectedProject) {

        this.selectedProject =
                selectedProject;
    }

    public String getSelectedPackage() {

        return selectedPackage;
    }

    public void setSelectedPackage(
            String selectedPackage) {

        this.selectedPackage =
                selectedPackage;
    }

    public String getSelectedFile() {

        return selectedFile;
    }

    public void setSelectedFile(
            String selectedFile) {

        this.selectedFile =
                selectedFile;
    }

    public String getSelectedClass() {

        return selectedClass;
    }

    public void setSelectedClass(
            String selectedClass) {

        this.selectedClass =
                selectedClass;
    }
}