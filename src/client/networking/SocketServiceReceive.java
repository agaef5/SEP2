package client.networking;

import java.io.BufferedReader;
import java.io.IOException;

public class SocketServiceReceive implements Runnable {
  private final BufferedReader in;
  private final SocketService socketService;

  public SocketServiceReceive(SocketService socketService, BufferedReader in) {
    this.socketService = socketService;
    this.in = in;
  }

  @Override
  public void run() {
    try {
      String jsonLine;
      while ((jsonLine = in.readLine()) != null) {
        // Forward the raw JSON string to the SocketService
        socketService.receive(jsonLine);
      }
    } catch (IOException e) {
      System.err.println("An I/O error occurred while reading from the input stream: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
