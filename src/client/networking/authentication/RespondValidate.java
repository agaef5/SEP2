package client.networking.authentication;

import client.ui.util.exceptions.InvalidMessageException;
import shared.LoginRespond;
import shared.RegisterRespond;
import shared.Respond;

/**
 * Utility class for validating {@link Respond} messages received from the server.
 * <p>
 * This class provides a method to validate the structure and content of a {@code Respond}
 * object based on its declared type and payload.
 */
public class RespondValidate
{
  /**
   * Validates the given object to ensure it is a properly formed {@link Respond}.
   * <p>
   * This method checks the type and payload of the {@code Respond} and ensures that
   * the payload matches the expected type based on the respond type.
   *
   * @param object the object to validate, expected to be an instance of {@code Respond}
   * @throws InvalidMessageException if the object is not a valid {@code Respond},
   *                                 has a null or empty type, or has an unexpected or null payload
   */
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
