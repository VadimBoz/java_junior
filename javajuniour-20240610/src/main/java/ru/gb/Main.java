package ru.gb;

import java.io.IOException;

public class Main {

  // Jetbrains Intellij IDEA

  // .zip

  public static void main(String[] args) throws IOException {

    System.out.println("Hello world!");
    Process p = Runtime.getRuntime().exec("docker images");
  }

}