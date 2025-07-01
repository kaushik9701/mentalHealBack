// ConversationRequest.java
package com.mentalHeal.mentalHeal.DTO;

import java.util.List;

public class ConversationRequest {
    private String email;
    private String message;
    private List<ConversationMessage> history;
    private String objectiveId; // Add this

    public ConversationRequest() {}

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<ConversationMessage> getHistory() {
        return history;
    }
    public void setHistory(List<ConversationMessage> history) {
        this.history = history;
    }
    public String getObjectiveId() {
        return objectiveId;
    }
    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }
}
