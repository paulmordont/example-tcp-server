package org.example;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerTest {

  Server server;

  @BeforeEach
  void setUp() {
    server = new Server(12345);
    server.start();
  }

  @AfterEach
  void tearDown() throws IOException {
    server.stop();
  }

  @Test
  void testServerWithOneClient() {
    Client client = new Client("0.0.0.0", 12345);
    try {
      client.connect();
      String first = client.sendStringAndGetResponse("first");
      assert first.equals("Client 0: tsrif");
      String second = client.sendStringAndGetResponse("second");
      assert second.equals("Client 0: dnoces");
      client.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void testServerWithFourParallelClients() {
    List<Client> clients = Stream.of(new Client("0.0.0.0", 12345), new Client("0.0.0.0", 12345),
        new Client("0.0.0.0", 12345), new Client("0.0.0.0", 12345)).collect(Collectors.toList());

    clients.forEach(c -> {
      try {
        c.connect();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });

    ConcurrentLinkedQueue<String> responses = new ConcurrentLinkedQueue<>();

    List<CompletableFuture<Void>> completableFutures = clients.stream().map(
            c -> CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> responses.add(c.sendStringAndGetResponse("1"))),
                CompletableFuture.runAsync(() -> responses.add(c.sendStringAndGetResponse("2")))))
        .collect(Collectors.toList());

    CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[4])).join();

    assert responses.contains("Client 2: 1");

    clients.forEach(c -> {
      try {
        c.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Test
  void testServerWithConnectingAndDisconnectingClient() {
    for (int i = 0; i < 4; i++) {
      Client client = new Client("0.0.0.0", 12345);
      try {
        client.connect();
        String response = client.sendStringAndGetResponse(Integer.toString(i));
        assert response.equals(String.format("Client %s: %s", i, i));
        client.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
