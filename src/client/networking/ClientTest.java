package client.networking;

import client.networking.authentication.SocketAuthenticationClient;
import client.networking.horses.SocketHorsesClient;
import client.ui.adminView.horseList.CreateEditHorseController;
import client.ui.adminView.horseList.CreateEditHorseVM;
import client.ui.userView.HorseListVM;
import client.ui.userView.HorseListViewController;
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

//    === CREATE HORSE VIEW - ADMIN

//    CreateEditHorseVM createEditRacerVM = new CreateEditHorseVM(socketRacersClient,socketservice);
//    socketservice.addListener(createEditRacerVM);
//    FXMLLoader loader = new FXMLLoader(getClass().getResource(
//        "/client/ui/adminView/horseList/CreateEditHorse.fxml"));
//    Parent root = loader.load();
//    CreateEditHorseController controller = loader.getController();
//    controller.init(createEditRacerVM);

//    === HORSE LIST VIEW - USER
    HorseListVM horseListVM = new HorseListVM(socketRacersClient,socketservice);
    socketservice.addListener(horseListVM);
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/client/ui/userView/HorseListView.fxml"));
    Parent root = loader.load();
    HorseListViewController controller = loader.getController();
    controller.initialize(horseListVM);

    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();

  }
}
