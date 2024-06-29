package ru.gb.lesson5.jsonMassages;

import ru.gb.lesson5.User;

import java.util.List;

/**
 * {
 *   "users": [
 *     {
 *       "login": "anonymous"
 *     },
 *     {
 *       "login": "nagibator"
 *     },
 *     {
 *       "login": "admin"
 *     }
 *   ]
 * }
 */
public class ListUsersResponse extends AbstractRequest {

  public static final TypeMessage TYPE = TypeMessage.LIST_USERS;
  private List<User> users;

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }


}
