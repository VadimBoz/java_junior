package ru.gb.lesson5;

public class User {

  private String login;
  // еще поля, описывающие юзера

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  @Override
  public String toString() {
    return "User{" +
            "login='" + login + '\'' +
            '}';
  }
}
