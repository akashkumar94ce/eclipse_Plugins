package com.company.aiassistant.workspace;

/**
 * Parsed workspace modification response.
 */
public class WorkspaceModificationResult {

    private String targetFile;

    private String modificationType;

    private String methodSignature;

    private String code;

    public String getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(
            String targetFile) {

        this.targetFile =
                targetFile;
    }

    public String getModificationType() {
        return modificationType;
    }

    public void setModificationType(
            String modificationType) {

        this.modificationType =
                modificationType;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public void setMethodSignature(
            String methodSignature) {

        this.methodSignature =
                methodSignature;
    }

    public String getCode() {
        return code;
    }

    public void setCode(
            String code) {

        this.code =
                code;
    }
}