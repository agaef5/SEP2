package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;

import server.networking.Server;
import shared.*;

/**
 * {@code ClientHandler} is responsible for handling communication with a connected client.
 * It processes incoming requests, delegates actions to the appropriate handlers, and sends responses back to the client.
 */
public class ClientHandler implements Runnable {
  private final Socket socket;
  private final Gson gson = new Gson();
  private final RequestHandler authRequestHandler;
  private final RequestHandler horseRequestHandler;
  private final RequestHandler raceRequestHandler;
  private BufferedReader in;
  private BufferedWriter out;


  /**
   * Constructor to initialize the {@code ClientHandler} with the client socket.
   * Sets up request handlers for authentication, horse-related actions, and race-related actions.
   *
   * @param socket The client socket for communication.
   */
  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.authRequestHandler = new RegisterAndLoginHandler();
    this.horseRequestHandler = new HorseHandler();
    this.raceRequestHandler = new RaceHandler();
  }

  /**
   * This method runs the client handler in a separate thread. It reads client messages,
   * processes them, and sends appropriate responses.
   */
  @Override
  public void run() {
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      System.out.println("Client connected: " + socket.getInetAddress());

      String line;
      while ((line = in.readLine()) != null) {
        try {
          JsonObject json = gson.fromJson(line, JsonObject.class);
          if (json.has("handler") && json.has("action") && json.has("payload")) {
            System.out.println(line);
            Request request = gson.fromJson(line, Request.class);
            handleClientRequest(request);
          } else {
            System.err.println("Malformed request: " + line);
          }
        } catch (JsonSyntaxException e) {
          System.err.println("Failed to parse JSON: " + line);
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      System.err.println("Client disconnected or error: " + e.getMessage());
    }
  }

  /**
   * Handles the incoming request from the client. It processes the request based on its handler and action.
   * The corresponding request handler is called, and a response is generated.
   *
   * @param request The client request to be processed.
   * @throws IOException if an I/O error occurs during response sending.
   */
  private void handleClientRequest(Request request) throws IOException {
    Respond response = null;
    String responseType = request.action();
    Object responsePayload = null;

    try {
      // Deserialize the payload based on handler and action
      switch (request.handler()) {
        case "auth" -> {
          responsePayload = authRequestHandler.handle(request.action(), request.payload());
        }
        case "horse" -> {
          responsePayload = horseRequestHandler.handle(request.action(), request.payload());
        }
        case "race" -> {
          responsePayload = raceRequestHandler.handle(request.action(), request.payload());
        }
        case "disconnect" -> {
          handleClientDisconnect();
          Server.removeClient(this);
          return;
        }
        default -> throw new IllegalArgumentException("Unknown handler: " + request.handler());
      }
    } catch (Exception e) {
      e.printStackTrace();
      // If error occurs, send the "Error" response
      response = wrapResponse("error", new ErrorResponse("Server processing error", e.getMessage()));
      send(response);
    }
    // Send back the response
    response = wrapResponse(responseType, responsePayload);
    send(response);
  }

  private void handleClientDisconnect(){
    try {
      System.out.println("Client requested disconnect: " + socket.getInetAddress());
      send(new Respond("disconnect", "Goodbye!"));
      if (!socket.isClosed()) socket.close();
      System.out.println("Closed socket for: " + socket.getInetAddress());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Wraps the response payload into a {@code Respond} object, including the response type.
   *
   * @param responseType The type of the response (e.g., "auth", "horse", "race").
   * @param responsePayload The payload to be sent in the response.
   * @return The wrapped {@code Respond} object.
   */
  private Respond wrapResponse(String responseType, Object responsePayload) {
    if ((responseType == null || responseType.isEmpty()) || responsePayload == null) {
      // TODO: error handling here
    }
    return new Respond(responseType, responsePayload);
  }

  /**
   * Sends a response to the client as a JSON-encoded string.
   *
   * @param message The response message object to be sent to the client.
   * @throws IOException if an I/O error occurs while sending the message.
   */
  public void send(Object message) throws IOException {
//    System.out.println("Sending message...");
//    System.out.println("Socket closed? " + socket.isClosed());
    String json = gson.toJson(message); // Convert the response object to JSON string
    out.write(json); // Write the JSON to output stream
    out.newLine();   // Send a newline to signal the end of the message
    out.flush();     // Flush the output stream to ensure data is sent immediately
  }
}
