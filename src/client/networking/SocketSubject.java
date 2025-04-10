package client.networking;

import client.ui.MessageListener;

public interface SocketSubject
{
  void notifyListener(String message);
  void addListener(MessageListener listener);
  void removeListener(MessageListener listener);
}
