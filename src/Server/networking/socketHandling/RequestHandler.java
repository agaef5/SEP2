package Server.networking.socketHandling;

import Shared.Request;

public interface RequestHandler
{
  String handle(Request request);
}
