package com.company.aiassistant.ai;

public class AIRequest {

    private AIActionType actionType;

    private String prompt;

    private String selectedCode;

    public AIActionType getActionType() {
        return actionType;
    }

    public void setActionType(AIActionType actionType) {
        this.actionType = actionType;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getSelectedCode() {
        return selectedCode;
    }

    public void setSelectedCode(String selectedCode) {
        this.selectedCode = selectedCode;
    }
}