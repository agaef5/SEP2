package client.ui.adminView.race;

import client.ui.common.Controller;
import client.ui.common.ViewModel;
import client.ui.navigation.MainWindowController;
import javafx.beans.binding.Bindings;
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

  private CreateRaceVM viewModel;
  private MainWindowController mainCtrl;

  @Override
  public void initialize(ViewModel viewModel) {
    this.viewModel = (CreateRaceVM) viewModel;

    // Bind choice‐box and list‐view
//    trackChoice.setItems(this.viewModel.getAvailableTracks());
//    trackChoice.valueProperty()
//            .bindBidirectional(this.viewModel.selectedTrackProperty());

    raceQueueList.setItems(this.viewModel.getRaceQueue());
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
//    raceNameField.textProperty()
//            .bindBidirectional(this.viewModel.raceNameProperty());
//    Bindings.bindBidirectional(
//            horseCountField.textProperty(),
//            this.viewModel.horseCountProperty(),
//            new javafx.util.converter.NumberStringConverter()
//    );

    // Bind button disable to VM.canCreate
//    createButton.disableProperty()
//            .bind(this.viewModel.canCreate().not());

    // Bind message
//    messageLabel.textProperty()
//            .bind(this.viewModel.messageProperty());

    // Button actions
//    createButton.setOnAction(e -> this.viewModel.createRace());
//    backButton.setOnAction(e -> {
//      if (mainCtrl != null) mainCtrl.loadAdminPanel();
//    });
  }

  @Override
  public void setWindowController(MainWindowController mainWindowController) {
    this.mainCtrl = mainWindowController;
  }
}
