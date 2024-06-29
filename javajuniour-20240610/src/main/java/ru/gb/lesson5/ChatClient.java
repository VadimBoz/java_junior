package ru.gb.lesson5;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.gb.lesson5.jsonMassages.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.util.*;

public class ChatClient {

//  private static String clientLogin;

  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static PrintWriter out;


  public static void main(String[] args) {
    System.out.println("Input login:");
    Scanner console = new Scanner(System.in);
    String clientLogin = console.nextLine();

    try (Socket server = new Socket("localhost", 8888)) {
      System.out.println("the connection is established");
      out = new PrintWriter(server.getOutputStream(), true);

      Scanner in = new Scanner(server.getInputStream());
      String loginRequest = createLoginRequest(clientLogin);
      out.println(loginRequest);

      String loginResponseString = in.nextLine();
      if (!checkLoginResponse(loginResponseString)) {
        // TODO: Можно обогатить причиной, чтобы клиент получал эту причину
        // (логин уже занят, ошибка аутентификации\авторизации, ...)
        System.out.println("Не удалось подключиться к серверу");
        String msgFromServer =  in.nextLine();
        SendMessage msgFromServer2 = objectMapper.reader().readValue(msgFromServer, SendMessage.class);
        TypeMessage typeMessage = msgFromServer2.getType();
        if (typeMessage.equals(TypeMessage.ERROR_MESSAGE)) {
          System.out.println("ERROR!!!" + msgFromServer2.getMessage());
        }
        return;
      }

      MessageFromServerListener messageFromServerListener = new MessageFromServerListener(in, objectMapper);
      new Thread(messageFromServerListener, "messageFromServerListener").start();


      while (true) {
        System.out.println();
        System.out.println("Что хочу сделать?");
        System.out.println("1. Послать сообщение другу");
        System.out.println("2. Послать сообщение всем");
        System.out.println("3. Получить список логинов");
        System.out.println("4. Завершить сеанс");

        String type = console.nextLine();
        if (type.equals("1")) {
          // TODO: считываете с консоли логин, кому отправить
          SendMessage request = new SendMessage(TypeMessage.MESSAGE_TO_RECIPIENT);
          System.out.println("input login recipient: ");
          request.setRecipient(console.nextLine());
          System.out.println("input message: ");
          request.setMessage(console.nextLine());

          String sendMsgRequest = objectMapper.writeValueAsString(request);
          out.println(sendMsgRequest);

        } else if (type.equals("2")) {
          SendMessage request = new SendMessage(TypeMessage.BROADCAST_MESSAGE);
          System.out.println("input message: ");
          request.setMessage(console.nextLine());
          String sendMsgRequest = objectMapper.writeValueAsString(request);
          out.println(sendMsgRequest);

        } else if (type.equals("3")) {
          SendMessage request = new SendMessage(TypeMessage.LIST_USERS);
          request.setRecipient(clientLogin);
          request.setMessage("none");
          String sendMsgRequest = objectMapper.writeValueAsString(request);
          System.out.println(sendMsgRequest);
          out.println(sendMsgRequest);

        } else if (type.equals("4")) {
          disconnectionMessage(out, clientLogin);
          messageFromServerListener.setConnected(false);
          in.close();
          out.close();
          console.close();
          break;
// TODO ме могу избавиться от ошибки  в этом методе или после непонятно
//          Exception in thread "messageFromServerListener" java.util.NoSuchElementException: No line found
//          at java.base/java.util.Scanner.nextLine(Scanner.java:1677)
//          at ru.gb.lesson5.jsonMassages.MessageFromServerListener.run(MessageFromServerListener.java:32)
//          at java.base/java.lang.Thread.run(Thread.java:1570)

        }
      }
    } catch (IOException e) {
      System.err.println("Ошибка во время подключения к серверу: " + e.getMessage());
    }

    System.out.println("Отключились от сервера");

  }
  private static void disconnectionMessage(PrintWriter out, String clientLogin) throws JsonProcessingException {
    SendDisconnectionRequest sendDisconnectionRequest = new SendDisconnectionRequest();
    sendDisconnectionRequest.setLogin(clientLogin);
    String sendMsgRequest = objectMapper.writeValueAsString(sendDisconnectionRequest);
    out.println(sendMsgRequest);
  }

  private static String createLoginRequest(String login) {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest.setLogin(login);

    try {
      return objectMapper.writeValueAsString(loginRequest);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Ошибка JSON: " + e.getMessage());
    }
  }

  private static boolean checkLoginResponse(String loginResponse) {
    try {
      LoginResponse resp = objectMapper.reader().readValue(loginResponse, LoginResponse.class);
      return resp.isConnected();
    } catch (IOException e) {
      System.err.println("Ошибка чтения JSON: " + e.getMessage());
      return false;
    }
  }



  private static void sleep() {
    try {
      Thread.sleep(Duration.ofMinutes(5));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }


}
