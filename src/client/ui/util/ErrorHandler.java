package client.ui.util;

import client.networking.SocketService;
import client.ui.util.exceptions.InvalidMessageException;
import com.google.gson.Gson;
import javafx.scene.control.Alert;
import shared.Request;

import java.io.IOException;
import java.sql.SQLException;

/**
 * The {@code ErrorHandler} class is responsible for handling errors that occur within the application.
 * It determines whether an error is client-side or server-side and takes appropriate actions to handle
 * and report those errors. The class is designed to manage errors that occur during communication with
 * the server, validation issues, or issues related to user input.
 * <p>
 * The main responsibility of the class is to ensure that errors are handled gracefully, providing meaningful
 * feedback to the user while also logging or reporting the error to the server if necessary.
 */
public class ErrorHandler {
  private SocketService socketService;

  /**
   * Constructs an {@code ErrorHandler} with a specified {@link SocketService} instance.
   *
   * @param socketService the {@code SocketService} used for server communication
   */
  public ErrorHandler(SocketService socketService)
  {
    this.socketService = socketService;
  }

  /**
   * Handles the given error based on its type (client-side or server-side).
   * It uses the exception type to determine whether to display a client-side error message or report
   * the error to the server.
   *
   * @param e the exception that occurred
   * @param errorSource the source or context of the error, used for logging and debugging
   */
  public static void handleError(Exception e, String errorSource) {
    // Determine if this is a client-side or server-side issue
    if (isClientError(e)) {
      handleClientError(e);
    } else if (isServerError(e)) {
      handleServerError(e, errorSource);
    }
  }

  /**
   * Handles client-side errors such as validation or input errors.
   * Displays a generic error message to the client.
   *
   * @param e the exception representing the client-side error
   */
  private static void handleClientError(Exception e) {
    String message = "An error occurred. Please try again.";
    displayErrorToClient(message);  // Use appropriate method to show the message to the client
  }

  /**
   * Handles server-side errors, such as issues related to server connectivity or database errors.
   * Sends detailed error information to the server and displays a generic message to the client.
   *
   * @param e the exception representing the server-side error
   * @param errorSource the source or context of the error, used for logging and debugging
   */
  private static void handleServerError(Exception e, String errorSource) {
    String serverMessage = "Internal error occurred. We are working to resolve it.";
    sendErrorToServer(e, errorSource);  // Send detailed error info to the server
    displayErrorToClient(serverMessage);  // Inform the client without exposing sensitive details
  }

  /**
   * Sends the error details to the server. This can be used for logging or alerting the server about issues.
   *
   * @param e the exception that caused the error
   * @param errorSource the source or context of the error
   */
  private static void sendErrorToServer(Exception e, String errorSource) {
    String detailedMessage = "Error in " + errorSource + ": " + e.getMessage();

    // TODO: Implement logic to send the error message to the server
    Gson gson = new Gson();
    Request request = new Request("error", "error", gson.toJsonTree(detailedMessage));
  }

  /**
   * Displays a generic error message to the client. This can be used for showing an alert, toast, or popup
   * in the UI.
   *
   * @param message the error message to display
   */
  private static void displayErrorToClient(String message) {
    showAlert("Error", message);
  }

  /**
   * Determines if the given exception represents a client-side error, typically errors related to input
   * validation or incorrect data.
   *
   * @param e the exception to check
   * @return {@code true} if the exception represents a client-side error, {@code false} otherwise
   */
  private static boolean isClientError(Exception e) {
    // Client-side errors may include validation or input errors
    return e instanceof IllegalArgumentException;
  }

  /**
   * Determines if the given exception represents a server-side error, typically issues related to connectivity,
   * database errors, or internal processing errors.
   *
   * @param e the exception to check
   * @return {@code true} if the exception represents a server-side error, {@code false} otherwise
   */
  private static boolean isServerError(Exception e) {
    // Server-side errors often relate to network issues or data processing problems
    return e instanceof SQLException || e instanceof IOException || e instanceof InvalidMessageException;
  }

  /**
   * Alert dedicated to inform user about error.
   *
   * @param title - title of the alert
   * @param content - content displayed in alert
   */
  public static void showAlert(String title, String content)
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}
