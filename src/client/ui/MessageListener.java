package client.ui;

import com.google.gson.JsonElement;

public interface MessageListener
{
//  void update(String message);
  void update(String type, String payload);
}
//this is how the update method could look like

//@Override
//public void update(String message) {
//  JsonObject json = JsonParser.parseString(message).getAsJsonObject();
//  String type = json.get("type").getAsString();
//
//  switch (type) {
//    case "login_response":
//      LoginRespond loginRespond = gson.fromJson(message, LoginRespond.class);
//      // handle login
//      break;
//    case "register_response":
//      RegisterRespond registerRespond = gson.fromJson(message, RegisterRespond.class);
//      // handle register
//      break;
//    case "broadcast":
//      String broadcastMsg = json.get("message").getAsString();
//      System.out.println("Broadcast: " + broadcastMsg);
//      break;
//    // add more cases like error etc...
//  }
//}
