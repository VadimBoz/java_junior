package ru.gb.lesson5.jsonMassages;

public class SendMessage  extends AbstractRequest {

    public TypeMessage type;
    private String recipient;
    private String message;

    public SendMessage() {
        setType(TypeMessage.MESSAGE);
    }

    public SendMessage(TypeMessage type) {
        setType(type);
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
