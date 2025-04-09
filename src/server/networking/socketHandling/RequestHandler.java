package server.networking.socketHandling;

import shared.Request;

public interface RequestHandler
{
  String handle(Request request);
}
