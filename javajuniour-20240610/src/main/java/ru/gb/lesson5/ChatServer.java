package ru.gb.lesson5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.lesson5.jsonMassages.AbstractRequest;
import ru.gb.lesson5.jsonMassages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {

  private final static ObjectMapper objectMapper = new ObjectMapper();

  // Socket - абстракция, к которой можно подключиться
  // ip-address + port - socket
  // network - сеть - набор соединенных устройств
  // ip-address - это адрес устройства в какой-то сети
  // 8080 - http
  // 443 - https
  // 35 - smtp
  // 21 - ftp
  // 5432 - стандартный порт postgres
  // клиент подключается к серверу

  /**
   * Порядок взаимодействия:
   * 1. Клиент подключается к серверу
   * 2. Клиент посылает сообщение, в котором указан логин. Если на сервере уже есть подключеный клиент с таким логином, то соедение разрывается
   * 3. Клиент может посылать 3 типа команд:
   * 3.1 list - получить логины других клиентов
   * <p>
   * 3.2 send @login message - отправить личное сообщение с содержимым message другому клиенту с логином login
   * 3.3 send message - отправить сообщение всем с содержимым message
   */

  // 1324.132.12.3:8888
  public static void main(String[] args) {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    Map<String, ClientHandler> clients = new ConcurrentHashMap<>();
    try (ServerSocket server = new ServerSocket(8888)) {
      System.out.println("Сервер запущен");

      while (true) {
        System.out.println("Ждем клиентского подключения");
        Socket client = server.accept();
        ClientHandler clientHandler = new ClientHandler(client, clients);
        new Thread(clientHandler).start();
      }
    } catch (IOException e) {
      System.err.println("Ошибка во время работы сервера: " + e.getMessage());
    }
  }

  private static class ClientHandler implements Runnable {

    private final Socket client;
    private final Scanner in;
    private final PrintWriter out;
    private final Map<String, ClientHandler> clients;
    private String clientLogin;

    public ClientHandler(Socket client, Map<String, ClientHandler> clients) throws IOException {
      this.client = client;
      this.clients = clients;

      this.in = new Scanner(client.getInputStream());
      this.out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
      System.out.println("Подключен новый клиент");
      try {
        String loginRequest = in.nextLine();
        System.out.println(loginRequest);
        LoginRequest request = objectMapper.reader().readValue(loginRequest, LoginRequest.class);
        this.clientLogin = request.getLogin();
      } catch (IOException e) {
        System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
        String unsuccessfulResponse = createLoginResponse(false);
        out.println(unsuccessfulResponse);
        try {
          sendMessage("Не удалось прочитать сообщение от клиента", TypeMessage.ERROR_MESSAGE, "none");
        } catch (JsonProcessingException ex) {
          System.out.println("сообщение не отправлено клиенту");
        }
        doClose();
        return;
      }

      System.out.println("Запрос от клиента: " + clientLogin);

      // Проверка, что логин не занят
      if (clients.containsKey(clientLogin)) {
        String unsuccessfulResponse = createLoginResponse(false);
        out.println(unsuccessfulResponse);
        try {
          sendMessage("логин занят другим пользователем", TypeMessage.ERROR_MESSAGE, "none");
        } catch (JsonProcessingException ex) {
          System.out.println("сообщение не отправлено клиенту");
        }
        doClose();
        return;
      }

      clients.put(clientLogin, this);
      String successfulLoginResponse = createLoginResponse(true);
      out.println(successfulLoginResponse);

      for (Map.Entry<String, ClientHandler>  entry : clients.entrySet()) {
        ClientHandler clientTo  = entry.getValue();
        String loginRecipients = clientTo.clientLogin;
          try {
            clientTo.sendMessage("подключился " + clientLogin, TypeMessage.INFO_MESSAGE, loginRecipients);
          } catch (JsonProcessingException e) {
            System.out.println("не удалось отправить сообщение для" + clientLogin);
          }
      }

      while (true) {
        String msgFromClient = in.nextLine();
        System.out.println(msgFromClient);

        TypeMessage type;
        try {
          AbstractRequest request = objectMapper.reader().readValue(msgFromClient, AbstractRequest.class);
          type = request.getType();
        } catch (IOException e) {
          System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
//          sendMessage("Не удалось прочитать сообщение: " + e.getMessage());
          try {
            sendMessage("Не удалось прочитать сообщение: " + e.getMessage(), TypeMessage.INFO_MESSAGE, clientLogin);
          } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
          }
          continue;
        }

        if (type.equals(TypeMessage.MESSAGE_TO_RECIPIENT)) {

          final SendMessage request;
          try {
            request = objectMapper.reader().readValue(msgFromClient, SendMessage.class);
          } catch (IOException e) {
            System.err.println("Не удалось прочитать сообщение от клиента [" + clientLogin + "]: " + e.getMessage());
            sendMessage("Не удалось прочитать сообщение SendMessageRequest: " + e.getMessage());
            continue;
          }

          ClientHandler clientTo = clients.get(request.getRecipient());
          if (clientTo == null) {
            try {
             sendMessage("Клиент с логином [" + request.getRecipient() + "] не найден", TypeMessage.ERROR_MESSAGE, clientLogin);
            } catch (JsonProcessingException e) {
              System.out.println("Клиент с логином [" + request.getRecipient() + "] не найден, сообщение не отправлено");
            }
            System.out.println("Клиент с логином [" + request.getRecipient() + "] не найден");
            continue;
          }

          try {
            clientTo.sendMessage(request.getMessage(), TypeMessage.MESSAGE_TO_RECIPIENT, this.clientLogin);
          } catch (JsonProcessingException e) {
            System.out.println("ошибка в методе sendMessage");
          }


        } else if (type.equals(TypeMessage.BROADCAST_MESSAGE)) { // BroadcastRequest.TYPE.equals(type)
          // TODO: Читать остальные типы сообщений
          final SendMessageRequest request;
          try {
            request = objectMapper.reader().readValue(msgFromClient, SendMessageRequest.class);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }

          String msgBroadcast = request.getMessage();
          String loginSender  = this.clientLogin;
          for (Map.Entry<String, ClientHandler>  entry : clients.entrySet()) {
                ClientHandler clientTo  = entry.getValue();
                String loginRecipients = clientTo.clientLogin;
                if (loginRecipients != this.clientLogin) {
                  try {
                    clientTo.sendMessage(msgBroadcast, TypeMessage.MESSAGE_TO_RECIPIENT, loginSender);
                  } catch (JsonProcessingException e) {
                    System.out.println("не удалось отправить сообщение для" + loginRecipients);
                  }
                }
          }

        } else if (type.equals(TypeMessage.LIST_USERS)) {
          List<User> listUsers  = new ArrayList<>();
          for (Map.Entry<String, ClientHandler>  entry : clients.entrySet()) {
            ClientHandler clientTo  = entry.getValue();
            User curUser = new User();
            curUser.setLogin(clientTo.clientLogin);
            listUsers.add(curUser);
          }

          ListUsersResponse listUsersResponse  = new ListUsersResponse();
          listUsersResponse.setType(TypeMessage.LIST_USERS);
          listUsersResponse.setUsers(listUsers);
          try {
            String response  = objectMapper.writer().writeValueAsString(listUsersResponse);
            out.println(response);
          } catch (JsonProcessingException e) {
            System.out.println("ошибка парсинга листа юзеров");
          }


        } else if (type.equals(TypeMessage.DISCONNECT_MESSAGE)) {
          SendDisconnectionRequest dr;
          try {
            dr = objectMapper.reader().readValue(msgFromClient, SendDisconnectionRequest.class);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
          System.out.println("пользователь  " + dr.getLogin() + " сделал запрос на отключение");
          clients.remove(dr.getLogin());
          for (Map.Entry<String, ClientHandler>  entry : clients.entrySet()) {
            ClientHandler clientTo  = entry.getValue();
            String loginRecipients = clientTo.clientLogin;
              try {
                clientTo.sendMessage("пользователь отключился " + clientLogin, TypeMessage.INFO_MESSAGE, loginRecipients);
              } catch (JsonProcessingException e) {
                System.out.println("не удалось отправить сообщение для" + clientLogin);
              }
          }

          doClose();
          break;

        } else {
          System.err.println("Неизвестный тип сообщения: " + type);
          try {
            sendMessage("Неизвестный тип сообщения: " + type, TypeMessage.ERROR_MESSAGE, clientLogin);
          } catch (JsonProcessingException e) {
            System.err.println("не удалось отправить сообщение об ошибке");
          }
          continue;
        }
      }
      doClose();
    }

    private void doClose() {
      try {
        in.close();
        out.close();
        client.close();
      } catch (IOException e) {
        System.err.println("Ошибка во время отключения клиента: " + e.getMessage());
      }
    }

    public void sendMessage(String message, TypeMessage type, String recipientLogin) throws JsonProcessingException {
      if (type == null) {
        out.println(message);
      } else if (type.equals(TypeMessage.MESSAGE_TO_RECIPIENT)) {
        SendMessage sendMessageResponse = new SendMessage(TypeMessage.MESSAGE_TO_RECIPIENT);
        sendMessageResponse.setRecipient(recipientLogin);
        sendMessageResponse.setMessage(message);
        String sendMsg = objectMapper.writeValueAsString(sendMessageResponse);
        out.println(sendMsg);

      } else if (type.equals(TypeMessage.INFO_MESSAGE)) {
        SendMessage sendMessageResponse = new SendMessage(TypeMessage.INFO_MESSAGE);
        sendMessageResponse.setRecipient(recipientLogin);
        sendMessageResponse.setMessage(message);
        String sendMsg = objectMapper.writeValueAsString(sendMessageResponse);
        out.println(sendMsg);

      } else if (type.equals(TypeMessage.ERROR_MESSAGE)) {
        SendMessage sendMessageResponse = new SendMessage(TypeMessage.ERROR_MESSAGE);
        sendMessageResponse.setRecipient(recipientLogin);
        sendMessageResponse.setMessage(message);
        String sendMsg = objectMapper.writeValueAsString(sendMessageResponse);
        out.println(sendMsg);

      }
    }

    public void sendMessage(String message) {
        out.println(message);
    }


    private String createLoginResponse(boolean success) {
      LoginResponse loginResponse = new LoginResponse();
      loginResponse.setConnected(success);
      try {
        return objectMapper.writer().writeValueAsString(loginResponse);
      } catch (JsonProcessingException e) {
        throw new RuntimeException("Не удалось создать loginResponse: " + e.getMessage());
      }
    }



      private static SendMessage readSendMessage(String message)  {
    SendMessage msg = new SendMessage();
    try {
      objectMapper.reader().readValue(message, SendMessage.class);
    } catch (IOException e) {
      System.err.println("Ошибка чтения JSON: " + e.getMessage());
    }
    return msg;
  }

  }

}
