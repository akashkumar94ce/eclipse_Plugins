package com.company.aiassistant.context;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds editor and source-code context
 * used when building AI prompts.
 */
public class CodeContext {

    private String fileName;

    private String fileExtension;

    private String selectedText;

    private String fullFileText;

    private String packageName;

    private String className;
    
    private String typeName;

    private String methodName;

    private List<String> imports =
            new ArrayList<String>();

    public String getFileName() {

        return fileName;
    }

    public void setFileName(
            String fileName) {

        this.fileName = fileName;
    }

    public String getFileExtension() {

        return fileExtension;
    }

    public void setFileExtension(
            String fileExtension) {

        this.fileExtension =
                fileExtension;
    }

    public String getSelectedText() {

        return selectedText;
    }

    public void setSelectedText(
            String selectedText) {

        this.selectedText =
                selectedText;
    }

    public String getFullFileText() {

        return fullFileText;
    }

    public void setFullFileText(
            String fullFileText) {

        this.fullFileText =
                fullFileText;
    }

    public String getPackageName() {

        return packageName;
    }

    public void setPackageName(
            String packageName) {

        this.packageName =
                packageName;
    }

    public String getClassName() {

        return className;
    }

    public void setClassName(
            String className) {

        this.className =
                className;
    }

    public String getMethodName() {

        return methodName;
    }

    public void setMethodName(
            String methodName) {

        this.methodName =
                methodName;
    }

    public List<String> getImports() {

        return imports;
    }

    public void setImports(
            List<String> imports) {

        this.imports =
                imports;
    }
    
    public String getTypeName() {

        return typeName;
    }
    
    public void setTypeName(
            String typeName) {

        this.typeName =
                typeName;
    }
}