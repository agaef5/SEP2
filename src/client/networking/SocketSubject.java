package client.networking;

import client.ui.MessageListener;
import com.google.gson.JsonElement;

public interface SocketSubject
{
  static void addListener(MessageListener listener)
  {

  }
  void notifyListener(String type, String payload);
  void removeListener(MessageListener listener);
}
