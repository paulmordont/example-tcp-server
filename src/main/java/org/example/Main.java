package org.example;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args) throws IOException {

    Server server = new Server(12345);
    server.start();
    List<CompletableFuture<Void>> futures = IntStream.range(0, 4)
        .mapToObj(i -> CompletableFuture.runAsync(() -> {
          Client client = new Client("0.0.0.0", 12345);
          try {
            client.connect();
            System.out.println(client.sendStringAndGetResponse("hello"));
            System.out.println(client.sendStringAndGetResponse("again"));
            System.out.println(client.sendStringAndGetResponse("wow"));
            client.close();
          } catch (IOException e) {
            System.out.println(e);
          }
        })).collect(Collectors.toList());

    CompletableFuture.allOf(futures.toArray(new CompletableFuture[4])).join();

    server.stop();

  }
}