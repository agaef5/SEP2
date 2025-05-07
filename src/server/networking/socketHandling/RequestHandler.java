package server.networking.socketHandling;

import com.google.gson.JsonElement;
import shared.Request;

/**
 * {@code RequestHandler} defines the contract for classes that handle specific types of requests.
 * Each handler is responsible for processing requests based on the provided action and payload.
 */
public interface RequestHandler {

  /**
   * Handles a request based on the provided action and payload.
   *
   * @param action The action to be performed (e.g., "login", "register", "createHorse").
   * @param payload The payload containing the necessary data for the action.
   * @return The result of processing the request, which can be any object depending on the action.
   * @throws IllegalArgumentException If the action or payload is invalid.
   */
  Object handle(String action, JsonElement payload);
}
