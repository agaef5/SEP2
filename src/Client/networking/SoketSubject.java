package Client.networking;

import Client.ui.MessageListener;

import java.util.ArrayList;

public interface SoketSubject
{
  void notifyListener(String message);
  void addListener(MessageListener listener);
  void removeListener(MessageListener listener);
}
