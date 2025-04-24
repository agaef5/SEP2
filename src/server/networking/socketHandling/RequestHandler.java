package server.networking.socketHandling;

import com.google.gson.JsonElement;
import shared.Request;

public interface RequestHandler
{
  Object handle(String action, JsonElement payload);
}
