package com.company.aiassistant.project;

/**
 * Parsed project generation result.
 */
public class ProjectGenerationResult {

    public static final String ACTION_CREATE_PROJECT =
            "CREATE_PROJECT";

    public static final String ACTION_CREATE_FILES =
            "CREATE_FILES";

    public static final String ACTION_CREATE_FILE =
            "CREATE_FILE";
    
    

    private String actionType;
    private String rawResponse;
    
    private String projectType;

    private GeneratedProject project =
            new GeneratedProject();

    public String getActionType() {

        return actionType;
    }

    public void setActionType(
            String actionType) {

        this.actionType =
                actionType;
    }

    public GeneratedProject getProject() {

        return project;
    }

    public void setProject(
            GeneratedProject project) {

        this.project =
                project;
    }
    
    public String getRawResponse() {

        return rawResponse;
    }
    
    public void setRawResponse(
            String rawResponse) {

        this.rawResponse =
                rawResponse;
    }
    
    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(
            String projectType) {
        this.projectType = projectType;
    }
}