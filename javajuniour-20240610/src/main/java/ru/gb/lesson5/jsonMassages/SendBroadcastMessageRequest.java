package ru.gb.lesson5.jsonMassages;

public class SendBroadcastMessageRequest extends AbstractRequest {
    public static final TypeMessage TYPE = TypeMessage.BROADCAST_MESSAGE;


    private String message;

    public SendBroadcastMessageRequest() {
        setType(TYPE);
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
