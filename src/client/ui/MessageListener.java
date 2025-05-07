package client.ui;

import com.google.gson.JsonElement;

/**
 * The {@code MessageListener} interface is used for listening to and handling different types of messages
 * received over the network or other communication channels. Implementing classes should define how to
 * process the received messages.
 * <p>
 * The interface provides methods to update listeners with different types of payloads. Each type of message
 * is handled by the implementing class according to the type and payload provided.
 */
public interface MessageListener
{
  /**
   * Updates the listener with a message containing a specific type and payload.
   *
   * @param type the type of the message (e.g., "login_response", "register_response", etc.)
   * @param payload the payload of the message, which is usually the content that is relevant to the type
   */
  void update(String type, String payload);

//  /**
//   * Updates the listener with a message that is an object. The type and payload can be extracted or processed
//   * as required.
//   *
//   * @param message the message object that can be processed as needed
//   */
//  void update(Object message);
}
