package server.networking.socketHandling;

import client.networking.exceptions.InvalidMessageException;
import shared.*;

public class RequestValidate
{

  public void decode(Request request) throws InvalidMessageException {
    if (request == null) {
      throw new InvalidMessageException("Request is null.");
    }

    if (request.handler() == null || request.handler().isEmpty()) {
      throw new InvalidMessageException("Invalid message: 'handler' is missing.");
    }

    if (request.action() == null || request.action().isEmpty()) {
      throw new InvalidMessageException("Invalid message: 'action' is missing.");
    }

    if (request.payload() == null) {
      throw new InvalidMessageException("Invalid message: 'payload' is null.");
    }

    // Validate expected payload types
    if (!(request.payload() instanceof LoginRequest || request.payload() instanceof RegisterRequest)) {
      throw new InvalidMessageException("Invalid payload type: must be LoginRequest or RegisterRequest.");
    }

    // Add more validation if needed
  }
}
