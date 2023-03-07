package org.example;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

  private int port;

  private ServerSocket serverSocket;

  public Server(int port) {
    this.port = port;
  }

  public void start() {
    try {
      serverSocket = new ServerSocket(port);
      System.out.println("Server is listening on port " + port);
      // create a new thread to accept connections in a non-blocking way
      new ServerAcceptConnectionThread(serverSocket).start();
    } catch (IOException ex) {
      System.out.println("Server exception: " + ex.getMessage());
      ex.printStackTrace();
    }
  }

  public void stop() throws IOException {
    serverSocket.close();
    System.out.println("Server has been stopped");
  }

}
