package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.Socket;

import server.model.Racer;
import shared.*;

public class ClientHandler implements Runnable {
  private final Socket socket;
  private BufferedReader in;
  private BufferedWriter out;
  private final Gson gson = new Gson();

  private final RequestHandler authRequestHandler;
  private final RequestHandler racerRequestHandler;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.authRequestHandler = new RegisterAndLoginHandler();
    this.racerRequestHandler = new RacerHandler();
  }

  @Override
  public void run() {
    try {
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

      System.out.println("Client connected: " + socket.getInetAddress());

      String line;
      while ((line = in.readLine()) != null) {
        try {
          // Deserialize the incoming line into a JsonObject first
          JsonObject json = gson.fromJson(line, JsonObject.class);

          if (json.has("handler") && json.has("action") && json.has("payload")) {
            // Now create the Request object with handler, action, and payload
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
      e.printStackTrace();
    }
  }

  private void handleClientRequest(Request request) throws IOException {
    Respond response = null;
    String responseType = request.action();
    Object responsePayload = null;

    // Deserialize the payload based on handler and action
    switch (request.handler()) {
      case "auth" -> {
        responsePayload = authRequestHandler.handle(request.action(), request.payload());
      }
      case "racer" -> {
        responsePayload = racerRequestHandler.handle(request.action(), request.payload());
      }
      default -> throw new IllegalArgumentException("Unknown handler: " + request.handler());
    }
    // Send back the response
    response = wrapResponse(responseType, responsePayload);
    send(response);
  }

  public Respond wrapResponse(String responseType, Object responsePayload){
    if((responseType == null || responseType.isEmpty()) || responsePayload == null)
    {
//      TODO: error handling here
    };
    return new Respond(responseType, responsePayload);
  }

  // Method to send response as JSON
  public void send(Object message) throws IOException {
    String json = gson.toJson(message); // Convert the response object to JSON string
    out.write(json); // Write the JSON to output stream
    out.newLine();   // Send a newline to signal the end of the message
    out.flush();     // Flush the output stream to ensure data is sent immediately
  }
}
