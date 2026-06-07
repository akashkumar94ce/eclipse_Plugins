package com.company.aiassistant.project;

import java.util.ArrayList;
import java.util.List;

/**
 * Workspace execution result.
 */
public class WorkspaceActionResult {

    private boolean success;

    private String message;

    private List<String> createdFiles =
            new ArrayList<String>();

    public boolean isSuccess() {

        return success;
    }

    public void setSuccess(
            boolean success) {

        this.success =
                success;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(
            String message) {

        this.message =
                message;
    }

    public List<String> getCreatedFiles() {

        return createdFiles;
    }
}