package client.startup;

import client.networking.SocketService;
import client.ui.authentication.login.LoginController;
import client.ui.authentication.register.RegisterController;
import client.ui.navigation.MainWindowController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RunClient extends Application
{
  public static void main(String[] args) throws IOException
  {
    launch();
  }

  @Override public void start(Stage primaryStage) throws Exception
  {
    SocketService socketservice = new SocketService("localhost",2910 );

//  MAIN WINDOW VIEW
//    Loading main window that will display pages
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/navigation/MainWindow.fxml"));
    Parent root = loader.load();
    MainWindowController mainWindowController = loader.getController();

//    Loading register page, that will be displayed in main window
    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource("/client/ui/authentication/login/Login.fxml"));
    Parent loginPageRoot = loginPageLoader.load();
    LoginController loginController = loginPageLoader.getController();

//  Initialize main window controller
    mainWindowController.initialize(socketservice, loginController);

    // Set the close request handler
    primaryStage.setOnCloseRequest(event -> {
      socketservice.disconnect();
      Platform.exit();
    });

//    Show the window
    primaryStage.setTitle("Saddle Up and Sell Your Soul");
    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    primaryStage.show();


//    ______________________________________________


//    === HORSE LIST VIEW - USER __________________________
//    SocketHorsesClient socketRacersClient = new SocketHorsesClient(socketservice);
//    HorseListVM horseListVM = new HorseListVM(socketRacersClient,socketservice);
//    socketservice.addListener(horseListVM);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/userView/HorseListView.fxml"));
//    Parent root = loader.load();
//    HorseListViewController controller = loader.getController();
//    controller.initialize(horseListVM);
//    Stage stage = new Stage();
//    stage.setScene(new Scene(root));
//    stage.show();
//    _________________________________________________
  }
}
