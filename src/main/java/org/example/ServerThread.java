package org.example;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {

  private Socket socket;
  private int clientNum;

  public ServerThread(Socket socket, int clientNum) {
    this.socket = socket;
    this.clientNum = clientNum;
  }

  public void run() {
    try {
      InputStream input = socket.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(input));

      OutputStream output = socket.getOutputStream();
      PrintWriter writer = new PrintWriter(output, true);

      String text;

      do {
        text = reader.readLine();
        if (text == null) {
          break;
        }
        String reverseText = new StringBuilder(text).reverse().toString();
        writer.println(String.format("Client %s: %s", clientNum, reverseText));

      } while (true);
      socket.close();
      System.out.println(String.format("Client %s disconnected", clientNum));
    } catch (IOException ex) {
      System.out.println("Server exception: " + ex.getMessage());
      ex.printStackTrace();
    }
  }
}