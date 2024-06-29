package ru.gb.lesson5.jsonMassages;

public class SendDisconnectionRequest {

    public  TypeMessage type = TypeMessage.DISCONNECT_MESSAGE;
    private String login;
    private String message;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login_dgdg) {

        this.login = login_dgdg;
    }

    public TypeMessage getType() {
        return type;
    }

    public  void setTYPE(TypeMessage type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
