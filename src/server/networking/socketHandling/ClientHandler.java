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
    Object result = null;

    // Deserialize the payload based on handler and action
    switch (request.handler()) {
      case "auth" -> {
        switch (request.action()) {
          case "login" -> {
            // Deserialize LoginRequest from payload
            LoginRequest loginRequest = gson.fromJson(request.payload(), LoginRequest.class);
            result = authRequestHandler.handle("login", loginRequest);
          }
          case "register" -> {
            // Deserialize RegisterRequest from payload
            RegisterRequest registerRequest = gson.fromJson(request.payload(), RegisterRequest.class);
            result = authRequestHandler.handle("register", registerRequest);
          }
        }
      }
      case "racer" -> {
        switch (request.action()) {
          case "getRacerList" -> {
            // Deserialize HorseListRequest from payload
            RacerListRequest listRequest = gson.fromJson(request.payload(), RacerListRequest.class);
            result = racerRequestHandler.handle("getRacerList", listRequest);
          }
          case "getRacer" -> {
            // Deserialize RacerRequest from payload
            RacerRequest racerRequest = gson.fromJson(request.payload(), RacerRequest.class);
            result = racerRequestHandler.handle("getRacer", racerRequest);
          }
          case "createRacer"-> {
            CreateRacerRequest createRacerRequest = gson.fromJson(request.payload(), CreateRacerRequest.class);
            Object createRacer = racerRequestHandler.handle("createRacer", createRacerRequest);
            result = new Respond("createRacer", createRacer);
          }
        }
      }
      default -> throw new IllegalArgumentException("Unknown handler: " + request.handler());
    }
    // Send back the result
    send(result);
  }

  // Method to send response as JSON
  public void send(Object message) throws IOException {
    String json = gson.toJson(message); // Convert the response object to JSON string
    out.write(json); // Write the JSON to output stream
    out.newLine();   // Send a newline to signal the end of the message
    out.flush();     // Flush the output stream to ensure data is sent immediately
  }
}
