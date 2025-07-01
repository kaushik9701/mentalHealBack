package com.mentalHeal.mentalHeal.DTO;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConversationResponse {
    private String reply;
    private String objectiveId;

    public ConversationResponse() {}

    public ConversationResponse(String reply, String objectiveId) {
        this.reply = reply;
        this.objectiveId = objectiveId;
    }

    @JsonProperty("reply")
    public String getReply() {
        return reply;
    }

    @JsonProperty("reply")
    public void setReply(String reply) {
        this.reply = reply;
    }

    @JsonProperty("objectiveId")
    public String getObjectiveId() {
        return objectiveId;
    }

    @JsonProperty("objectiveId")
    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }
}
