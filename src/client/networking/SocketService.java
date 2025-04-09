package client.networking;

import client.ui.MessageListener;
import shared.Request;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class SocketService implements SocketSubject
{


  private PrintWriter out;
  private ArrayList<MessageListener> listeners = new ArrayList<>();
  private Gson gson;


  public SocketService(String host, int port) throws IOException
  {
    Socket socket=new Socket(host,port);
    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(),true);
    this.gson = new Gson();

// iniciate SocketServiceReceive for receiving messages from server
    new Thread(new SocketServiceReceive(this,in)).start();
  }


  public void sendRequest (Request request)
  {
    String jsonRequest = gson.toJson(request);
    out.println(jsonRequest);
  }


  public void receive (String message)
  {
    System.out.println("Server>> " + message);
    notifyListener(message);
  }

  @Override public void notifyListener(String message)
  {
  for (MessageListener listener : listeners)
  {
    listener.update(message);
  }
  }

  @Override public void addListener(MessageListener listener)
  {
    listeners.add(listener);

  }

  @Override public void removeListener(MessageListener listener)
  {
    listeners.remove(listener);
  }
}
