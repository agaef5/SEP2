package server.networking.socketHandling;

import shared.Request;

public interface RequestHandler
{
  Object handle(String action, Object payload);
}
