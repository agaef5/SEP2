package server.networking.socketHandling;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;

public abstract class BaseRequestHandler implements RequestHandler {
  protected final Gson gson = new Gson();

  @Override
  public final Object handle(String action, JsonElement payload) {
    try {
      return safeHandle(action, payload);
    } catch (JsonSyntaxException e) {
      throw new IllegalArgumentException("Malformed JSON payload: " + e.getMessage(), e);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Missing required fields in payload: " + e.getMessage(), e);
    } catch (IllegalArgumentException e) {
      // Pass through IllegalArgumentException with action-related issues
      throw e;
    } catch (Exception e) {
      throw new RuntimeException("Unexpected error handling request: " + e.getMessage(), e);
    }
  }

  protected <T> T parsePayload(JsonElement payload, Class<T> clazz) {
    try {
      return gson.fromJson(payload, clazz);
    } catch (JsonSyntaxException | NullPointerException e) {
      throw new IllegalArgumentException("Invalid payload structure for " + clazz.getSimpleName(), e);
    }
  }

  protected abstract Object safeHandle(String action, JsonElement payload);
}
