package server.networking.socketHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import com.google.gson.Gson;
import shared.Request;


// This class handles each client requests in a separate thread
public class ClientHandler implements Runnable
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private Gson gson;
  private final RegisterAndLoginHandler registerAndLoginHandler;


  public ClientHandler(Socket socket)
  {
    this.socket = socket;
    this.gson = new Gson();
   this.registerAndLoginHandler = new RegisterAndLoginHandler();
  }

  @Override public void run()
  {
    try
    {
      //iniciate the streams
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      System.out.println("Client connected: " + socket.getInetAddress());

      out.println("Server connected. Please login or register.");

      // Loop to keep listening for login/register requests
      while (true)
      {
        String receivedMessage = in.readLine(); // Receive JSON string from client
        if (receivedMessage != null)
        {
          handleClientRequest(receivedMessage); // call method to handle the message
        }
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

    private void handleClientRequest(String jsonMessage) throws IOException
    {
      Request request = gson.fromJson(jsonMessage, Request.class);

      Object result;

      switch (request.handler())
      {
        case "auth" -> result = registerAndLoginHandler.handle(request.action() , request.payload());
        // Future handlers can be added here
        default -> throw new IllegalStateException("Unexpected handler: " + request.handler());
      }

      //here will be respond from result
        String respond = gson.toJson(result);
        out.println(respond);
    }
  public void send(String message) {
    out.println(message);
  }

}



