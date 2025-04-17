package client.networking;

import client.ui.MessageListener;
import shared.Request;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class SocketService implements SocketSubject {
  private ObjectOutputStream out;
  private ArrayList<MessageListener> listeners = new ArrayList<>();

  public SocketService(String host, int port) throws IOException {
    Socket socket = new Socket(host, port);

    // Important: create ObjectOutputStream before ObjectInputStream
    this.out = new ObjectOutputStream(socket.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

    // Start receiving thread
    new Thread(new SocketServiceReceive(this, in)).start();
  }

  public void sendRequest(Request request) {
    try {
      out.writeObject(request);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void receive(Object response) {
    System.out.println("Server>> " + response);
    notifyListener(response);
  }

  @Override
  public void notifyListener(Object message) {
    for (MessageListener listener : listeners) {
      listener.update(message);
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
