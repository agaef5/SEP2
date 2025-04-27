package client.validation;

import client.networking.SocketService;
import client.validation.exceptions.InvalidMessageException;
import com.google.gson.Gson;
import shared.Request;

import java.io.IOException;
import java.sql.SQLException;

public class ErrorHandler {
  private SocketService socketService;

  public ErrorHandler(SocketService socketService)
  {
    this.socketService = socketService;
  }

  public static void handleError(Exception e, String errorSource) {
    // Determine if this is a client-side or server-side issue
    if (isClientError(e)) {
      handleClientError(e);
    } else if (isServerError(e)) {
      handleServerError(e, errorSource);
    }
  }

  // Handle client-side errors (e.g., validation errors, input errors)
  private static void handleClientError(Exception e) {
    // You can decide to show the error message to the client here
    // For example, a UI-friendly message for the client:
    String message = "An error occurred. Please try again.";
    displayErrorToClient(message);  // Use appropriate method to show the message to the client
  }

  // Handle server-side errors (e.g., issues related to server connectivity, database errors)
  private static void handleServerError(Exception e, String errorSource) {

    String serverMessage = "Internal error occurred. We are working to resolve it.";
    sendErrorToServer(e, errorSource);  // Send detailed error info to the server
    displayErrorToClient(serverMessage);  // Inform the client without exposing sensitive details
  }

  // Send error details to the server (could be logging, alerting, etc.)
  private static void sendErrorToServer(Exception e, String errorSource) {
    String detailedMessage = "Error in " + errorSource + ": " + e.getMessage();

    Gson gson = new Gson();
    Request request = new Request("error", "error", gson.toJsonTree(detailedMessage));
  }

  // Display error to the client (for UI or user communication)
  private static void displayErrorToClient(String message) {
    // Code to display the message to the user (e.g., a popup, toast, or alert in the UI)
    System.out.println(message);  // This is for example purposes; use a UI framework in a real app
  }

  // Check if the error is client-side (e.g., input errors, invalid data)
  private static boolean isClientError(Exception e) {
    // You can base this on error types or error messages
    return e instanceof IllegalArgumentException;
  }

  // Check if the error is server-side (e.g., database unavailability, server timeout)
  private static boolean isServerError(Exception e) {
    // Server-side errors often relate to issues with connectivity, server, or data processing
    return e instanceof SQLException || e instanceof IOException || e instanceof InvalidMessageException;
  }
}
