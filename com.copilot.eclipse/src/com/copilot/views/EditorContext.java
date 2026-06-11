package com.copilot.views;

public class EditorContext {
    private String projectName;
    private String fileName;
    private String fileType;
    private String containerPath;
    private String selectedText;
    private String fullFileContent;
    private String projectSkeleton; // NEW FIELD

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getContainerPath() { return containerPath; }
    public void setContainerPath(String containerPath) { this.containerPath = containerPath; }

    public String getSelectedText() { return selectedText; }
    public void setSelectedText(String selectedText) { this.selectedText = selectedText; }

    public String getFullFileContent() { return fullFileContent; }
    public void setFullFileContent(String fullFileContent) { this.fullFileContent = fullFileContent; }

    public String getProjectSkeleton() { return projectSkeleton; }
    public void setProjectSkeleton(String projectSkeleton) { this.projectSkeleton = projectSkeleton; }
}