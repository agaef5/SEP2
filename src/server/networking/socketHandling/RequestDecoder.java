package server.networking.socketHandling;

import client.networking.exceptions.InvalidMessageException;
import com.google.gson.Gson;
import shared.*;

public class RequestDecoder
{
  private final Gson gson = new Gson();


  public void decode(String jsonRespond) throws InvalidMessageException
  {
    try
    {
    Request request = gson.fromJson(jsonRespond,Request.class);


    if (request.handler()==null|| request.handler().isEmpty())
    {
      throw new InvalidMessageException("Invalid message: 'handler' is missing.");
    }

    if (request.action() == null||request.action().isEmpty())
    {
      throw new InvalidMessageException("Invalid message: 'action' is missing");
    }

    if (request.payload()==null)
    {
      throw new InvalidMessageException("Invalid 'payload' type, payload is null");
    }

    if (!(request.payload() instanceof LoginRequest || request.payload() instanceof RegisterRequest))
    {
      throw new InvalidMessageException("Invalid payload type: must be LoginRequest or RegisterRequest.");
    }

    //if there is more validation add it here

    } catch (Exception e)
    {
  throw new InvalidMessageException("Message decoding failed: " + e.getMessage());
    }
  }
}
