package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class ServerAcceptConnectionThread extends Thread {

  private ServerSocket serverSocket;

  private int clientNum;

  public ServerAcceptConnectionThread(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
    this.clientNum = 0;
  }

  public void run() {
    while (true) {
      Socket socket = null;
      try {
        socket = serverSocket.accept();
      } catch (SocketException e) {
        // this exception is thrown in case server socket is closed, meaning we could
        // return gracefully here
        return;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      System.out.println(String.format("Client %s connected", clientNum));

      // in order to be able to work with multiple connections concurrently,
      // new thread is created responsible for reading/writing data
      new ServerClientConnectionThread(socket, clientNum).start();

      clientNum++;
    }
  }
}
