package ru.gb.lesson5.jsonMassages;

/**
 * {
 *   "connected": true
 * }
 */
public class LoginResponse {

  private boolean connected;

  public boolean isConnected() {
    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }
}
