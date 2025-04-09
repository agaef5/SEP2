package client.networking;

import client.networking.authentication.RespondDecoder;
import client.networking.exceptions.InvalidMessageException;

import java.io.BufferedReader;
import java.io.IOException;

public class SocketServiceReceive implements Runnable
{
  private BufferedReader in;
  private SocketService socketService;
  private RespondDecoder respondDecoder;

  public SocketServiceReceive(SocketService socketService, BufferedReader in)
  {
    this.socketService = socketService;
    this.in = in;
    this.respondDecoder = new RespondDecoder();
  }

  @Override public void run()
  {
    try
    {
      String msg;
      while ((msg = in.readLine()) != null)
      {
        System.out.println("Server says: " + msg);

        // Validate and decode the message
        try
        {
          //check if the msg is valid
          respondDecoder.decode(msg);
          //if its valid than it can proceed
          //if not valid, than it will throw exception InvalidMessageException
          socketService.receive(msg);
        }
        catch (InvalidMessageException e)
        {
          // log the error
          System.err.println("Invalid message received: " + e.getMessage());
          System.err.println("Received message: " + msg);
        }
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

