package client.networking;

import client.ui.MessageListener;
import com.google.gson.JsonElement;

public interface SocketSubject
{
  void notifyListener(String type, String payload);
  void addListener(MessageListener listener);
  void removeListener(MessageListener listener);
}
