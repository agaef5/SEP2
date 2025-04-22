package client.networking;

import client.ui.MessageListener;

public interface SocketSubject
{
  void notifyListener(Object obj);
  void addListener(MessageListener listener);
  void removeListener(MessageListener listener);
}
