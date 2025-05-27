package client.startup;

import client.networking.SocketService;
import client.ui.authentication.login.LoginController;
import client.ui.authentication.register.RegisterController;
import client.ui.navigation.MainWindowController;
import com.google.gson.Gson;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.Request;

import java.io.IOException;

/**
 * Entry point for launching the JavaFX client application.
 *
 * Initializes the socket connection, sets up the model manager and UI,
 * and loads the main application window.
 */
public class RunClient extends Application {

  /**
   * Main method that starts the JavaFX runtime.
   *
   * @param args command-line arguments (not used)
   * @throws IOException if initialization fails
   */
  public static void main(String[] args) throws IOException {
    launch();
  }

  /**
   * Initializes and starts the primary JavaFX stage.
   * Sets up the socket connection, UI controller, and scene.
   *
   * @param primaryStage the main window of the application
   * @throws Exception if FXML loading or initialization fails
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    String HOST = "localhost";
    int PORT = 2910;
    ClientAppInitializer clientAppInitializer = new ClientAppInitializer(HOST, PORT);
    SocketService socketService = clientAppInitializer.getSocketService();

    // MAIN WINDOW VIEW
    // Loading main window that will display pages
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/navigation/MainWindow.fxml"));
    Parent root = loader.load();
    MainWindowController mainWindowController = loader.getController();
    mainWindowController.setStage(primaryStage);

    // Loading login page, that will be displayed in main window
    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource("/client/ui/authentication/login/Login.fxml"));
    Parent loginPageRoot = loginPageLoader.load();
    LoginController loginController = loginPageLoader.getController();

    // Initialize main window controller with model and login controller
    mainWindowController.initialize(clientAppInitializer.getModelManager(), loginController);

    // Set the close request handler to disconnect cleanly
    primaryStage.setOnCloseRequest(event -> {
      socketService.disconnect();
      Platform.exit();
      System.out.println("Client disconnected and closed");
    });

    // Show the window
    primaryStage.setTitle("Saddle Up and Sell Your Soul");
    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    primaryStage.show();
  }
}
