package client.networking;

import client.ui.MessageListener;
import com.google.gson.Gson;
import shared.Request;
import shared.Respond;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketService implements SocketSubject {
  private final BufferedWriter out;
  private final Gson gson = new Gson();
  private final ArrayList<MessageListener> listeners = new ArrayList<>();

  public SocketService(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);

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
      e.printStackTrace();
    }
  }

  public void receive(String jsonResponse) {
    System.out.println("Server>> " + jsonResponse.toString());
    Respond respond = gson.fromJson(jsonResponse, Respond.class);

    if(respond == null || respond.payload() == null) return;{
//      TODO: error handling
    }
    String payload = gson.toJson(respond.payload());

    notifyListener(respond.type(), payload);
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
