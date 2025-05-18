package client.ui.adminView.adminPanel;


import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;


public class AdminPanelController implements Controller
{
  @FXML private Button addHorse;
  @FXML private Button addRace;
  @FXML private Button editUser;
  @FXML private Label raceInfo;


  private AdminPanelVM viewModel;
  private MainWindowController mainWindowController;
  public AdminPanelController(){};


  @FXML
  public void initialize(ViewModel viewModel)
  {
    this.viewModel = (AdminPanelVM) viewModel;
    raceInfo.textProperty().bindBidirectional(this.viewModel.raceInfoTextProperty());
    addHorse.setOnAction(e -> {
      mainWindowController.loadHorsePage();
    });
    addRace.setOnAction(e -> {
      mainWindowController.loadRacePage();
    });
  }


  @Override
  public void setWindowController(MainWindowController mainWindowController)
  {
    this.mainWindowController = mainWindowController;
  }
}
