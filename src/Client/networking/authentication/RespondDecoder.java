package Client.networking.authentication;

import Client.networking.exceptions.InvalidMessageException;
import Shared.LoginRespond;
import Shared.RegisterRespond;
import Shared.Respond;
import com.google.gson.Gson;

public class RespondDecoder
{
  private final Gson gson = new Gson();


  public void decode(String jsonRespond) throws InvalidMessageException
  {
    try
    {
    Respond respond = gson.fromJson(jsonRespond,Respond.class);


    if (respond.type()==null|| respond.type().isEmpty())
      {
        throw new InvalidMessageException("Invalid message: 'type' is missing.");
      }
    if (respond.payload()==null)
    {
      throw new InvalidMessageException("Invalid 'payload' type, payload is null");
    }

    if (respond.payload()!= LoginRespond.class||respond.payload()!= RegisterRespond.class)
    {
      throw new InvalidMessageException("Invalid payload type");
    }

    //if there is more validation add it here

    } catch (Exception e)
    {
  throw new InvalidMessageException("Message decoding failed: " + e.getMessage());
    }
  }
}
