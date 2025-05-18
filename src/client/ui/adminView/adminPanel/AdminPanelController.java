package client.ui.adminView.adminPanel;


import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
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


  private AdminPanelVM vm;
  private MainWindowController mainWindowController;
  public AdminPanelController(){};


  @FXML
  public void initialize(ViewModel viewModel)
  {
    vm = (AdminPanelVM) viewModel;
    raceInfo.textProperty().bindBidirectional(vm.raceInfoTextProperty());
    addHorse.setOnAction(e -> {
      vm.onAddHorse();
      mainWindowController.loadHorsePage();
    });
    addRace.setOnAction(e -> {
      vm.onAddRace();
      mainWindowController.loadRacePage();
    });
    editUser.setOnAction(e -> {
      vm.onEditUser();
    });
  }


  @Override
  public void setWindowController(MainWindowController mainWindowController)
  {
    this.mainWindowController = mainWindowController;
  }


//    // Bind the race info label to the ViewModel property
//    //raceInfo.textProperty().bind(this.vm.getNextRaceInfo());
//
////    // Set up navigation actions for buttons
////    addRace.setOnAction(e -> loadScene(
////        "/client/ui/adminView/race/CreateRace.fxml"));
////    addHorse.setOnAction(e -> loadScene("/client/ui/racerList/adminView/racer/CreateEditRacer.fxml"));
////    //    editUser.setOnAction(e -> loadScene("/client/ui/racerList/adminView/user/EditUser.fxml"));
//  }
//  @Override
//  public void setWindowController(MainWindowController mainWindowController) {
//    if(mainWindowController != null)
//      this.mainWindowController = mainWindowController;
//  }
//
//  /**
//   * Loads a new scene and sets it as the current scene in the application window, using the Window controller.
//   *
//   * @param event - "event" triggered by clicking on one of the buttons on the AdminController
//   */
//  public void loadPage(ActionEvent event){
//      if (event.getSource() == addHorse) {
//        mainWindowController.loadHorsePage();
//      }
//
//      if (event.getSource() == addRace) {
//        mainWindowController.loadRacePage();
//      }
//  }
}
