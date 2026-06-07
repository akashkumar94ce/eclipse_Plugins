package com.company.aiassistant.project;

/**
 * Represents a generated file.
 */
public class GeneratedFile {

    private String path;
    private String packageName;
    private String fileType;
    private String fileName;
    private String content;

    public String getPath() {

        return path;
    }

    public void setPath(
            String path) {

        this.path = path;
    }

    public String getContent() {

        return content;
    }

    public void setContent(
            String content) {

        this.content = content;
    }

    @Override
    public String toString() {

        return path;
    }
    
    public String getPackageName() {

        return packageName;
    }

    public void setPackageName(
            String packageName) {

        this.packageName =
                packageName;
    }

    public String getFileName() {

        return fileName;
    }

    public void setFileName(
            String fileName) {

        this.fileName =
                fileName;
    }
    
    public String getFileType() {

        return fileType;
    }
    
    public void setFileType(
            String fileType) {

        this.fileType =
                fileType;
    }
    
}