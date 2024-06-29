package ru.gb.lesson5.jsonMassages;

public enum TypeMessage {
    MESSAGE ("message"),
    MESSAGE_REQUEST ("requestMessage"),
    ERROR_MESSAGE ("errorMessage"),
    INFO_MESSAGE ("infoMessage"),
    BROADCAST_MESSAGE ("broadcastMessage"),
    MESSAGE_TO_RECIPIENT ("messageToRcipient"),
    LOGIN ("loginMessage"),
    LIST_USERS ("listUsersMessage"),
    DISCONNECT_MESSAGE ("disconnectMessage"),
    MESSAGE_FROM_USER_TO_RECIPIENT ("messageFromUserToRecipient");


    private final String type;
    private TypeMessage(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
