package com.dengage.sdk.notification.models;

import com.google.gson.annotations.SerializedName;

public class Open extends ModelBase {
    @SerializedName("messageId")
    private int messageId;

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    @SerializedName("messageDetails")
    private String messageDetails;

    public String getMessageDetails() {
        return this.messageDetails;
    }

    public void setMessageDetails(String messageDetails) {
        this.messageDetails = messageDetails;
    }
}
