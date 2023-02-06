package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

  private String host;
  private int port;

  Socket socket;

  PrintWriter writer;

  BufferedReader reader;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void connect() throws IOException {
    socket = new Socket(host, port);
    writer = new PrintWriter(socket.getOutputStream(), true);
    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
  }

  public String sendStringAndGetResponse(String data) {
    try {
      writer.println(data);
      return reader.readLine();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void close() throws IOException {
    socket.close();
  }
}
