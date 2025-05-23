package client.networking;

import client.modelManager.MessageListener;
import client.ui.util.ErrorHandler;
import client.ui.util.RespondValidate;
import com.google.gson.Gson;
import server.networking.exceptions.InvalidMessageException;
import shared.Request;
import shared.Respond;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Provides communication services for socket-based interactions with the server.
 * The class sends and receives JSON-encoded requests and responses using a socket connection.
 * It also supports registering and notifying listeners for events triggered by server responses.
 */
public class SocketService implements SocketSubject {
  private final Socket socket;
  private final BufferedWriter out;
  private final BufferedReader in;
  private boolean running = true;
  private final Gson gson = new Gson();
  private final ArrayList<MessageListener> listeners = new ArrayList<>();
  private ErrorHandler errorHandler;
  private Thread receiveThread;

  /**
   * Constructs a new {@code SocketService} that connects to the specified server.
   *
   * @param host the server's host address
   * @param port the port on which the server is listening
   * @throws IOException if an I/O error occurs when creating the socket or streams
   */
  public SocketService(String host, int port) throws IOException {
    this.socket = new Socket(host, port);
    this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

//  Initiate an error handler
    errorHandler = new ErrorHandler(this);

    // Start a new thread to handle incoming messages from the server
    this.receiveThread = new Thread(new SocketServiceReceive(this, in));
    receiveThread.start();

  }

  /**
   * Adds a {@link MessageListener} to the list of listeners that will be notified
   * when a new message is received.
   *
   * @param listener the {@code MessageListener} to add
   */
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  /**
   * Sends a request to the server. The request is serialized as a JSON string and sent over the socket connection.
   *
   * @param request the request to send
   */
  public void sendRequest(Request request) {
    try {
      String json = gson.toJson(request);
      out.write(json);
      out.newLine(); // Marks the end of the JSON message
      out.flush();
    } catch (IOException e) {
      ErrorHandler.handleError(e, "SocketService");
    }
  }

  /**
   * Receives a JSON-encoded response from the server, decodes it into a {@link Respond} object,
   * and notifies the listeners with the response's type and payload.
   *
   * @param jsonResponse the raw JSON response from the server
   */
  public void receive(String jsonResponse) {
    try {
      // First check if the response is a valid JSON object
      if (!jsonResponse.trim().startsWith("{")) {
        System.err.println("Received invalid JSON: " + jsonResponse);
        return;
      }
      Respond respond = gson.fromJson(jsonResponse, Respond.class);

      try{
      Respond respondDecoded = RespondValidate.decode(respond);
      System.out.println("\nRespond decoded: " + respondDecoded);
      if(respondDecoded.type().equals("disconnect")) return;

        // Convert payload to string for notification
        // The payload might already be a string, or it might be an object
        Object payload = respondDecoded.payload();

        // Check if the payload is already a string
        if (payload instanceof String) {
          notifyListener(respondDecoded.type(), (String) payload);
        } else {
          // If it's not a string, convert it to a JSON string
          notifyListener(respondDecoded.type(), gson.toJson(payload));
        }
      } catch (InvalidMessageException e) {
        ErrorHandler.handleError(e, "RespondValidate");
      }
    } catch (Exception e) {
      System.err.println("Error processing response: " + e.getMessage());
    }
  }

  public boolean isRunning() {
    return running;
  }

  public void disconnect() {
    running = false;
    try {
      Request disconnectRequest = new Request("disconnect", "disconnect",
              new Gson().toJsonTree("disconnect"));
      sendRequest(disconnectRequest);

      Thread.sleep(1000);

      removeAllListeners();
      if (socket != null && !socket.isClosed()) socket.close();
      if (in != null) in.close();
      if (out != null) out.close();
    } catch (IOException | InterruptedException e) {
      ErrorHandler.handleError(e, "Error while disconnecting from server");
    }
  }

  /**
   * Notifies all registered listeners of a new event.
   *
   * @param type the type of the event
   * @param payload the payload data associated with the event
   */
  @Override
  public void notifyListener(String type, String payload) {
    for (MessageListener listener : listeners) {
      listener.update(type, payload);
    }
  }

  /**
   * Removes a {@link MessageListener} from the list of listeners.
   *
   * @param listener the {@code MessageListener} to remove
   */
  @Override
  public void removeListener(MessageListener listener) {
    listeners.remove(listener);
  }


  /**
   * Removes all listeners so that they will no longer receive notifications.
   *
   */
  @Override
  public void removeAllListeners(){
    listeners.clear();
  }
}
