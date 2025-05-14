package client.networking;

import client.ui.util.ErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * This class is responsible for receiving data from the server through a socket connection.
 * It runs in a separate thread and continuously listens for incoming messages.
 * When a new message is received, it forwards the raw JSON string to the {@link SocketService} for processing.
 */
public class SocketServiceReceive implements Runnable {
  private final BufferedReader in;
  private final SocketService socketService;

  /**
   * Constructs a new {@code SocketServiceReceive} instance.
   *
   * @param socketService the {@link SocketService} that will process the received messages
   * @param in the {@link BufferedReader} used to read incoming data from the socket
   */
  public SocketServiceReceive(SocketService socketService, BufferedReader in) {
    this.socketService = socketService;
    this.in = in;
  }

  /**
   * Continuously reads lines of JSON data from the server and forwards them to the {@link SocketService}.
   * This method runs in a separate thread to avoid blocking the main thread of the application.
   *
   * @throws IOException if an error occurs while reading from the socket
   */
  @Override
  public void run() {
    try {
      String jsonLine;
      // Continuously read lines of JSON data from the server
      while ( socketService.isRunning() && (jsonLine = in.readLine()) != null) {
        // Forward the raw JSON string to the SocketService for further processing
        socketService.receive(jsonLine);
      }
    } catch (IOException e) {
      // Handle any IO exceptions (e.g., if the connection is lost)
      if (socketService.isRunning()) {
        ErrorHandler.handleError(e, "SocketServiceReceive");
      }else{
        ErrorHandler.handleError(e, "SocketServiceReceive");
      }
    }
  }
}
