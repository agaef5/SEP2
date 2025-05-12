package client.networking;

import client.ui.common.MessageListener;

/**
 * The {@code SocketSubject} interface defines the contract for objects that are capable of notifying listeners
 * about events or data changes. This is a part of the observer pattern where listeners are notified when new data
 * is available or when an event occurs.
 * <p>
 * Implementations of this interface allow listeners to subscribe to receive updates based on the type and payload
 * of the data.
 */
public interface SocketSubject
{
  /**
   * Adds a listener to the list of listeners that will be notified when an event occurs or data is available.
   * <p>
   * This method is a placeholder and should be implemented by the concrete class to allow listeners to be added.
   *
   * @param listener the listener to be added to the notification list
   */
  static void addListener(MessageListener listener) {
    // This method is left empty in the interface, expecting implementation in the concrete class
  }

  /**
   * Notifies all registered listeners about a new event or data change.
   *
   * @param type the type of the event or data
   * @param payload the actual data associated with the event, typically a JSON string or data object
   */
  void notifyListener(String type, String payload);

  /**
   * Removes a listener from the list of listeners so that it will no longer receive notifications.
   *
   * @param listener the listener to be removed
   */
  void removeListener(MessageListener listener);
}
