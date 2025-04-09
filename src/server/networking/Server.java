package server.networking;

import server.networking.socketHandling.ClientHandler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server
{

  private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();

  public static void broadcast(String message) {
    for (ClientHandler client : clients) {
      client.send(message);
    }
  }

  public static void addClient(ClientHandler clientHandler) {
    clients.add(clientHandler);
  }

  public static void removeClient(ClientHandler clientHandler) {
    clients.remove(clientHandler);
  }

  public void start() throws IOException
  {
    ServerSocket serverSocket = new ServerSocket(2910);
    System.out.println("Server started, listening for connections...");
    while(true){
      Socket socket = serverSocket.accept();
      ClientHandler socketHandler = new ClientHandler(socket);
      addClient(socketHandler);
      Thread socketThread = new Thread(socketHandler);
      socketThread.start();
    }
  }
}
