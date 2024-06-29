package ru.gb.lesson5.jsonMassages;

/**
 * {
 *   "type": "users"
 * }
 */
public class ListUsersRequest extends AbstractRequest {
  public static final TypeMessage TYPE = TypeMessage.LIST_USERS;

  public TypeMessage getType() {
    return TYPE;
  }


}
