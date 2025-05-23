package server.networking;

import com.google.gson.Gson;
import server.networking.socketHandling.ClientHandler;
import shared.Respond;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The {@code Server} class represents a multi-client server that listens for client connections,
 * processes requests, and supports broadcasting messages to all connected clients.
 */
public class Server {

  // Thread-safe list to store the connected clients.
  private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

  /**
   * Broadcasts a message to all connected clients.
   * <p>
   * This method wraps the given type and payload into a {@link Respond} object,
   * serializes it to JSON, and sends it to each client using their {@link ClientHandler}.
   * </p>
   *
   * @param type    the type of the message (e.g., "race_finished", "error", "broadcast")
   * @param payload the data to be sent as the message payload (must be serializable by Gson)
   * @throws RuntimeException if an {@link IOException} occurs while sending to any client
   */
  public static void broadcast(String type, Object payload) {
    Respond response = new Respond(type, payload);

//    Line for debugging purposes:
    System.out.println("Server sends: " + response.type() + ", payload: " + response.payload());

    for (ClientHandler client : clients) {
      try {
        client.send(response); // send JSON-formatted response
      } catch (IOException e) {
        throw new RuntimeException("Error broadcasting message: " + e.getMessage(), e);
      }
    }
  }

  /**
   * Adds a client to the list of connected clients.
   *
   * @param clientHandler The {@code ClientHandler} instance representing the client to be added.
   */
  public static void addClient(ClientHandler clientHandler) {
    clients.add(clientHandler);
  }

  /**
   * Removes a client from the list of connected clients.
   *
   * @param clientHandler The {@code ClientHandler} instance representing the client to be removed.
   */
  public static void removeClient(ClientHandler clientHandler) {
    clients.remove(clientHandler);
  }

  /**
   * Starts the server and begins accepting client connections.
   * Each new connection is handled by a {@code ClientHandler} in a separate thread.
   *
   * @throws IOException If an I/O error occurs while setting up or accepting connections.
   */
  public void start() throws IOException {
    try (ServerSocket serverSocket = new ServerSocket(2910)) {
      System.out.println("Server started, listening for connections...");

      while (true) {
        // Accept a new client connection
        Socket socket = serverSocket.accept();

        // Create a new handler for the client
        ClientHandler socketHandler = new ClientHandler(socket);

        // Add the new client to the list of connected clients
        addClient(socketHandler);

        // Start a new thread to handle this client
        Thread socketThread = new Thread(socketHandler);
        socketThread.start();
      }
    } catch (IOException e) {
      System.err.println("Error starting the server: " + e.getMessage());
      throw e;
    }
  }
}
