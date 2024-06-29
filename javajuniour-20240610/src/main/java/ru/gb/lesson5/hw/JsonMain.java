package ru.gb.lesson5.hw;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import ru.gb.lesson5.jsonMassages.*;

import java.io.IOException;

public class JsonMain {
  static ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) throws IOException {
//    ListResponse response = new ListResponse();

//    User user1 = new User();
//    user1.setLogin("anonymous");
//
//    User user2 = new User();
//    user2.setLogin("nagibator");
//
//    response.setUsers(List.of(user1, user2));
//
//    ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
    ObjectWriter writer = new ObjectMapper().writer();
//    String s = writer.writeValueAsString(response);
//    System.out.println(s);
//
//
//    ListRequest listRequest = new ListRequest();
//    listRequest.setType(TypeMessage.LOGIN);
//    String s1 = writer.writeValueAsString(listRequest);
//    System.out.println(s1);
//    {"type":"INFO_MESSAGE","recipient":"trh","message":"подключился trh"}
//    {"type":"INFO_MESSAGE","recipient":"user","message":"sdcsdcdcdc"}
//
//    LoginRequest loginRequest  = new LoginRequest();
//    loginRequest.setLogin("anonymous1");
//    System.out.println(writer.writeValueAsString(loginRequest));
//
    SendMessage msg = new SendMessage(TypeMessage.INFO_MESSAGE);
    msg.setMessage("sdcsdcdcdc");
    msg.setRecipient("user");
        System.out.println(writer.writeValueAsString(msg));



    String str = "{\"type\":\"MESSAGE_TO_RECIPIENT\",\"recipient\":\"u9\",\"message\":\"подключился u9\"}";
    AbstractRequest ss = objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).readValue(str, AbstractRequest.class);
    TypeMessage typeMessage = ss.getType();
    System.out.println(typeMessage);
  }

}
