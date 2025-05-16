package client.ui.adminView.race;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import shared.DTO.RaceDTO;
import shared.DTO.RaceTrackDTO;


public class CreateRaceController implements Controller {
  @FXML private TextField       raceNameField;
  @FXML private TextField       horseCountField;
  @FXML private ChoiceBox<RaceTrackDTO> trackChoice;
  @FXML private Button          createButton;
  @FXML private Button          backButton;
  @FXML private ListView<RaceDTO> raceQueueList;
  @FXML private Label           messageLabel;

  private CreateRaceVM vm;
  private MainWindowController mainCtrl;

  @Override
  public void initialize(ViewModel viewModel) {
    vm = (CreateRaceVM) viewModel;

    // Bind choice‐box and list‐view
    trackChoice.setItems(vm.getAvailableTracks());
    trackChoice.valueProperty()
            .bindBidirectional(vm.selectedTrackProperty());

    raceQueueList.setItems(vm.getRaceQueue());
    raceQueueList.setCellFactory(lv -> new ListCell<>() {
      @Override
      protected void updateItem(RaceDTO item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty || item == null
                ? null
                : item.name() + " @ " + item.raceTrack().name());
      }
    });

    // Bind form fields
    raceNameField.textProperty()
            .bindBidirectional(vm.raceNameProperty());
    Bindings.bindBidirectional(
            horseCountField.textProperty(),
            vm.horseCountProperty(),
            new javafx.util.converter.NumberStringConverter()
    );

    // Bind button disable to VM.canCreate
    createButton.disableProperty()
            .bind(vm.canCreate().not());

    // Bind message
    messageLabel.textProperty()
            .bind(vm.messageProperty());

    // Button actions
    createButton.setOnAction(e -> vm.createRace());
    backButton.setOnAction(e -> {
      if (mainCtrl != null) mainCtrl.loadAdminPanel();
    });
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainCtrl = mainWindowController;
  }
}
