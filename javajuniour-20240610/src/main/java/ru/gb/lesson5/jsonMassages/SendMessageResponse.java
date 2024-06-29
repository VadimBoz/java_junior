package ru.gb.lesson5.jsonMassages;

public class SendMessageResponse extends AbstractRequest {
    public static TypeMessage TYPE = TypeMessage.MESSAGE_FROM_USER_TO_RECIPIENT;
    private String sender;
    private String message;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
