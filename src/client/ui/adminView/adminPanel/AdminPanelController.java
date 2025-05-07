package client.ui.adminView.adminPanel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPanelController
{
  @FXML private Button addRace;
  @FXML private Button addHorse;
  @FXML private Button editUser;
  @FXML private Label raceInfo;
  private AdminPanelVM viewModel;

  //empty constructor for FXML
  public AdminPanelController(){};

  @FXML
  private void init(AdminPanelVM viewModel)
  {
    this.viewModel = viewModel;

    //binding the label to vm property
    raceInfo.textProperty().bind(viewModel.getNextRaceInfo());

    //set up the buttons
    addRace.setOnAction(e -> loadScene(
        "/client/ui/adminView/race/CreateRace.fxml"));
    addHorse.setOnAction(e -> loadScene("/client/ui/racerList/adminView/racer/CreateEditRacer.fxml"));
//    editUser.setOnAction(e -> loadScene("/client/ui/racerList/adminView/user/EditUser.fxml"));
  }

  private void loadScene(String fxmlPath)
  {
    try
    {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
      Parent root = loader.load();
      Stage stage = (Stage) addRace.getScene().getWindow();
      stage.setScene(new Scene(root));
    } catch (IOException e)
    {
      e.printStackTrace();

    }
  }
}
