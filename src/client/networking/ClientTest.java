package client.networking;

import client.networking.authentication.SocketAuthenticationClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.SocketRaceClient;
import client.ui.adminView.AdminTabbedWindowController;
import client.ui.adminView.adminPanel.AdminPanelController;
import client.ui.adminView.horseList.CreateEditHorseController;
import client.ui.adminView.horseList.CreateEditHorseVM;
import client.ui.adminView.race.CreateRaceController;
import client.ui.adminView.race.CreateRaceVM;
import client.ui.userView.bettingPage.HorseListVM;
import client.ui.userView.bettingPage.HorseListViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.model.Admin;

import java.io.IOException;

public class ClientTest extends Application
{
  public static void main(String[] args) throws IOException
  {
    launch();
  }

  @Override public void start(Stage primaryStage) throws Exception
  {
    SocketService socketservice = new SocketService("localhost",2910 );
    SocketAuthenticationClient socketauth = new SocketAuthenticationClient(socketservice);
    SocketHorsesClient socketRacersClient = new SocketHorsesClient(socketservice);
    SocketRaceClient socketRaceClient = new SocketRaceClient(socketservice);

//    ADMIN PANEL
//    Loading main window that will display pages
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/adminView/AdminTabbedWindow.fxml"));
    Parent root = loader.load();
    AdminTabbedWindowController tabbedWindowController = loader.getController();


//    Loading landing page, that will be displayed in main window
    FXMLLoader startPageLoader = new FXMLLoader(getClass().getResource("/client/ui/adminView/adminPanel/AdminPanel.fxml"));
    Parent startPageRoot = startPageLoader.load();
    AdminPanelController startPageController = startPageLoader.getController();

//    Initializing window with loaded landing page
    tabbedWindowController.initialize(socketservice, socketauth, startPageController);

//    Show the window
    primaryStage.setTitle("Manage VIAPets");
    primaryStage.setScene(new Scene(root));
    primaryStage.sizeToScene();
    primaryStage.show();


//    === CREATE HORSE VIEW - ADMIN
//    CreateEditHorseVM createEditRacerVM = new CreateEditHorseVM(socketRacersClient,socketservice);
//    socketservice.addListener(createEditRacerVM);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/adminView/horseList/CreateEditHorse.fxml"));
//    Parent root = loader.load();
//    CreateEditHorseController controller = loader.getController();
//    controller.init(createEditRacerVM);


//    === CREATE RACE VIEW - ADMIN
//    CreateRaceVM createRaceVM = new CreateRaceVM(socketRaceClient,socketservice);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/adminView/race/CreateRace.fxml") );
//    Parent root = loader.load();
//    CreateRaceController createRaceController = loader.getController();
//    createRaceController.initialize(createRaceVM);

//    === HORSE LIST VIEW - USER
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
