package client.networking;

import client.networking.authentication.SocketAuthenticationClient;
import client.networking.horses.SocketHorsesClient;
import client.networking.race.SocketRaceClient;
import client.ui.adminView.horseList.CreateEditHorseController;
import client.ui.adminView.horseList.CreateEditHorseVM;
import client.ui.userView.bettingPage.UserBettingViewController;
import client.ui.userView.bettingPage.UserBettingViewVM;
import client.ui.userView.landingPage.UserLandingPageController;
import client.ui.userView.landingPage.UserLandingPageVM;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

//    === CREATE HORSE VIEW - ADMIN

//    CreateEditHorseVM createEditRacerVM = new CreateEditHorseVM(socketRacersClient,socketservice);
//    socketservice.addListener(createEditRacerVM);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/adminView/horseList/CreateEditHorse.fxml"));
//    Parent root = loader.load();
//    CreateEditHorseController controller = loader.getController();
//    controller.initialize(createEditRacerVM);


//    === CREATE RACE VIEW - ADMIN
//    CreateRaceVM createRaceVM = new CreateRaceVM(socketRaceClient,socketservice);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/adminView/race/CreateRace.fxml") );
//    Parent root = loader.load();
//    CreateRaceController createRaceController = loader.getController();
//    createRaceController.initialize(createRaceVM);



//    ===USER LANDING PAGE ===
    UserLandingPageVM userLandingPageVM = new UserLandingPageVM(socketRaceClient, socketservice);
    socketservice.addListener(userLandingPageVM);
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
            "/client/ui/userView/landingPage/userLandingPage.fxml"));
    Parent root = loader.load();
    UserLandingPageController controller = loader.getController();
    controller.initialize(userLandingPageVM, socketRacersClient, socketservice, socketRaceClient);


//    === USER BETTING VIEW ===
//    UserBettingViewVM userBettingViewVM = new UserBettingViewVM(socketRacersClient,socketservice);
//    socketservice.addListener(userBettingViewVM);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/userView//bettingPage/UserBettingView.fxml"));
//    Parent root = loader.load();
//    UserBettingViewController controller = loader.getController();
//    controller.initialize(userBettingViewVM);

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();

  }
}
