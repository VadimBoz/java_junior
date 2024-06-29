package ru.gb.lesson5.jsonMassages;

import ru.gb.lesson5.jsonMassages.TypeMessage;

public class AbstractRequest {

  private TypeMessage type;

  public TypeMessage getType() {
    return type;
  }

  public void setType(TypeMessage type) {
    this.type = type;
  }
}
