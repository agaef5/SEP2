package client.ui.util;

import com.google.gson.Gson;
import server.networking.exceptions.InvalidMessageException;
import shared.Respond;

/**
 * The {@code RespondValidate} class is responsible for decoding and validating {@link Respond} objects.
 * It ensures that the received response data is in a valid format and properly serialized. If the response
 * is not valid, an {@link InvalidMessageException} will be thrown.
 * <p>
 * The primary function of this class is to check the integrity of the response, especially the payload,
 * and convert it into a valid {@code Respond} object.
 */
public class RespondValidate
{
  /**
   * Decodes and validates a given object, ensuring it is a valid {@code Respond} object.
   * If the object is not a {@code Respond}, or if the payload is invalid, an exception will be thrown.
   * <p>
   * The method checks if the payload is of type {@code String}, and if not, it will convert the payload
   * into a JSON string representation.
   *
   * @param object the object to be decoded and validated
   * @return a valid {@code Respond} object with a decoded payload
   * @throws InvalidMessageException if the object is not an instance of {@code Respond} or the payload is invalid
   */
  public static Respond decode(Object object) throws InvalidMessageException {
    // Ensure the object is an instance of Respond
    if (!(object instanceof Respond respond)) {
      throw new InvalidMessageException("Invalid object: Not an instance of Respond.");
    }

    Gson gson = new Gson();
    String payload;

    // Handle payload depending on its type
    if (respond.payload() instanceof String strPayload) {
      payload = strPayload; // Payload is already a valid JSON string
    } else {
      payload = gson.toJson(respond.payload()); // Convert the payload to JSON if it's not a string
    }

    // Check if the payload is valid
    if (payload == null || payload.isEmpty()) {
      throw new InvalidMessageException("Invalid payload: payload is null or empty.");
    }

    // Log the decoded payload for debugging purposes
    System.out.println("Decoded payload: " + payload);

    // Return the Respond object with the decoded payload
    return new Respond(respond.type(), payload);
  }
}
