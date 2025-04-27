package client.networking;

import client.ui.MessageListener;
import client.validation.ErrorHandler;
import client.validation.RespondValidate;
import com.google.gson.Gson;
import server.networking.exceptions.InvalidMessageException;
import shared.Request;
import shared.Respond;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketService implements SocketSubject {
  private final BufferedWriter out;
  private final Gson gson = new Gson();
  private final ArrayList<MessageListener> listeners = new ArrayList<>();
  private  ErrorHandler errorHandler;

  public SocketService(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);
    errorHandler = new ErrorHandler(this);

    this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    new Thread(new SocketServiceReceive(this, in)).start();
  }

  public void sendRequest(Request request) {
    try {
      String json = gson.toJson(request);
      out.write(json);
      out.newLine(); // very important to mark the end of the JSON message
      out.flush();
    } catch (IOException e) {
      ErrorHandler.handleError(e, "SocketService");
    }
  }

  public void receive(String jsonResponse) {
    System.out.println("Server>> " + jsonResponse.toString());
    try
    {
      Respond respond = RespondValidate.decode(jsonResponse);
      notifyListener(respond.type(), (String) respond.payload());
    }catch (InvalidMessageException e){
      ErrorHandler.handleError(e, "RespondValidate");
    }
  }

  @Override
  public void notifyListener(String type, String payload) {
    for (MessageListener listener : listeners) {
      listener.update(type, payload);
    }
  }

  @Override
  public void addListener(MessageListener listener) {
    listeners.add(listener);
  }

  @Override
  public void removeListener(MessageListener listener) {
    listeners.remove(listener);
  }
}
