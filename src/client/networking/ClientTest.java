package client.networking;

import client.networking.authentication.SocketAuthenticationClient;
import shared.LoginRequest;
import shared.RegisterRequest;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientTest
{
  public static void main(String[] args) throws IOException
  {
    SocketService socketservice = new SocketService("localhost",2910 );
    SocketAuthenticationClient socketauth = new SocketAuthenticationClient(socketservice);
    System.out.println("sending regRequest");
    RegisterRequest regReq = new RegisterRequest("samojak","123456789","123456789","SAMO@GMAIL.COM");
    socketauth.registerUser(regReq);
    System.out.println("sending logRequest");
    LoginRequest logReq = new LoginRequest("Samojak", "123456789");
    socketauth.loginUser(logReq);



  }
}
