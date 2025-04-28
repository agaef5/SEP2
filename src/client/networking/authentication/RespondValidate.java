package client.networking.authentication;


import client.validation.exceptions.InvalidMessageException;
import shared.LoginRespond;
import shared.RegisterRespond;
import shared.Respond;

public class RespondValidate
{

  public static void decode(Object object) throws InvalidMessageException
  {
    if (!(object instanceof Respond respond)) {
      throw new InvalidMessageException("Invalid object: Not an instance of Respond.");
    }

    if (respond.type() == null || respond.type().isEmpty()) {
      throw new InvalidMessageException("Invalid message: 'type' is missing.");
    }

    Object payload = respond.payload();
    if (payload == null) {
      throw new InvalidMessageException("Invalid payload: payload is null.");
    }

    // Example payload validation based on respond type
    switch (respond.type()) {
      case "login":
        if (!(payload instanceof LoginRespond)) {
          throw new InvalidMessageException("Expected LoginRespond payload.");
        }
        break;
      case "register":
        if (!(payload instanceof RegisterRespond)) {
          throw new InvalidMessageException("Expected RegisterRespond payload.");
        }
        break;
      default:
        throw new InvalidMessageException("Unknown respond type: " + respond.type());
    }
  }
}
