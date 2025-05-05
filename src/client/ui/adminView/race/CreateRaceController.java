package client.ui.adminView.race;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.model.RaceTrack;

import java.io.IOException;
import java.sql.SQLException;

public class CreateRaceController
{
  @FXML private TextField nrOfHorses;
  @FXML private ChoiceBox<RaceTrack> raceTrack;
  @FXML private TextField raceName;
  @FXML private Button createRace;
  @FXML private Button back;

  private CreateRaceVM createRaceVM;


  public void initialize() throws SQLException
  {
    createRaceVM = new CreateRaceVM();

    //bind raceTrack items
    raceTrack.setItems(createRaceVM.getAvailableRaceTracks());
    createRaceVM.selectedRaceTrackProperty().bind(raceTrack.getSelectionModel().selectedItemProperty());

    //listener on textfield for nr of horses
    nrOfHorses.textProperty().addListener((obs, oldVal, newVal) ->
    {
      try
      {
        createRaceVM.horseCountProperty().set(Integer.parseInt(newVal));
      }
      catch (NumberFormatException e)
      {
        createRaceVM.horseCountProperty().set(0); // or error
      }

      //bind name
      raceName.textProperty()
          .bindBidirectional(createRaceVM.raceNameProperty());
    }
    );
  }

  @FXML private void onCreateRaceClicked() throws SQLException
  {
    if(createRaceVM.isValid()){
      createRaceVM.createRace();
    }
    else {
      showAlert("Incorrect input", "Make sure all the fields are filled.");
    }
  }

  @FXML private void onBackClicked() throws IOException
  {
    FXMLLoader loader = new FXMLLoader(getClass().getResource(
        "/client/ui/adminView/adminPanel/AdminPanel.fxml"));
    Parent root = loader.load();
    Stage stage = (Stage) back.getScene().getWindow();
    stage.setScene(new Scene(root));
  }

  //trying out new things with alerts... :-) But maybe this should be / or is already done in separate class
  private void showAlert(String title, String content)
  {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }
}

