package Client.networking;

import java.io.BufferedReader;
import java.io.IOException;

public class SocketServiceReceive implements Runnable
{
  private BufferedReader in;
  private SocketService socketService;

  public SocketServiceReceive(SocketService socketService, BufferedReader in)
  {
    this.socketService=socketService;
    this.in=in;
  }


  @Override public void run()
  {
    try
    {
     String msg;
     while ((msg = in.readLine())!= null)
     {
       System.out.println("Server says: " + msg);

       //notify listeners
       socketService.receive(msg);
     }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
