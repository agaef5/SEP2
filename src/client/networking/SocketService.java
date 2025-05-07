package client.networking;

import client.ui.MessageListener;
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
  private final BufferedWriter out;
  private final Gson gson = new Gson();
  private final ArrayList<MessageListener> listeners = new ArrayList<>();
  private ErrorHandler errorHandler;

  /**
   * Constructs a new {@code SocketService} that connects to the specified server.
   *
   * @param host the server's host address
   * @param port the port on which the server is listening
   * @throws IOException if an I/O error occurs when creating the socket or streams
   */
  public SocketService(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);
    errorHandler = new ErrorHandler(this);

    this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    // Start a new thread to handle incoming messages from the server
    new Thread(new SocketServiceReceive(this, in)).start();
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
    System.out.println("Server>> " + jsonResponse.toString());
    try {
      System.out.println("Trying to decode the Respond");
      Respond respond = gson.fromJson(jsonResponse, Respond.class);
      Respond respondDecoded = RespondValidate.decode(respond);
      System.out.println("Respond decoded: " + respondDecoded);
      // Notify listeners with the response type and payload
      notifyListener(respondDecoded.type(), (String) respondDecoded.payload());
    } catch (InvalidMessageException e) {
      ErrorHandler.handleError(e, "RespondValidate");
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
      System.out.println("Notifying listeners");
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
}
