package client.ui.util;

import com.google.gson.Gson;
import server.networking.exceptions.InvalidMessageException;
import shared.Respond;

public class RespondValidate
{
  public static Respond decode(Object object) throws InvalidMessageException {
    if (!(object instanceof Respond respond)) {
      throw new InvalidMessageException("Invalid object: Not an instance of Respond.");
    }

    if (respond.type() == null || respond.type().isEmpty()) {
      throw new InvalidMessageException("Invalid message: 'type' is missing.");
    }

    Gson gson = new Gson();
    String payload;

    if (respond.payload() instanceof String strPayload) {
      payload = strPayload; // already JSON
    } else {
      payload = gson.toJson(respond.payload());
    }

    if (payload == null || payload.isEmpty()) {
      throw new InvalidMessageException("Invalid payload: payload is null.");
    }

    System.out.println("Decoded payload: " + payload); // ✅ Log here

    return new Respond(respond.type(), payload);
  }


}
