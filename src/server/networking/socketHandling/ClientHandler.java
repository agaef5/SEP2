package server.networking.socketHandling;

import java.io.*;
import java.net.Socket;

import shared.Request;

public class ClientHandler implements Runnable {
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private final RequestHandler requestHandler;

  public ClientHandler(Socket socket) {
    this.socket = socket;
    this.requestHandler = new RegisterAndLoginHandler();
  }

  @Override
  public void run() {
    try {
      // IMPORTANT: Initialize ObjectOutputStream first
      out = new ObjectOutputStream(socket.getOutputStream());
      out.flush(); // flush header
      in = new ObjectInputStream(socket.getInputStream());

      System.out.println("Client connected: " + socket.getInetAddress());

      while (true) {
        Object incoming = in.readObject();
        if (incoming instanceof Request request) {
          handleClientRequest(request);
        } else {
          System.err.println("Unknown object received: " + incoming);
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void handleClientRequest(Request request) throws IOException {
    Object result;

    switch (request.handler()) {
      case "auth" -> result = requestHandler.handle(request.action(), request.payload());
      case "racer" -> {RequestHandler raceRequestHandler = new RacerHandler();
                        result = raceRequestHandler.handle(request.action(), request.payload());}
      default -> throw new IllegalStateException("Unexpected handler: " + request.handler());
    }

    // Send back result to client
    out.writeObject(result);
    out.flush();
  }

  public void send(Object message) throws IOException {
    out.writeObject(message);
    out.flush();
  }
}
