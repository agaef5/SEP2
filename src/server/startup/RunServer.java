package server.startup;

import server.model.RaceManager;
import server.networking.Server;
import java.io.IOException;

/**
 * The {@code RunServer} class is responsible for starting the server by initializing and starting the
 * server instance.
 * This is the entry point for the server-side application.
 */
public class RunServer
{
  /**
   * The main method that initializes and starts the server.
   *
   * @param args Command-line arguments passed to the program. Not used in this case.
   * @throws IOException If an I/O error occurs during the server startup.
   */
  public static void main(String[] args) throws IOException
  {
    // Create an instance of the server
    Server server = new Server();
    Thread managerThread = new Thread(RaceManager.getInstance());

    // Start the server
    managerThread.start();
    server.start();
  }
}
