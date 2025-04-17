package client.networking;

import java.io.IOException;
import java.io.ObjectInputStream;

public class SocketServiceReceive implements Runnable {
  private final ObjectInputStream in;
  private final SocketService socketService;

  public SocketServiceReceive(SocketService socketService, ObjectInputStream in) {
    this.socketService = socketService;
    this.in = in;
  }

  @Override
  public void run() {
    try {
      Object response;
      while ((response = in.readObject()) != null) {
       // System.out.println("Server says (object): " + response);

        // Directly send the object to listeners
        socketService.receive(response);
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
