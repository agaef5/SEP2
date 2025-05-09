package client.startup;

import client.networking.SocketService;
import client.networking.authentication.SocketAuthenticationClient;
import client.ui.adminView.AdminViewController;
import client.ui.adminView.adminPanel.AdminPanelController;
import javafx.application.Application;
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
    SocketAuthenticationClient socketAuth = new SocketAuthenticationClient(socketservice);

//    TODO: start with the authentication instead of admin view, after authenticating, redirect user to given tabbedwindow (Admin or User)

//    ADMIN VIEW
//    Loading main window that will display pages
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/adminView/AdminViewWindow.fxml"));
    Parent root = loader.load();
    AdminViewController tabbedWindowController = loader.getController();

//    Loading landing page, that will be displayed in main window
    FXMLLoader startPageLoader = new FXMLLoader(getClass().getResource("/client/ui/adminView/adminPanel/AdminPanel.fxml"));
    Parent startPageRoot = startPageLoader.load();
    AdminPanelController startPageController = startPageLoader.getController();

//    Initializing window with loaded landing page
    tabbedWindowController.initialize(socketservice, socketAuth, startPageController);

//    Show the window
    primaryStage.setTitle("Saddle Up and Sell Your Soul");
    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    primaryStage.show();


//    === HORSE LIST VIEW - USER
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
  }
}
