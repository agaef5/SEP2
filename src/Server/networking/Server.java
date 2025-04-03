package Server.networking;

import Server.networking.socketHandling.ClientHandler;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  public void start() throws IOException
  {
    ServerSocket serverSocket = new ServerSocket(2910);
    System.out.println("Server started, listening for connections...");
    while(true){
      Socket socket = serverSocket.accept();
      ClientHandler socketHandler = new ClientHandler(socket);
      Thread socketThread = new Thread(socketHandler);
      socketThread.start();
    }
  }
}
