package server.networking.socketHandling;

import client.networking.exceptions.InvalidMessageException;
import com.google.gson.Gson;
import shared.*;

public class RequestDecoder
{
  private Gson gson = new Gson();


  public void decode(String jsonRespond) throws InvalidMessageException
  {
    try
    {
    Request request =  gson.fromJson(jsonRespond,Request.class);


    if (request.getHandler()==null|| request.getHandler().isEmpty())
    {
      throw new InvalidMessageException("Invalid message: 'handler' is missing.");
    }

    if (request.getAction() == null||request.getAction().isEmpty())
    {
      throw new InvalidMessageException("Invalid message: 'action' is missing");
    }

    if (request.getPayload()==null)
    {
      throw new InvalidMessageException("Invalid 'payload' type, payload is null");
    }

    if (!(request.getPayload() instanceof LoginRequest || request.getPayload() instanceof RegisterRequest))
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
