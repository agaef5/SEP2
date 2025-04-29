package client.networking;

import client.networking.authentication.SocketAuthenticationClient;
import client.networking.racers.SocketRacersClient;
import client.ui.racerList.adminView.CreateEditRacerController;
import client.ui.racerList.adminView.CreateEditRacerVM;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import shared.LoginRequest;
import shared.RegisterRequest;

import java.io.BufferedReader;
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
    SocketRacersClient socketRacersClient = new SocketRacersClient(socketservice);
    CreateEditRacerVM createEditRacerVM = new CreateEditRacerVM(socketRacersClient,socketservice);
    socketservice.addListener(createEditRacerVM);
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ui/racerList/adminView/CreateEditRacer.fxml"));
    Parent root = loader.load();
    CreateEditRacerController controller = loader.getController();
    controller.init(createEditRacerVM);
    Stage stage = new Stage();
    stage.setScene(new Scene(root));
    stage.show();

  }
}
