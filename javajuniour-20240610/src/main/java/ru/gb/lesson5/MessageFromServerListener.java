package ru.gb.lesson5;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.lesson5.jsonMassages.AbstractRequest;
import ru.gb.lesson5.jsonMassages.ListUsersResponse;
import ru.gb.lesson5.jsonMassages.SendMessage;
import ru.gb.lesson5.jsonMassages.TypeMessage;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MessageFromServerListener implements Runnable {

    public boolean connected = true;
    private Scanner in;
    private ObjectMapper objectMapper;

    public MessageFromServerListener(Scanner in, ObjectMapper objectMapper) {
        this.in = in;
        this.objectMapper = objectMapper;
    }


    public void setConnected(boolean connected)  {
        this.connected = connected;

    }
    @Override
    public void run() {
        TypeMessage type ;
        while (connected) {

            // TODO: парсим сообщение в AbstractRequest
            //  по полю type понимаем, что это за request, и обрабатываем его нужным образом
            String msgFromServer = in.nextLine();
            try {
                AbstractRequest answer = objectMapper.
                        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).
                        reader().
                        readValue(msgFromServer, AbstractRequest.class);
                type = answer.getType();
            } catch (IOException e) {
                System.err.println("Не удалось прочитать сообщение от сервера  " + e.getMessage());
                continue;
            }

            SendMessage receiveMsg;
            ListUsersResponse listUsersResponse;
            try {
                if (type.equals(TypeMessage.MESSAGE_TO_RECIPIENT)) {
                    receiveMsg  = objectMapper.reader().readValue(msgFromServer, SendMessage.class);
                    System.out.println("Сообщение от сервера: сообщение от пользователя " + receiveMsg.getRecipient()
                            + " " + receiveMsg.getMessage());
                } else if (type.equals(TypeMessage.INFO_MESSAGE)) {
                    receiveMsg  = objectMapper.reader().readValue(msgFromServer, SendMessage.class);
                    System.out.println("Сообщение от сервера:" + receiveMsg.getMessage());
                } else if (type.equals(TypeMessage.ERROR_MESSAGE)) {
                    receiveMsg  = objectMapper.reader().readValue(msgFromServer, SendMessage.class);
                    System.out.println("ERROR!!!" + receiveMsg.getMessage());
                } else if (type.equals(TypeMessage.LIST_USERS)) {
                    listUsersResponse = objectMapper.reader().readValue(msgFromServer, ListUsersResponse.class);
                    List<User> userList = listUsersResponse.getUsers();
                    System.out.println("Список пользователей:");
                    userList.forEach(System.out::println);
                } else {
                    System.out.println("сообщение от сервера нераспознано" + msgFromServer);
                    System.out.println();
                }
            } catch (IOException e) {
                System.out.println("неопознанный тип сообщения от сервера");
            }

        }


    }



}
